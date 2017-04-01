package com.ditcherj.contextio;

import com.ditcherj.contextio.responses.AccountResponse;
import com.ditcherj.contextio.responses.AccountsResponse;
import com.ditcherj.contextio.responses.ContextIOResponse;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public class ContextIO {

    private static final Logger logger = LoggerFactory.getLogger(ContextIO.class);
    private static final String END_POINT = "https://api.context.io/2.0/";
    private String oauthKey;
    private String oauthSecret;

    /**
     * Instantiate a new ContextIO object. Your OAuth consumer key and secret can be
     * found under the "settings" tab of the developer console (https://console.context.io/#settings)
     * @param key Your Context.IO OAuth consumer key
     * @param secret Your Context.IO OAuth consumer secret
     */
    public ContextIO(String key, String secret) {
        this.oauthKey = key;
        this.oauthSecret = secret;
    }

    public AccountResponse getAccount(String account) {
        if (StringUtils.isEmpty(account)) 
            throw new IllegalArgumentException("account must be string representing accountId");

        final String endpoint = "accounts/" + account;
        return this.get(endpoint , null, AccountResponse.class);

    }

    public AccountsResponse getAccounts() {
        final String endpoint = "accounts";
        return this.get(endpoint , null, AccountsResponse.class);
    }

    public <P, T extends ContextIOResponse<P>> T get(String endpoint, Map<String, String> params, Class<T> clazz) {
        return doCall(Verb.GET, endpoint, params, clazz);
    }

    public <P, T extends ContextIOResponse<P>> T post(String endpoint, Map<String, String> params, Class<T> clazz) {
        return doCall(Verb.POST, endpoint, params, clazz);
    }

    public <P, T extends ContextIOResponse<P>> T delete(String endpoint, Map<String, String> params, Class<T> clazz) {
        return doCall(Verb.DELETE, endpoint, params, clazz);
    }

    public <P, T extends ContextIOResponse<P>> T doCall(Verb method, String endpoint, Map<String, String> params, Class<T> clazz)  {
        T response = null;

        try{
            if(params == null)
                params = new HashMap<>();

            String baseUrl = END_POINT + endpoint;

            if (Verb.GET.equals(method)) {
                URIBuilder uriBuilder = new URIBuilder(baseUrl);
                for(Map.Entry<String, String> param : params.entrySet()) {
                    uriBuilder.addParameter(param.getKey(), param.getValue());
                }
                baseUrl = uriBuilder.build().toString();
            }

            logger.trace("baseUrl["+baseUrl+"] method["+method+"]");

            OAuthService service = new ServiceBuilder()
                    .apiKey(this.oauthKey)
                    .apiSecret(this.oauthSecret)
                    .build(new ContextIOApi());

            OAuthRequest request = new OAuthRequest(method, baseUrl);

            if(method.equals(Verb.POST)){
                if(endpoint.endsWith("messages"))
                    this.handlePostMessageRequest(request, params);
                else{
                    for (Map.Entry<String, String> entry : params.entrySet())
                        request.addBodyParameter(entry.getKey(), entry.getValue());
                }
            }

            Token nullToken = new OAuth1AccessToken("", "");
            service.signRequest(nullToken, request);

            Response oauthResponse = service.execute(request);

            String content = oauthResponse.getBody();
            if(content.startsWith(JsonToken.START_ARRAY.asString()))
                response = this.decodeResponseAsList(clazz, new TypeReference<List<P>>(){}, content);
            else
                response = this.decodeResponse(clazz, content);

            response.setCode(oauthResponse.getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }

        return response;
    }

    private <P, T extends ContextIOResponse<P>> T decodeResponse(Class<T> clazz, String content) {
        ObjectMapper objectMapper = new ObjectMapper();

        T response = null;
        try {
            if(!content.startsWith(JsonToken.START_ARRAY.asString()))
                response = objectMapper.readValue(content, clazz);
            else
                response = clazz.newInstance();

            Class<P> payloadClazz = (Class<P>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
            P payload = objectMapper.readValue(content, payloadClazz);
            response.setPayload(payload);

        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(this.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return response;
    }

    private <P, T extends ContextIOResponse<P>> T decodeResponseAsList(Class<T> clazz, TypeReference type, String content) {
        ObjectMapper objectMapper = new ObjectMapper();

        T response = null;
        try {
            if(!content.startsWith(JsonToken.START_ARRAY.asString()))
                response = objectMapper.readValue(content, clazz);
            else
                response = clazz.newInstance();

            P payload = objectMapper.readValue(content, type);
            logger.trace("asdasdasd: " + payload.toString());
            response.setPayload(payload);

        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(this.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void handlePostMessageRequest(OAuthRequest request, Map<String, String> params) throws IOException {
        String boundary = generateBoundaryString();
        request.addHeader("Content-type", "multipart/form-data; boundary=" + boundary);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create().setBoundary(boundary);
            for(Map.Entry<String, String> entry : params.entrySet()){
                String param = entry.getKey();
                if(param.equals("message"))
                    entityBuilder = entityBuilder.addBinaryBody("message", params.get(param).getBytes(StandardCharsets.UTF_8), ContentType.create("message/rfc822"), "message.eml");
                else
                    entityBuilder = entityBuilder.addTextBody(param, params.get(param));
            }

            HttpEntity entity = entityBuilder.build();
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int)entity.getContentLength());

            entity.writeTo(baos);
            String payload = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            request.setPayload(payload.getBytes(StandardCharsets.UTF_8));

    }

    private static String generateBoundaryString() {
        return Long.toHexString(System.nanoTime());
    }
}
