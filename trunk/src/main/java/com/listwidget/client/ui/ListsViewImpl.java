package com.listwidget.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.HasData;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.mvp.EditListPlace;
import com.listwidget.shared.proxy.ItemListProxy;

public class ListsViewImpl extends Composite implements ListsView
{
	VerticalPanel vPanel = new VerticalPanel();
	TextBox newListName = new TextBox();
	private Presenter presenter;
	private ClientFactory clientFactory;
	private CellTable<ItemListProxy> cellTable = new CellTable<ItemListProxy>();

	public ListsViewImpl(final ClientFactory clientFactory)
	{
		this.clientFactory = clientFactory;
		initView();
		vPanel.add(new Label("New List"));
		vPanel.add(newListName);
		initWidget(vPanel);
		
		// Note Flyweight pattern: only one instance of HyperlinkCell passed to the Column
		Column<ItemListProxy, Hyperlink> linkColumn = new Column<ItemListProxy, Hyperlink>(new HyperlinkCell())
		{
			@Override
			public Hyperlink getValue(ItemListProxy list)
			{
				String proxyToken = clientFactory.getRequestFactory().getHistoryToken(list.stableId());
				String historyToken = clientFactory.getHistoryMapper().getToken(new EditListPlace(proxyToken));
				Hyperlink h = new Hyperlink(list.getName(),historyToken);
				return h;
			}
		};
		cellTable.addColumn(linkColumn, "Edit");
		
		TextColumn<ItemListProxy> ownerColumn = new TextColumn<ItemListProxy>() {
			@Override
			public String getValue(ItemListProxy list) {
				return list.getOwner().getEmail();
			}
		};
		cellTable.addColumn(ownerColumn, "Owner");
		
		vPanel.add(cellTable);
	}

	private void initView()
	{
		newListName.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode())
				{
					presenter.persistList(newListName.getText());
					// Clear entry box
					newListName.setValue(null);
				}
			}
		});
		Scheduler.get().scheduleDeferred(new Command()
		{
			@Override
			public void execute()
			{
				newListName.setFocus(true);
			}
		});
	}

	@Override
	public void setPresenter(Presenter p)
	{
		this.presenter = p;
	}

	@Override
	public HasData<ItemListProxy> getDisplay()
	{
		return cellTable;
	}
}
