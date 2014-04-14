package org.zjy.synergy.entity.base;

import org.hibernate.annotations.Cascade;
import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zjy on 14-1-15.
 */

@Entity
@Table(name = "bd_user")
public class UserEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_user")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkUser;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @ManyToMany (fetch=FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(name = "bd_user_role", joinColumns = @JoinColumn(name = "fk_user"), inverseJoinColumns = @JoinColumn(name = "fk_role"))
    private List<RoleEntity> roleList = new LinkedList<>();

    public Integer getPkUser() {
        return pkUser;
    }

    public void setPkUser(Integer pkUser) {
        this.pkUser = pkUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RoleEntity> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleEntity> roleList) {
        this.roleList = roleList;
    }
}
