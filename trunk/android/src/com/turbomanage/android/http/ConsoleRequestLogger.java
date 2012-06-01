package com.turbomanage.android.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Default {@link RequestLogger} used by {@link BasicHttpClient}.
 * In recent versions of Android, System.out.println() gets directed to
 * LogCat so this can work for Android, too.
 * http://stackoverflow.com/questions/2220547/why-doesnt-system-out-println-work-in-android
 *  
 * @author David M. Chandler
 */
public class ConsoleRequestLogger implements RequestLogger {

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.RequestLogger#isLoggingEnabled()
     */
    public boolean isLoggingEnabled() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.RequestLogger#logRequest(java.net.HttpURLConnection, java.lang.Object)
     */
    @Override
    public void logRequest(HttpURLConnection uc, Object content) throws IOException {
        System.out.println("=== HTTP Request ===");
        System.out.println("send url: " + uc.getURL().toString());
        if (content instanceof String) {
            System.out.println("Content: " + (String) content);
        }
        logHeaders(uc.getRequestProperties());
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.RequestLogger#logResponse(java.net.HttpURLConnection)
     */
    @Override
    public void logResponse(HttpURLConnection uc) throws IOException {
        System.out.println("=== HTTP Response ===");
        System.out.println("receive url: " + uc.getURL().toString());
        System.out.println("status: " + uc.getResponseCode());
        logHeaders(uc.getHeaderFields());
    }

    /**
     * Iterate over request or response headers and log them.
     * 
     * @param map
     */
    private void logHeaders(Map<String, List<String>> map) {
        for (String field : map.keySet()) {
            List<String> headers = map.get(field);
            for (String header : headers) {
                System.out.println(field + ":" + header);
            }
        }
    }

}
