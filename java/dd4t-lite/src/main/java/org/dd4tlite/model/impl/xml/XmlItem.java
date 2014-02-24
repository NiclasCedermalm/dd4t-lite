package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Field;
import org.dd4tlite.model.Item;
import org.dd4tlite.util.DateUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author nic
 */
public abstract class XmlItem implements Item {

    @Attribute
    private String id;

    @Attribute
    private String title;

    @ElementList(required = false, type=XmlField.class)
    private List<Field> metadata;

    @Attribute(name = "revisionDate", required = false)
    protected String revisionDateAsString;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public List<Field> getMetadata() {
        return this.metadata;
    }

    @Override
    public Field getMetadata(String name) {
        return null;
    }

    @Override
    public Date getRevisionDate() {
        if (revisionDateAsString == null || revisionDateAsString.equals(""))
            return new Date();
        try {
            return DateUtils.xml2Date(revisionDateAsString);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
