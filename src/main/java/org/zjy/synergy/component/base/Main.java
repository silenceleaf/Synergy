package org.zjy.synergy.component.base;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zjy.entity.JsonResponse;
import org.zjy.entity.base.ResponseFuncTreeEntity;
import org.zjy.service.base.FunctionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zjy on 14-1-9.
 */

@Controller
@RequestMapping(value="")
public class Main {
    private final Logger logger =  Logger.getLogger(getClass());

    @Autowired
    private FunctionService functionService;

    @RequestMapping(method = RequestMethod.GET)
    public String page(HttpServletRequest request, HttpServletResponse response) {
        return "base/main";
    }

    @RequestMapping(value="/functionTree", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getFuncTreeNode (int node) {
        List<ResponseFuncTreeEntity> funcTreeEntityList = functionService.dynamicGetFuncTree(1, node);
        JsonResponse result = new JsonResponse();
        if (funcTreeEntityList != null) {
            result.setSuccess(true);
            result.getMsg().addAll(funcTreeEntityList);
        } else {
            result.setSuccess(false);
        }
        return result;
    }
}
