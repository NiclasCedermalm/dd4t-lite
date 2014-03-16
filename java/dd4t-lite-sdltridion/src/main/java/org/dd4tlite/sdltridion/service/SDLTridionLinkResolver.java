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
