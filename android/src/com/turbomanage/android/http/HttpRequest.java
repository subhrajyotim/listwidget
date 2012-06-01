package com.turbomanage.android.http;

/**
 * Holds data for an HTTP request to be made with the attached HTTP client.
 * 
 * @author David M. Chandler
 */
public abstract class HttpRequest {
    
    public static final String URLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String MULTIPART = "multipart/form-data";

    // The client that will be used to execute the request
    protected final AbstractHttpClient httpClient;

    public HttpRequest(AbstractHttpClient client) {
        this.httpClient = client;
    }

    public abstract HttpResponse execute();

}
