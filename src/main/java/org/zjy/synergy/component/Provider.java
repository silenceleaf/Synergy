package org.zjy.synergy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zjy.synergy.entity.AbstractJsonEntity;
import org.zjy.synergy.entity.JsonResponse;
import org.zjy.synergy.entity.KeyValueEntity;
import org.zjy.synergy.entity.base.ResponseOperationEntity;
import org.zjy.synergy.service.base.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by junyan Zhang on 14-2-16.
 */
@Controller
public class Provider {
    @Autowired
    private FunctionService functionService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private DataService dataService;
    @Autowired
    private LogService logService;

    public Provider() {
    }

    @RequestMapping(value="/function", method = RequestMethod.GET)
    @ResponseBody
    public String getFunctionBody(int functionId) {
        return functionService.getFunctionBody (functionId);
    }

    @RequestMapping(value="/toolBar", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getToolbar(int toolBarId) {
        List<ResponseOperationEntity> operationEntityList = operationService.getOperationList(toolBarId);
        JsonResponse result = new JsonResponse();
        if (operationEntityList != null) {
            result.setSuccess(true);
            result.getMsg().addAll(operationEntityList);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/template", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getTemplate (String id) {
        // functionId and user both located a template record
        List<Integer> templateIdList = new LinkedList<>();
        String temp = "";
        for (int i = 0; i < id.length(); i++) {
            if(id.charAt(i) == '_') {
                try {
                    int value = Integer.valueOf(temp);
                    templateIdList.add(value);
                } catch (NumberFormatException e) {
                    temp = new String();
                    continue;
                }
                temp = new String();
            } else {
               temp += id.charAt(i);
            }
        }
        // last one
        try {
            templateIdList.add(Integer.valueOf(temp));
        } catch (NumberFormatException e) {
            /*if something wrong, just ignore*/
        }
        List<AbstractJsonEntity> jsonEntityList = templateService.getTemplateById(templateIdList);
        JsonResponse jsonResponse = new JsonResponse(false);
        if (jsonEntityList != null) {
            jsonResponse.setMsg(jsonEntityList);
            jsonResponse.setSuccess(true);
        }
        return jsonResponse;
    }

    @RequestMapping(value="/metadata", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getMetadataTemplate (String id) {
        int tid;
        try {
            int split = id.indexOf("_");
            if (split < 0)
                tid = Integer.valueOf(id);
            else
                tid = Integer.valueOf(id.substring(0, split));
        } catch (NumberFormatException e) {
            return new JsonResponse(false);
        }
        List<AbstractJsonEntity> jsonEntityList = templateService.getTemplateById(tid);
        JsonResponse jsonResponse = new JsonResponse(false);
        if (jsonEntityList != null) {
            jsonResponse.setMsg(jsonEntityList);
            jsonResponse.setSuccess(true);
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/data/read/{templateId}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse readData (@PathVariable(value="templateId") String templateId, HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        List<KeyValueEntity> parameterList = new LinkedList<>();
        while (parameterNames.hasMoreElements()) {
            String parameter = parameterNames.nextElement();
            if (parameter.equalsIgnoreCase("templateId") || parameter.equalsIgnoreCase("_dc")) {
                continue;
            } else {
                String parameterValue = request.getParameter(parameter);
                if (parameterValue == null) {
                    logService.error("no parameter value.");
                    return null;
                }
                parameterList.add(new KeyValueEntity(parameter, parameterValue));
            }
        }

        int tid;
        try {
            int split = templateId.indexOf("_");
            if (split < 0)
                tid = Integer.valueOf(templateId);
            else
                tid = Integer.valueOf(templateId.substring(0, split));
        } catch (NumberFormatException e) {
            return new JsonResponse(false);
        }
        List<AbstractJsonEntity> entityList = dataService.getData(tid, parameterList);
        if (tid == 0 || entityList == null)
            return new JsonResponse(false);
        else
            return new JsonResponse(true, entityList);
    }

}
