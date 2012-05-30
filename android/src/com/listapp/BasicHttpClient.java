
package com.listapp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Basic HTTP client that facilitates simple GET and POST requests using
 * HttpUrlConnection.
 * 
 * @author David M. Chandler
 */
public class BasicHttpClient {

    /**
     * Interface hook whereby callers can get access to properties of the
     * HttpUrlConnection and I/O streams. This might be used to check response
     * code, set a content type, or stream content from a source other than the
     * parameter maps. Register with
     * {@link BasicHttpClient#setRequestInterceptor(RequestInterceptor)}.
     * 
     * @author David M. Chandler
     */
    public interface RequestInterceptor {
        /**
         * Called before writing to the OutputStream, so it's possible to set or
         * modify connection properties. This method may be empty. If the
         * implementation completes writing to the OutputStream, it should
         * return true so that BasicHttpClient can close it without reading.
         * 
         * @param urlConnection Exposes the current connection
         * @return boolean True if the method wrote to the OutputStream
         * @throws IOException
         */
        boolean onBeforeWrite(HttpURLConnection urlConnection, String content) throws IOException;

        /**
         * Called before reading from the InputStream, so it's possible to check
         * response code or use a custom reader. This method may be empty. If
         * the implementation consumes the InputStream, it should return true so
         * that BasicHttpClient can close it without reading.
         * 
         * @param urlConnection Exposes the current connection
         * @param in Exposes the open InputStream
         * @return boolean True if the method consumed the InputStream
         * @throws IOException
         */
        boolean onBeforeRead(HttpURLConnection urlConnection) throws IOException;
    }

    private static final String TAG = BasicHttpClient.class.getName();
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
    private RequestInterceptor requestInterceptor;

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

    public String get(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + "?" + queryString, GET, null);
    }

    public String post(String path, Map<String, String> params) {
        String data = null;
        if (params != null) {
            data = urlEncode(params);
        }
        return doHttpMethod(path, POST, data);
    }

    public String put(String path, String content) {
        return doHttpMethod(path, PUT, content);
    }

    public String delete(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + queryString, DELETE, null);
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
        uc.setReadTimeout(2000);
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
                    break;

                default:
                    throw new IllegalArgumentException("Unknown HTTP method");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }

        try {
            if (uc.getDoOutput() && content != null) {
                writeOutputStream(content);
            }
            resBody = readInputStream();
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
        return resBody;
    }

    protected String readInputStream() {
        InputStream in = null;
        String response = null;
        try {
            in = uc.getInputStream();
            boolean readComplete = false;
            if (this.requestInterceptor != null) {
                // Invoke interceptor
                readComplete = this.requestInterceptor.onBeforeRead(uc);
            }
            if (!readComplete) {
                response = read(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Try to read error stream
            // response = readErrorStream();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    protected String readErrorStream() {
        String response = null;
        InputStream err = uc.getErrorStream();
        response = read(err);
        return response;
    }

    protected void writeOutputStream(String content) {
        OutputStream out = null;
        try {
            boolean writeComplete = false;
            if (this.requestInterceptor != null) {
                // Invoke interceptor
                writeComplete = requestInterceptor.onBeforeWrite(uc, content);
            }
            if (!writeComplete) {
                out = uc.getOutputStream();
                out.write(content.getBytes(UTF8));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
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
    private String urlEncode(Map<String, String> params) {
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
        }
    }

    public void setRequestInterceptor(RequestInterceptor interceptor) {
        this.requestInterceptor = interceptor;
    }

    private void logHeaders(Map<String, List<String>> map) {
        for (String field : map.keySet()) {
            List<String> headers = map.get(field);
            for (String header : headers) {
                Log.i(TAG, field + ":" + header);
            }
        }
    }

}
