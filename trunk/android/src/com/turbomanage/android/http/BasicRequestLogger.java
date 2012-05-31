package com.turbomanage.android.http;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Default {@link RequestLogger} used by {@link BasicHttpClient}.
 *  
 * @author David M. Chandler
 */
public class BasicRequestLogger implements RequestLogger {

    private static final String TAG = RequestLogger.class.getSimpleName();

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.RequestLogger#isLoggingEnabled()
     */
    public boolean isLoggingEnabled() {
        return Log.isLoggable(TAG, Log.INFO);
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.RequestLogger#logRequest(java.net.HttpURLConnection, java.lang.Object)
     */
    @Override
    public void logRequest(HttpURLConnection uc, Object content) throws IOException {
        Log.i(TAG, "=== HTTP Request ===");
        Log.i(TAG, "send url: " + uc.getURL().toString());
        if (content instanceof String) {
            Log.i(TAG, "Content: " + (String) content);
        }
        logHeaders(uc.getRequestProperties());
    }

    /* (non-Javadoc)
     * @see com.turbomanage.android.http.RequestLogger#logResponse(java.net.HttpURLConnection)
     */
    @Override
    public void logResponse(HttpURLConnection uc) throws IOException {
        Log.i(TAG, "=== HTTP Response ===");
        Log.i(TAG, "receive url: " + uc.getURL().toString());
        Log.i(TAG, "status: " + uc.getResponseCode());
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
                Log.i(TAG, field + ":" + header);
            }
        }
    }

}
