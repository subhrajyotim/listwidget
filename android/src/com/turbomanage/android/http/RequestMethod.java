package com.turbomanage.android.http;

public enum RequestMethod {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false);
    
    private boolean doOutput;
    
    RequestMethod(boolean doOutput) {
        this.doOutput = doOutput;
    }
    
    public boolean getDoOutput() {
        return this.doOutput;
    }
    
    public String getMethodName() {
        return this.toString();
    }
}
