package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.entity.AbstractJsonEntity;

/**
 * Created by junyan Zhang on 14-2-16.
 */
public class ResponseOperationEntity extends AbstractJsonEntity {
    private String xtype;
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer width;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String toolTip;

    public ResponseOperationEntity(int id, String xtype, String text, Integer width) {
        this.id = id;
        this.xtype = xtype;
        this.text = text;
        this.width = width;
    }

    public ResponseOperationEntity() {
    }

    public String getXtype() {
        return xtype;
    }

    public void setXtype(String xtype) {
        this.xtype = xtype;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
}
