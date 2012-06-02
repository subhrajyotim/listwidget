package com.turbomanage.android.http;

public class HttpPostRequest extends HttpRequest {

    private String path;
    private ParameterMap params;
    private String contentType;
    private byte[] data;

    public HttpPostRequest(AbstractHttpClient client, String path, ParameterMap params) {
        super(client);
        this.path = path;
        this.params = params;
    }
    
    public HttpPostRequest(AbstractHttpClient client, String path, String contentType, byte[] data) {
        super(client);
        this.path = path;
        this.contentType = contentType;
        this.data = data;
    }

    @Override
    public HttpResponse execute() {
        if (params != null) {
            byte[] data = null;
            if (params != null) {
                data = params.urlEncodedBytes();
            }
            httpClient.doHttpMethod(path, HttpMethod.POST, URLENCODED, data);
        }
        else if (data != null) {
            return httpClient.doHttpMethod(path, HttpMethod.POST, contentType, data);
        }
        // TODO nothing to POST?
        return null;
    }

}
