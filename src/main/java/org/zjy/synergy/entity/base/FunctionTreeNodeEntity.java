package org.zjy.synergy.entity.base;

import org.zjy.entity.AbstractEntity;
import org.zjy.service.base.PropertiesService;

import javax.persistence.*;
import java.util.List;

/**
 * Created by junyan Zhang on 14-2-8.
 */

@Entity
@Table(name="bd_function")
public class FunctionTreeNodeEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_function")
    private Integer pkFunction;

    @Column(name = "function_no")
    private String functionNo;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_zh")
    private String nameZh;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent")
    private FunctionTreeNodeEntity parent;

    @Column(name = "display")
    private Boolean display;

    @Column(name = "node_index")
    private Integer nodeIndex;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "description_zh")
    private String descriptionZh;

    @Column(name = "file")
    private String file;

    @Column(name = "url")
    private String url;

    @Column(name = "leaf")
    private Boolean leaf;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.EAGER)
    private List<FunctionTreeNodeEntity> children;

    @Transient
    private ResponseFuncTreeEntity responseEntity;

    public ResponseFuncTreeEntity getResponseTreeNode(int language) {
        if (responseEntity == null) {
            responseEntity = new ResponseFuncTreeEntity();
            switch (language) {
                case PropertiesService.EN:
                    responseEntity.setText(getNameEn());
                    responseEntity.setDescription(getDescriptionEn());
                    responseEntity.setQtitle(getNameEn());
                    responseEntity.setQtip(getDescriptionEn());
                    break;
                case PropertiesService.ZH:
                    responseEntity.setText(getNameZh());
                    responseEntity.setDescription(getDescriptionZh());
                    responseEntity.setQtitle(getNameZh());
                    responseEntity.setQtip(getDescriptionZh());
                    break;
            }
            responseEntity.setCls(getChildren().size() > 0 ? "folder" : "file");
            responseEntity.setParentId(getParent().getPkFunction());
            responseEntity.setLeaf(isLeaf());
            responseEntity.setId(getPkFunction());
            responseEntity.setUrl(getUrl());
        }
        return responseEntity;
    }

    public Integer getPkFunction() {
        return pkFunction;
    }

    public void setPkFunction(Integer pkFunction) {
        this.pkFunction = pkFunction;
    }

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

    public FunctionTreeNodeEntity getParent() {
        return parent;
    }

    public void setParent(FunctionTreeNodeEntity parent) {
        this.parent = parent;
    }

    public Boolean isDisplay() {
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

    public Boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public List<FunctionTreeNodeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<FunctionTreeNodeEntity> child) {
        this.children = child;
    }
}
