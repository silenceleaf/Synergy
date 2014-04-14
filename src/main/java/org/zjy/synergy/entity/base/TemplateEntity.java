package org.zjy.synergy.entity.base;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zjy on 14-1-25.
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="bd_template")
public class TemplateEntity extends AbstractEntity {
    @Id
    @Column(name="pk_template")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkTemplate;
    @Column(name = "fk_function")
    private Integer fkFunction;
    @Column(name = "fk_metadata")
    private Integer fkMetadata;
    @Column(name = "type")
    private Integer type;
    @Column(name="module")
    private String module;
    @Column(name="user")
    private Integer user;
    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "templateEntity", cascade = {CascadeType.ALL})
    @MapKeyColumn(name = "name")
    private Map<String, TemplateFieldEntity> fieldEntities = new LinkedHashMap<>();

    public Integer getPkTemplate() {
        return pkTemplate;
    }

    public void setPkTemplate(Integer pkTemplate) {
        this.pkTemplate = pkTemplate;
    }

    public Integer getFkFunction() {
        return fkFunction;
    }

    public void setFkFunction(Integer fkFunction) {
        this.fkFunction = fkFunction;
    }

    public Integer getFkMetadata() {
        return fkMetadata;
    }

    public void setFkMetadata(Integer fkMetadata) {
        this.fkMetadata = fkMetadata;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, TemplateFieldEntity> getFieldEntities() {
        return fieldEntities;
    }

    public void putFieldEntity (TemplateFieldEntity templateFieldEntity){
        templateFieldEntity.setTemplateEntity(this);
        fieldEntities.put(templateFieldEntity.getName(), templateFieldEntity);
    }
}
