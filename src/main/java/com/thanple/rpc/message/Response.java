package com.thanple.rpc.message;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class Response implements Serializable {
    private String uuid;
    private boolean isSuccess;
    private String msg;
    private Object result;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
