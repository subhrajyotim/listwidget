package com.listwidget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.listwidget.client.mvp.AppPlaceHistoryMapper;
import com.listwidget.client.ui.EditListView;
import com.listwidget.client.ui.ListsView;
import com.listwidget.client.ui.desktop.DesktopApp;
import com.listwidget.client.ui.desktop.EditListViewImpl;
import com.listwidget.client.ui.desktop.ListsViewImpl;
import com.listwidget.shared.service.ListwidgetRequestFactory;

public class ClientFactoryImpl implements ClientFactory {
	private final EventBus eventBus = new SimpleEventBus();
	private final PlaceController placeController = new PlaceController(
			eventBus);
	private final PlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
	private final ListwidgetRequestFactory rf = GWT.create(ListwidgetRequestFactory.class);
	private final ListsView listsView = new ListsViewImpl(this);
	private final EditListView editListView = new EditListViewImpl();
	private final ListwidgetApp app = new DesktopApp(this);

	public ClientFactoryImpl()
	{
		rf.initialize(eventBus);
//		this.tokenizerFactory = new TokenizerFactory(this);
//		historyMapper.setFactory(tokenizerFactory);
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public ListsView getListsView()
	{
		return listsView;
	}

	@Override
	public ListwidgetRequestFactory getRequestFactory()
	{
		return rf;
	}

	@Override
	public PlaceHistoryMapper getHistoryMapper()
	{
		return historyMapper;
	}

	@Override
	public EditListView getEditListView()
	{
		return editListView;
	}

	@Override
	public ListwidgetApp getApp()
	{
		return app;
	}
}
