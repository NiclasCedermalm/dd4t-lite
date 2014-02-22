package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Component;
import org.dd4tlite.model.Field;
import org.dd4tlite.model.Schema;
import org.simpleframework.xml.ElementList;

import java.util.List;
import java.util.Map;

/**
 * @author nic
 */
public class XmlComponent extends XmlItem implements Component {


    @ElementList (required = false, type=XmlField.class)
    private List<Field> content;


    @Override
    public Schema getSchema() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Field> getContent() {
       return this.content;
    }

    @Override
    public Field getContent(String fieldName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
