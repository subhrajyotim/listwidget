package com.listwidget.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.listwidget.client.mvp.AppPlaceHistoryMapper;
import com.listwidget.client.ui.EditListView;
import com.listwidget.client.ui.ListsView;
import com.listwidget.shared.service.ListwidgetRequestFactory;

public interface ClientFactory {
	EventBus getEventBus();

	PlaceController getPlaceController();

	AppPlaceHistoryMapper getHistoryMapper();

	ListsView getListsView();

	EditListView getEditListView();

	ListwidgetRequestFactory getRequestFactory();
}
