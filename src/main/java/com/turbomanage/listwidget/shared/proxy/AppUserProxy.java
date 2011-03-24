package com.turbomanage.listwidget.shared.proxy;

import com.google.gwt.requestfactory.shared.ProxyFor;
import com.turbomanage.listwidget.domain.AppUser;
import com.turbomanage.listwidget.server.locator.ObjectifyLocator;

@ProxyFor(value=AppUser.class, locator=ObjectifyLocator.class)
public interface AppUserProxy extends DatastoreObjectProxy

{
	String getEmail();
}