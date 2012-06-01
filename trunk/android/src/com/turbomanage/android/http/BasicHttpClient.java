
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

    /**
     * Execute a GET request and return the response.
     * 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public HttpResponse get(String path, ParameterMap params) {
        String queryString = null;
        if (params != null) {
            queryString = params.urlEncode();
        }
        return doHttpMethod(path + "?" + queryString, HttpMethod.GET, null, null);
    }

    /**
     * Execute a POST request with parameter map and return the response.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public HttpResponse post(String path, ParameterMap params) {
//        byte[] data = null;
//        if (params != null) {
//            data = params.urlEncodedBytes();
//        }
//        return doHttpMethod(path, HttpMethod.POST, URLENCODED, data);
        return new HttpPostRequest(this, path, params).execute();
    }

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
    public HttpResponse post(String path, String contentType, byte[] data) {
        return doHttpMethod(path, HttpMethod.POST, contentType, data);
    }

    /**
     * Execute a PUT request with the supplied content and return the response.
     * 
     * @param path
     * @param contentType
     * @param data
     * @return Response object
     */
    public HttpResponse put(String path, String contentType, byte[] data) {
        return doHttpMethod(path, HttpMethod.PUT, contentType, data);
    }

    /**
     * Execute a DELETE request and return the response.
     * 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @return Response object
     */
    public HttpResponse delete(String path, ParameterMap params) {
        String queryString = null;
        if (params != null) {
            queryString = params.urlEncode();
        }
        return doHttpMethod(path + queryString, HttpMethod.DELETE, null, null);
    }

}
