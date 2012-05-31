
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
    private RequestHandler requestHandler;
    private RequestLogger requestLogger;

    /**
     * Constructs a new client with base URL that will be appended in the 
     * request methods. Example: http://turbomanage.com:80
     * 
     * @param baseUrl
     */
    public AbstractHttpClient(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            // Strip trailing slash
            baseUrl = baseUrl.substring(0, baseUrl.length()-1);
        }
        try {
            new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(baseUrl + " is not a valid URL", e);
        }
        this.baseUrl = baseUrl;
    }

    /**
     * Execute a GET request and return the response.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public abstract Object get(String path, Map<String, String> params);

    /**
     * Execute a POST request and return the response.
     * 
     * @param path
     * @param params
     * @return
     */
    public abstract Object post(String path, Map<String, String> params);

    /**
     * Execute a PUT request with the supplied content and return the response.
     * 
     * @param path
     * @param content
     * @return Response object
     */
    public abstract Object put(String path, Object content);

    /**
     * Execute a DELETE request and return the response.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public abstract Object delete(String path, Map<String, String> params);

    /**
     * Executes a request.
     * 
     * @param path Part of the URL after the host & port
     * @param httpMethod Request method
     * @param contentType MIME type of the request
     * @param content Request data
     * @return Response object
     */
    protected Object doHttpMethod(String path, HttpMethod httpMethod, String contentType, Object content) {
    
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

    protected void prepareConnection(HttpURLConnection urlConnection, HttpMethod httpMethod, String contentType) throws IOException {
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
