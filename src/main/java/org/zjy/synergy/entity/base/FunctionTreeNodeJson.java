package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.synergy.entity.AbstractJsonEntity;

import javax.persistence.*;

/**
 * Created by junyan Zhang on 14-4-16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FunctionTreeNodeJson extends AbstractJsonEntity{
    private String functionNo;
    private String nameEn;
    private String nameZh;
    private Boolean display;
    private Integer nodeIndex;
    private String descriptionEn;
    private String descriptionZh;
    private String file;
    private String url;
    private Boolean leaf;

    public String getFunctionNo() {
        return functionNo;
    }

    public void setFunctionNo(String functionNo) {
        this.functionNo = functionNo;
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

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }
}
