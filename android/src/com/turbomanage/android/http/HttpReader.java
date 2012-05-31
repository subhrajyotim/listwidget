
package com.turbomanage.android.http;

import java.io.IOException;
import java.io.InputStream;

public interface HttpReader {

    /**
     * Reads from the InputStream.
     * 
     * @param in An open {@link InputStream}
     * @return Object contained in the response
     * @throws IOException
     */
    Object readStream(InputStream in) throws IOException;

}
