package com.listwidget.client.ui;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.listwidget.shared.proxy.ListItemProxy;
import com.listwidget.shared.proxy.ItemListProxy.ListType;

public interface EditListView extends IsWidget
{
	interface Presenter
	{
		void addItem(String string);
		void updateListName(String value);
	}
	void setPresenter(Presenter p);

	HasValue<String> getListName();
	HasData<ListItemProxy> getDataTable();
	Column<ListItemProxy,String> getItemTextColumn();
}