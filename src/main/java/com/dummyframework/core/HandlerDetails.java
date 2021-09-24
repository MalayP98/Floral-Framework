package com.dummyframework.core;

import java.lang.reflect.Method;

public class HandlerDetails {

    private final Object component;
    private final Method calledMethod;
    private final boolean atResponseBody;;

    public HandlerDetails(HandlerDetailsBuilder builder) {
        this.component = builder.getComponent();
        this.calledMethod = builder.getCalledMethod();
        this.atResponseBody = builder.getAtResponseBody();
    }

    public Object getComponent() {
        return component;
    }

    public Method getCalledMethod() {
        return calledMethod;
    }

    public boolean isVoid() {
        return (this.calledMethod.getReturnType().equals(Void.TYPE));
    }

    public boolean atResponseBody() {
        return this.atResponseBody;
    }

    @Override
    public String toString() {
        return "HandlerDetails [atResponseBody=" + atResponseBody + ", calledMethod=" + calledMethod + ", component="
                + component + "]";
    }
}
