package org.zjy.synergy.entity.base;

import org.zjy.entity.AbstractJsonEntity;
import org.zjy.service.base.LogService;

/**
 * Created by junyan Zhang on 14-2-6.
 */
public class ResponsePromptEntity extends AbstractJsonEntity {
    private String message;
    private LogService logService;

    public ResponsePromptEntity(LogService logService, int promptId, String... additionalInfo) {
        this.logService = logService;
        setId(promptId);
        this.message = logService.prompt(promptId, additionalInfo);
    }

    public String getMessage() {
        return message;
    }

    public void setPromptMessage(int promptId, String... additionalInfo) {
        setId(promptId);
        this.message = logService.prompt(promptId, additionalInfo);
    }
}
