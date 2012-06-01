
package com.turbomanage.android.http;


/**
 * Basic HTTP client that facilitates simple GET, POST, PUT, and DELETE
 * requests. To handle response types other than String, extend
 * {@link AbstractHttpClient} instead of this class. To implement buffering,
 * streaming, or set other request properties, set an alternate
 * {@link RequestHandler}.
 * 
 * @author David M. Chandler
 */
/**
 * @author David M. Chandler
 *
 */
public class BasicHttpClient extends AbstractHttpClient {

    /**
     * Constructs a new client with empty baseUrl. When used this way, the path passed
     * to a request method must be the complete URL.
     */
    public BasicHttpClient() {
        this("");
    }

    /**
     * Constructs a new client using the default {@link RequestHandler} and
     * {@link RequestLogger}.
     */
    public BasicHttpClient(String baseUrl) {
        super(baseUrl);
        setRequestHandler(new AbstractRequestHandler() {});
        setRequestLogger(new ConsoleRequestLogger());
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#get(java.lang.String, com.turbomanage.android.http.ParameterMap)
     */
    @Override
    public HttpResponse get(String path, ParameterMap params) {
        String queryString = null;
        if (params != null) {
            queryString = params.urlEncode();
        }
        return doHttpMethod(path + "?" + queryString, HttpMethod.GET, null, null);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#post(java.lang.String, com.turbomanage.android.http.ParameterMap)
     */
    @Override
    public HttpResponse post(String path, ParameterMap params) {
        byte[] data = null;
        if (params != null) {
            data = params.urlEncodedBytes();
        }
        return doHttpMethod(path, HttpMethod.POST, URLENCODED, data);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#post(java.lang.String, java.lang.String, byte[])
     */
    @Override
    public HttpResponse post(String path, String contentType, byte[] data) {
        return doHttpMethod(path, HttpMethod.POST, contentType, data);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#put(java.lang.String, java.lang.String, byte[])
     */
    @Override
    public HttpResponse put(String path, String contentType, byte[] data) {
        return doHttpMethod(path, HttpMethod.PUT, contentType, data);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.AbstractHttpClient#delete(java.lang.String, com.turbomanage.android.http.ParameterMap)
     */
    @Override
    public HttpResponse delete(String path, ParameterMap params) {
        String queryString = null;
        if (params != null) {
            queryString = params.urlEncode();
        }
        return doHttpMethod(path + queryString, HttpMethod.DELETE, null, null);
    }

}
