package org.dd4tlite.model.impl.xml;

import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;

import java.math.BigDecimal;

/**
 * @author nic
 */
public class XmlNumber implements XmlValue {

    @Text
    private String numberString;

    private BigDecimal number;


    @Commit
    private void convert() {
        this.number = new BigDecimal(this.numberString);
    }

    @Override
    public Object getValue() {
        return this.number;
    }
}
