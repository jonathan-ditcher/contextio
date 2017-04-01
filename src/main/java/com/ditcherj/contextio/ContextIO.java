package com.ditcherj.contextio;

import com.ditcherj.contextio.dto.Account;
import com.ditcherj.contextio.dto.SortOrder;
import com.ditcherj.contextio.responses.AccountResponse;
import com.ditcherj.contextio.responses.AccountsResponse;
import com.ditcherj.contextio.responses.ListContactsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
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

        Response response = this.get(endpoint, null);
        return new ResponseBuilder(response).decodeResponse(AccountResponse.class);
    }

    public AccountsResponse getAccounts() {
        final String endpoint = "accounts";

        Response response = this.get(endpoint, null);
        List<Account> accounts = new ResponseBuilder(response).decodeResponseAsList(new TypeReference<List<Account>>(){});

        AccountsResponse accountsResponse = new AccountsResponse();
        accountsResponse.setAccounts(accounts);

        return accountsResponse;
    }

    public ListContactsResponse listContacts(String accoun) {
        return this.listContacts(accoun, null, null, null, null, null, null, null);
    }

    public ListContactsResponse listContacts(String account,
                                             String search,
                                             Integer active_before,
                                             Integer active_after,
                                             String sort_by,
                                             SortOrder sort_order,
                                             Integer limit,
                                             Integer offset) {
        logger.trace("");

        if (StringUtils.isEmpty(account))
            throw new IllegalArgumentException("account must be string representing accountId");

        Map<String, String> params = new HashMap<>();

        if(!StringUtils.isEmpty(search))
            params.put("search", search);
        if(active_before != null)
            params.put("active_before", String.valueOf(active_before));
        if(active_after != null)
            params.put("active_after", String.valueOf(active_after));
        if(!StringUtils.isEmpty(sort_by))
            params.put("sort_by", sort_by);
        if(sort_order != null)
            params.put("sort_order", sort_order.name());
        if(limit != null)
            params.put("limit", String.valueOf(limit));
        if(offset != null)
            params.put("offset", String.valueOf(offset));

        final String endpoint = "accounts/"+account+"/contacts";
        Response response = this.get(endpoint, params);

        return new ResponseBuilder(response).decodeResponse(ListContactsResponse.class);
    }

    private Response get(String endpoint, Map<String, String> params) {
        return doCall(Verb.GET, endpoint, params);
    }

    private Response post(String endpoint, Map<String, String> params) {
        return doCall(Verb.POST, endpoint, params);
    }

    private Response delete(String endpoint, Map<String, String> params) {
        return doCall(Verb.DELETE, endpoint, params);
    }

    private Response doCall(Verb method, String endpoint, Map<String, String> params)  {
        Response response = null;

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
                    .debugStream(System.out)
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

            response = service.execute(request);
            } catch (Exception e) {
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
