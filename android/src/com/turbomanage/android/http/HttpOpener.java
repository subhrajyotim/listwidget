
package com.turbomanage.android.http;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface HttpOpener {

    /**
     * Opens an HTTP connection.
     * 
     * @param url Absolute URL
     * @return an open {@link HttpURLConnection}
     * @throws IOException
     */
    HttpURLConnection openConnection(String url) throws IOException;

}
