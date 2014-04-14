package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.synergy.entity.AbstractJsonEntity;

/**
 * Created by junyan Zhang on 14-3-20.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class TemplateFieldJson extends AbstractJsonEntity {

    private Integer fkMetadataDetail;
    private String name;
    private String type;
    private Integer fieldIndex;
    private String defaultValue;
    private String textEn;
    private String textZh;
    private String description;

    // conceal attribute
    private Integer fkTemplate;
    private Integer fkFunction;

    public Integer getFkMetadataDetail() {
        return fkMetadataDetail;
    }

    public void setFkMetadataDetail(Integer fkMetadataDetail) {
        this.fkMetadataDetail = fkMetadataDetail;
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

    public Integer getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(Integer fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getTextEn() {
        return textEn;
    }

    public void setTextEn(String textEn) {
        this.textEn = textEn;
    }

    public String getTextZh() {
        return textZh;
    }

    public void setTextZh(String textZh) {
        this.textZh = textZh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFkTemplate() {
        return fkTemplate;
    }

    public void setFkTemplate(Integer fkTemplate) {
        this.fkTemplate = fkTemplate;
    }

    public Integer getFkFunction() {
        return fkFunction;
    }

    public void setFkFunction(Integer fkFunction) {
        this.fkFunction = fkFunction;
    }
}
