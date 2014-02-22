package org.dd4tlite.factory.org.dd4tlite.factory.impl;

import org.dd4tlite.factory.PageFactory;
import org.dd4tlite.model.Page;

/**
 * SDL Tridion Broker Page Factory
 * @author nic
 */
public class SDLTridionBrokerPageFactory implements PageFactory {

    private int publicationId;

    public SDLTridionBrokerPageFactory(int publicationId) {
        this.publicationId = publicationId;
    }

    @Override
    public Page getPage(String url) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
