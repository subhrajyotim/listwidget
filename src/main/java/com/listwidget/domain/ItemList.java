package com.listwidget.domain;

import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Entity;
import com.listwidget.shared.proxy.ItemListProxy.ListType;

@Entity
public class ItemList extends DatastoreObject
{
	private String name;
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
//		return new ItemListDao().getItems(this);
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
}
