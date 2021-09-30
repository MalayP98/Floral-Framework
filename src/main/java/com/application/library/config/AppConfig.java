package com.application.library.config;

import com.dummyframework.annotations.Config;
import com.dummyframework.core.Properties;

@Config
public class AppConfig {

    public void setAppName() {
        Properties.set("APP_NAME", "app");
    }

}
