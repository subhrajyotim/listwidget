package com.listwidget.server.servlet;

import com.google.gwt.requestfactory.server.DefaultExceptionHandler;
import com.google.gwt.requestfactory.server.ExceptionHandler;
import com.google.gwt.requestfactory.server.RequestFactoryServlet;
import com.google.gwt.requestfactory.server.ServiceLayerDecorator;

/**
 * Temporary workaround for http://code.google.com/p/google-web-toolkit/issues/detail?id=5807
 * 
 * @author turbomanage
 */
public class BugFixRequestFactoryServlet extends RequestFactoryServlet {

	public BugFixRequestFactoryServlet() {
		this(new DefaultExceptionHandler(), new BugFixSLD());
	}

	public BugFixRequestFactoryServlet(ExceptionHandler exceptionHandler,
			ServiceLayerDecorator... serviceDecorators) {
		super(exceptionHandler, serviceDecorators);
	}

}
