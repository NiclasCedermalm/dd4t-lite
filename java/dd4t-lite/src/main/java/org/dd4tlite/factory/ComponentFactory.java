package org.dd4tlite.factory;

import org.dd4tlite.model.Component;

/**
 * Component Factory.
 * Interface to query dynamic components.
 *
 * @author nic
 */
public interface ComponentFactory {

    public Component getComponent(String id);
}
