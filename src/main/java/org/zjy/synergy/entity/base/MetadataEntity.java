package org.zjy.synergy.entity.base;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by junyan Zhang on 14-2-26.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "bd_metadata")
public class MetadataEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_metadata")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer pkMetadata;
    @Column(name = "name")
    private String name;
    @Column(name = "entity_type")
    private Integer entityType;
    @Column(name = "display_name_en")
    private String displayNameEn;
    @Column(name = "display_name_zh")
    private String getDisplayNameZh;
    @Column(name = "primary_table")
    private String primaryTable;
    @Column(name = "primary_pk")
    private String primaryPk;
    @Column(name = "json_entity")
    private String jsonEntity;
    @Column(name = "description_en")
    private String descriptionEn;
    @Column(name = "description_zh")
    private String descriptionZh;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "metadataEntity", cascade = {CascadeType.ALL})
    @MapKey(name = "name")
    private Map<String, MetadataDetailEntity> metadataDetailEntityMap = new LinkedHashMap<>();

    public MetadataEntity() {
        metadataDetailEntityMap = new LinkedHashMap<>();
    }

    public Integer getPkMetadata() {
        return pkMetadata;
    }

    public void setPkMetadata(Integer pkMetadata) {
        this.pkMetadata = pkMetadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getDisplayNameEn() {
        return displayNameEn;
    }

    public void setDisplayNameEn(String displayNameEn) {
        this.displayNameEn = displayNameEn;
    }

    public String getGetDisplayNameZh() {
        return getDisplayNameZh;
    }

    public void setGetDisplayNameZh(String getDisplayNameZh) {
        this.getDisplayNameZh = getDisplayNameZh;
    }

    public String getPrimaryTable() {
        return primaryTable;
    }

    public void setPrimaryTable(String tableName) {
        this.primaryTable = tableName;
    }

    public String getPrimaryPk() {
        return primaryPk;
    }

    public void setPrimaryPk(String primaryPk) {
        this.primaryPk = primaryPk;
    }

    public String getJsonEntity() {
        return jsonEntity;
    }

    public void setJsonEntity(String jsonEntity) {
        this.jsonEntity = jsonEntity;
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

    public Map<String, MetadataDetailEntity> getMetadataDetailEntityMap() {
        return metadataDetailEntityMap;
    }

    public boolean containsColumn (String columnName) {
        return metadataDetailEntityMap.containsKey(columnName);
    }

    public MetadataDetailEntity getMetadataDetail (String name) {
        return metadataDetailEntityMap.get(name);
    }

    public void putMetadataDetail (MetadataDetailEntity metadataDetailEntity) {
        metadataDetailEntityMap.put(metadataDetailEntity.getName(), metadataDetailEntity);
        metadataDetailEntity.setMetadataEntity(this);
    }
}
