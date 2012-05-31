
package com.turbomanage.android.http;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface HttpErrorHandler {

    /**
     * Invoked for any {@link IOException}s.
     * 
     * @param urlConnection The connection for which the error occurred
     * @param e The exception that was thrown
     */
    void onError(HttpURLConnection urlConnection, Exception e);

}
