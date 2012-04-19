package com.turbomanage.listwidget.client.ui.desktop;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.EditTextCell;
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
import com.turbomanage.listwidget.client.ClientFactory;
import com.turbomanage.listwidget.client.mvp.EditListPlace;
import com.turbomanage.listwidget.client.ui.ListsView;
import com.turbomanage.listwidget.client.ui.widget.HyperlinkCell;
import com.turbomanage.listwidget.client.ui.widget.MessageWidget;
import com.turbomanage.listwidget.shared.proxy.NamedListProxy;

public class ListsViewImpl extends Composite implements ListsView
{
	VerticalPanel vPanel = new VerticalPanel();
	TextBox newListName = new TextBox();
	private Presenter presenter;
	private ClientFactory clientFactory;
	private CellTable<NamedListProxy> cellTable = new CellTable<NamedListProxy>();
	private Column<NamedListProxy, String> nameColumn;

	public ListsViewImpl()
	{
		// For GWT Designer
	}
	
	public ListsViewImpl(final ClientFactory clientFactory)
	{
		this.clientFactory = clientFactory;
		initView();
		vPanel.add(new MessageWidget(clientFactory.getEventBus()));
		vPanel.add(new Label("New List"));
		vPanel.add(newListName);
		initWidget(vPanel);
		
		// Note Flyweight pattern: only one instance of HyperlinkCell passed to the Column
		Column<NamedListProxy, Hyperlink> linkColumn = new Column<NamedListProxy, Hyperlink>(new HyperlinkCell())
		{
			@Override
			public Hyperlink getValue(NamedListProxy list)
			{
				String proxyToken = clientFactory.getRequestFactory().getHistoryToken(list.stableId());
				String historyToken = clientFactory.getHistoryMapper().getToken(new EditListPlace(proxyToken));
				Hyperlink h = new Hyperlink(list.getName(),historyToken);
				return h;
			}
		};
		
		// Editable column for list name
		nameColumn = new Column<NamedListProxy,String>(new EditTextCell())
		{
			@Override
			public String getValue(NamedListProxy list)
			{
				return list.getName();
			}
		};

		ActionCell<NamedListProxy> actionCell = new ActionCell<NamedListProxy>("Del",new Delegate<NamedListProxy>()
		{
			@Override
			public void execute(NamedListProxy object)
			{
				presenter.removeList(object);
			}
		});
		Column<NamedListProxy, NamedListProxy> delColumn = new Column<NamedListProxy, NamedListProxy>(actionCell)
		{
			@Override
			public NamedListProxy getValue(NamedListProxy object)
			{
				return object;
			}
		};
//		Column<NamedListProxy, Anchor> delColumn = new Column<NamedListProxy, Anchor>(new AnchorCell())
//		{
//			@Override
//			public Anchor getValue(final NamedListProxy list)
//			{
//				Anchor a = new Anchor("Del");
//				a.addClickHandler(new ClickHandler()
//				{
//					@Override
//					public void onClick(ClickEvent event)
//					{
//						Window.alert("got click"	);
//						presenter.removeList(list);
//					}
//				});
//				return a;
//			}
//		};

		// Display-only column showing owner email
		TextColumn<NamedListProxy> ownerColumn = new TextColumn<NamedListProxy>() {
			@Override
			public String getValue(NamedListProxy list) {
				return list.getOwner().getEmail();
			}
		};
		
		cellTable.addColumn(linkColumn, "Edit");
		cellTable.addColumn(delColumn, "Del");
//		cellTable.addColumn(nameColumn,"List name");
//		cellTable.addColumn(ownerColumn, "Owner");
		
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
	public HasData<NamedListProxy> getDataTable()
	{
		return cellTable;
	}

	@Override
	public Column<NamedListProxy, String> getNameColumn()
	{
		return nameColumn;
	}
}
