package com.listwidget.shared.proxy;

import com.google.gwt.requestfactory.shared.EntityProxy;

/**
 * Generic base interface for proxies of domain types that extend DatastoreObject 
 * 
 * @author turbomanage
 */
public interface DatastoreObjectProxy extends EntityProxy
{
	Long getId();
	
	Integer getVersion();
}
