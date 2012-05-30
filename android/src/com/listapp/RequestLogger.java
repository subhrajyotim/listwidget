package com.listapp;

import android.util.Log;

import com.listapp.BasicHttpClient.RequestInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class RequestLogger implements RequestInterceptor {

    private static final String TAG = RequestLogger.class.getName();

    @Override
    public boolean onBeforeWrite(HttpURLConnection uc, String content)
            throws IOException {
        Log.i(TAG, "send url: " + uc.getURL().toString());
        logHeaders(uc.getRequestProperties());
        Log.i(TAG, "content: " + content);
        return false;
    }

    @Override
    public boolean onBeforeRead(HttpURLConnection uc) throws IOException {
        Log.i(TAG, "receive url: " + uc.getURL().toString());
        Log.i(TAG, "status: " + uc.getResponseCode());
        logHeaders(uc.getHeaderFields());
        return false;
    }

    private void logHeaders(Map<String, List<String>> map) {
        for (String field : map.keySet()) {
            List<String> headers = map.get(field);
            for (String header : headers) {
                Log.i(TAG, field + ":" + header);
            }
        }
    }

}
