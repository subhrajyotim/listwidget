package com.turbomanage.android.http;

public abstract class TaskCallback {

    public abstract void onSuccess(HttpResponse httpResponse);
    
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
