package com.turbomanage.listwidget.server.service;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gwt.requestfactory.server.RequestFactoryServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.turbomanage.listwidget.domain.AppUser;
import com.turbomanage.listwidget.domain.ItemList;

/**
 * @author drfibonacci
 *
 */
public class ItemListDao extends ObjectifyDao<ItemList>
{
	@Override
	public List<ItemList> listAll()
	{
		return this.listAllForUser();
	}

	/**
	 * Wraps put() so as not to return a Key, which RF can't handle
	 * 
	 * @param obj
	 */
	public void save(ItemList list)
	{
		AppUser loggedInUser = LoginService.getLoggedInUser();
		list.setOwner(loggedInUser);
		this.put(list);
	}

	// Note: requires no args since it's an instance method
	public ItemList saveAndReturn(ItemList list)
	{
		AppUser loggedInUser = LoginService.getLoggedInUser();
		list.setOwner(loggedInUser);
		Key<ItemList> key = this.put(list);
		try
		{
			return this.get(key);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Remove a list. Since items are embedded, they are removed automatically.
	 * 
	 * @param list
	 */
	public void removeList(ItemList list)
	{
		this.delete(list);
	}
	
}
