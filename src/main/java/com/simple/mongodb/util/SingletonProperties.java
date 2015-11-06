package com.simple.mongodb.util;


import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Dong Wang.
 * Created on 14/12/26 下午3:54
 * <p>
 * 枚举单列模式
 * 私有构造方法中，对property进行赋值和初始化
 */
@Log4j
enum SingletonProperties {
    INSTANCE;

    private Properties property;

    SingletonProperties() {
        property = new Properties();

        loadPropertiesFile();
    }

    private void loadPropertiesFile() {
        try (InputStream inputStream = new FileInputStream(SingletonProperties.class.getResource("/mongodb.properties").getPath())) {
            property.load(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    protected Properties getProperty() {
        return this.property;
    }
}
