package org.zjy.synergy.entity;

/**
 * Created by zjy on 14-1-25.
 */
public class KeyValueEntity {
    private String key;
    private String value;

    public KeyValueEntity() {
    }

    public KeyValueEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
