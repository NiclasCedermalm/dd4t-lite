package org.dd4tlite.util;

import java.io.*;

/**
 * IO Utils
 *
 * @author nic
 */
public class IOUtils {

    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        }
        else {
            return "";
        }
    }
}
