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
