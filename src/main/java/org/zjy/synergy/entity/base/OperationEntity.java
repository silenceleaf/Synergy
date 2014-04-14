package org.zjy.synergy.entity.base;

import org.zjy.synergy.entity.AbstractEntity;
import org.zjy.synergy.service.base.PropertiesService;

import javax.persistence.*;

/**
 * Created by junyan Zhang on 14-2-15.
 */

@Entity
@Table(name="bd_operation")
public class OperationEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_operation")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkOperation;
    @ManyToOne
    @JoinColumn(name = "fk_operation_group")
    private OperationGroupEntity operationGroup;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_zh")
    private String nameZh;
    @Column(name = "description_en")
    private String descriptionEn;
    @Column(name = "description_zh")
    private String descriptionZh;
    @Column(name = "xtype")
    private String xtype;
    @Column(name = "width")
    private Integer width;

    @Transient
    private ResponseOperationEntity responseOperationEntity;

    public ResponseOperationEntity getResponseEntity (int language) {
        if (responseOperationEntity == null) {
            switch (language) {
                case PropertiesService.EN:
                    responseOperationEntity = new ResponseOperationEntity(getPkOperation(), getXtype(), getNameEn(), getWidth());
                    responseOperationEntity.setToolTip(getDescriptionEn());
                    break;
                case PropertiesService.ZH:
                    responseOperationEntity = new ResponseOperationEntity(getPkOperation(), getXtype(), getNameZh(), getWidth());
                    responseOperationEntity.setToolTip(getDescriptionZh());
                    break;
            }
        }
        return responseOperationEntity;
    }

    public Integer getPkOperation() {
        return pkOperation;
    }

    public void setPkOperation(Integer pkOperation) {
        this.pkOperation = pkOperation;
    }

    public OperationGroupEntity getOperationGroup() {
        return operationGroup;
    }

    public void setOperationGroup(OperationGroupEntity operationGroup) {
        this.operationGroup = operationGroup;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
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

    public String getXtype() {
        return xtype;
    }

    public void setXtype(String xtype) {
        this.xtype = xtype;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
