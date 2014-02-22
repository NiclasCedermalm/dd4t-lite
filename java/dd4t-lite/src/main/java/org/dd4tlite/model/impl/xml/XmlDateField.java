package org.dd4tlite.model.impl.xml;

import org.dd4tlite.util.DateUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;

import java.text.ParseException;
import java.util.Date;

/**
 * @author nic
 */
public class XmlDateField implements XmlValue {

    // TODO: How to define new simple elements? <date>[date]</date>

    @Text
    private String xmlDate;

    private Date date;

    @Commit
    private void convert() throws Exception {
        this.date = DateUtils.xml2Date(xmlDate);
    }

    @Override
    public Object getValue() {
        return this.date;
    }

    @Override
    public String toString() {
        return xmlDate;
    }
}
