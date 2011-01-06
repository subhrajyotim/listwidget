package com.listwidget.shared.proxy;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.listwidget.domain.AppUser;
import com.listwidget.server.locator.ObjectifyLocator;

@ProxyFor(value=AppUser.class, locator=ObjectifyLocator.class)
public interface AppUserProxy extends DatastoreObjectProxy

{
	String getEmail();
}