package com.listwidget.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.listwidget.client.ClientFactory;

public class WestActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;
	private ListsActivity listsActivity;

	/**
	 * AppActivityMapper associates each Place with its corresponding
	 * {@link Activity}
	 *
	 * @param clientFactory Factory to be passed to activities
	 */
	public WestActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
		this.listsActivity = new ListsActivity(clientFactory);
	}

	/**
	 * Map each Place to its corresponding Activity.
	 */
	@Override
	public Activity getActivity(Place place) {
		// Always return the listsActivity
		return listsActivity;
	}

}
