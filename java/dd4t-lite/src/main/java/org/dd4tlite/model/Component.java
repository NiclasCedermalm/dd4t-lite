package org.dd4tlite.model;

import java.util.List;
import java.util.Map;

/**
 * @author nic
 */
public interface Component extends Item {

    public enum ComponentType {
        Normal, Multimedia
    }

    public Schema getSchema();

    /**
     * Get the content
     *
     * @return a map of field objects representing the content
     */
    public List<Field> getContent();

    public Field getContent(String fieldName);

}
