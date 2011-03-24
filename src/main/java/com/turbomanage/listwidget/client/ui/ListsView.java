package com.turbomanage.listwidget.client.ui;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.turbomanage.listwidget.shared.proxy.ItemListProxy;

public interface ListsView extends IsWidget
{
	interface Presenter
	{
		void persistList(String listName);

		void removeList(ItemListProxy list);
	}

	void setPresenter(Presenter p);
	
	/**
	 * Exposes CellTable to the presenter so the AsyncDataProvider can update the table.
	 * Uses the {@link HasData} interface so as not to leak a Widget into the presenter. 
	 * @return
	 */
	HasData<ItemListProxy> getDataTable();
	
	/**
	 * Exposes column model to the presenter so it can add a field updater
	 * @return List name column
	 */
	Column<ItemListProxy,String> getNameColumn();
}
