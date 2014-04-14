package org.zjy.synergy.service.base;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.JoinedSubclassEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.persister.entity.UnionSubclassEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjy.config.annotation.EntityOnly;
import org.zjy.config.annotation.Language;
import org.zjy.config.annotation.Mapping;
import org.zjy.entity.AbstractJsonEntity;
import org.zjy.entity.base.*;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by junyan Zhang on 14-2-26.
 */

@Service
public class MetadataService {

    @Autowired
    private LogService logService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private DataService dataService;
    @Autowired
    private PropertiesService propertiesService;

    private final String retrieveMetadata = "from MetadataEntity where name = ? and dr = 0";
    // user filter is disabled
    private final String retrieveUserTemplateByMetadataId = "from TemplateEntity where fkMetadata = ? and user = ? and dr = 0";
    private final String retrieveDefaultTemplateByMetadataId = "from TemplateEntity where fkMetadata = ? and user is null and dr = 0";
    private final String retrieveMetadataTemplateById = "from MetadataTemplateEntity where fkTemplate = ? and dr = 0";
    private final String retrieveMetadataCount = "select count(*) from MetadataEntity where name = ? and dr = 0";
    private final String retrieveMaxMetadataPk = "select max(pkMetadata) from MetadataEntity";
    private final String retrieveAllMetadata = "from MetadataEntity where dr = 0";
    private final String retrieveTemplateByMetadata = "from TemplateEntity where fkMetadata = ? and dr = 0";

    // metadata detail type
    public static final int PRIMARY_KEY = 1;
    public static final int PERSISTENT = 2;
    public static final int TRANSIENT = 3;
    public static final int DEFAULT_FIELD = 4;
    public static final int ONE_TO_MANY = 5;
    public static final int MANY_TO_ONE = 6;
    public static final int ONE_TO_ONE = 7;
    public static final int MANY_TO_MANY = 8;

    // metadata entity type
    private final int SINGLE = 1;
    private final int JOIN = 2;
    private final int UNION = 3;

    private Map<String, List<AbstractJsonEntity>> metadataTemplateCache;

    public MetadataService() {

    }

    // get metadata by user
//    @SuppressWarnings("unchecked")
//    public List<AbstractJsonEntity> getMetadataById (int metadataId) {
//        Session session = sessionFactory.openSession();
//        // user authority is disabled, fake user id is passed
//        List<TemplateEntity> templateEntityList = session.createQuery(retrieveUserTemplateByMetadataId)
//                .setParameter(0, metadataId).setParameter(1, 1).list();
//
//        if (templateEntityList.size() ==0) {
//            templateEntityList = session.createQuery(retrieveDefaultTemplateByMetadataId)
//                    .setParameter(0, metadataId).list();
//            if (templateEntityList.size() == 0) {
//                logService.error("Can't find template, id = " + metadataId);
//                return null;
//            }
//        } else if (templateEntityList.size() > 1) {
//            logService.info("User's template is not unique, get first one.");
//        }
//        TemplateEntity templateEntity = templateEntityList.get(0);
//        List<MetadataTemplateEntity> metadataTemplateEntityList = session.createQuery(retrieveMetadataTemplateById)
//                .setParameter(0, templateEntity.getPkTemplate()).list();
//
//        List<AbstractJsonEntity> dataGridModel = new LinkedList<>();
//        for (MetadataTemplateEntity metadataTemplateEntity : metadataTemplateEntityList)
//            dataGridModel.add(metadataTemplateEntity.getModelJson());
//
//        session.close();
//        return dataGridModel;
//    }


    @SuppressWarnings("unchecked")
    public void refreshMetadata() {
        Map<String, ClassMetadata> metadataMap = sessionFactory.getAllClassMetadata();
        for (Iterator i = metadataMap.values().iterator(); i.hasNext(); ) {
            Object persister = i.next();
            try {
                if (persister instanceof SingleTableEntityPersister) {
                    SingleTableEntityPersister singleTableEntityPersister = (SingleTableEntityPersister) persister;
                    saveOrUpdateMetadata(singleTableEntityPersister.getEntityName());
                } else if (persister instanceof JoinedSubclassEntityPersister) {
                    JoinedSubclassEntityPersister joinedSubclassEntityPersister = (JoinedSubclassEntityPersister) persister;
                    saveOrUpdateMetadata(joinedSubclassEntityPersister.getEntityName());
                } else if (persister instanceof UnionSubclassEntityPersister) {
                    UnionSubclassEntityPersister unionSubclassEntityPersister = (UnionSubclassEntityPersister) persister;
                    saveOrUpdateMetadata(unionSubclassEntityPersister.getEntityName());
                } else {
                    logService.error("Type can not be identified.");
                    return;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        // save or update template table
        Session session = sessionFactory.openSession();
        List<MetadataEntity> metadataEntityList = session.createQuery(retrieveAllMetadata).list();
        for (MetadataEntity metadataEntity : metadataEntityList) {
            List<TemplateEntity> templateEntityList = session.createQuery(retrieveTemplateByMetadata).setParameter(0, metadataEntity.getPkMetadata()).list();
            if (templateEntityList != null && templateEntityList.size() > 0) {
                // update template entity
                for (TemplateEntity templateEntity : templateEntityList) {
                    templateEntity.setFkMetadata(metadataEntity.getPkMetadata());
                    templateEntity.setType(templateService.METADATA_TEMPLATE);
                    templateEntity.setModule(metadataEntity.getName());
                    Map<String, MetadataDetailEntity> metadataDetailEntityMap = metadataEntity.getMetadataDetailEntityMap();
                    for (Iterator iterator = metadataDetailEntityMap.entrySet().iterator(); iterator.hasNext(); ) {
                        MetadataDetailEntity metadataDetailEntity = (MetadataDetailEntity) ((Map.Entry) iterator.next()).getValue();
                        if (templateEntity.getFieldEntities().containsKey(metadataDetailEntity.getName())) {
                            TemplateFieldEntity templateFieldEntity = templateEntity.getFieldEntities().get(metadataDetailEntity.getName());
                            templateFieldEntity.setFkMetadataDetail(metadataDetailEntity.getPkMetadataDetail());
                            templateFieldEntity.setName(metadataDetailEntity.getName());
                        } else {
                            TemplateFieldEntity templateFieldEntity = new TemplateFieldEntity();
                            templateFieldEntity.setFkMetadataDetail(metadataDetailEntity.getPkMetadataDetail());
                            templateFieldEntity.setName(metadataDetailEntity.getName());
                            templateEntity.putFieldEntity(templateFieldEntity);
                        }
                    }
                    session.merge(templateEntity);
                    session.flush();
                }
            } else {
                TemplateEntity templateEntity = new TemplateEntity();
                templateEntity.setFkMetadata(metadataEntity.getPkMetadata());
                templateEntity.setType(templateService.METADATA_TEMPLATE);
                templateEntity.setModule(metadataEntity.getName());
                Map<String, MetadataDetailEntity> metadataDetailEntityMap = metadataEntity.getMetadataDetailEntityMap();
                for (Iterator iterator = metadataDetailEntityMap.entrySet().iterator(); iterator.hasNext(); ) {
                    MetadataDetailEntity metadataDetailEntity = (MetadataDetailEntity) ((Map.Entry) iterator.next()).getValue();
                    TemplateFieldEntity templateFieldEntity = new TemplateFieldEntity();
                    templateFieldEntity.setFkMetadataDetail(metadataDetailEntity.getPkMetadataDetail());
                    templateFieldEntity.setName(metadataDetailEntity.getName());
                    templateEntity.putFieldEntity(templateFieldEntity);
                }
                session.save(templateEntity);
                session.flush();
            }
        }

        session.flush();
        session.close();
    }

    @SuppressWarnings("unchecked")
    private void saveOrUpdateMetadata(String entityName) {
        Session session = sessionFactory.openSession();
        Class entityClass;
        try {
            entityClass = Class.forName(entityName);
        } catch (ClassNotFoundException e) {
            logService.error("Class " + entityName + " can't be found.");
            return;
        }
        String jsonEntityName = entityName.substring(0, entityName.lastIndexOf("Entity")) + "Json";

        List<MetadataEntity> metadataEntityList;
        try {
            metadataEntityList = session.createQuery(retrieveMetadata).setString(0, entityName).list();
            if (metadataEntityList.size() == 0) {
                metadataEntityList = new LinkedList<>();
                MetadataEntity metadataEntity = new MetadataEntity();
                metadataEntityList.add(metadataEntity);
            }
        } catch (HibernateException e) {
            metadataEntityList = new LinkedList<>();
            MetadataEntity metadataEntity = new MetadataEntity();
            metadataEntityList.add(metadataEntity);
        }

        for (MetadataEntity metadataEntity : metadataEntityList) {
            // transient field, and update field type, data type
            metadataEntity.setName(entityName);
            metadataEntity.setPrimaryTable(((Table) entityClass.getAnnotation(Table.class)).name());
            metadataEntity.setJsonEntity(jsonEntityName);
            for (Field field : entityClass.getDeclaredFields()) {
                MetadataDetailEntity metadataDetailEntity = null;
                if (metadataEntity.getMetadataDetailEntityMap().size() > 0)
                    metadataDetailEntity = metadataEntity.getMetadataDetailEntityMap().get(field.getName());

                if (metadataDetailEntity == null) {
                    metadataDetailEntity = new MetadataDetailEntity();
                    metadataEntity.putMetadataDetail(metadataDetailEntity);
                }

                metadataDetailEntity.setName(field.getName());
                metadataDetailEntity.setDataType(field.getType().getName());
                if (field.getName().equalsIgnoreCase("dr") || field.getName().equalsIgnoreCase("ts"))
                    metadataDetailEntity.setType(DEFAULT_FIELD);
                else if (field.getAnnotation(Id.class) != null)
                    metadataDetailEntity.setType(PRIMARY_KEY);
                else if (field.getAnnotation(Column.class) != null)
                    metadataDetailEntity.setType(PERSISTENT);
                else if (field.getAnnotation(Transient.class) != null)
                    metadataDetailEntity.setType(TRANSIENT);
                else if (field.getAnnotation(OneToMany.class) != null)
                    metadataDetailEntity.setType(ONE_TO_MANY);
                else if (field.getAnnotation(ManyToOne.class) != null)
                    metadataDetailEntity.setType(MANY_TO_ONE);
                else if (field.getAnnotation(OneToOne.class) != null)
                    metadataDetailEntity.setType(ONE_TO_ONE);
                else if (field.getAnnotation(ManyToMany.class) != null)
                    metadataDetailEntity.setType(MANY_TO_MANY);
                else
                    metadataDetailEntity.setType(TRANSIENT);

                switch (metadataDetailEntity.getType()) {
                    case PRIMARY_KEY:
                        metadataEntity.setPrimaryPk(field.getName());
                        metadataDetailEntity.setFieldName(field.getAnnotation(Column.class).name());
                        break;
                    case PERSISTENT:
                        metadataDetailEntity.setFieldName(field.getAnnotation(Column.class).name());
                        break;
                    case TRANSIENT:
                        // something
                        break;
                    case DEFAULT_FIELD:
                        // something
                        break;
                    case ONE_TO_MANY:
                        // something
                        break;
                    case MANY_TO_ONE:
                        // something
                        break;
                    case ONE_TO_ONE:
                        // something
                        break;
                    case MANY_TO_MANY:
                        // something
                        break;
                    default:
                        // something
                        break;
                }
                // json field
                if (field.getAnnotation(EntityOnly.class) != null) {
                    metadataDetailEntity.setJsonName(null);
                } else if (field.getAnnotation(Language.class) != null) {
                    Language languageAnnotation = field.getAnnotation(Language.class);
                    metadataDetailEntity.setJsonName(field.getName().substring(0, field.getName().length() - 2));
                    metadataDetailEntity.setLanguage(languageAnnotation.value());
                } else if (field.getAnnotation(Mapping.class) != null) {
                    Mapping mappingAnnotation = field.getAnnotation(Mapping.class);
                    metadataDetailEntity.setJsonName(mappingAnnotation.value());
                } else if (field.getAnnotation(Id.class) != null) {
                    metadataDetailEntity.setJsonName("id");
                } else {
                    metadataDetailEntity.setJsonName(field.getName());
                }

                session.saveOrUpdate(metadataEntity);
                session.flush();
            }
        }
        session.close();
    }
}
