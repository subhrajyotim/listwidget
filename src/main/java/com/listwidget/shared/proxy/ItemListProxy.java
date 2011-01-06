package com.listwidget.shared.proxy;

import java.util.List;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.listwidget.domain.ItemList;
import com.listwidget.server.locator.ObjectifyLocator;

@ProxyFor(value = ItemList.class, locator = ObjectifyLocator.class)
public interface ItemListProxy extends DatastoreObjectProxy
{
	// TODO enums work!
	public enum ListType {NOTES, TODO}

	String getName();
	void setName(String name);
	List<ListItemProxy> getItems();
	ListType getListType();
	AppUserProxy getOwner();
	void setListType(ListType type);
	void setItems(List<ListItemProxy> asList);
}


