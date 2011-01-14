package com.listwidget.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.listwidget.client.ui.EditListView;
import com.listwidget.client.ui.ListsView;
import com.listwidget.shared.service.ListwidgetRequestFactory;

public interface ClientFactory {
	EventBus getEventBus();

	PlaceController getPlaceController();

	PlaceHistoryMapper getHistoryMapper();

	ListsView getListsView();

	EditListView getEditListView();

	ListwidgetRequestFactory getRequestFactory();
}
