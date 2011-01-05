package com.listwidget.client.mvp;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.ui.ListsView;
import com.listwidget.client.ui.ListsView.Presenter;
import com.listwidget.shared.proxy.ItemListProxy;
import com.listwidget.shared.proxy.ListItemProxy;
import com.listwidget.shared.proxy.ItemListProxy.ListType;
import com.listwidget.shared.service.ListkeeperRequestFactory;
import com.listwidget.shared.service.ListkeeperRequestFactory.ItemListRequestContext;

/**
 * Shows all lists available
 *
 * @author drfibonacci
 */
public class ListsActivity extends AbstractActivity implements Activity, Presenter
{
	private Logger logger = Logger.getLogger(ListsActivity.class.getName());
	private ClientFactory clientFactory;
	private ListsView v;

	public ListsActivity(ClientFactory cf)
	{
		this.clientFactory = cf;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus)
	{
		v = clientFactory.getListsView();
		v.setPresenter(this);
		panel.setWidget(v);
		populateLists();
	}

	private void populateLists()
	{
		logger.info("firing listAll");
		ListkeeperRequestFactory rf = this.clientFactory.getRequestFactory();
		Request<List<ItemListProxy>> findAllReq = rf.itemListRequest().listAll();
		findAllReq.fire(new Receiver<List<ItemListProxy>>()
		{
			@Override
			public void onSuccess(List<ItemListProxy> response)
			{
				v.populateLists(response);
			}
		});
	}

	@Override
	public void persistList(String listName)
	{
		final ListkeeperRequestFactory rf = this.clientFactory.getRequestFactory();
		ItemListRequestContext reqCtx = rf.itemListRequest();
		final ItemListProxy newList = reqCtx.create(ItemListProxy.class);
		newList.setName(listName);
		newList.setListType(ListType.TODO);
		reqCtx.persist(newList).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response)
			{
				populateLists();
			}
		});
	}
}
