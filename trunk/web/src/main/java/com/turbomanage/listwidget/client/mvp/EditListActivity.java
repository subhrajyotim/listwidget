package com.turbomanage.listwidget.client.mvp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.turbomanage.listwidget.client.ClientFactory;
import com.turbomanage.listwidget.client.event.MessageEvent;
import com.turbomanage.listwidget.client.ui.EditListView;
import com.turbomanage.listwidget.client.ui.EditListView.Presenter;
import com.turbomanage.listwidget.client.ui.widget.MessageWidget.MessageType;
import com.turbomanage.listwidget.shared.proxy.ListItemProxy;
import com.turbomanage.listwidget.shared.proxy.NamedListProxy;
import com.turbomanage.listwidget.shared.service.ListwidgetRequestFactory;
import com.turbomanage.listwidget.shared.service.NamedListService;

public class EditListActivity extends AbstractActivity implements Presenter
{
	private Logger logger = Logger.getLogger(EditListActivity.class.getName());
	private ClientFactory clientFactory;
	private EntityProxyId<NamedListProxy> itemListId;
	private NamedListProxy editList;
	private EditListView view;
	private String itemListToken;
	private ListDataProvider<ListItemProxy> itemsProvider;
	private EventBus eventBus;

	public EditListActivity(ClientFactory cf, EditListPlace editListPlace)
	{
		this.clientFactory = cf;
		this.itemListToken = editListPlace.getToken();
	}

  @Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus)
	{
		this.eventBus = eventBus;
		// Find the entity proxy
		final ListwidgetRequestFactory req = clientFactory.getRequestFactory();
		EntityProxyId<NamedListProxy> proxyId = req
				.getProxyId(this.itemListToken);
		this.itemListId = proxyId;
		// .with("items") required to retrieve relations
		Request<NamedListProxy> findReq = clientFactory.getRequestFactory()
				.find(itemListId).with("items");
		view = clientFactory.getEditListView();
		findReq.fire(new Receiver<NamedListProxy>()
		{
			@Override
			public void onSuccess(NamedListProxy itemList)
			{
				// Gotcha--if do this, must call panel.setWidget in onSuccess
				// also
				assert (view != null);
				assert itemList != null;
				editList = itemList;
				view.getListName().setValue(itemList.getName());
				List<ListItemProxy> items = itemList.getItems();
				if (items == null)
				{
					items = new ArrayList<ListItemProxy>();
				}
				itemsProvider = new ListDataProvider<ListItemProxy>(items);
				itemsProvider.addDataDisplay(view.getDataTable());
			}
		});
		view.getItemTextColumn().setFieldUpdater(new ItemsUpdater());
		// Gotcha--make sure it's not null if you expect to see anything
		panel.setWidget(clientFactory.getEditListView());
		view.setPresenter(this);
	}

	@Override
	public void addItem(String itemText)
	{
		logger.info("persisting new listitem");
		NamedListService reqCtx = clientFactory.getRequestFactory()
				.namedListService();
		ListItemProxy newItem = reqCtx.create(ListItemProxy.class);
		newItem.setItemText(itemText);
		// Required?
		editList = reqCtx.edit(editList);
		// newItem.setParent(editList);
		List<ListItemProxy> items = editList.getItems();
		if (items == null)
		{
			editList.setItems(new ArrayList<ListItemProxy>());
		}
		editList.getItems().add(newItem);
		reqCtx.save(editList).with("items").to(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response)
			{
				logger.info("updating items");
				itemsProvider.setList(editList.getItems());
				itemsProvider.refresh();
			}
		}).fire();
	}

	private class ItemsUpdater implements FieldUpdater<ListItemProxy, String>
	{
		@Override
		public void update(int index, ListItemProxy item, final String newText)
		{
			NamedListService reqCtx = clientFactory.getRequestFactory()
					.namedListService();
			ListItemProxy editItem = reqCtx.edit(item);
			editItem.setItemText(newText);
			editList = reqCtx.edit(editList);
			editList.getItems().set(index, editItem);
			// Save the list since items are embedded
			reqCtx.save(editList).fire(new Receiver<Void>()
			{
				@Override
				public void onSuccess(Void response)
				{
					eventBus.fireEvent(new MessageEvent(newText + " updated",
							MessageType.INFO));
				}
			});
		}
	}

	@Override
	public void updateListName(String newName)
	{
		NamedListService reqCtx = clientFactory.getRequestFactory()
				.namedListService();
		editList = reqCtx.edit(editList);
		editList.setName(newName);
		reqCtx.save(editList).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response)
			{
				eventBus.fireEvent(new MessageEvent("List name updated",
						MessageType.INFO));
			}
		});
	};
}
