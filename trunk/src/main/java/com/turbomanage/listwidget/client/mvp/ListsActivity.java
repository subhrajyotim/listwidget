package com.turbomanage.listwidget.client.mvp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.turbomanage.listwidget.client.ClientFactory;
import com.turbomanage.listwidget.client.event.MessageEvent;
import com.turbomanage.listwidget.client.ui.ListsView;
import com.turbomanage.listwidget.client.ui.ListsView.Presenter;
import com.turbomanage.listwidget.client.ui.widget.MessageWidget.MessageType;
import com.turbomanage.listwidget.shared.proxy.ListItemProxy;
import com.turbomanage.listwidget.shared.proxy.ListType;
import com.turbomanage.listwidget.shared.proxy.NamedListProxy;
import com.turbomanage.listwidget.shared.service.ListwidgetRequestFactory;
import com.turbomanage.listwidget.shared.service.NamedListService;

/**
 * Shows all lists available
 * 
 * @author turbomanage
 */
public class ListsActivity extends AbstractActivity implements Activity,
		Presenter
{
	@Override
  public String mayStop() {
    return null;//"Please hold on. This activity may be stopping";
  }

  @Override
  public void onStop() {
    // TODO Auto-generated method stub
    super.onStop();
  }

  private Logger logger = Logger.getLogger(ListsActivity.class.getName());
	private ClientFactory clientFactory;
	private ListsView display;
	private NamedListDataProvider listDataProvider;
	private EventBus eventBus;
  private ListwidgetRequestFactory myRF;

	public static class NamedListDataProvider extends
			ListDataProvider<NamedListProxy>
	{
		private Logger logger = Logger
				.getLogger(ListsActivity.NamedListDataProvider.class.getName());
		private ListwidgetRequestFactory rf;

		public NamedListDataProvider(ListwidgetRequestFactory requestFactory)
		{
			this.rf = requestFactory;
		}

		@Override
		protected void onRangeChanged(HasData<NamedListProxy> display)
		{
			getData();
		}
		
		private void getData()
		{
			logger.warning("getting data");
			// To retrieve relations and value types, use .with()
			Request<List<NamedListProxy>> findAllReq = rf.namedListService()
					.listAll().with("owner");
			// Receiver specifies return type
			findAllReq.fire(new Receiver<List<NamedListProxy>>()
			{
				@Override
				public void onSuccess(List<NamedListProxy> response)
				{
					updateRowCount(response.size(), true);
					updateRowData(0, response);
				}
			});
		}
	}

	public ListsActivity(ClientFactory cf)
	{
	  new ListDataProvider(new ArrayList());
		this.clientFactory = cf;
		this.myRF = clientFactory.getRequestFactory();
		this.listDataProvider = new NamedListDataProvider(this.myRF);
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

	public void persistList(String listName)
	{
		NamedListService reqCtx = myRF.namedListService();
		final NamedListProxy newList = reqCtx.create(NamedListProxy.class);
		newList.setName(listName);
		newList.setItems(new ArrayList<ListItemProxy>());
		newList.setListType(ListType.TODO);
		reqCtx.save(newList).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void v)
			{
				// Refresh table
			  /*
			   * I think this fails because the recently persisted item is still frozen during this method
			   * and getData() can't call setters.
			   * Which means I shouldn't be calling for the whole list including this item.
			   */
				listDataProvider.getData();
				// Go to edit place for the new list
				String proxyToken = clientFactory.getRequestFactory()
						.getHistoryToken(newList.stableId());
				clientFactory.getPlaceController().goTo(
						new EditListPlace(proxyToken));
			}
		});
	}

	/**
	 * Updater for the editable list name column. Must be in presenter because
	 * needs to fire requests
	 * 
	 * @author David Chandler
	 */
	public class NameFieldUpdater implements
			FieldUpdater<NamedListProxy, String>
	{
		@Override
		public void update(int index, NamedListProxy obj, final String newName)
		{
			NamedListService reqCtx = myRF.namedListService();
			NamedListProxy editable = reqCtx.edit(obj);
			editable.setName(newName);
			reqCtx.save(editable).fire(new Receiver<Void>()
			{
				@Override
				public void onSuccess(Void response)
				{
				  // Update status message
					eventBus.fireEvent(new MessageEvent(newName + " updated",
							MessageType.INFO));
				}
			});
		}
	}

	@Override
	public void removeList(NamedListProxy list)
	{
		NamedListService reqCtx = clientFactory.getRequestFactory().namedListService();
		reqCtx.removeList(list).fire(new Receiver<Void>()
		{
			@Override
			public void onSuccess(Void response)
			{
				listDataProvider.getData();
			}
		});
	};

}