package com.turbomanage.listwidget.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.Place;
import com.turbomanage.listwidget.client.ClientFactory;

public class CenterActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 *
	 * @param clientFactory Factory to be passed to activities
	 */
	public CenterActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	/**
	 * Map each Place to its corresponding Activity.
	 */
	@Override
	public Activity getActivity(Place place) {
		if (place instanceof EditListPlace) {
			return new EditListActivity(clientFactory, (EditListPlace) place);
		}
		return null;
	}

}
