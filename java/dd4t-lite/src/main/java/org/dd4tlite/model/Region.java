package org.dd4tlite.model;

import java.util.List;

/**
 * Region
 *
 * @author nic
 */
public interface Region {

    public String getName();

    public List<ComponentPresentation> getComponentPresentations();

    public Template getTemplate();
}
