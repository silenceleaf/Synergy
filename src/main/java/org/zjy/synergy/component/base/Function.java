package org.zjy.synergy.component.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zjy.synergy.entity.AbstractEntity;
import org.zjy.synergy.entity.AbstractJsonEntity;
import org.zjy.synergy.entity.JsonResponse;
import org.zjy.synergy.entity.base.*;
import org.zjy.synergy.service.base.DataService;
import org.zjy.synergy.service.base.LogService;
import org.zjy.synergy.service.base.TemplateService;

import java.util.List;

/**
 * Created by junyan Zhang on 14-3-6.
 */

@Controller
public class Function {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private DataService dataService;
    @Autowired
    private LogService logService;

    private String retrieveTemplateByFunctionId = "from TemplateEntity where fkFunction = ?";
    private String retrieveOperationGroupByFunctionId = "from OperationGroupEntity where fkFunction = ?";

    @RequestMapping(value = "/base/function", method = RequestMethod.GET)
    public String page() {
        return "base/function";
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/data/{operation}/10008_2", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse writeTemplateField(@PathVariable(value = "operation") String operation,
                                  @RequestBody String json) {
        Session session = sessionFactory.openSession();

        if (operation.equalsIgnoreCase("create")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10008);
            if (jsonEntityList == null)
                return new JsonResponse(false);

            TemplateFieldJson templateFieldJson = (TemplateFieldJson) jsonEntityList.get(0);
            if (templateFieldJson == null || templateFieldJson.getFkFunction() == null) {
                return new JsonResponse(false);
            }
            List<TemplateEntity> templateEntityList = session.createQuery(retrieveTemplateByFunctionId).setParameter(0, templateFieldJson.getFkFunction()).list();
            TemplateEntity templateEntity;
            if (templateEntityList.size() == 0) {
                templateEntity = new TemplateEntity();
                templateEntity.setFkFunction(templateFieldJson.getFkFunction());
                templateEntity.setType(TemplateService.TEMPLATE);
            } else {
                templateEntity = templateEntityList.get(0);
            }
            List<AbstractEntity> abstractEntityList = dataService.mergeJsonWithEntity(jsonEntityList, 10008);
            for (AbstractEntity abstractEntity : abstractEntityList) {
                TemplateFieldEntity templateFieldEntity = (TemplateFieldEntity) abstractEntity;
                templateEntity.putFieldEntity(templateFieldEntity);
            }
            session.saveOrUpdate(templateEntity);
        } else if (operation.equalsIgnoreCase("update")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10008);
            List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(jsonEntityList, 10008);
            for (AbstractEntity entity : entityList) {
                if (entity != null) {
                    session.saveOrUpdate(entity);
                } else {
                    return new JsonResponse(false);
                }
            }
        } else if (operation.equalsIgnoreCase("destroy")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10008);
            List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(jsonEntityList, 10008);
            for (AbstractEntity entity : entityList) {
                TemplateFieldEntity templateFieldEntity = (TemplateFieldEntity) entity;
                if (entity != null && templateFieldEntity.getPkTemplateField() != null) {
                    session.delete(templateFieldEntity);
                } else {
                    return new JsonResponse(false);
                }
            }
        } else {
            logService.error("Undefined operation. " + operation);
        }
        session.flush();
        session.close();
        return new JsonResponse(true);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/data/{operation}/10007_1", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse writeOperation(@PathVariable(value = "operation") String operation,
                                           @RequestBody String json) {
        Session session = sessionFactory.openSession();
        if (operation.equalsIgnoreCase("create")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10007);
            if (jsonEntityList == null)
                return new JsonResponse(false);

            OperationJson operationJson = (OperationJson) jsonEntityList.get(0);
            if (operationJson == null || operationJson.getFkFunction() == null) {
                return new JsonResponse(false);
            }
            List<OperationGroupEntity> operationGroupEntityList = session.createQuery(retrieveOperationGroupByFunctionId).setParameter(0, operationJson.getFkFunction()).list();
            OperationGroupEntity operationGroupEntity;
            if (operationGroupEntityList.size() == 0) {
                operationGroupEntity = new OperationGroupEntity();
                operationGroupEntity.setFkFunction(operationJson.getFkFunction());
            } else {
                operationGroupEntity = operationGroupEntityList.get(0);
            }
            List<AbstractEntity> abstractEntityList = dataService.mergeJsonWithEntity(jsonEntityList, 10007);
            for (AbstractEntity abstractEntity : abstractEntityList) {
                OperationEntity operationEntity = (OperationEntity) abstractEntity;
                operationGroupEntity.addOperation(operationEntity);
            }
            session.saveOrUpdate(operationGroupEntity);
        } else if (operation.equalsIgnoreCase("update")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10007);
            List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(jsonEntityList, 10007);
            for (AbstractEntity entity : entityList) {
                if (entity != null) {
                    session.saveOrUpdate(entity);
                } else {
                    return new JsonResponse(false);
                }
            }
        } else if (operation.equalsIgnoreCase("destroy")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10007);
            List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(jsonEntityList, 10007);
            for (AbstractEntity entity : entityList) {
                OperationEntity operationEntity = (OperationEntity) entity;
                if (entity != null && operationEntity.getPkOperation() != null) {
                    session.delete(operationEntity);
                } else {
                    return new JsonResponse(false);
                }
            }
        } else {
            logService.error("Undefined operation. " + operation);
        }
        session.flush();
        session.close();
        return new JsonResponse(true);
    }
}
