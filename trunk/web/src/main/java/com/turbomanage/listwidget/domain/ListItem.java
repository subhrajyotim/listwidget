package com.turbomanage.listwidget.domain;

import java.util.Date;


/**
 * POJO that represents an item in a list.
 */
public class ListItem
{
	private String itemText;
	private Date dateCreated;
	// Getters and setters
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
//	public void setParent(NamedList parent)
//	{
//		this.parentKey = new NamedListDao().key(parent);
//	}
//
//	public NamedList getParent()
//	{
//		try
//		{
//			return new NamedListDao().get(parentKey);
//		}
//		catch (EntityNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		return null;
//	}

}
