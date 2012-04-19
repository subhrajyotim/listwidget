package com.turbomanage.listwidget.client.ui.desktop;

import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.HasData;
import com.turbomanage.listwidget.client.ui.EditListView;
import com.turbomanage.listwidget.shared.proxy.ListItemProxy;

public class EditListViewImpl extends Composite implements EditListView
{
	private VerticalPanel layoutPanel = new VerticalPanel();
	private Label lblListName = new Label("List name");
	private TextBox editListName;
	private Label lblNewItem = new Label("New item");
	private TextBox newItemText = new TextBox();
	private CellTable<ListItemProxy> cellTable = new CellTable<ListItemProxy>();
	private Column<ListItemProxy, String> itemColumn;
	private Column<ListItemProxy, Date> dateColumn;
	private Presenter presenter;

	public EditListViewImpl()
	{
		
		initWidget(layoutPanel);
		
		layoutPanel.add(lblListName);
		
		editListName = new TextBox();
		editListName.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (KeyCodes.KEY_ENTER==event.getNativeKeyCode())
				{
					// Delegate to presenter
					presenter.updateListName(editListName.getValue());
				}
			}
		});
		layoutPanel.add(editListName);
		
		layoutPanel.add(lblNewItem);
		
		layoutPanel.add(newItemText);
		newItemText.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (KeyCodes.KEY_ENTER==event.getNativeKeyCode())
				{
					// Delegate to presenter
					presenter.addItem(newItemText.getValue());
					newItemText.setValue(null);
				}
			}
		});
		
		itemColumn = new Column<ListItemProxy, String>(new EditTextCell()) {
			@Override
			public String getValue(ListItemProxy object) {
				return object.getItemText();
			}
		};
		cellTable.addColumn(itemColumn, "Item text");
		
		dateColumn = new Column<ListItemProxy, Date>(new DateCell()) {
			@Override
			public Date getValue(ListItemProxy object) {
				return new Date();
			}
		};
		
		cellTable.addColumn(dateColumn, "Created on");
		layoutPanel.add(cellTable);
	}

	@Override
	public void setPresenter(Presenter p)
	{
		this.presenter = p;
	}

	@Override
	public HasValue<String> getListName()
	{
		return this.editListName;
	}

	@Override
	public HasData<ListItemProxy> getDataTable()
	{
		return this.cellTable;
	}

	@Override
	public Column<ListItemProxy, String> getItemTextColumn()
	{
		return itemColumn;
	}
}
