package com.rest.yun.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesReader {

    private static Logger logger = LoggerFactory.getLogger(PropertiesReader.class);

    /**
     * get properties
     *
     * @param file
     *            the properties file's path
     * @return properties a Properties object
     */
    public static Properties read(String file) {
        InputStream in = PropertiesReader.class.getResourceAsStream(file);
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("Cannot load properties file", e);
            throw new RuntimeException("Cannot load properties file");
        }
        return properties;
    }
}
