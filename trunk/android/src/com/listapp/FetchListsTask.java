package com.listapp;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.google.web.bindery.event.shared.UmbrellaException;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.turbomanage.listwidget.shared.proxy.NamedListProxy;
import com.turbomanage.listwidget.shared.service.ListwidgetRequestFactory;

public class FetchListsTask extends AsyncTask<Void, Void, List<NamedListProxy>> {

	private static final String TAG = FetchListsTask.class.getName();
	private ListappActivity activity;
	private List<NamedListProxy> resultLists;
	private String errMsg;
	
	public FetchListsTask(ListappActivity activity) {
		this.activity = activity;
	}
	
	@Override
	protected List<NamedListProxy> doInBackground(Void... params) {
		
        ListwidgetRequestFactory requestFactory = Util.getRequestFactory(activity,
                ListwidgetRequestFactory.class);
        Request<List<NamedListProxy>> listAllRequest = requestFactory.namedListService().listAll();

        try {
			listAllRequest.fire(new Receiver<List<NamedListProxy>>() {

				@Override
				public void onSuccess(List<NamedListProxy> lists) {
					resultLists = lists;
				}
				
				@Override
				public void onFailure(ServerFailure error) {
					super.onFailure(error);
					errMsg = error.getMessage();
					Log.w(TAG, errMsg);
					cancel(true);
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof UmbrellaException) {
				errMsg = e.getCause().getLocalizedMessage();
			} else {
				errMsg = e.getLocalizedMessage();
			}
			cancel(true);
		} 
		return resultLists;
	}
	
	@Override
	protected void onPostExecute(List<NamedListProxy> result) {
		super.onPostExecute(result);
		activity.setLists(result);
	}
	
	@Override
	protected void onCancelled() {
		if ((errMsg != null) && (errMsg.contains("Log In"))) {
			activity.login();
		} else {
			activity.showError(errMsg);
		}
	}

}
