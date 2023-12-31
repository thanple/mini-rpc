package com.thanple.rpc.message;

import java.io.Serializable;

/**
 * @author Liubsyy
 * @date 2023/12/31
 */
public class Request implements Serializable {
    private String uuid;

    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
