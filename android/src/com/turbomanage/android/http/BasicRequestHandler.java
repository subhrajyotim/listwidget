
package com.turbomanage.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Default {@link RequestHandler} used by {@link BasicHttpClient}. It is intended to be
 * used for simple requests with small amounts of data only (a few kB), as it does no
 * buffering, chunking, streaming, etc.
 * 
 * Only character set supported is UTF-8.
 * Only {@link String} content is supported.
 * All responses are treated as {@link String}s.
 * 
 * @author David M. Chandler
 */
public class BasicRequestHandler implements RequestHandler {

    public static final String UTF8 = "UTF-8";
    
    /**
     * Opens the connection.
     * 
     * @see com.turbomanage.android.http.RequestHandler#openConnection(java.lang.String)
     */
    @Override
    public HttpURLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        return uc;
    }

    /** 
     * Sets request properties based on the request method. Override this method to modify 
     * or set additional properties.
     * 
     * @see com.turbomanage.android.http.RequestHandler#prepareConnection(java.net.HttpURLConnection, com.turbomanage.android.http.HttpMethod, java.lang.String)
     */
    @Override
    public void prepareConnection(HttpURLConnection urlConnection, HttpMethod httpMethod,
            String contentType) throws IOException {
        urlConnection.setRequestProperty("Accept-Charset", UTF8);
        // TODO Make this configurable
        urlConnection.setReadTimeout(2000);
        urlConnection.setRequestMethod(httpMethod.getMethodName());
        urlConnection.setDoOutput(httpMethod.getDoOutput());
        if (contentType != null) {
            urlConnection.setRequestProperty("Content-Type", contentType);
        }
    }

    /**
     * Writes a {@link String}. Override this method to handle other content types.
     * 
     * @see com.turbomanage.android.http.RequestHandler#writeStream(java.io.OutputStream, java.lang.Object)
     */
    @Override
    public void writeStream(OutputStream out, Object content) throws IOException {
        String data = (String) content;
        out.write(data.getBytes(UTF8));
    }

    /**
     * Reads the input into a {@link String}. Override this method to handle other
     * content types.
     * 
     * @see com.turbomanage.android.http.RequestHandler#readStream(java.io.InputStream)
     */
    @Override
    public Object readStream(InputStream in) throws IOException {
        return read(in);
    }

    /**
     * Just prints a stack trace.
     * 
     * @see com.turbomanage.android.http.RequestHandler#onError(java.net.HttpURLConnection, java.lang.Exception)
     */
    @Override
    public void onError(HttpURLConnection uc, Exception e) {
        e.printStackTrace();
    }

    /**
     * Reads an {@link InputStream} into a {@link String} using {@link Scanner}.
     */
    private String read(InputStream in) {
        try {
            return new Scanner(in).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

}
