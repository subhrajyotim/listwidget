package com.listwidget.shared.proxy;

import com.google.appengine.api.users.User;
import com.google.gwt.requestfactory.shared.ProxyFor;

@ProxyFor(User.class)
public interface AppUserProxy extends DatastoreObjectProxy

{
	String getEmail();
}