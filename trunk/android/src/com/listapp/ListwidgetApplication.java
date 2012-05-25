package com.listapp;

import android.app.Application;
import android.widget.ArrayAdapter;

import java.net.CookieHandler;
import java.net.CookieManager;

public class ListwidgetApplication extends Application {
	private ArrayAdapter<String> adapter;
    private CookieManager cookieManager;
	
	public ArrayAdapter<String> getAdapter(ListappActivity activity) {
		if (adapter == null) {
			adapter = new ArrayAdapter<String>(activity, R.layout.listitem);
		}
		return adapter;
	}

    public CookieManager getCookieManager() {
        if (this.cookieManager == null) {
            this.cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
        }
        return this.cookieManager; 
    }
	
//	public ListwidgetRequestFactory getRequestFactory() {
//	    if (this.rf == null) {
//	        this.rf = Util.getRequestFactory(getApplicationContext(), ListwidgetRequestFactory.class);
//	    }
//	    return this.rf;
//	}
}
