package org.dd4tlite.model.impl.xml;

import org.dd4tlite.model.Page;
import org.dd4tlite.service.LinkResolver;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.Validate;

import java.util.Map;

/**
 * XML Component Link
 *
 * @author nic
 */
public class XmlComponentLink implements XmlValue {

    @Text
    private String link;

    private LinkResolver linkResolver;

    private Page page;

    @Commit
    public void initialize(Map map) {
        this.linkResolver = (LinkResolver) map.get(LinkResolver.class);
        this.page = (Page) map.get(Page.class);
    }

    @Override
    public Object getValue() {
        if ( this.linkResolver != null ) {
            return this.linkResolver.resolveLink(this.link, this.page);
        }
        else {
            return this.link;
        }
    }
}
