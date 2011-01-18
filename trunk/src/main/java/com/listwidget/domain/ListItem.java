package com.listwidget.domain;

import java.util.Date;


public class ListItem
{
	private String itemText;
	private Date dateCreated;
	
	public Date getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public ListItem()
	{
		// TODO Auto-generated constructor stub
	}

	public ListItem(String itemText)
	{
		setItemText(itemText);
	}

	public String getItemText()
	{
		return itemText;
	}

	public void setItemText(String itemText)
	{
		this.itemText = itemText;
	}

//	public void setId(Long id)
//	{
//		this.id = id;
//	}
//
//	public Long getId()
//	{
//		return id;
//	}
//
//	public Integer getVersion()
//	{
//		return version;
//	}
//
//	public void setVersion(Integer version)
//	{
//		this.version = version;
//	}
//
//	public void setParent(ItemList parent)
//	{
//		this.parentKey = new ItemListDao().key(parent);
//	}
//
//	public ItemList getParent()
//	{
//		try
//		{
//			return new ItemListDao().get(parentKey);
//		}
//		catch (EntityNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		return null;
//	}

}
