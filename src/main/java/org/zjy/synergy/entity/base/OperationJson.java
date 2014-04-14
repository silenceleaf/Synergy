package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.synergy.entity.AbstractJsonEntity;


/**
 * Created by junyan Zhang on 14-4-2.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationJson extends AbstractJsonEntity {
    private Integer fkFunction;
    private String nameEn;
    private String nameZh;
    private String descriptionEn;
    private String descriptionZh;
    private String xtype;
    private Integer width;

    public Integer getFkFunction() {
        return fkFunction;
    }

    public void setFkFunction(Integer fkFunction) {
        this.fkFunction = fkFunction;
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
