package org.zjy.synergy.entity.base;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by junyan Zhang on 14-2-26.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "bd_metadata_detail")
public class MetadataDetailEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_metadata_detail")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer pkMetadataDetail;
    @ManyToOne
    @JoinColumn(name = "fk_metadata")
    private MetadataEntity metadataEntity;
    @Column(name = "name")
    private String name;
    @Column(name = "json_name")
    private String jsonName;
    @Column(name = "language")
    private Integer language;
    @Column(name = "type")
    private Integer type;
    @Column(name = "data_type")
    private String dataType;
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "display_level")
    private Integer displayLevel;
    @Column(name = "description_en")
    private String descriptionEn;
    @Column(name = "description_zh")
    private String descriptionZh;

    public MetadataDetailEntity() {

    }

    public Integer getPkMetadataDetail() {

        return pkMetadataDetail;
    }

    public void setPkMetadataDetail(Integer pkMetadataDetail) {
        this.pkMetadataDetail = pkMetadataDetail;
    }

    public MetadataEntity getMetadataEntity() {
        return metadataEntity;
    }

    public void setMetadataEntity(MetadataEntity metadataEntity) {
        this.metadataEntity = metadataEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonName() {
        return jsonName;
    }

    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionZh() {
        return descriptionZh;
    }

    public void setDescriptionZh(String descriptionZh) {
        this.descriptionZh = descriptionZh;
    }
}
