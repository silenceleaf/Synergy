package org.zjy.synergy.entity.base;

import org.zjy.synergy.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by junyan Zhang on 14-2-12.
 */
@Entity
@Table(name = "bd_authority")
public class AuthorityEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_authority")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkAuthority;

    @Column(name = "fk_role")
    private Integer fkRole;

    @Column(name = "fk_function")
    private Integer fkFunction;

    public Integer getPkAuthority() {
        return pkAuthority;
    }

    public void setPkAuthority(Integer pkAuthority) {
        this.pkAuthority = pkAuthority;
    }

    public Integer getFkRole() {
        return fkRole;
    }

    public void setFkRole(Integer fkRole) {
        this.fkRole = fkRole;
    }

    public Integer getFkFunction() {
        return fkFunction;
    }

    public void setFkFunction(Integer fkFunction) {
        this.fkFunction = fkFunction;
    }

}
