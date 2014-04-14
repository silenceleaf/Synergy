package org.zjy.synergy.entity.base;

import org.zjy.entity.AbstractEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by junyan Zhang on 14-2-15.
 */

@Entity
@Table(name="bd_operation_group")
public class OperationGroupEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_operation_group")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkOperationGroup;
    @Column(name = "fk_function")
    private Integer fkFunction;
    @Column(name = "url")
    private String url;
    @Column(name = "group_name_en")
    private String groupNameEn;
    @Column(name = "group_name_zh")
    private String groupNameZh;

    @OneToMany(mappedBy="operationGroup", cascade={CascadeType.ALL})
    private List<OperationEntity> operationEntityList = new LinkedList<>();

    public OperationGroupEntity addOperation (OperationEntity operationEntity) {
        operationEntity.setOperationGroup(this);
        operationEntityList.add(operationEntity);
        return this;
    }

    public Integer getPkOperationGroup() {
        return pkOperationGroup;
    }

    public void setPkOperationGroup(Integer pkOperationGroup) {
        this.pkOperationGroup = pkOperationGroup;
    }

    public Integer getFkFunction() {
        return fkFunction;
    }

    public void setFkFunction(Integer fkFunction) {
        this.fkFunction = fkFunction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroupNameEn() {
        return groupNameEn;
    }

    public void setGroupNameEn(String groupNameEn) {
        this.groupNameEn = groupNameEn;
    }

    public String getGroupNameZh() {
        return groupNameZh;
    }

    public void setGroupNameZh(String groupNameZh) {
        this.groupNameZh = groupNameZh;
    }

    public List<OperationEntity> getOperationEntityList() {
        return operationEntityList;
    }

    public void setOperationEntityList(List<OperationEntity> operationEntityList) {
        this.operationEntityList = operationEntityList;
    }
}
