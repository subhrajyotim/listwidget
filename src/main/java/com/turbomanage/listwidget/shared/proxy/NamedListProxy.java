package com.turbomanage.listwidget.shared.proxy;

import java.util.List;

import com.google.gwt.requestfactory.shared.EntityProxy;
import com.google.gwt.requestfactory.shared.ProxyFor;
import com.turbomanage.listwidget.domain.NamedList;
import com.turbomanage.listwidget.server.locator.ObjectifyLocator;

@ProxyFor(value = NamedList.class, locator = ObjectifyLocator.class)
public interface NamedListProxy extends EntityProxy
{
	// Note: enums work!
	public enum ListType {NOTES, TODO}

	String getName();
	void setName(String name);
	List<ListItemProxy> getItems();
	ListType getListType();
	AppUserProxy getOwner();
	void setListType(ListType type);
	void setItems(List<ListItemProxy> asList);
}


