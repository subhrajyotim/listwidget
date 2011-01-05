package com.listwidget.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.listwidget.shared.proxy.ListItemProxy;
import com.listwidget.shared.proxy.ItemListProxy.ListType;

public class EditListViewImpl extends Composite implements EditListView
{
	VerticalPanel vPanel = new VerticalPanel();
	FlexTable t = new FlexTable();
	Anchor back = new Anchor("Back");
	Label listName = new Label();
	Label listType = new Label();
	TextBox newItemText = new TextBox();
	private Presenter presenter;

	public EditListViewImpl()
	{
		back.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});
		vPanel.add(back);
		vPanel.add(new Label("Edit List"));
		vPanel.add(listName);
		vPanel.add(listType);
		vPanel.add(newItemText);
		vPanel.add(t);
		initWidget(vPanel);
		newItemText.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (KeyCodes.KEY_ENTER==event.getNativeKeyCode())
				{
					presenter.addItem(newItemText.getText());
					// clear entry box
					newItemText.setValue(null);
				}
			}
		});
	}

	@Override
	public void populateItems(List<ListItemProxy> items)
	{
		t.removeAllRows();
		if (items==null) return;
		int i = 0;
		for (ListItemProxy item : items)
		{
			t.setText(i, 0, item.getItemText());
			i++;
		}
	}

	@Override
	public void setListName(String name)
	{
		listName.setText(name);
	}

	@Override
	public void setPresenter(Presenter p)
	{
		this.presenter = p;
	}

	@Override
	public void setListType(ListType listType)
	{
		this.listType.setText(listType.toString());
	}

}
