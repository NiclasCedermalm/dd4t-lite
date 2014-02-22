package org.dd4tlite.model;

import java.util.List;

/**
 * @author nic
 */
public interface Field {

    public enum FieldType {

        // Behövs MultilineText verkligen????
        Text, MultiLineText, Xhtml, Keyword, Embedded, MultiMediaLink, ComponentLink, ExternalLink, Number, Date
    }

    /**
     * Get the values of the field.
     *
     * @return a list of objects, where the type is depending of the field type.
     *         Never returns null.
     */
    public List<? extends Object> getValues();

    /**
     * Get the name of the field.
     *
     * @return the name of the field
     */
    public String getName();

    /**
     * Get the xPath of the field (used for SiteEdit)
     *
     * @return the xPath of the field
     */
    public String getXPath();

    /**
     * Get the field type
     *
     * @return the field type
     */
    public FieldType getType();
}
