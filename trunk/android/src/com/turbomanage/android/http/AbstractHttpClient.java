
package com.turbomanage.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Lightweight HTTP client that facilitates GET, POST, PUT, and DELETE requests using
 * {@link HttpURLConnection}. Extend this class to support specialized content and 
 * response types (see {@link BasicHttpClient} for an example). To enable streaming,
 * buffering, or other types of readers / writers, set an alternate {@link RequestHandler}.
 * 
 * @author David M. Chandler
 */
public abstract class AbstractHttpClient {

    static {
        ensureCookieManager();
    }

    public static final String URLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String MULTIPART = "multipart/form-data";

    protected String baseUrl;
    
    private RequestLogger requestLogger;
    private RequestHandler requestHandler;
    private Map<String, String> requestHeaders = new TreeMap<String, String>();

    /**
     * Constructs a new client with base URL that will be appended in the 
     * request methods. It may be empty or any part of a URL.
     * 
     * Examples: 
     * http://turbomanage.com
     * http://turbomanage.com:987
     * http://turbomanage.com:987/resources
     * 
     * @param baseUrl
     */
    public AbstractHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Execute a GET request and return the response.
     * 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public abstract HttpResponse get(String path, ParameterMap params);

    /**
     * Execute a POST request with parameter map and return the response.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public abstract HttpResponse post(String path, ParameterMap params);

    /**
     * Execute a POST request with a chunk of data.
     * 
     * The supplied parameters are URL encoded and sent as the request content.
     * 
     * @param path
     * @param contentType
     * @param data
     * @return Response object
     */
    public abstract HttpResponse post(String path, String contentType, byte[] data);

    /**
     * Execute a PUT request with the supplied content and return the response.
     * 
     * @param path
     * @param contentType
     * @param data
     * @return Response object
     */
    public abstract HttpResponse put(String path, String contentType, byte[] data);

    /**
     * Execute a DELETE request and return the response.
     * 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public abstract HttpResponse delete(String path, ParameterMap params);

    /**
     * This is the method that drives each request. It implements the request
     * lifecycle defined as open, prepare, write, read. Each of these methods
     * in turn delegates to the {@link RequestHandler} associated with this client.
     * 
     * @param path Whole or partial URL string, will be appended to baseUrl
     * @param httpMethod Request method
     * @param contentType MIME type of the request
     * @param content Request data
     * @return Response object
     */
    protected HttpResponse doHttpMethod(String path, HttpMethod httpMethod, String contentType, byte[] content) {
    
        HttpURLConnection uc = null;
        HttpResponse httpResponse = null;
    
        try {
            uc = openConnection(path);
            prepareConnection(uc, httpMethod, contentType);
            appendRequestHeaders(uc);
            if (requestLogger.isLoggingEnabled()) {
                requestLogger.logRequest(uc, content);
            }
            if (uc.getDoOutput() && content != null) {
                int status = writeOutputStream(uc, content);
            }
            httpResponse = readInputStream(uc);
            if (requestLogger.isLoggingEnabled()) {
                requestLogger.logResponse(uc);
            }
        } catch (IOException e) {
            requestHandler.onError(uc, e);
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
        return httpResponse;
    }

    protected HttpURLConnection openConnection(String path) throws IOException {
        String requestUrl = baseUrl + path;
        try {
            new URL(requestUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(requestUrl + " is not a valid URL", e);
        }
        return requestHandler.openConnection(requestUrl);
    }

    protected void prepareConnection(HttpURLConnection urlConnection, HttpMethod httpMethod,
            String contentType) throws IOException {
        requestHandler.prepareConnection(urlConnection, httpMethod, contentType);
    }

    /**
     * Append all headers added with {@link #addHeader(String, String)}
     * to the request.
     * 
     * @param urlConnection
     */
    private void appendRequestHeaders(HttpURLConnection urlConnection) {
        for (String name : requestHeaders.keySet()) {
            String value = requestHeaders.get(name);
            urlConnection.setRequestProperty(name, value);
        }
    }

    protected int writeOutputStream(HttpURLConnection uc, byte[] content) throws IOException {
        OutputStream out = null;
        try {
            out = uc.getOutputStream();
            if (out != null) {
                requestHandler.writeStream(out, content);
            }
            return uc.getResponseCode();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected HttpResponse readInputStream(HttpURLConnection uc) throws IOException {
        InputStream in = null;
        byte[] responseBody = null;
        try {
            in = uc.getInputStream();
            if (in != null) {
                responseBody = requestHandler.readStream(in);
            }
            return new HttpResponse(uc, responseBody);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Convenience method creates a new ParameterMap to hold query params
     * 
     * @return Parameter map
     */
    public ParameterMap newParams() {
        return new ParameterMap();
    }

    /**
     * Adds to the headers that will be sent with each request from this client instance.
     * The request headers added with this method are applied by calling 
     * {@link HttpURLConnection#setRequestProperty(String, String)} 
     * after {@link #prepareConnection(HttpURLConnection, HttpMethod, String)}, so they may
     * supplement or replace headers which have already been set.
     *   
     * Calls to {@link #addHeader(String, String)} may be chained.
     * 
     * To clear all headers added with this method, call {@link #clearHeaders()}.
     * 
     * @param name
     * @param value
     * @return
     */
    public AbstractHttpClient addHeader(String name, String value) {
        requestHeaders.put(name, value);
        return this;
    }
    
    /**
     * Clears all request headers that have been added using 
     * {@link #addHeader(String, String)}. This method has no effect on
     * headers which result from request properties set by this class or its
     * associated {@link RequestHandler} when preparing the {@link HttpURLConnection}.
     */
    public void clearHeaders() {
        requestHeaders.clear();
    }

    /**
     * Returns the {@link CookieManager} associated with this client.
     * 
     * @return CookieManager
     */
    public static CookieManager getCookieManager() {
        return (CookieManager) CookieHandler.getDefault();
    }

    public void setRequestLogger(RequestLogger logger) {
        this.requestLogger = logger;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * Initialize the app-wide {@link CookieManager}. This is all that's necessary to
     * enable all Web requests within the app to automatically send and receive cookies.
     */
    protected static void ensureCookieManager() {
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
    }

}
