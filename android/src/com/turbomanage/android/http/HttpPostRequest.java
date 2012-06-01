package com.turbomanage.android.http;

public class HttpPostRequest extends HttpRequest {

    private String path;
    private ParameterMap params;

    public HttpPostRequest(AbstractHttpClient client, String path, ParameterMap params) {
        super(client);
        this.path = path;
        this.params = params;
    }

    @Override
    public HttpResponse execute() {
        byte[] data = null;
        if (params != null) {
            data = params.urlEncodedBytes();
        }
        return httpClient.doHttpMethod(path, HttpMethod.POST, URLENCODED, data);
    }

}
