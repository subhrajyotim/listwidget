package com.listwidget.server.servlet;

import java.lang.reflect.Method;

import com.google.gwt.requestfactory.server.ServiceLayerDecorator;
import com.google.gwt.requestfactory.shared.Service;
import com.google.gwt.requestfactory.shared.ServiceLocator;

/**
 * Temporary workaround for http://code.google.com/p/google-web-toolkit/issues/detail?id=5807

 * @author turbomanage
 *
 */
public class BugFixSLD extends ServiceLayerDecorator {

	  @Override
	public Method resolveRequestContextMethod(String requestContextClass,
			String methodName) {
		  System.out.println(requestContextClass + methodName);
		return super.resolveRequestContextMethod(requestContextClass, methodName);
	}

	@Override
	  public Object createServiceInstance(Method contextMethod, Method domainMethod) {
		// Temp workaround for GWT issue 5807
	    Class<? extends ServiceLocator> locatorType = getTop().resolveServiceLocator(
	        contextMethod, domainMethod);
	    ServiceLocator locator = newInstance(locatorType, ServiceLocator.class);
	    Class<?> enclosing = contextMethod.getDeclaringClass();
	    Service s = enclosing.getAnnotation(Service.class);
	    return locator.getInstance(s.value());
	  }
	  
	  private <T> T newInstance(Class<T> clazz, Class<? super T> base) {
		    Throwable ex;
		    try {
		      return clazz.newInstance();
		    } catch (InstantiationException e) {
		      ex = e;
		    } catch (IllegalAccessException e) {
		      ex = e;
		    }
		    return this.<T> die(ex,
		        "Could not instantiate %s %s. Is it default-instantiable?",
		        base.getSimpleName(), clazz.getCanonicalName());
		  }


}
