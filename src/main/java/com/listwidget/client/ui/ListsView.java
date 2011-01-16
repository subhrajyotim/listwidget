package com.listwidget.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.listwidget.shared.proxy.ItemListProxy;

public interface ListsView extends IsWidget
{
	interface Presenter
	{
		void persistList(String listName);
	}

	void setPresenter(Presenter p);

	HasData<ItemListProxy> getDisplay();
}
