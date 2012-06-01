package com.turbomanage.android.http;


public class AsyncHttpClient extends AbstractHttpClient {

    public AsyncHttpClient() {
        this("");
    }
    
    public AsyncHttpClient(String baseUrl) {
        super(baseUrl);
        setRequestHandler(new AbstractRequestHandler() {
        });
        setRequestLogger(new ConsoleRequestLogger());
    }

    public void get(String path, ParameterMap params, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpRequest req = new HttpGetRequest(this, path, params);
        task.execute(req);
    }

    public void post(String path, ParameterMap params, TaskCallback callback) {
        DoHttpRequestTask task = new DoHttpRequestTask(callback);
        HttpRequest req = new HttpPostRequest(this, path, params);
        task.execute(req);
    }

    public void post(String path, String contentType, byte[] data, TaskCallback callback) {
        throw new RuntimeException("AsyncHttpClient must call the async version of this method");
    }

    public void put(String path, String contentType, byte[] data, TaskCallback callback) {
        throw new RuntimeException("AsyncHttpClient must call the async version of this method");
    }

    public void delete(String path, ParameterMap params, TaskCallback callback) {
        throw new RuntimeException("AsyncHttpClient must call the async version of this method");
    }

}
