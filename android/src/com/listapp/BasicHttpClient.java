
package com.listapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Basic HTTP client that facilitates simple GET and POST requests using
 * HttpUrlConnection.
 * 
 * @author drfibonacci
 */
public class BasicHttpClient {

    public static final String UTF8 = "UTF-8";
    public static final String URLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String MULTIPART = "multipart/form-data";
    private static final int GET = 0;
    private static final int POST = 1;
    private static final int PUT = 2;
    private static final int DELETE = 3;

    private static CookieManager COOKIE_MANAGER;

    private String baseUrl;

    private HttpURLConnection uc;

    static {
        ensureCookieManager();
    }
    
    /**
     * Constructs a new client with base URL that can be appended in the get()
     * or post() methods. Initializes the CookieManager. Should not end in "/".
     * 
     * @param baseUrl
     */
    public BasicHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String doGet(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = getQueryString(params);
        }
        return doHttpMethod(path, GET, queryString);
    }

    public String doPost(String path, Map<String, String> params) {
        String data = null;
        if (params != null) {
            data = getQueryString(params);
        }
        return doHttpMethod(path, POST, data);
    }

    public String doPut(String path, String content) {
        return doHttpMethod(path, POST, content);
    }

    public String doDelete(String path, Map<String, String> params) {
        String data = null;
        if (params != null) {
            data = getQueryString(params);
        }
        return doHttpMethod(path, POST, data);
    }

    /**
     * Makes a request to the server
     * 
     * @param path
     * @param httpMethod
     * @param content
     * @return String response body
     */
    protected String doHttpMethod(String path, int httpMethod, String content) {

        OutputStream out = null;
        InputStream in = null;
        String resBody = null;

        // Just in case
        uc = null;

        try {
            URL url = new URL(baseUrl + path);
            uc = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        uc.setRequestProperty("Accept-Charset", UTF8);
        uc.setReadTimeout(5000);
        try {
            switch (httpMethod) {
                case GET:
                    uc.setRequestMethod("GET");
                    break;
                case POST:
                    uc.setRequestMethod("POST");
                    uc.setDoOutput(true);
                    uc.setRequestProperty("Content-Type", URLENCODED);
                    break;
                case PUT:
                    uc.setRequestMethod("PUT");
                    uc.setDoOutput(true);
                    break;
                case DELETE:
                    uc.setRequestMethod("DELETE");
                    uc.setDoOutput(true);
                    uc.setRequestProperty("Content-Type", URLENCODED);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown HTTP method");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }

        int contentLength = (content == null) ? 0 : content.length(); 
        if (uc.getDoOutput() && contentLength > 0) {
            try {
                uc.setFixedLengthStreamingMode(content.length());
                out = uc.getOutputStream();
                out.write(content.getBytes(UTF8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close out before opening in to tell server request is
                // complete
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

        // Read response
        try {
            in = uc.getInputStream();
            resBody = read(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (uc != null) {
                uc.disconnect();
            }
        }

        return resBody;
    }

    /**
     * Allows callers of BasicHttpClient to obtain information about the
     * response
     * 
     * @return
     */
    public HttpURLConnection getConnection() {
        return uc;
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
    private String getQueryString(Map<String, String> params) {
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
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler == null) {
            COOKIE_MANAGER = new CookieManager();
            CookieHandler.setDefault(COOKIE_MANAGER);
        }
    }

    public static CookieManager getCookieManager() {
        return COOKIE_MANAGER;
    }

    public String read(InputStream in) {
        try {
            return new Scanner(in).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
