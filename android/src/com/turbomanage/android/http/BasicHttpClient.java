
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
        RequestHandler defaultRequestHandler = new BasicRequestHandler();
        setHttpErrorHandler(defaultRequestHandler);
        setHttpOpener(defaultRequestHandler);
        setHttpPreparer(defaultRequestHandler);
        setHttpReader(defaultRequestHandler);
        setHttpWriter(defaultRequestHandler);
        setRequestLogger(new BasicRequestLogger());
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#get(java.lang.String, java.util.Map)
     */
    @Override
    public String get(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + "?" + queryString, RequestMethod.GET, null, null).toString();
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#post(java.lang.String, java.util.Map)
     */
    @Override
    public String post(String path, Map<String, String> params) {
        String data = null;
        if (params != null) {
            data = urlEncode(params);
        }
        return doHttpMethod(path, RequestMethod.POST, URLENCODED, data).toString();
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#post(java.lang.String, byte[], java.lang.String)
     */
    @Override
    public Object post(String path, String contentType, Object data) {
        return doHttpMethod(path, RequestMethod.POST, contentType, data);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#put(java.lang.String, byte[], java.lang.String)
     */
    @Override
    public Object put(String path, String contentType, Object data) {
        return doHttpMethod(path, RequestMethod.PUT, contentType, data);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#delete(java.lang.String, java.util.Map)
     */
    @Override
    public String delete(String path, Map<String, String> params) {
        String queryString = null;
        if (params != null) {
            queryString = urlEncode(params);
        }
        return doHttpMethod(path + queryString, RequestMethod.DELETE, null, null).toString();
    }

}