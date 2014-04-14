package org.zjy.synergy.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.zjy.entity.AbstractJsonEntity;

/**
 * Created by junyan Zhang on 14-3-31.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateJson extends AbstractJsonEntity {
    private String module;
    private Integer user;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}
