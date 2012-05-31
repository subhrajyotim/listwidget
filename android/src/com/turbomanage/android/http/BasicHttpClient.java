
package com.turbomanage.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic HTTP client that facilitates simple GET, POST, PUT, and DELETE requests using
 * {@link HttpURLConnection}. Protected methods may be overridden to handle
 * other content types; however, most customization can be achieved simply by setting an
 * alternate {@link RequestHandler}.
 * 
 * @author David M. Chandler
 */
public class BasicHttpClient {

    public static final String URLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String MULTIPART = "multipart/form-data";

    private String baseUrl;
    private RequestHandler requestHandler;
    private RequestLogger requestLogger;

    static {
        ensureCookieManager();
    }

    /**
     * Constructs a new client with base URL that can be appended in the get()
     * or post() methods. Sets the default {@link CookieManager}, which is used
     * by all {@link HttpURLConnection}s in the VM.
     * 
     * @param baseUrl
     */
    public BasicHttpClient(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            // Strip trailing slash
            baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        }
        try {
            URL url = new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(baseUrl + " is not a valid URL", e);
        }
        this.baseUrl = baseUrl;
        setRequestHandler(new BasicRequestHandler());
        setRequestLogger(new BasicRequestLogger());
    }

    /**
     * Execute a GET request and return the response body. The supplied parameters
     * are URL encoded and sent as the query string. If using the default 
     * {@link BasicRequestHandler}, the response will be a {@link String}.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public Object get(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + "?" + queryString, HttpMethod.GET, null, null);
    }

    /**
     * Execute a POST request and return the response body. The supplied
     * parameters are URL encoded and sent as the request content. If using
     * the default {@link BasicRequestHandler}, the response will be a {@link String}.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public Object post(String path, Map<String, String> params) {
        String data = null;
        if (params != null) {
            data = urlEncode(params);
        }
        return doHttpMethod(path, HttpMethod.POST, URLENCODED, data);
    }

    /**
     * Executes a PUT request with the supplied content and returns the response.
     * 
     * @param path
     * @param content
     * @return Response object
     */
    public Object put(String path, Object content) {
        return doHttpMethod(path, HttpMethod.PUT, MULTIPART, content);
    }

    /**
     * Executes a DELETE request and returns the response. The supplied parameters
     * are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public Object delete(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + queryString, HttpMethod.DELETE, null, null);
    }

    /**
     * Executes a request.
     * 
     * @param path Part of the URL after the host & port
     * @param httpMethod Request method
     * @param contentType MIME type of the request
     * @param content Request data
     * @return Response object
     */
    protected Object doHttpMethod(String path, HttpMethod httpMethod,
            String contentType, Object content) {

        HttpURLConnection uc = null;
        Object response = null;

        try {
            uc = openConnection(path);
            prepareConnection(uc, httpMethod, contentType);
            if (requestLogger.isLoggingEnabled()) {
                requestLogger.logRequest(uc, content);
            }
            if (uc.getDoOutput() && content != null) {
            }
            int status = writeOutputStream(uc, content);
            response = readInputStream(uc);
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
        return response;
    }

    protected HttpURLConnection openConnection(String path) throws IOException {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("path must start with /");
        }
        return requestHandler.openConnection(baseUrl + path);
    }

    protected void prepareConnection(HttpURLConnection urlConnection, HttpMethod httpMethod,
            String contentType) throws IOException {
        requestHandler.prepareConnection(urlConnection, httpMethod, contentType);
    }

    protected int writeOutputStream(HttpURLConnection uc, Object content) throws IOException {
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

    protected Object readInputStream(HttpURLConnection uc) throws IOException {
        InputStream in = null;
        Object response = null;
        try {
            in = uc.getInputStream();
            if (in != null) {
                response = requestHandler.readStream(in);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    /**
     * Convenience method creates a Map to hold query params
     * 
     * @return Map queryParams
     */
    public Map<String, String> newQueryParams() {
        return new HashMap<String, String>();
    }

    /**
     * Returns URL encoded data
     * 
     * @return
     */
    public String urlEncode(Map<String, String> params) {
        if (params == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key);
            String value = params.get(key);
            if (value != null) {
                sb.append("=");
                sb.append(URLEncoder.encode(value));
            }
        }
        return sb.toString();
    }

    /**
     * Initializes the app-wide CookieManager that will be used for all
     * requests.
     */
    private static void ensureCookieManager() {
        if (CookieHandler.getDefault() == null) {
            CookieHandler.setDefault(new CookieManager());
        }
    }

    /**
     * Returns the {@link CookieManager} associated with this client.
     * 
     * @return CookieManager
     */
    public static CookieManager getCookieManager() {
        return (CookieManager) CookieHandler.getDefault();
    }

    /**
     * Setter for the {@link RequestHandler}.
     *  
     * @param handler
     */
    public void setRequestHandler(RequestHandler handler) {
        this.requestHandler = handler;
    }

    /**
     * Setter for the {@link RequestLogger}.
     * 
     * @param logger
     */
    public void setRequestLogger(RequestLogger logger) {
        this.requestLogger = logger;
    }

}