package com.listapp;

import android.os.AsyncTask;

import com.listapp.model.ListItem;

import java.util.List;

public class FetchItemsTask extends AsyncTask<Void, Void, List<ListItem>> {

	private ViewListActivity activity;
	private String listId;
	
	public FetchItemsTask(ViewListActivity a) {
		activity = a;
		listId = a.getIntent().getExtras().getString("listId");
	}
	
	@Override
	protected List<ListItem> doInBackground(Void... params) {
        return null;
//		final List<ListItem> items = new ArrayList<ListItem>();
//		ListwidgetRequestFactory rf = Util.getRequestFactory(activity, ListwidgetRequestFactory.class);
//		EntityProxyId<NamedList> proxyId = rf.getProxyId(listId);
//		Request<NamedList> listReq = rf.find(proxyId).with("items");
//		listReq.fire(new Receiver<NamedList>() {
//			@Override
//			public void onSuccess(NamedList list) {
//				items.addAll(list.getItems());
//			}
//		});
//		return items;
	}

	@Override
	protected void onPostExecute(List<ListItem> result) {
		super.onPostExecute(result);
//		activity.setItems(result);
	}
}