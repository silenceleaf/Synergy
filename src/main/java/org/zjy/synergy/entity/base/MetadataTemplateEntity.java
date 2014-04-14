package org.zjy.synergy.entity.base;

import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by junyan Zhang on 14-3-9.
 */
@Entity
@Table(name = "bd_template_field")
public class MetadataTemplateEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_template_field")
    private Integer pkTemplateField;

    @Column(name = "fk_template")
    private Integer fkTemplate;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "field_index")
    private Integer fieldIndex;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "text_en")
    private String textEn;

    @Column(name = "text_zh")
    private String textZh;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="fk_metadata_detail")
    private MetadataDetailEntity metadataDetailEntity = new MetadataDetailEntity();

    @Transient
    private String jsonName;

    @Transient
    private String dataType;

    @Transient
    private String fieldName;

    @Transient
    private Integer displayLevel;



    public Integer getPkTemplateField() {
        return pkTemplateField;
    }

    public void setPkTemplateField(Integer pkTemplateField) {
        this.pkTemplateField = pkTemplateField;
    }

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

    public MetadataDetailEntity getMetadataDetailEntity() {
        return metadataDetailEntity;
    }

    public void setMetadataDetailEntity(MetadataDetailEntity metadataDetailEntity) {
        this.metadataDetailEntity = metadataDetailEntity;
    }

    public String getJsonName() {
        if (this.jsonName == null)
            this.jsonName = metadataDetailEntity.getJsonName();
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
        metadataDetailEntity.setJsonName(jsonName);
    }

    public String getDataType() {
        if (dataType == null)
            dataType = metadataDetailEntity.getDataType();
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
        metadataDetailEntity.setDataType(dataType);
    }

    public String getFieldName() {
        if (fieldName == null)
            this.fieldName = metadataDetailEntity.getFieldName();
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
        metadataDetailEntity.setFieldName(fieldName);
    }

    public Integer getDisplayLevel() {
        if (fieldName == null)
            this.displayLevel = metadataDetailEntity.getDisplayLevel();
        return displayLevel;
    }

    public void setDisplayLevel(Integer displayLevel) {
        this.displayLevel = displayLevel;
        metadataDetailEntity.setDisplayLevel(displayLevel);
    }
}
