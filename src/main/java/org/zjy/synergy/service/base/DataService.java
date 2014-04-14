package org.zjy.synergy.service.base;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjy.synergy.entity.AbstractEntity;
import org.zjy.synergy.entity.AbstractJsonEntity;
import org.zjy.synergy.entity.KeyValueEntity;
import org.zjy.synergy.entity.base.MetadataDetailEntity;
import org.zjy.synergy.entity.base.MetadataEntity;
import org.zjy.synergy.entity.base.TemplateEntity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by junyan Zhang on 14-3-8.
 */

@Service
public class DataService {
    private final int defaultPageSize = 100;
    private final String retrieveAllMetadata = "from MetadataEntity where dr = 0";

    @Autowired
    private LogService logService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private PropertiesService propertiesService;

    private Map<String, MetadataEntity> metadataCache;
    private Map<Integer, MetadataEntity> templateIdMetadataCache;
    private Map<String, Map<String, EntityConvertHelper>> entityToJsonConvertCache;
    private Map<String, Map<String, EntityConvertHelper>> jsonToEntityConvertCache;

    // store get and set method, improve reflection efficiency.
    private class EntityConvertHelper {
        private Method getMethod;
        private Method setMethod;

        public EntityConvertHelper(Method getMethod, Method setMethod) {
            this.getMethod = getMethod;
            this.setMethod = setMethod;
        }

        public void entityToJson(AbstractEntity entityObject, AbstractJsonEntity jsonObject,  boolean skipNull) {
            if (skipNull) {
                try {
                    Object object = getMethod.invoke(entityObject);
                    if (object != null)
                        setMethod.invoke(jsonObject, object);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logService.error("Error when convert entity to json entity.");
                }
            } else {
                try {
                    setMethod.invoke(jsonObject, getMethod.invoke(entityObject));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logService.error("Error when convert entity to json entity.");
                }
            }
        }

        public void entityToJson(AbstractEntity entityObject, AbstractJsonEntity jsonObject) {
            entityToJson(entityObject, jsonObject, true);
        }

        public void jsonToEntity(AbstractJsonEntity jsonObject, AbstractEntity entityObject, boolean skipNull) {
            if (skipNull) {
                try {
                    Object object = getMethod.invoke(jsonObject);
                    if (object != null)
                        setMethod.invoke(entityObject, getMethod.invoke(jsonObject));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logService.error("Error when convert entity to json entity.");
                }
            } else {
                try {
                    setMethod.invoke(entityObject, getMethod.invoke(jsonObject));
                } catch (InvocationTargetException | IllegalAccessException e) {
                    logService.error("Error when convert entity to json entity.");
                }
            }
        }

        public void jsonToEntity(AbstractJsonEntity jsonObject, AbstractEntity entityObject) {
            jsonToEntity(jsonObject, entityObject, true);
        }

        public Method getGetMethod() {
            return getMethod;
        }

        public void setGetMethod(Method getMethod) {
            this.getMethod = getMethod;
        }

        public Method getSetMethod() {
            return setMethod;
        }

        public void setSetMethod(Method setMethod) {
            this.setMethod = setMethod;
        }
    }

    public DataService() {
    }

    // update data
    public List<AbstractEntity> mergeJsonWithEntity (String json, int templateId) {
        Session session = sessionFactory.openSession();
        MetadataEntity metadataEntity;
        if (templateIdMetadataCache == null || !templateIdMetadataCache.containsKey(templateId)) {
            TemplateEntity templateEntity = (TemplateEntity) session.get(TemplateEntity.class, templateId);
            if (templateEntity == null) {
                logService.error("Can not find template entity.");
                return null;
            }
            metadataEntity = (MetadataEntity) session.get(MetadataEntity.class, templateEntity.getFkMetadata());
            if (metadataEntity == null) {
                logService.error("Can not find metadata entity.");
                return null;
            }
            if (templateIdMetadataCache == null) {
                templateIdMetadataCache = new HashMap<>();
            }
            templateIdMetadataCache.put(templateId, metadataEntity);
        } else {
            metadataEntity = templateIdMetadataCache.get(templateId);
        }

        if (jsonToEntityConvertCache == null || !jsonToEntityConvertCache.containsKey(metadataEntity.getName()))
            initJsonToEntityConvertCache(metadataEntity);
        Map<String, EntityConvertHelper> jsonToEntityConvertHelper = jsonToEntityConvertCache.get(metadataEntity.getName());

        List<AbstractJsonEntity> jsonEntityList = stringToJsonList(json, metadataEntity);
        if (jsonEntityList == null) {
            logService.error("Error when convert to json entity");
            return null;
        }
        Class entityClass;
        try {
            entityClass = Class.forName(metadataEntity.getName());
        } catch (ClassNotFoundException e) {
            logService.error("Can not find entity class " + metadataEntity.getName());
            return null;
        }
        List<AbstractEntity> entityList = new LinkedList<>();
        for (AbstractJsonEntity jsonEntity : jsonEntityList) {
            AbstractEntity entity;
            if (jsonEntity.getId() == null || jsonEntity.getId() == 0) {
                try {
                    entity = (AbstractEntity) entityClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logService.error("Error when instance entity class");
                    return null;
                }
            } else {
                entity = (AbstractEntity)session.get(entityClass, jsonEntity.getId());
            }
            Iterator iterator = jsonToEntityConvertHelper.entrySet().iterator();
            while (iterator.hasNext()) {
                EntityConvertHelper entityConvertHelper = (EntityConvertHelper) ((Map.Entry) iterator.next()).getValue();
                entityConvertHelper.jsonToEntity(jsonEntity, entity);
            }
            entityList.add(entity);
        }
        session.close();
        return entityList;
    }

    public List<AbstractEntity> mergeJsonWithEntity (List<AbstractJsonEntity> jsonEntityList, int templateId) {
        Session session = sessionFactory.openSession();
        MetadataEntity metadataEntity;
        if (templateIdMetadataCache == null || !templateIdMetadataCache.containsKey(templateId)) {
            TemplateEntity templateEntity = (TemplateEntity) session.get(TemplateEntity.class, templateId);
            if (templateEntity == null) {
                logService.error("Can not find template entity.");
                return null;
            }
            metadataEntity = (MetadataEntity) session.get(MetadataEntity.class, templateEntity.getFkMetadata());
            if (metadataEntity == null) {
                logService.error("Can not find metadata entity.");
                return null;
            }
            if (templateIdMetadataCache == null) {
                templateIdMetadataCache = new HashMap<>();
            }
            templateIdMetadataCache.put(templateId, metadataEntity);
        } else {
            metadataEntity = templateIdMetadataCache.get(templateId);
        }

        if (jsonToEntityConvertCache == null || !jsonToEntityConvertCache.containsKey(metadataEntity.getName()))
            initJsonToEntityConvertCache(metadataEntity);
        Map<String, EntityConvertHelper> jsonToEntityConvertHelper = jsonToEntityConvertCache.get(metadataEntity.getName());

        Class entityClass;
        try {
            entityClass = Class.forName(metadataEntity.getName());
        } catch (ClassNotFoundException e) {
            logService.error("Can not find entity class " + metadataEntity.getName());
            return null;
        }
        List<AbstractEntity> entityList = new LinkedList<>();
        for (AbstractJsonEntity jsonEntity : jsonEntityList) {
            AbstractEntity entity;
            if (jsonEntity.getId() == null || jsonEntity.getId() == 0) {
                try {
                    entity = (AbstractEntity) entityClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logService.error("Error when instance entity class");
                    return null;
                }
            } else {
                entity = (AbstractEntity)session.get(entityClass, jsonEntity.getId());
            }
            Iterator iterator = jsonToEntityConvertHelper.entrySet().iterator();
            while (iterator.hasNext()) {
                EntityConvertHelper entityConvertHelper = (EntityConvertHelper) ((Map.Entry) iterator.next()).getValue();
                entityConvertHelper.jsonToEntity(jsonEntity, entity);
            }
            entityList.add(entity);
        }
        session.close();
        return entityList;
    }

    // retrieve data with default page size
    public List<AbstractJsonEntity> getData(int templateId, List<KeyValueEntity> queryParameters, int start, String orderBy) {
        return getData(templateId, queryParameters, start, defaultPageSize, orderBy);
    }

    // retrieve data without limit
    public List<AbstractJsonEntity> getData(int templateId, List<KeyValueEntity> queryParameters) {
        return getData(templateId, queryParameters, 0, 0, null);
    }

    // TODO: standard realization, no display level processing, should be modified to template realization
    @SuppressWarnings("unchecked")
    public List<AbstractJsonEntity> getData(int templateId, List<KeyValueEntity> queryParameters, int start, int limit, String orderBy) {
        Session session = sessionFactory.openSession();
        TemplateEntity templateEntity = (TemplateEntity) session.get(TemplateEntity.class, templateId);
        if (templateEntity == null)
            return null;

        MetadataEntity metadataEntity = (MetadataEntity) session.get(MetadataEntity.class, templateEntity.getFkMetadata());
        String hql;
        if (metadataEntity == null) {
            logService.error("Can't find entity: entity pk " + templateEntity.getFkMetadata());
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder("from " + metadataEntity.getName() + " where ");
            for (KeyValueEntity keyValueEntity : queryParameters) {
                int pointIndex = keyValueEntity.getKey().indexOf('.');
                String attributeBody = pointIndex== -1 ? keyValueEntity.getKey() : keyValueEntity.getKey().substring(0, pointIndex);
                MetadataDetailEntity metadataDetailEntity = metadataEntity.getMetadataDetailEntityMap().get(attributeBody);
                if (metadataDetailEntity == null) {
                    logService.debug("parameter " + keyValueEntity.getKey() + " do not exist.");
                    return null;
                }
                if (pointIndex > 0) {
                    if (metadataCache == null)
                        loadMetadataCache();

                    String subAttributeName = keyValueEntity.getKey().substring(++pointIndex);
                    MetadataEntity attributeEntity = metadataCache.get(metadataDetailEntity.getDataType());
                    if (attributeEntity != null && attributeEntity.getMetadataDetailEntityMap().containsKey(subAttributeName)) {
                        MetadataDetailEntity subAttributeDetail = attributeEntity.getMetadataDetailEntityMap().get(subAttributeName);
                        if (subAttributeDetail.getDataType().contains("Integer"))
                            stringBuilder.append(keyValueEntity.getKey()).append(" = ").append(keyValueEntity.getValue()).append(" and ");
                        else if (subAttributeDetail.getDataType().contains("String"))
                            stringBuilder.append(keyValueEntity.getKey()).append(" = '").append(keyValueEntity.getValue()).append("' and ");
                    } else {
                        logService.error("Can not resolve parameter: " + keyValueEntity.getKey());
                        return null;
                    }
                } else {
                    if (metadataDetailEntity.getDataType().contains("Integer"))
                        stringBuilder.append(keyValueEntity.getKey()).append(" = ").append(keyValueEntity.getValue()).append(" and ");
                    else if (metadataDetailEntity.getDataType().contains("String"))
                        stringBuilder.append(keyValueEntity.getKey()).append(" = '").append(keyValueEntity.getValue()).append("' and ");
                }
            }

            stringBuilder.append("dr = 0");
            if (orderBy != null) {
                if (metadataEntity.getMetadataDetailEntityMap().containsKey(orderBy)) {
                    stringBuilder.append(" order by " + orderBy + ";");
                } else {
                    logService.error(" order by parameter " + orderBy + " do not exist.");
                }
            }
            hql = stringBuilder.toString();
        }
        List<AbstractJsonEntity> jsonEntityList = new LinkedList<>();
        List<AbstractEntity> dataEntityList = session.createQuery(hql).list();
        for (AbstractEntity entity : dataEntityList)
            jsonEntityList.add(entityToJson(metadataEntity, entity));

        session.close();
        return jsonEntityList;
    }

    public AbstractJsonEntity entityToJson(AbstractEntity dataEntity) {
        // load all metadata in cache
        if (metadataCache == null)
            loadMetadataCache();

        MetadataEntity metadataEntity = metadataCache.get(dataEntity.getClass().getName());
        if (metadataEntity == null) {
            logService.error("Can't find metadata " + dataEntity.getClass().getName());
            return null;
        }

        return entityToJson(metadataEntity, dataEntity);
    }

    public AbstractJsonEntity entityToJson(MetadataEntity metadataEntity, AbstractEntity dataEntity) {
        if (metadataEntity == null)
            return null;

        Map<String, EntityConvertHelper> convertCacheItem;
        if (entityToJsonConvertCache != null && entityToJsonConvertCache.containsKey(metadataEntity.getName()))
            convertCacheItem = entityToJsonConvertCache.get(metadataEntity.getName());
        else
            convertCacheItem = initEntityToJsonConvertCache(metadataEntity);

        if (convertCacheItem == null) {
            logService.error("Initialize convert cache error, entity convert can't go on.");
            return null;
        }
        AbstractJsonEntity jsonEntity;
        try {
            jsonEntity = (AbstractJsonEntity) Class.forName(metadataEntity.getJsonEntity()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logService.error("Initialize json entity error.");
            return null;
        }

        for (Iterator iterator = convertCacheItem.entrySet().iterator(); iterator.hasNext(); ) {
            EntityConvertHelper entityConvertHelper = (EntityConvertHelper) ((Map.Entry) iterator.next()).getValue();
            entityConvertHelper.entityToJson(dataEntity, jsonEntity);
        }

        return jsonEntity;
    }

//    public List<AbstractEntity> convertStringToEntity(String json, int templateId) {
//        if (templateIdMetadataCache == null) {
//            templateIdMetadataCache = new HashMap<>();
//        }
//        MetadataEntity metadataEntity;
//        if (!templateIdMetadataCache.containsKey(templateId)) {
//            Session session = sessionFactory.openSession();
//            TemplateEntity templateEntity = (TemplateEntity) session.get(TemplateEntity.class, templateId);
//            if (templateEntity == null) {
//                logService.error("Can not find template. id: " + templateId);
//                return null;
//            }
//            metadataEntity = (MetadataEntity) session.get(MetadataEntity.class, templateEntity.getFkMetadata());
//            templateIdMetadataCache.put(templateId, metadataEntity);
//            session.close();
//        } else {
//            metadataEntity = templateIdMetadataCache.get(templateId);
//        }
//
//        List<AbstractJsonEntity> jsonEntityList = stringToJsonList(json, metadataEntity);
//        if (jsonEntityList == null) {
//            logService.error("Error when convert string to json entity");
//            return null;
//        }
//        for (AbstractJsonEntity jsonEntity : jsonEntityList) {
//            Map<String, EntityConvertHelper> convertCacheItem;
//            if (jsonToEntityConvertCache != null && jsonToEntityConvertCache.containsKey(metadataEntity.getName()))
//                convertCacheItem = jsonToEntityConvertCache.get(metadataEntity.getName());
//            else
//                convertCacheItem = initJsonToEntityConvertCache(metadataEntity);
//
//            AbstractEntity entity;
//            try {
//                entity = (AbstractEntity) Class.forName(metadataEntity.getName()).newInstance();
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//                logService.error("Initialize json entity error.");
//                return null;
//            }
//            Iterator iterator = convertCacheItem.entrySet().iterator();
//            while (iterator.hasNext()) {
//                //String jsonFieldName = (String)((Map.Entry)iterator.next()).getKey();
//                EntityConvertHelper entityConvertHelper = (EntityConvertHelper) ((Map.Entry) iterator.next()).getValue();
//                entityConvertHelper.jsonToEntity(jsonEntity, entity);
//            }
//        }
//        return entity;
//    }

    public AbstractEntity jsonToEntity(AbstractJsonEntity jsonEntity, MetadataEntity metadataEntity) {
        if (jsonEntity == null) {
            logService.error("Error when convert string to json entity");
        }
        Map<String, EntityConvertHelper> convertCacheItem;
        if (jsonToEntityConvertCache != null && jsonToEntityConvertCache.containsKey(metadataEntity.getName()))
            convertCacheItem = jsonToEntityConvertCache.get(metadataEntity.getName());
        else
            convertCacheItem = initJsonToEntityConvertCache(metadataEntity);

        AbstractEntity entity;
        try {
            entity = (AbstractEntity) Class.forName(metadataEntity.getName()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logService.error("Initialize json entity error.");
            return null;
        }
        Iterator iterator = convertCacheItem.entrySet().iterator();
        while (iterator.hasNext()) {
            //String jsonFieldName = (String)((Map.Entry)iterator.next()).getKey();
            EntityConvertHelper entityConvertHelper = (EntityConvertHelper)((Map.Entry)iterator.next()).getValue();
            entityConvertHelper.jsonToEntity(jsonEntity, entity);
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    private Map<String, EntityConvertHelper> initEntityToJsonConvertCache(MetadataEntity metadataEntity) {
        Map<String, EntityConvertHelper> convertCacheItem = new HashMap<>();
        Iterator iterator = metadataEntity.getMetadataDetailEntityMap().entrySet().iterator();
        while (iterator.hasNext()) {
            MetadataDetailEntity metadataDetailEntity = (MetadataDetailEntity) ((Map.Entry) iterator.next()).getValue();
            String name = metadataDetailEntity.getName();
            String jsonName = metadataDetailEntity.getJsonName();
            if (jsonName == null) {
                continue;
            }
            String getMethodName;
            String setMethodName;
            if (metadataDetailEntity.getDataType().contains("Boolean")) {
                getMethodName = "is" + name.substring(0, 1).toUpperCase() + name.substring(1);
                setMethodName = "set" + jsonName.substring(0, 1).toUpperCase() + jsonName.substring(1);
            } else {
                if (metadataDetailEntity.getLanguage() != null) {
                    if (metadataDetailEntity.getLanguage() == propertiesService.getAppLanguage()) {
                        getMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        setMethodName = "set" + jsonName.substring(0, 1).toUpperCase() + jsonName.substring(1);
                    } else {
                        continue;
                    }
                } else {
                    getMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    setMethodName = "set" + jsonName.substring(0, 1).toUpperCase() + jsonName.substring(1);
                }
            }

            Class entityClass;
            Class jsonClass;
            try {
                entityClass = Class.forName(metadataEntity.getName());
                jsonClass = Class.forName(metadataEntity.getJsonEntity());
            } catch (ClassNotFoundException e) {
                logService.error("Get entity class or json class error");
                return null;
            }
            // try to find set method in json class
            Method getMethod;
            Method setMethod;
            if (metadataDetailEntity != null) {
                try {
                    getMethod = entityClass.getMethod(getMethodName);
                    Class attributeType = Class.forName(metadataDetailEntity.getDataType());
                    setMethod = jsonClass.getMethod(setMethodName, attributeType);
                } catch (ClassNotFoundException e) {
                    //logService.debug("Can't find class " + metadataDetailEntity.getDataType() + " in build path.");
                    continue;
                } catch (NoSuchMethodException e) {
                    //logService.debug("Can't find method " + getMethodName + " or " + setMethodName + " in " + metadataEntity.getJsonEntity());
                    continue;
                }
                convertCacheItem.put(name, new EntityConvertHelper(getMethod, setMethod));
            }
        }
        if (entityToJsonConvertCache == null) {
            entityToJsonConvertCache = new HashMap<>();
        }
        entityToJsonConvertCache.put(metadataEntity.getName(), convertCacheItem);
        return convertCacheItem;
    }

    @SuppressWarnings("unchecked")
    private Map<String, EntityConvertHelper> initJsonToEntityConvertCache(MetadataEntity metadataEntity) {
        Map<String, EntityConvertHelper> convertCacheItem = new HashMap<>();
        Iterator iterator = metadataEntity.getMetadataDetailEntityMap().entrySet().iterator();
        while (iterator.hasNext()) {
            MetadataDetailEntity metadataDetailEntity = (MetadataDetailEntity) ((Map.Entry) iterator.next()).getValue();
            String entityFieldName = metadataDetailEntity.getName();
            String jsonFieldName = metadataDetailEntity.getJsonName();
            if (jsonFieldName == null) {
                continue;
            }
            String getMethodName;
            String setMethodName;
            if (metadataDetailEntity.getDataType().contains("Boolean")) {
                getMethodName = "is" + jsonFieldName.substring(0, 1).toUpperCase() + jsonFieldName.substring(1);
                setMethodName = "set" + entityFieldName.substring(0, 1).toUpperCase() + entityFieldName.substring(1);
            } else {
                if (metadataDetailEntity.getLanguage() != null) {
                    if (metadataDetailEntity.getLanguage() == propertiesService.getAppLanguage()) {
                        getMethodName = "get" + jsonFieldName.substring(0, 1).toUpperCase() + jsonFieldName.substring(1);
                        setMethodName = "set" + entityFieldName.substring(0, 1).toUpperCase() + entityFieldName.substring(1);
                    } else {
                        continue;
                    }
                } else {
                    getMethodName = "get" + jsonFieldName.substring(0, 1).toUpperCase() + jsonFieldName.substring(1);
                    setMethodName = "set" + entityFieldName.substring(0, 1).toUpperCase() + entityFieldName.substring(1);
                }
            }

            Class entityClass;
            Class jsonClass;
            try {
                entityClass = Class.forName(metadataEntity.getName());
                jsonClass = Class.forName(metadataEntity.getJsonEntity());
            } catch (ClassNotFoundException e) {
                logService.error("Get entity class or json class error");
                return null;
            }
            // try to find set method in json class
            Method getMethod;
            Method setMethod;
            if (metadataDetailEntity != null) {
                try {
                    getMethod = jsonClass.getMethod(getMethodName);
                    Class attributeType = Class.forName(metadataDetailEntity.getDataType());
                    setMethod = entityClass.getMethod(setMethodName, attributeType);
                } catch (ClassNotFoundException e) {
                    //logService.debug("Can't find class " + metadataDetailEntity.getDataType() + " in build path.");
                    continue;
                } catch (NoSuchMethodException e) {
                    //logService.debug("Can't find method " + getMethodName + " or " + setMethodName + " in " + metadataEntity.getJsonEntity());
                    continue;
                }
                convertCacheItem.put(jsonFieldName, new EntityConvertHelper(getMethod, setMethod));
            }
        }
        if (jsonToEntityConvertCache == null) {
            jsonToEntityConvertCache = new HashMap<>();
        }
        jsonToEntityConvertCache.put(metadataEntity.getName(), convertCacheItem);
        return convertCacheItem;
    }

    @SuppressWarnings("unchecked")
    public List<AbstractJsonEntity> stringToJsonList(String jsonString, int templateId) {
        if (templateIdMetadataCache == null) {
            templateIdMetadataCache = new HashMap<>();
        }
        Class jsonEntityClass;
        MetadataEntity metadataEntity;
        if (!templateIdMetadataCache.containsKey(templateId)) {
            Session session = sessionFactory.openSession();
            TemplateEntity templateEntity = (TemplateEntity) session.get(TemplateEntity.class, templateId);
            if (templateEntity == null) {
                logService.error("Can not find template. id: " + templateId);
                return null;
            }
            metadataEntity = (MetadataEntity) session.get(MetadataEntity.class, templateEntity.getFkMetadata());
            templateIdMetadataCache.put(templateId, metadataEntity);
            session.close();
        } else {
            metadataEntity = templateIdMetadataCache.get(templateId);
        }

        try {
            jsonEntityClass = Class.forName(metadataEntity.getJsonEntity());
        } catch (ClassNotFoundException e) {
            logService.error("Can not find json entity class.");
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //LinkedList<HashMap<String, String>> mapList;
        AbstractJsonEntity jsonEntity;
        List<AbstractJsonEntity> jsonEntityList;
        try {
            if (jsonString.startsWith("[")) {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, jsonEntityClass);
                jsonEntityList = objectMapper.readValue(jsonString, javaType);
            } else {
                AbstractJsonEntity singleJsonEntity = (AbstractJsonEntity)objectMapper.readValue(jsonString, jsonEntityClass);
                jsonEntityList = new LinkedList<>();
                jsonEntityList.add(singleJsonEntity);
            }
        } catch (JsonParseException | JsonMappingException e) {
            logService.error(" Json parse error when convert string to json.");
            return null;
        } catch (IOException e) {
            logService.error("IO Error when convert string to json.");
            return null;
        }
        return jsonEntityList;
    }

    @SuppressWarnings("unchecked")
    public List<AbstractJsonEntity> stringToJsonList(String jsonString, MetadataEntity metadataEntity) {

        Class jsonEntityClass;
        if (metadataEntity != null) {
            try {
                jsonEntityClass = Class.forName(metadataEntity.getJsonEntity());
            } catch (ClassNotFoundException e) {
                logService.error("Can not find json entity class.");
                return null;
            }
        } else {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //LinkedList<HashMap<String, String>> mapList;
        AbstractJsonEntity jsonEntity;
        List<AbstractJsonEntity> jsonEntityList;
        try {
            if (jsonString.startsWith("[")) {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, jsonEntityClass);
                jsonEntityList = objectMapper.readValue(jsonString, javaType);
            } else {
                AbstractJsonEntity singleJsonEntity = (AbstractJsonEntity)objectMapper.readValue(jsonString, jsonEntityClass);
                jsonEntityList = new LinkedList<>();
                jsonEntityList.add(singleJsonEntity);
            }
        } catch (JsonParseException | JsonMappingException e) {
            logService.error(" Json parse error when convert string to json.");
            return null;
        } catch (IOException e) {
            logService.error("IO Error when convert string to json.");
            return null;
        }
        return jsonEntityList;
    }

    @SuppressWarnings("unchecked")
    private void loadMetadataCache() {
        metadataCache = new HashMap<>();
        Session session = sessionFactory.openSession();
        List<MetadataEntity> metadataEntityList = session.createQuery(retrieveAllMetadata).list();
        for (MetadataEntity metadataEntity : metadataEntityList)
            metadataCache.put(metadataEntity.getName(), metadataEntity);

        session.close();
    }
}
