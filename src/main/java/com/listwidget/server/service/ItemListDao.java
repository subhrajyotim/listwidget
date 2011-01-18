package com.listwidget.server.service;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gwt.requestfactory.server.RequestFactoryServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.listwidget.domain.AppUser;
import com.listwidget.domain.ItemList;

public class ItemListDao extends ObjectifyDao<ItemList>
{
	@Override
	public List<ItemList> listAll()
	{
		// Find all lists for logged in user
		AppUser loggedInUser = (AppUser) RequestFactoryServlet.getThreadLocalRequest().getAttribute("loggedInUser");
		if (loggedInUser != null)
		{
			Key<AppUser> ownerKey = new AppUserDao().key(loggedInUser);
			Query<ItemList> q = ofy().query(ItemList.class);
			return q.filter("owner", ownerKey).list();
		}
		return null;
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
	
//	public List<ListItem> getItems(ItemList list)
//	{
//		return ofy().query(ListItem.class).filter("parent", list.getId()).list();
//	}

}
