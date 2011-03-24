package com.turbomanage.listwidget.shared.service;

import java.util.List;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.requestfactory.shared.Service;
import com.turbomanage.listwidget.server.locator.DaoServiceLocator;
import com.turbomanage.listwidget.server.service.ItemListDao;
import com.turbomanage.listwidget.shared.proxy.ItemListProxy;

public interface ListwidgetRequestFactory extends RequestFactory
{

	/**
	 * Service stub for methods in ItemListDao
	 * 
	 * TODO Enhance RequestFactory to enable service stubs to extend a base interface
	 * so we don't have to repeat methods from the base ObjectifyDao in each stub
	 */
	@Service(value = ItemListDao.class, locator = DaoServiceLocator.class)
	interface ItemListRequestContext extends RequestContext
	{
		Request<List<ItemListProxy>> listAll();
		Request<Void> save(ItemListProxy list);
		Request<ItemListProxy> saveAndReturn(ItemListProxy newList);
		Request<Void> removeList(ItemListProxy list);
	}
	
	ItemListRequestContext itemListRequest();

}
