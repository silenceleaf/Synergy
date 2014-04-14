package org.zjy.synergy.entity.base;

import org.zjy.synergy.entity.AbstractJsonEntity;

/**
 * Created by junyan Zhang on 14-1-29.
 */
public class ResponseTemplateEntity extends AbstractJsonEntity {

    private String name;
    private String type;
    private String defaultValue;
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
