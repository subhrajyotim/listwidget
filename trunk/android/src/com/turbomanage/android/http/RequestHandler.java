
package com.turbomanage.android.http;


/**
 * Interface that defines the request lifecycle used by {@link BasicHttpClient}.
 * RequestHandler is composed of many sub-interfaces so that each handler can 
 * be set independently if needed. You can provide your own implementation by calling
 * {@link BasicHttpClient#setRequestInterceptor(RequestInterceptor)} or set
 * the individual handlers independently like
 * {@link BasicHttpClient#setHttpErrorHandler(HttpErrorHandler)}.
 * 
 * See {@link BasicRequestHandler} for a simple implementation.
 * 
 * @author David M. Chandler
 */
public interface RequestHandler extends HttpErrorHandler, HttpWriter, HttpReader, HttpPreparer, HttpOpener {

}
