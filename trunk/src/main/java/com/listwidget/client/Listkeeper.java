package com.listwidget.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.listwidget.client.mvp.AppActivityMapper;
import com.listwidget.client.mvp.ListsPlace;

public class Listkeeper implements EntryPoint
{
	private ClientFactory clientFactory = GWT.create(ClientFactory.class);
	private Place defaultPlace = new ListsPlace();
	private SimplePanel appWidget = new SimplePanel();

	@Override
	public void onModuleLoad()
	{
		/**
		 * This is the entry point method.
		 */
		// Create ClientFactory using deferred binding so we can replace with
		// different
		// impls in gwt.xml
		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();

		// Start ActivityManager for the main widget with our ActivityMapper
		ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
		activityManager.setDisplay(appWidget);

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		PlaceHistoryMapper historyMapper = clientFactory.getHistoryMapper();
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		RootPanel.get().add(appWidget);
		// Goes to place represented on URL or default place
		historyHandler.handleCurrentHistory();
		
		// Hide wait GIF
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));

	}

}