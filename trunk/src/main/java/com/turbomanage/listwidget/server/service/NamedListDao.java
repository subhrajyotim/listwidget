package com.turbomanage.listwidget.server.service;

import java.util.List;

import com.googlecode.objectify.Key;
import com.turbomanage.listwidget.domain.AppUser;
import com.turbomanage.listwidget.domain.NamedList;

/**
 * @author turbomanage
 */
public class NamedListDao extends ObjectifyDao<NamedList>
{
	@Override
	public List<NamedList> listAll()
	{
		return this.listAllForUser();
	}

	/**
	 * Wraps put() so as not to return a Key, which RF can't handle
	 * 
	 * @param obj
	 */
	public void save(NamedList list)
	{
		AppUser loggedInUser = LoginService.getLoggedInUser();
		list.setOwner(loggedInUser);
		this.put(list);
	}

	public NamedList saveAndReturn(NamedList list)
	{
		AppUser loggedInUser = LoginService.getLoggedInUser();
		list.setOwner(loggedInUser);
		Key<NamedList> key = this.put(list);
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
	public void removeList(NamedList list)
	{
		this.delete(list);
	}
	
}
