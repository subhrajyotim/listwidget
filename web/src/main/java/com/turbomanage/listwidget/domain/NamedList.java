package com.turbomanage.listwidget.domain;

import java.util.List;

import javax.persistence.Embedded;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.turbomanage.listwidget.server.service.AppUserDao;
import com.turbomanage.listwidget.shared.proxy.ListType;

/**
 * POJO that represents a list of items such as a ToDo list.
 * The items are stored as an embedded object.
 */
@Entity
@XmlRootElement
public class NamedList extends DatastoreObject
{
	private String name;
	private Key<AppUser> owner;
	private ListType listType;
	@Embedded 	private List<ListItem> items;
  
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
