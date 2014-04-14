package org.zjy.synergy.component.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zjy.synergy.entity.AbstractEntity;
import org.zjy.synergy.entity.JsonResponse;
import org.zjy.synergy.service.base.DataService;
import org.zjy.synergy.service.base.LogService;
import org.zjy.synergy.service.base.MetadataService;

import java.util.List;

/**
 * Created by junyan Zhang on 14-3-31.
 */
@Controller
public class Metadata {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private DataService dataService;
    @Autowired
    private LogService logService;


    @RequestMapping(value="/base/metadata", method = RequestMethod.GET)
    public String page () {
        return "base/metadata";
    }

    @RequestMapping(value="/base/metadata/refresh", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse refreshMetadata () {
        metadataService.refreshMetadata();
        return new JsonResponse(true);
    }

    @RequestMapping(value = "/data/{operation}/10008_1", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse writeData (@PathVariable(value="operation") String operation,
                                   @RequestBody String json) {
        Session session = sessionFactory.openSession();
        JsonResponse jsonResponse = new JsonResponse(false);
        List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(json, 10008);
        //AbstractEntity entity = dataService.convertStringToEntity(json, templateId);
        if (operation.equalsIgnoreCase("create")) {
            for (AbstractEntity entity : entityList) {
                if (entity != null) {
                    session.save(entity);
                } else {
                    return jsonResponse;
                }
            }
        } else if (operation.equalsIgnoreCase("update")) {
            for (AbstractEntity entity : entityList) {
                if (entity != null) {
                    session.update(entity);
                } else {
                    return jsonResponse;
                }
            }
        } else if (operation.equalsIgnoreCase("destroy")) {
            for (AbstractEntity entity : entityList) {
                if (entity != null) {
                    session.delete(entity);
                } else {
                    return jsonResponse;
                }
            }
        } else {
            logService.error("Undefined operation. " + operation);
        }
        jsonResponse.setSuccess(true);
        session.flush();
        session.close();
        return jsonResponse;
    }
}
