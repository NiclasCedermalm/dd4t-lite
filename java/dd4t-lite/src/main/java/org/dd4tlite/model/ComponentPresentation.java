package org.dd4tlite.model;

/**
 * Component Presentation
 *
 * @author nic
 */
public interface ComponentPresentation {

    // TODO: How much of schema information is needed really?
    // Basically view name + component fields
    // Plus the data needed to generate XPM + be able to query SmartTarget (to limit what component presentations to be shown...)

    public boolean isDynamic();

    public Template getComponentTemplate();

    public Component getComponent();



}
