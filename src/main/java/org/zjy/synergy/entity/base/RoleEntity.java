package org.zjy.synergy.entity.base;

import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zjy on 14-1-25.
 */
@Entity
@Table(name = "bd_role")
public class RoleEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_role")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkRole;

    @Column(name = "name")
    private String name;

    @ManyToMany (fetch=FetchType.EAGER)
    @JoinTable(name = "bd_user_role", joinColumns = @JoinColumn(name = "fk_role"), inverseJoinColumns = @JoinColumn(name = "fk_user"))
    private List<UserEntity> userList = new LinkedList<>();

    public Integer getPkRole() {
        return pkRole;
    }

    public void setPkRole(Integer pkRole) {
        this.pkRole = pkRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }
}
