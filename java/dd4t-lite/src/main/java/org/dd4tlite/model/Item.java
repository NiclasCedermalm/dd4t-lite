package org.dd4tlite.model;

import java.util.Date;
import java.util.List;

/**
 * Item. Base for all content types.
 *
 * @author nic
 */
public interface Item {

    /**
     * Get the tridion id.
     *
     * @return the tridion id i.e. tcm:1-1-32
     */
    public String getId();


    /**
     * Get the title
     *
     * @return
     */
    public String getTitle();

    /**
     * Get revision date
     *
     * @return revision date
     */
    public Date getRevisionDate();


    public List<Field> getMetadata();

    public Field getMetadata(String name);

    /**
     * Get the last published date
     *
     * @return the last published date
     */
    // TODO: WHERE TO PLACE THIS ONE????
    //public Date getLastPublishedDate();

}
