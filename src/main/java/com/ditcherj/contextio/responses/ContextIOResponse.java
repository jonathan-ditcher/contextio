package com.ditcherj.contextio.responses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
public class ContextIOResponse {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ContextIOResponse.class);

    private int code;
    private Map<String, String> headers;
    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;
    private String contentType;
    private Response rawResponse;
    private boolean hasError;


    public ContextIOResponse(int code, Map<String, String> requestHeaders, Map<String, String> responseHeaders, Response rawResponse) {
        this.code = code;
        this.requestHeaders = requestHeaders;
        this.responseHeaders = responseHeaders;
        this.rawResponse = rawResponse;
        this.contentType = rawResponse.getHeader("Content-Type");
        // TODO: this.headers = ;
    }


    public void decodeResponse() {
        if (code != 200 || !contentType.equals("application/json")) {
            hasError = true;
        } else {
            // TODO: decode json response to rawResponse

            // TODO: if (array_key_exists('messages', $this->decodedResponse) && (count($this->decodedResponse['messages']) > 0)) hasError = true;
        }
    }

    public<T extends BaseResponse> T decodeResponse(Class T) {
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = null;
        try {
            obj = (T) objectMapper.readValue(this.rawResponse.getBody(), T);
            obj.setCode(this.code);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            logger.warn(this.toString());
        }
        return obj;
    }

    public<T> List<T> decodeResponseAsList(TypeReference type) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> list = null;
        try {
            list = (List<T>)objectMapper.readValue(this.rawResponse.getBody(), type);
            for(T obj : list) {
                if(obj instanceof BaseResponse)
                    ((BaseResponse)obj).setCode(this.code);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(this.toString());
        }
        return list;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Response getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(Response rawResponse) {
        this.rawResponse = rawResponse;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    @Override
    public String toString() {
        return "ContextIOResponse{" +
                "code=" + code +
                ", headers=" + headers +
                ", requestHeaders=" + requestHeaders +
                ", responseHeaders=" + responseHeaders +
                ", contentType='" + contentType + '\'' +
                ", rawResponse=" + rawResponse +
                ", hasError=" + hasError +
                '}';
    }
}
