package com.listwidget.client.mvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.shared.EntityProxyId;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.ui.EditListView;
import com.listwidget.client.ui.EditListView.Presenter;
import com.listwidget.domain.ListItem;
import com.listwidget.shared.proxy.ItemListProxy;
import com.listwidget.shared.proxy.ListItemProxy;
import com.listwidget.shared.service.ListwidgetRequestFactory;
import com.listwidget.shared.service.ListwidgetRequestFactory.ItemListRequestContext;

public class EditListActivity extends AbstractActivity implements Presenter
{
	private Logger logger = Logger.getLogger(EditListActivity.class.getName());
	private ClientFactory clientFactory;
	private EntityProxyId<ItemListProxy> itemListId;
	private ItemListProxy editList;
	private EditListView view;
	private String itemListToken;

	public EditListActivity(ClientFactory cf, EditListPlace editListPlace)
	{
		this.clientFactory = cf;
		this.itemListToken = editListPlace.getToken();
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus)
	{
		// Find the entity proxy
		final ListwidgetRequestFactory req = clientFactory.getRequestFactory();
		EntityProxyId<ItemListProxy> proxyId = req.getProxyId(this.itemListToken);
		this.itemListId = proxyId;
		// .with("items") required to retrieve relations
		logger.info("Finding list");
		Request<ItemListProxy> findReq = clientFactory.getRequestFactory().find(itemListId).with("items");
		view = clientFactory.getEditListView();
		findReq.fire(new Receiver<ItemListProxy>()
		{
			@Override
			public void onSuccess(ItemListProxy itemList)
			{
				// Gotcha--if do this, must call panel.setWidget in onSuccess also
				assert (view != null);
				assert itemList != null;
				editList = itemList;
				view.setListName(itemList.getName());
				view.setListType(itemList.getListType());
				view.populateItems(itemList.getItems());
			}
		});
		// Gotcha--make sure it's not null if you expect to see anything
		panel.setWidget(view);
		view.setPresenter(this);
	}

	@Override
	public void addItem(String itemText)
	{
		logger.info("persisting new listitem");
		ItemListRequestContext reqCtx = clientFactory.getRequestFactory().itemListRequest();
		ListItemProxy newItem = reqCtx.create(ListItemProxy.class);
		newItem.setItemText(itemText);
		// Required?
		editList = reqCtx.edit(editList);
//		newItem.setParent(editList);
		List<ListItemProxy> items = editList.getItems();
		if (items==null)
		{
			editList.setItems(new ArrayList<ListItemProxy>());
		}
		editList.getItems().add(newItem);
		reqCtx.save(editList).with("items").to(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response)
			{
				view.populateItems(editList.getItems());
			}
		}).fire();
	}

}
