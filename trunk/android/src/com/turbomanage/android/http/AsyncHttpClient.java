package com.turbomanage.android.http;

/**
 * An HTTP client that completes all requests asynchronously using
 * {@link DoHttpRequestTask}. All methods take a callback argument which
 * is passed to the task and whose methods are invoked when the request
 * completes or throws on exception.
 * 
 * @author David M. Chandler
 */
public class AsyncHttpClient extends AbstractHttpClient {

    /**
     * Constructs a new client with empty baseUrl. When used this way, the path passed
     * to a request method must be the complete URL.
     */
    public AsyncHttpClient() {
        this("");
    }
    
    /**
     * Constructs a new client using the default {@link RequestHandler} and
     * {@link RequestLogger}.
     */
    public AsyncHttpClient(String baseUrl) {
        super(baseUrl);
        setRequestHandler(new AbstractRequestHandler() {
        });
        setRequestLogger(new ConsoleRequestLogger());
    }

    /**
     * Execute a GET request and invoke the callback on completion.
     * 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @param callback
     */
    public void get(String path, ParameterMap params, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpRequest req = new HttpGetRequest(this, path, params);
        task.execute(req);
    }

    /**
     * Execute a POST request with parameter map and invoke the callback on completion.
     * 
     * @param path
     * @param params
     * @param callback
     */
    public void post(String path, ParameterMap params, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpRequest req = new HttpPostRequest(this, path, params);
        task.execute(req);
    }

    /**
     * Execute a POST request with a chunk of data and invoke the callback on completion.
     * 
     * @param path
     * @param contentType
     * @param data
     * @param callback
     */
    public void post(String path, String contentType, byte[] data, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpPostRequest req = new HttpPostRequest(this, path, contentType, data);
        task.execute(req);
    }

    /**
     * Execute a PUT request with the supplied content and invoke the callback on completion.
     * 
     * @param path
     * @param contentType
     * @param data
     * @param callback
     */
    public void put(String path, String contentType, byte[] data, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpPutRequest req = new HttpPutRequest(this, path, contentType, data);
        task.execute(req);
    }

    /**
     * Execute a DELETE request and invoke the callback on completion.
     * 
     * The supplied parameters are URL encoded and sent as the query string.
     * 
     * @param path
     * @param params
     * @param callback
     */
    public void delete(String path, ParameterMap params, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpDeleteRequest req = new HttpDeleteRequest(this, path, params);
        task.execute(req);
    }

}
