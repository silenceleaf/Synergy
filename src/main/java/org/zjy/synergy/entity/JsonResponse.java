package org.zjy.synergy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zjy on 14-1-26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse {
    private Boolean success;

    private int total = 0;
    private List<AbstractJsonEntity> msg;

    public JsonResponse() {
        msg = new LinkedList<>();
    }

    public JsonResponse(Boolean success) {
        this.success = success;
        msg = new LinkedList<>();
    }

    public JsonResponse(Boolean success, List<AbstractJsonEntity> msg) {
        this.success = success;
        if (msg != null)
            this.msg = msg;
        else
            msg = new LinkedList<>();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public JsonResponse addResponseMsg(AbstractJsonEntity responseBody) {
        msg.add(responseBody);
        return this;
    }

    public List<AbstractJsonEntity> getMsg() {
        return msg;
    }

    public void setMsg(List<AbstractJsonEntity> msg) {
        this.msg = msg;
    }
}
