package com.listwidget.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.listwidget.shared.proxy.ListItemProxy;
import com.listwidget.shared.proxy.ItemListProxy.ListType;

public interface EditListView extends IsWidget
{
	interface Presenter
	{
		void addItem(String string);
	}
	void setPresenter(Presenter p);

	void setListName(String name);

	void setListType(ListType listType);

	void populateItems(List<ListItemProxy> list);
}
