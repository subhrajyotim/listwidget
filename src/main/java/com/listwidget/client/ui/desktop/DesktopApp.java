package com.listwidget.client.ui.desktop;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.activity.shared.CachingActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.SimplePanel;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.mvp.AppActivityMapper;
import com.listwidget.client.mvp.CenterActivityMapper;
import com.listwidget.client.mvp.ListsPlace;
import com.listwidget.client.mvp.WestActivityMapper;

public class DesktopApp implements EntryPoint
{
	private ClientFactory clientFactory = GWT.create(ClientFactory.class);
	private Place defaultPlace = new ListsPlace();

	public void onModuleLoad()
	{
		/**
		 * This is the entry point method.
		 */
		RootPanel rootPanel = RootPanel.get();
	
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PCT);
		rootPanel.add(dockLayoutPanel, 10, 10);
		
		SimplePanel westPanel = new SimplePanel();
		dockLayoutPanel.addWest(westPanel, 20);
		
		SimplePanel centerPanel = new SimplePanel();
		dockLayoutPanel.add(centerPanel);
		
		// Create ClientFactory using deferred binding so we can replace with
		// different
		// impls in gwt.xml
		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();
		
		// Start ActivityManager for the nav (west) panel with our WestActivityMapper
		ActivityMapper westActivityMapper = new WestActivityMapper(clientFactory);
		ActivityManager westActivityManager = new ActivityManager(westActivityMapper, eventBus);
		westActivityManager.setDisplay(westPanel);
		
		// Start ActivityManager for the main (center) panel with our CenterActivityMapper
		ActivityMapper centerActivityMapper = new CenterActivityMapper(clientFactory);
		ActivityManager centerActivityManager = new ActivityManager(centerActivityMapper, eventBus);
		centerActivityManager.setDisplay(centerPanel);
		
		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		PlaceHistoryMapper historyMapper = clientFactory.getHistoryMapper();
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		RootPanel.get().add(dockLayoutPanel);
		// Goes to place represented on URL or default place
		historyHandler.handleCurrentHistory();
		
		// Hide wait GIF
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
	}
}
