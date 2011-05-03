package com.turbomanage.listwidget.client.ui;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.turbomanage.listwidget.shared.proxy.NamedListProxy;

public interface ListsView extends IsWidget
{
	interface Presenter
	{
		void persistList(String listName);

		void removeList(NamedListProxy list);
	}

	void setPresenter(Presenter p);
	
	/**
	 * Exposes CellTable to the presenter so the AsyncDataProvider can update the table.
	 * Uses the {@link HasData} interface so as not to leak a Widget into the presenter. 
	 * @return
	 */
	HasData<NamedListProxy> getDataTable();
	
	/**
	 * Exposes column model to the presenter so it can add a field updater
	 * @return List name column
	 */
	Column<NamedListProxy,String> getNameColumn();
}
