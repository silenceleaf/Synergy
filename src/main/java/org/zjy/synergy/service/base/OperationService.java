package org.zjy.synergy.service.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjy.entity.base.OperationEntity;
import org.zjy.entity.base.OperationGroupEntity;
import org.zjy.entity.base.ResponseOperationEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by junyan Zhang on 14-2-15.
 */

@Service
public class OperationService {
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LogService logService;
    @Autowired
    private AuthorityService authorityService;

    private Map<Integer, List<ResponseOperationEntity>> operationCache;
    //private String retrieve

    public OperationService() {
        operationCache = new HashMap<>();
    }

    public List<ResponseOperationEntity> getOperationList (int toolBarId) {
        if (!operationCache.containsKey(toolBarId)) {
            List<ResponseOperationEntity> operationEntityList = new LinkedList<>();
            Session session = sessionFactory.openSession();
            OperationGroupEntity operationGroupEntity = (OperationGroupEntity)session.get(OperationGroupEntity.class, toolBarId);
            if (operationGroupEntity != null) {
                for (OperationEntity operationEntity : operationGroupEntity.getOperationEntityList())
                    operationEntityList.add(operationEntity.getResponseEntity(propertiesService.getAppLanguage()));

                operationCache.put(toolBarId, operationEntityList);
                session.close();
                return operationEntityList;
            } else {
                logService.error("try to get a not exist group.");
                return  null;
            }
        } else {
            return operationCache.get(toolBarId);
        }
    }

}
