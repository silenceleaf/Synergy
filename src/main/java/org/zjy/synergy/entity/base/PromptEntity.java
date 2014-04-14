package org.zjy.synergy.entity.base;

import org.zjy.entity.AbstractEntity;

import javax.persistence.*;

/**
 * Created by zjy on 14-1-24.
 */
@Entity
@Table(name = "bd_prompt", schema = "", catalog = "")
public class PromptEntity extends AbstractEntity {
    @Id
    @Column(name = "pk_prompt")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer pkPrompt;

    @Column(name = "level")
    private Integer level;

    @Column(name = "module")
    private String module;

    @Column(name = "detail_en")
    private String detailEn;

    @Column(name = "detail_zh")
    private String detailZh;

    public Integer getPkPrompt() {
        return pkPrompt;
    }

    public void setPkPrompt(Integer pkPrompt) {
        this.pkPrompt = pkPrompt;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDetailEn() {
        return detailEn;
    }

    public void setDetailEn(String detailEn) {
        this.detailEn = detailEn;
    }

    public String getDetailZh() {
        return detailZh;
    }

    public void setDetailZh(String detailZh) {
        this.detailZh = detailZh;
    }
}
