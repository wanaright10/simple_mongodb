package com.simple.mongodb.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dong Wang.
 * Created on 14-4-1 下午4:42.
 * <p>
 * this is the get all .properties file value by some key tools
 * it contains project.properties and another .properties
 */
public interface Configure {

    /**
     * read properties file value by some key
     */
    static String getProperty(String key) {
        return SingletonProperties.INSTANCE.getProperty().getProperty(key);
    }

    /**
     * get the key int the properties by value
     * the reverse of getProperty() method
     */
    static String getReverseProperty(String value) {
        String result = "";
        Set<Map.Entry<Object, Object>> propertiesEntry = SingletonProperties.INSTANCE.getProperty().entrySet();

        for (Iterator<Map.Entry<Object, Object>> iterator = propertiesEntry.iterator(); iterator.hasNext(); ) {

            Map.Entry<Object, Object> entry = iterator.next();
            String keyInEntry = (String) entry.getKey();
            String valueInEntry = (String) entry.getValue();

            if (!EmptyUtil.isNullOrEmpty(valueInEntry) && !EmptyUtil.isNullOrEmpty(keyInEntry) && valueInEntry.equals(value)) {
                result = keyInEntry;
                break;
            }
        }
        return result;
    }
}
