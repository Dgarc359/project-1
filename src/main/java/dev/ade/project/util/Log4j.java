package dev.ade.project.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * The Log4j class helps to provide a static method getLogger for other
 * classes to get a logger to log exception messages.
 */
public class Log4j {
    private static Logger logger = Logger.getLogger(Log4j.class);

    static {
        File error = new File("..\\..\\..\\log4j.properties");
        PropertyConfigurator.configure(error.getPath());
    }

    public static Logger getLogger() {
        return logger;
    }

}

