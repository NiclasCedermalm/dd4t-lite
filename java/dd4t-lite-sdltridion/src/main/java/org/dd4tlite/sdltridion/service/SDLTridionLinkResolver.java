package org.dd4tlite.sdltridion.service;

import com.tridion.linking.ComponentLink;
import com.tridion.linking.Link;
import com.tridion.util.TCMURI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dd4tlite.model.Page;
import org.dd4tlite.service.LinkResolver;

import java.text.ParseException;

/**
 * SDL Tridion Link Resolver
 *
 * @author nic
 */
public class SDLTridionLinkResolver implements LinkResolver {

    static private Log log = LogFactory.getLog(SDLTridionLinkResolver.class);

    static final String DEFAULT_URI   = "tcm:0-0-0";

    @Override
    public String resolveLink(String id, Page sourcePage) {

        String linkUrl = "";
        if ( id == null ) return linkUrl;
        if ( !id.startsWith("tcm:") ) {
            return id;
        }
        try {
            TCMURI componentUriValue = new TCMURI(id);
            ComponentLink componentLink = new ComponentLink(componentUriValue.getPublicationId());
            Link link = componentLink.getLink(sourcePage.getId(), id, DEFAULT_URI, "", "", false, false);
            if ( link.isResolved() ) {
                linkUrl = link.getURL();
            }
        }
        catch (ParseException e) {
            log.error("Invalid link TCM URI!", e);
        }
        return linkUrl;
    }
}
