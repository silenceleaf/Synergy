package org.zjy.synergy.service.base;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjy.synergy.entity.base.PromptEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjy on 14-1-25.
 */


@Service
public class LogService {
    private final Logger logger = Logger.getLogger(getClass());
    private int appLanguage = 0;

    // log level
    private final int all = 0;
    private final int debug = 1;
    private final int info = 2;
    private final int warn = 3;
    private final int error = 4;
    private final int fatal = 5;
    private final int off = 6;

    private final String retrieveAllPrompt = "from PromptEntity as prompt where prompt.dr = 0";
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PropertiesService propertiesService;

    private Map<Integer, PromptEntity> promptCache;

    public LogService() {

    }

    public String prompt (int promptCode, String... additionalInfo) {
        return getPromptDetailFromCache(promptCode, additionalInfo);
    }

    public void debug (String ... logInfo) {
        if (logInfo != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : logInfo) {
                stringBuilder.append(str);
                stringBuilder.append(" ");
            }
            logger.debug(stringBuilder.toString());
        }
    }

    public void info (String ... logInfo) {
        if (logInfo != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : logInfo) {
                stringBuilder.append(str);
                stringBuilder.append(" ");
            }
            logger.info(stringBuilder.toString());
        }
    }

    public void warn (String ... logInfo) {
        if (logInfo != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : logInfo) {
                stringBuilder.append(str);
                stringBuilder.append(" ");
            }
            logger.warn(stringBuilder.toString());
        }
    }

    public void error (String ... logInfo) {
        if (logInfo != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : logInfo) {
                stringBuilder.append(str);
                stringBuilder.append(" ");
            }
            logger.error(stringBuilder.toString());
        }
    }

    public void fatal (String ... logInfo) {
        if (logInfo != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : logInfo) {
                stringBuilder.append(str);
                stringBuilder.append(" ");
            }
            logger.fatal(stringBuilder.toString());
        }
    }

    @SuppressWarnings(value={"unchecked"})
    private String getPromptDetailFromCache(int promptCode, String... additionalInfo) {
        if (promptCache == null) {
            appLanguage = propertiesService.getAppLanguage();

            Session session = sessionFactory.openSession();
            List<PromptEntity> allPromptList = session.createQuery(retrieveAllPrompt).list();
            promptCache = new HashMap<>();
            for (PromptEntity promptEntity : allPromptList)
                promptCache.put(promptEntity.getPkPrompt(), promptEntity);
            session.close();
        }

        int logLevel = promptCache.get(promptCode).getLevel();

        String detailPrompt = "";
        switch (appLanguage) {
            case PropertiesService.EN:
                detailPrompt = promptCache.get(promptCode).getDetailEn();
                break;
            case PropertiesService.ZH:
                detailPrompt = promptCache.get(promptCode).getDetailZh();
                break;
        }
        StringBuilder additionalInfoBuilder = new StringBuilder();
        for (String additional : additionalInfo)
            additionalInfoBuilder.append("(").append(additional).append(")");
        additionalInfoBuilder.append(".");

        StringBuilder promptBuilder = new StringBuilder();
        switch (logLevel) {
            case debug:
                promptBuilder.append("Debug (").append(String.valueOf(promptCode)).append("): ").append(detailPrompt)
                        .append(" ").append(additionalInfoBuilder);
                logger.debug(promptBuilder.toString());
                break;
            case info:
                promptBuilder.append("Info (").append(String.valueOf(promptCode)).append("): ").append(detailPrompt)
                        .append(" ").append(additionalInfoBuilder);
                logger.info(promptBuilder.toString());
                break;
            case warn:
                promptBuilder.append("Warn (").append(String.valueOf(promptCode)).append("): ").append(detailPrompt)
                        .append(" ").append(additionalInfoBuilder);
                logger.warn(promptBuilder.toString());
                break;
            case error:
                promptBuilder.append("Error (").append(String.valueOf(promptCode)).append("): ").append(detailPrompt)
                        .append(" ").append(additionalInfoBuilder);
                logger.error(promptBuilder.toString());
                break;
            case fatal:
                promptBuilder.append("Fatal (").append(String.valueOf(promptCode)).append("): ").append(detailPrompt)
                        .append(" ").append(additionalInfoBuilder);
                logger.fatal(promptBuilder.toString());
                break;
        }
        // TODO write prompt into log

        return promptBuilder.toString();
    }
}
