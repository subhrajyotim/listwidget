
package com.turbomanage.android.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public interface HttpWriter {

    /**
     * Writes to an open, prepared connection. This method is only called when
     * {@link HttpURLConnection#getDoOutput()} is true and there is non-null content. 
     * 
     * @param out An open {@link OutputStream}
     * @param content Data to send with the request
     * @throws IOException
     */
    void writeStream(OutputStream out, Object content) throws IOException;

}
