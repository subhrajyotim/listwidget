package com.listwidget.client.mvp;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.listwidget.client.ClientFactory;
import com.listwidget.client.event.MessageEvent;
import com.listwidget.client.ui.ListsView;
import com.listwidget.client.ui.ListsView.Presenter;
import com.listwidget.client.ui.widget.MessageWidget.MessageType;
import com.listwidget.shared.proxy.ItemListProxy;
import com.listwidget.shared.proxy.ItemListProxy.ListType;
import com.listwidget.shared.service.ListwidgetRequestFactory;
import com.listwidget.shared.service.ListwidgetRequestFactory.ItemListRequestContext;

/**
 * Shows all lists available
 * 
 * @author turbomanage
 */
public class ListsActivity extends AbstractActivity implements Activity,
		Presenter
{
	private Logger logger = Logger.getLogger(ListsActivity.class.getName());
	private ClientFactory clientFactory;
	private ListsView display;
	private ListDataProvider listDataProvider;
	private EventBus eventBus;

	public static class ListDataProvider extends
			AsyncDataProvider<ItemListProxy>
	{
		private Logger logger = Logger
				.getLogger(ListsActivity.ListDataProvider.class.getName());
		private ListwidgetRequestFactory rf;

		public ListDataProvider(ListwidgetRequestFactory requestFactory)
		{
			this.rf = requestFactory;
		}

		@Override
		protected void onRangeChanged(HasData<ItemListProxy> display)
		{
			getData();
		}

		private void getData()
		{
			Request<List<ItemListProxy>> findAllReq = rf.itemListRequest()
					.listAll().with("owner");
			findAllReq.fire(new Receiver<List<ItemListProxy>>()
			{
				@Override
				public void onSuccess(List<ItemListProxy> response)
				{
					updateRowData(0, response);
				}
			});
		}
	}

	public ListsActivity(ClientFactory cf)
	{
		this.clientFactory = cf;
		this.listDataProvider = new ListDataProvider(clientFactory.getRequestFactory());
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus)
	{
		display = clientFactory.getListsView();
		this.eventBus = eventBus;
		display.setPresenter(this);
		panel.setWidget(display);
		// Triggers listDataProvider#onRangeChanged() to call for data
		listDataProvider.addDataDisplay(display.getDataTable());
		// Make list name field editable
		display.getNameColumn().setFieldUpdater(new NameFieldUpdater());
	}

	@Override
	public void persistList(String listName)
	{
		final ListwidgetRequestFactory rf = this.clientFactory
				.getRequestFactory();
		ItemListRequestContext reqCtx = rf.itemListRequest();
		final ItemListProxy newList = reqCtx.create(ItemListProxy.class);
		newList.setName(listName);
		newList.setListType(ListType.TODO);
		reqCtx.saveAndReturn(newList).fire(new Receiver<ItemListProxy>()
		{
			@Override
			public void onSuccess(final ItemListProxy savedList)
			{
				// Refresh table
				listDataProvider.getData();
				Scheduler.get().scheduleDeferred(new Command()
				{
					@Override
					public void execute()
					{
						// Go to edit place for the new list
						String proxyToken = clientFactory.getRequestFactory().getHistoryToken(newList.stableId());
						String historyToken = clientFactory.getHistoryMapper().getToken(new EditListPlace(proxyToken));
						clientFactory.getPlaceController().goTo(new EditListPlace(proxyToken));
					}
				});
			}
		});
	}

	/**
	 * Updater for the editable list name column. Must be in presenter because needs to fire requests
	 *  
	 * @author David Chandler
	 */
	public class NameFieldUpdater implements FieldUpdater<ItemListProxy,String>
	{
		@Override
		public void update(int index, ItemListProxy obj, final String newName)
		{
			ItemListRequestContext reqCtx = clientFactory.getRequestFactory().itemListRequest();
			ItemListProxy editable = reqCtx.edit(obj);
			editable.setName(newName);
			reqCtx.save(editable).fire(new Receiver<Void>()
			{
				@Override
				public void onSuccess(Void response)
				{
					eventBus.fireEvent(new MessageEvent(newName + " updated",MessageType.INFO));
				}
			});
		}
	};
	
}