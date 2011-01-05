package com.listwidget.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.listwidget.shared.proxy.ItemListProxy;

public interface ListsView extends IsWidget
{
	interface Presenter
	{
		void persistList(String listName);
	}

	void setPresenter(Presenter p);

	void populateLists(List<ItemListProxy> itemLists);
}
