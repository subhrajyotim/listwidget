package com.turbomanage.android.http;

public class HttpGetRequest extends HttpRequest {

    private String path;
    private ParameterMap params;

    public HttpGetRequest(AbstractHttpClient client, String path, ParameterMap params) {
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
        return httpClient.doHttpMethod(path + "?" + queryString, HttpMethod.GET, null, null);
    }

}
