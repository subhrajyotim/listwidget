package com.listwidget.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.mvp.EditListPlace;
import com.listwidget.shared.proxy.ItemListProxy;

public class ListsViewImpl extends Composite implements ListsView
{
	VerticalPanel vPanel = new VerticalPanel();
	FlexTable t = new FlexTable();
	TextBox newListName = new TextBox();
	private Presenter presenter;
	private ClientFactory clientFactory;

	public ListsViewImpl(ClientFactory clientFactory)
	{
		this.clientFactory = clientFactory;
		initView();
		vPanel.add(new Label("New List"));
		vPanel.add(newListName);
		vPanel.add(t);
		initWidget(vPanel);
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
				}
			}
		});
	}

	@Override
	public void populateLists(List<ItemListProxy> itemLists)
	{
		int i = 1;
		for (ItemListProxy list : itemLists)
		{
			String proxyToken = clientFactory.getRequestFactory().getHistoryToken(list.stableId());
			String historyToken = clientFactory.getHistoryMapper().getToken(new EditListPlace(proxyToken));
			Hyperlink h = new Hyperlink(list.getName(),historyToken);
			t.setWidget(i, 0, h);
			t.setText(i, 1, list.getOwner().getEmail());
			i++;
		}
	}

	@Override
	public void setPresenter(Presenter p)
	{
		this.presenter = p;
	}
}
