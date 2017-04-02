package com.ditcherj.contextio;

import com.ditcherj.contextio.dto.*;
import com.ditcherj.contextio.responses.*;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        accountsResponse.setCode(response.getCode());
        return accountsResponse;
    }

    public ListContactsResponse listContacts(String account) {
        return this.listContacts(account, null, null, null, null, null, null, null);
    }

    public ListContactsResponse listContacts(String account,
                                             String search,
                                             Integer activeBefore,
                                             Integer activeAfter,
                                             String sortBy,
                                             SortOrder sortOrder,
                                             Integer limit,
                                             Integer offset) {
        logger.trace("");

        if (StringUtils.isEmpty(account))
            throw new IllegalArgumentException("account must be string representing accountId");

        Map<String, String> params = new HashMap<>();

        if(!StringUtils.isEmpty(search))
            params.put("search", search);
        if(activeBefore != null)
            params.put("active_before", String.valueOf(activeBefore));
        if(activeAfter != null)
            params.put("active_after", String.valueOf(activeAfter));
        if(!StringUtils.isEmpty(sortBy))
            params.put("sort_by", sortBy);
        if(sortOrder != null)
            params.put("sort_order", sortOrder.name());
        if(limit != null)
            params.put("limit", String.valueOf(limit));
        if(offset != null)
            params.put("offset", String.valueOf(offset));

        final String endpoint = "accounts/"+account+"/contacts";
        Response response = this.get(endpoint, params);

        return new ResponseBuilder(response).decodeResponse(ListContactsResponse.class);
    }

    public ContactResponse getContact(String account, String email) {
        logger.trace("");

        if (StringUtils.isEmpty(account))
            throw new IllegalArgumentException("account must be string representing accountId");
        if (StringUtils.isEmpty(email))
            throw new IllegalArgumentException("email required");

        String endpoint = null;
        try {
            endpoint = "accounts/"+account+"/contacts/" + URLEncoder.encode(email, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Response response = this.get(endpoint, null);

        return new ResponseBuilder(response).decodeResponse(ContactResponse.class);
    }

    public ListMessagesResponse getMessages(String account) {
        final String endpoint = "accounts/"+account+"/messages";

        Response response = this.get(endpoint, null);
        List<Message> messages = new ResponseBuilder(response).decodeResponseAsList(new TypeReference<List<Message>>(){});

        ListMessagesResponse messagesResponse = new ListMessagesResponse();
        messagesResponse.setMessages(messages);

        return messagesResponse;
    }

    public PostMessageResponse addMessageToFolder(String account, String dst_source, String dst_folder, String message,
                                                Boolean flag_seen, Boolean flag_answered, Boolean flag_flagged, Boolean flag_deleted, Boolean flag_draft) {
        if (StringUtils.isEmpty(account))
            throw new IllegalArgumentException("account must be string representing accountId");
        if (StringUtils.isEmpty(dst_source))
            throw new IllegalArgumentException("source required");
        if (StringUtils.isEmpty(dst_folder))
            throw new IllegalArgumentException("folder required");
        if (StringUtils.isEmpty(message))
            throw new IllegalArgumentException("message required");

        Map<String, String> params = new HashMap<>();
        params.put("dst_source", dst_source);
        params.put("dst_folder", dst_folder);
        params.put("message", message);

        if(flag_seen != null)
            params.put("flag_seen", flag_seen ? "1" : "0");
        if(flag_answered != null)
            params.put("flag_answered", flag_seen ? "1" : "0");
        if(flag_flagged != null)
            params.put("flag_flagged", flag_seen ? "1" : "0");
        if(flag_deleted != null)
            params.put("flag_deleted", flag_seen ? "1" : "0");
        if(flag_draft != null)
            params.put("flag_draft", flag_seen ? "1" : "0");

        final String endpoint = "accounts/"+account+"/messages";

        Response response = this.post(endpoint, null);

        return new ResponseBuilder(response).decodeResponse(PostMessageResponse.class);
    }

    public MessageBodyResponse getMessageBody(String account, String messageId, Type type){
        if (StringUtils.isEmpty(account))
            throw new IllegalArgumentException("account must be string representing accountId");
        if (StringUtils.isEmpty(messageId))
            throw new IllegalArgumentException("messageId required");

        Map<String, String> params = new HashMap<>();
        if(type != null)
            params.put("type", type.getMimeType());

        final String endpoint = "accounts/"+account+"/messages/" + messageId + "/body";

        Response response = this.get(endpoint, null);

        List<MessageBody> messageBodies = new ResponseBuilder(response).decodeResponseAsList(new TypeReference<List<MessageBody>>(){});

        MessageBodyResponse messagesResponse = new MessageBodyResponse();
        messagesResponse.setMessageBodies(messageBodies);
        messagesResponse.setCode(response.getCode());

        return messagesResponse;
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
                params.entrySet().stream().forEach(e -> uriBuilder.addParameter(e.getKey(), e.getValue()));
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
