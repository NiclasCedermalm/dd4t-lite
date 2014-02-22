package org.dd4tlite.factory;

import org.dd4tlite.model.Page;

/**
 * Page Factory
 *
 * @author nic
 */
public interface PageFactory {


    // TODO: What type of exception to throw here???
    Page getPage(String url) throws Exception;
}
