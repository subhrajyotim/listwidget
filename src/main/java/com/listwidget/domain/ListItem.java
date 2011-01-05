package com.listwidget.domain;


public class ListItem
{
	private String itemText;
//	private Integer version = 0;
	// Dummy field due JsonRequestProcessor:1543
//	transient private ItemList parent;
//	@Parent
//	private
//	Key<ItemList> parentKey;

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
