package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.synergy.entity.AbstractJsonEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by junyan Zhang on 14-2-8.
 */

public class ResponseFuncTreeEntity extends AbstractJsonEntity {
    private boolean allowDrag;
    private boolean allowDrop;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResponseFuncTreeEntity> children;
    private String cls;
    private String description;
    private boolean expandable;
    private boolean expanded;
    private String icon;
    private boolean leaf;
    private boolean loaded;
    private Integer parentId;
    private String qtip;
    private String qtitle;
    private boolean root;
    private String text;
    private String url;


    public ResponseFuncTreeEntity() {
        // Some default value
        this.allowDrag = false;
        this.allowDrop = false;
        // if using extjs dynamic load, no children
        //this.children = new LinkedList<>();
        this.loaded = false;
        this.expandable = true;
        this.expanded = true;
    }

    public boolean isAllowDrag() {
        return allowDrag;
    }

    public void setAllowDrag(boolean allowDrag) {
        this.allowDrag = allowDrag;
    }

    public boolean isAllowDrop() {
        return allowDrop;
    }

    public void setAllowDrop(boolean allowDrop) {
        this.allowDrop = allowDrop;
    }

    public List<ResponseFuncTreeEntity> getChildren() {
        return children;
    }

    public ResponseFuncTreeEntity addChildrenNode (ResponseFuncTreeEntity child) {
        if (children == null)
            children = new LinkedList<>();

        children.add(child);
        return child;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getQtip() {
        return qtip;
    }

    public void setQtip(String qtip) {
        this.qtip = qtip;
    }

    public String getQtitle() {
        return qtitle;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
