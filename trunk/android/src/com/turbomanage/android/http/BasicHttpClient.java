
package com.turbomanage.android.http;

import java.util.Map;

/**
 * Basic HTTP client that facilitates simple GET, POST, PUT, and DELETE requests. To handle 
 * response types other than String, extend {@link AbstractHttpClient} instead of this class.
 * To implement buffering, streaming, or set other request properties, set an alternate 
 * {@link RequestHandler}.
 * 
 * @author David M. Chandler
 */
public class BasicHttpClient extends AbstractHttpClient {

    /**
     * Constructs a new client using the default {@link RequestHandler} and
     * {@link RequestLogger}.
     * 
     * @see AbstractHttpClient
     */
    public BasicHttpClient(String baseUrl) {
        super(baseUrl);
        setRequestHandler(new BasicRequestHandler());
        setRequestLogger(new BasicRequestLogger());
    }

    /** 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @see com.turbomanage.android.http.AbstractHttpClient#get(java.lang.String, java.util.Map)
     */
    @Override
    public String get(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + "?" + queryString, HttpMethod.GET, null, null).toString();
    }

    /**
     * The supplied parameters are URL encoded and sent as the request content.
     * 
     * @see com.turbomanage.android.http.AbstractHttpClient#post(java.lang.String, java.util.Map)
     */
    @Override
    public String post(String path, Map<String, String> params) {
        String data = null;
        if (params != null) {
            data = urlEncode(params);
        }
        return doHttpMethod(path, HttpMethod.POST, URLENCODED, data).toString();
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#put(java.lang.String, java.lang.Object)
     */
    @Override
    public String put(String path, Object content) {
        return doHttpMethod(path, HttpMethod.PUT, MULTIPART, content).toString();
    }

    /**
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @see com.turbomanage.android.http.AbstractHttpClient#delete(java.lang.String, java.util.Map)
     * 
     * @param path
     * @param params
     * @return Response object
     */
    @Override
    public String delete(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + queryString, HttpMethod.DELETE, null, null).toString();
    }

}