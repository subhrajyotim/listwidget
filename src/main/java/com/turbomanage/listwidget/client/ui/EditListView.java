package com.turbomanage.listwidget.client.ui;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.turbomanage.listwidget.shared.proxy.ListItemProxy;

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
