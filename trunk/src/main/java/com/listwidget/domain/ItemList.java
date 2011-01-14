package com.listwidget.domain;

import java.util.List;

import javax.persistence.Embedded;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.listwidget.server.service.AppUserDao;
import com.listwidget.shared.proxy.ItemListProxy.ListType;

@Entity
public class ItemList extends DatastoreObject
{
	private String name;
	private Key<AppUser> owner;
	private ListType listType;
	@Embedded
	private List<ListItem> items;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<ListItem> getItems()
	{
		return items;
	}

	public void setItems(List<ListItem> items)
	{
		this.items = items;
	}

	public void setListType(ListType listType)
	{
		this.listType = listType;
	}

	public ListType getListType()
	{
		return listType;
	}

	public AppUser getOwner()
	{
		try
		{
			return new AppUserDao().get(owner);
		} catch (EntityNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setOwner(AppUser owner)
	{
		this.owner = new AppUserDao().key(owner);
	}
	
}
