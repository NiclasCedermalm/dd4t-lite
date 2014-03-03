package org.dd4tlite.service;

import org.dd4tlite.model.Page;

/**
 * Link Resolver
 *
 * @author nic
 */
public interface LinkResolver {

    public String resolveLink(String id, Page sourcePage);
}
