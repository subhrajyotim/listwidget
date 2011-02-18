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
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.SimplePanel;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.ListwidgetApp;
import com.listwidget.client.mvp.AppActivityMapper;
import com.listwidget.client.mvp.CenterActivityMapper;
import com.listwidget.client.mvp.ListsPlace;
import com.listwidget.client.mvp.WestActivityMapper;

public class DesktopApp implements ListwidgetApp
{
	private Place defaultPlace = new ListsPlace();
	private final ClientFactory clientFactory;

	public DesktopApp(ClientFactory clientFactory)
	{
		this.clientFactory = clientFactory;
	}
	
	public Widget getAppWidget()
	{
		EventBus eventBus = clientFactory.getEventBus();
		PlaceController placeController = clientFactory.getPlaceController();
		
		/*
		 * The new DockLayoutPanel attaches to the whole browser window because it
		 * uses absolute positioning to get improved efficiency in the browser.
		 * But we want to run the GWT app in a div (not the whole page), so we have
		 * to use the older DockPanel instead.
		 */
		DockPanel dockPanel = new DockPanel();
		dockPanel.setSpacing(20);
		
		SimplePanel westPanel = new SimplePanel();
		dockPanel.add(westPanel, DockPanel.WEST);
//		dockLayoutPanel.addWest(westPanel, 20);
		
		SimplePanel centerPanel = new SimplePanel();
		dockPanel.add(centerPanel, DockPanel.CENTER);
		
		// Start ActivityManager for the nav (west) panel with our WestActivityMapper
		ActivityMapper westActivityMapper = new WestActivityMapper(clientFactory);
		ActivityManager westActivityManager = new ActivityManager(westActivityMapper, eventBus);
		westActivityManager.setDisplay(westPanel);
		
		// Start ActivityManager for the main (center) panel with our CenterActivityMapper
		ActivityMapper centerActivityMapper = new CenterActivityMapper(clientFactory);
		ActivityManager centerActivityManager = new ActivityManager(centerActivityMapper, eventBus);
		centerActivityManager.setDisplay(centerPanel);
		
		return dockPanel;
	}

}
