package org.dd4tlite.model;

import java.util.List;

/**
 * Page
 *
 * @author nic
 */
public interface Page extends Item {

    public String getPath();

    public String getFilename();

    public List<ComponentPresentation> getComponentPresentations();

    public Template getPageTemplate();

    public List<Region> getRegions();

    public Region getRegion(String title);

}
