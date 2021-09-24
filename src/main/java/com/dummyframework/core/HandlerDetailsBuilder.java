package com.dummyframework.core;

import java.lang.reflect.Method;

public class HandlerDetailsBuilder {

    private Object component;
    private Method calledMethod;
    private boolean atResponseBody;

    public Object getComponent() {
        return component;
    }

    public void setComponent(Object component) {
        this.component = component;
    }

    public Method getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(Method calledMethod) {
        this.calledMethod = calledMethod;
    }

    public void setAtResponseBody(boolean atResponseBody) {
        this.atResponseBody = atResponseBody;
    }

    public boolean getAtResponseBody() {
        return this.atResponseBody;
    }
}
