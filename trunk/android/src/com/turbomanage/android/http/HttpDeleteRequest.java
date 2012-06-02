package com.turbomanage.android.http;

public class HttpDeleteRequest extends HttpRequest {

    private String path;
    private ParameterMap params;

    public HttpDeleteRequest(AbstractHttpClient client, String path, ParameterMap params) {
        super(client);
        this.path = path;
        this.params = params;
    }

    @Override
    public HttpResponse execute() {
        String queryString = null;
        if (params != null) {
            queryString = params.urlEncode();
        }
        return httpClient.doHttpMethod(path + "?" + queryString, HttpMethod.DELETE, null, null);
    }

}
