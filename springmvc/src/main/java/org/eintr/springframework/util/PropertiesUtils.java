package org.eintr.springframework.util;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public class PropertiesUtils {

    public static <T extends Properties> T parseProperties(String text, Class<T> targetClass) {
        Assert.notNull(text, "Text must not be null");
        Assert.notNull(targetClass, "Target class must not be null");

        if (Properties.class == targetClass) {
            Properties properties = new Properties();
            String[] keyvalues = text.split(";");
            for (String keyvalue : keyvalues) {
                String[] kv = keyvalue.split("=");
                if (kv.length != 2) {
                    return null;
                }
                properties.setProperty(kv[0], kv[1]);
            }
            return (T) properties;
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }
}
