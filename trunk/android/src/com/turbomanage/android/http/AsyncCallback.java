package com.turbomanage.android.http;

public abstract class AsyncCallback {

    public abstract void onSuccess(HttpResponse httpResponse);
    
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
