package org.zjy.synergy.entity.base;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.zjy.synergy.config.annotation.EntityOnly;
import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by junyan Zhang on 14-1-29.
 */

@Entity
//@SelectBeforeUpdate
@DynamicInsert
@DynamicUpdate
@Table(name="bd_template_field")
public class TemplateFieldEntity extends AbstractEntity {
    @Id
    @Column(name="pk_template_field")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkTemplateField;

    @EntityOnly
    @ManyToOne
    @JoinColumn(name = "fk_template", updatable = false)
    private TemplateEntity templateEntity;

    @EntityOnly
    @Column(name = "fk_metadata_detail", updatable = false)
    private Integer fkMetadataDetail;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private String type;

    @Column(name = "field_index")
    private Integer fieldIndex;

    @Column(name="default_value")
    private String defaultValue;

    @Column(name="text_en")
    private String textEn;

    @Column(name="text_zh")
    private String textZh;

    @Column(name="description")
    private String description;

    public Integer getPkTemplateField() {
        return pkTemplateField;
    }

    public void setPkTemplateField(Integer pkTemplateField) {
        this.pkTemplateField = pkTemplateField;
    }

    public Integer getFkMetadataDetail() {
        return fkMetadataDetail;
    }

    public void setFkMetadataDetail(Integer fkMetadataDetail) {
        this.fkMetadataDetail = fkMetadataDetail;
    }

    public Integer getFkTemplate() {
        return templateEntity.getPkTemplate();
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

    public TemplateEntity getTemplateEntity() {
        return templateEntity;
    }

    public void setTemplateEntity(TemplateEntity templateEntity) {
        this.templateEntity = templateEntity;
    }


}
