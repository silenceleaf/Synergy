package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.synergy.entity.AbstractJsonEntity;

/**
 * Created by junyan Zhang on 14-3-9.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataTemplateJson extends AbstractJsonEntity {
    private Integer fkTemplate;
    private String name;
    private String type;
    private String defaultValue;
    private String displayEn;
    private String displayZh;
    private String description;
    private String dataType;
    private String fieldName;
    private Integer displayLevel;

    public Integer getFkTemplate() {
        return fkTemplate;
    }

    public void setFkTemplate(Integer fkTemplate) {
        this.fkTemplate = fkTemplate;
    }

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

    public String getDisplayEn() {
        return displayEn;
    }

    public void setDisplayEn(String displayEn) {
        this.displayEn = displayEn;
    }

    public String getDisplayZh() {
        return displayZh;
    }

    public void setDisplayZh(String displayZh) {
        this.displayZh = displayZh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(Integer displayLevel) {
        this.displayLevel = displayLevel;
    }
}
