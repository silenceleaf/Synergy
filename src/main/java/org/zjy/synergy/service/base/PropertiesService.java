package org.zjy.synergy.service.base;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by zjy on 14-1-25.
 */

@Service
public class PropertiesService extends Properties {
    private final Logger logger =  Logger.getLogger(getClass());
    private int appLanguage = 0;

    // language
    public static final int EN = 1;
    public static final int ZH = 2;

    public PropertiesService() {
        try {
            this.load(new FileInputStream("src/main/webapp/WEB-INF/app.properties"));
        } catch (Exception e) {
            logger.error("Open properties file error.");
        }
    }

    public int getAppLanguage () {
        if (appLanguage == 0) {
            String appLanguageConfig = this.getProperty("app.language");
            switch (appLanguageConfig) {
                case "en":
                    appLanguage = EN;
                    break;
                case "zh":
                    appLanguage = ZH;
                    break;
                default:
                    appLanguage = EN;
                    break;
            }
        }
        return appLanguage;
    }

    public String getLanguageStr () {
        switch (getAppLanguage()) {
            case EN:
                return "En";
            case ZH:
                return "Zh";
            default:
                return "En";
        }
    }

}
