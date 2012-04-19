package com.listapp;

import android.app.Application;
import android.widget.ArrayAdapter;

public class ListwidgetApplication extends Application {
	private ArrayAdapter<String> adapter;
	
	public ArrayAdapter<String> getAdapter(ListappActivity activity) {
		if (adapter == null) {
			adapter = new ArrayAdapter<String>(activity, R.layout.listitem);
		}
		return adapter;
	}
}
