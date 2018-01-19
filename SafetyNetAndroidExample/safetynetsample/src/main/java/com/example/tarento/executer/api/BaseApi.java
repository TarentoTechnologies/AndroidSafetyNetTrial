package com.example.tarento.executer.api;

import com.android.volley.Request;
import com.example.tarento.model.BaseModel;
import com.example.tarento.ui.BuildConfig;

import java.util.Hashtable;

/**
 * API class All the network method signatures are initialized..
 */
public abstract class BaseApi {

    private Hashtable<String, String> headers;
    private int contentType;
    private int method;
    private String url;
    private int requestId;
    private String body;
    private boolean shouldCache;

    /**
     * Serialize a response data using GSON and store it into DB
     *
     * @param responseJsonObject
     * @throws Exception
     */
    public abstract BaseModel serialize(String responseJsonObject) throws Exception;

    public boolean isShouldCache() {
        return shouldCache;
    }

    /**
     * disable the cache for a particular url,
     *
     * @param isShouldCache
     */
    public void setShouldCache(boolean isShouldCache) {
        this.shouldCache = isShouldCache;
    }

    public Hashtable<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Hashtable<String, String> headers) {
        this.headers = headers;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMethod() {
        return method == 0 ? Request.Method.GET : method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUrl() {
        return BuildConfig.BASE_URL + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
