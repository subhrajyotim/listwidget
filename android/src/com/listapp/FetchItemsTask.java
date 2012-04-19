package com.listapp;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.turbomanage.listwidget.shared.proxy.ListItemProxy;
import com.turbomanage.listwidget.shared.proxy.NamedListProxy;
import com.turbomanage.listwidget.shared.service.ListwidgetRequestFactory;

public class FetchItemsTask extends AsyncTask<Void, Void, List<ListItemProxy>> {

	private ViewListActivity activity;
	private String listId;
	
	public FetchItemsTask(ViewListActivity a) {
		activity = a;
		listId = a.getIntent().getExtras().getString("listId");
	}
	
	@Override
	protected List<ListItemProxy> doInBackground(Void... params) {
		final List<ListItemProxy> items = new ArrayList<ListItemProxy>();
		ListwidgetRequestFactory rf = Util.getRequestFactory(activity, ListwidgetRequestFactory.class);
		EntityProxyId<NamedListProxy> proxyId = rf.getProxyId(listId);
		Request<NamedListProxy> listReq = rf.find(proxyId).with("items");
		listReq.fire(new Receiver<NamedListProxy>() {
			@Override
			public void onSuccess(NamedListProxy list) {
				items.addAll(list.getItems());
			}
		});
		return items;
	}

	@Override
	protected void onPostExecute(List<ListItemProxy> result) {
		super.onPostExecute(result);
		activity.setItems(result);
	}
}