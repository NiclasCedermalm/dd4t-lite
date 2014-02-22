package org.dd4tlite.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date Utils
 *
 * @author nic
 */
public abstract class DateUtils {

    private static SimpleDateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static Date xml2Date(String xmlDate) throws ParseException {
        return xmlDateFormat.parse(xmlDate);
    }

    public static String date2xml(Date date) {
        return xmlDateFormat.format(date);
    }
}
