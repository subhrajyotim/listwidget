package com.turbomanage.listwidget.shared.service;

import java.util.List;

import com.google.gwt.requestfactory.shared.Request;
import com.google.gwt.requestfactory.shared.RequestContext;
import com.google.gwt.requestfactory.shared.Service;
import com.turbomanage.listwidget.server.locator.DaoServiceLocator;
import com.turbomanage.listwidget.server.service.ObjectifyDao;

/**
 * Base interface for DAO service stubs. Not yet supported in GWT, see
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=5807
 *
 * @param <T>
 */
public interface ObjectifyRequestContext<T> extends RequestContext {
	Request<List<T>> listAll();
	Request<Void> put(T obj);
}
