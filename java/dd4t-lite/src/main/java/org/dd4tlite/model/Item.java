/*
 *  Copyright 2014 Niclas Cedermalm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
