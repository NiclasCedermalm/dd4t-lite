package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;

/**
 * Interface for XML values
 *
 * @author nic
 */
public interface XmlValue {

    public Object getValue();

    // TODO: Have the possiblity to get untransformed values???

}
