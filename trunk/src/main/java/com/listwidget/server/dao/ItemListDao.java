package com.listwidget.server.dao;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.listwidget.domain.ItemList;

public class ItemListDao extends ObjectifyDao<ItemList>
{
	/**
	 * Wraps put() so as not to return a Key, which RF can't handle
	 * 
	 * @param obj
	 */
	public void save(ItemList obj)
	{
		this.put(obj);
	}

	// Note: requires no args since it's an instance method
	public ItemList persistAndReturnSelf(ItemList list)
	{
		ItemListDao dao = new ItemListDao();
		Key<ItemList> key = dao.put(list);
		try
		{
			return dao.get(key);
		}
		catch (EntityNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	
//	public List<ListItem> getItems(ItemList list)
//	{
//		return ofy().query(ListItem.class).filter("parent", list.getId()).list();
//	}

}
