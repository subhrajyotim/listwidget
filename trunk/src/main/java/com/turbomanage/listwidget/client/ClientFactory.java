package com.turbomanage.listwidget.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.turbomanage.listwidget.client.ui.EditListView;
import com.turbomanage.listwidget.client.ui.ListsView;
import com.turbomanage.listwidget.shared.service.ListwidgetRequestFactory;

public interface ClientFactory {
	EventBus getEventBus();

	PlaceController getPlaceController();

	PlaceHistoryMapper getHistoryMapper();

	ListsView getListsView();

	EditListView getEditListView();

	ListwidgetRequestFactory getRequestFactory();

	ListwidgetApp getApp();
}
