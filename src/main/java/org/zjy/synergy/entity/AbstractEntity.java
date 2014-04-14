package org.zjy.synergy.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by zjy on 14-1-15.
 */


@MappedSuperclass
public class AbstractEntity {
    @Column(name="dr", updatable = false)
    protected Integer dr = 0;
    @Column(name="ts", updatable = false)
    protected String ts;

    protected Integer getDr() {
        return dr;
    }

    protected void setDr(Integer dr) {
        this.dr = dr;
    }

    protected String getTs() {
        return ts;
    }

    protected void setTs(String ts) {
        this.ts = ts;
    }
}
