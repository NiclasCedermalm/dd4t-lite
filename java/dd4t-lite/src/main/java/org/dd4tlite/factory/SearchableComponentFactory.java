package org.dd4tlite.factory;

import org.dd4tlite.model.Component;

import java.util.Collection;

/**
 * Searchable Component Factory
 * Is an extension which allows to search for dynamic content based on content field/metadata conditions
 *
 * @author nic
 */
public interface SearchableComponentFactory extends ComponentFactory {

    // TODO: Specify how filter expressions is specified
    // Use JUEL??

    public Collection<Component> search();

}
