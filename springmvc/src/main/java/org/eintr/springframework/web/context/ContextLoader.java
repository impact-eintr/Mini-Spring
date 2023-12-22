package org.eintr.springframework.web.context;

import javax.servlet.ServletContext;

public class ContextLoader {


    public static final String CONTEXT_ID_PARAM = "contextId";


    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    public static final String CONTEXT_CLASS_PARAM = "contextClass";


    public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";


    public static final String GLOBAL_INITIALIZER_CLASSES_PARAM = "globalInitializerClasses";


    private static final String INIT_PARAM_DELIMITERS = ",; \t\n";


    private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";
}
