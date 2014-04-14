package org.zjy.synergy.entity.base;

import org.zjy.synergy.entity.AbstractJsonEntity;

/**
 * Created by junyan Zhang on 14-4-7.
 */
public class RoleJson extends AbstractJsonEntity {
    private String name;

    public RoleJson() {
    }

    public RoleJson(int roleId, String name) {
        setId(roleId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
