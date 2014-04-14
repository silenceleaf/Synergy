package org.zjy.synergy.service.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjy.entity.AbstractJsonEntity;
import org.zjy.entity.base.*;

import java.util.*;

/**
 * Created by zjy on 14-1-25.
 */


@Service
public class TemplateService {
    public static final int TEMPLATE = 1;
    public static final int METADATA_TEMPLATE = 2;

    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LogService logService;
    @Autowired
    private DataService dataService;

    private Map<Integer, List<TemplateEntity>> templateCache;

    private String retrieveTemplatesByModule = "from TemplateEntity where module in (:modules) and dr = 0";
    private String retrieveMetadataTemplateById = "from MetadataTemplateEntity where fkTemplate in (:fkTemplate) and dr = 0";
    private String retrieveTemplateFieldById = "from TemplateFieldEntity where templateEntity.pkTemplate in (:fkTemplate) and dr = 0";


    public TemplateService() {
        templateCache = new LinkedHashMap<>();
    }



    @SuppressWarnings(value = {"unchecked"})
    public List<AbstractJsonEntity> getTemplateById(int functionId) {
        // (int userId), userId is not supported now
        return getTemplateById(Arrays.asList(functionId));
    }

    @SuppressWarnings(value = {"unchecked"})
    public List<AbstractJsonEntity> getTemplateById(List<Integer> templateId) {
        // TODO (int userId), userId is not supported now
        if (templateId == null || templateId.size() == 0) {
            logService.error("function id can not be null or empty.");
            return null;
        }
        Session session = sessionFactory.openSession();
        List<AbstractJsonEntity> result = new LinkedList<>();
        List<MetadataTemplateEntity> metadataTemplateEntityList = session.createQuery(retrieveMetadataTemplateById).setParameterList("fkTemplate", templateId).list();
        for (MetadataTemplateEntity metadataTemplateEntity : metadataTemplateEntityList) {
            if (metadataTemplateEntity.getJsonName() != null) {
                GridModelJson gridModelJson = new GridModelJson();
                gridModelJson.setId(metadataTemplateEntity.getPkTemplateField());
                gridModelJson.setName(metadataTemplateEntity.getJsonName());
                gridModelJson.setType(metadataTemplateEntity.getType());
                gridModelJson.setFieldIndex(metadataTemplateEntity.getFieldIndex());
                gridModelJson.setDefaultValue(metadataTemplateEntity.getDefaultValue());
                if (propertiesService.getAppLanguage() == PropertiesService.EN)
                    gridModelJson.setText(metadataTemplateEntity.getTextEn());
                else if (propertiesService.getAppLanguage() == PropertiesService.ZH)
                    gridModelJson.setText(metadataTemplateEntity.getTextZh());
                else
                    gridModelJson.setText(metadataTemplateEntity.getTextEn());

                gridModelJson.setDescription(metadataTemplateEntity.getDescription());
                result.add(gridModelJson);
            }
        }
        session.close();
        return result;
    }

//    @SuppressWarnings(value = {"unchecked"})
//    public List<AbstractJsonEntity> getTemplateByModule(String... modules) {
//        Session session = sessionFactory.openSession();
//        List<AbstractJsonEntity> result = new LinkedList<>();
//
//        List<TemplateEntity> templates = session.createQuery(retrieveTemplatesByModule).setParameterList("modules", modules).list();
//        for (TemplateEntity template : templates) {
//            Map<String, TemplateFieldEntity> fieldEntities = template.getFieldEntities();
//            Iterator iterator = fieldEntities.entrySet().iterator();
//            while (iterator.hasNext()) {
//                TemplateFieldEntity templateFieldEntity = (TemplateFieldEntity)((Map.Entry)iterator.next()).getValue();
//                result.add(dataService.entityToJson(templateFieldEntity));
//            }
//        }
//        session.close();
//        return result;
//    }
//
//
//    @SuppressWarnings(value = {"unchecked"})
//    public List<TemplateEntity> getTemplate(String... modules) {
//        Session session = sessionFactory.openSession();
//        List<TemplateEntity> result = new LinkedList<>();
//        List<TemplateEntity> templates = session.createQuery(retrieveTemplatesByModule).setParameterList("modules", modules).list();
//        for (TemplateEntity template : templates)
//            result.add(template);
//        session.close();
//        return result;
//    }
}
