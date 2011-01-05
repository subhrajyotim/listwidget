package com.listwidget.server.locator;

import com.google.gwt.requestfactory.shared.ServiceLocator;

/**
 * Generic locator service that can be referenced in the @Service annotation
 * for any RequestFactory service stub
 *  
 * @author drfibonacci
 */
public class DaoServiceLocator implements ServiceLocator {

	@Override
	public Object getInstance(Class<?> clazz) {
		System.out.println(clazz);
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
