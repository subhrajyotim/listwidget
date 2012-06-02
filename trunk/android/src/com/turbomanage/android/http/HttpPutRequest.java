package com.turbomanage.android.http;

public class HttpPutRequest extends HttpRequest {

    private String path;
    private String contentType;
    private byte[] data;

    public HttpPutRequest(AbstractHttpClient client, String path, String contentType, byte[] data) {
        super(client);
        this.path = path;
        this.contentType = contentType;
        this.data = data;
    }

    @Override
    public HttpResponse execute() {
        return httpClient.doHttpMethod(path, HttpMethod.POST, contentType, data);
    }

}
