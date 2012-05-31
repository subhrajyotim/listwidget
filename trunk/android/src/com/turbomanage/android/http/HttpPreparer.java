
package com.turbomanage.android.http;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface HttpPreparer {

    /**
     * Prepares a previously opened connection. It is called after the request method
     * and content type have been set, but before writing to the OutputStream, 
     * so it's possible to set or modify connection properties, request headers, etc.
     * 
     * @param urlConnection An open connection
     * @throws IOException
     */
    void prepareConnection(HttpURLConnection urlConnection) throws IOException;

}
