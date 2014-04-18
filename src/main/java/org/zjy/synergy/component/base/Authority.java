package org.zjy.synergy.component.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zjy.synergy.entity.JsonResponse;
import org.zjy.synergy.entity.base.AuthorityEntity;
import org.zjy.synergy.entity.base.FunctionTreeNodeEntity;
import org.zjy.synergy.entity.base.RoleEntity;
import org.zjy.synergy.service.base.DataService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by junyan Zhang on 14-4-15.
 */

@Controller
public class Authority {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private DataService dataService;

    private String retrieveRoleByName = "from RoleEntity where name = ?";
    private String retrieveAuthority = "select fkFunction from AuthorityEntity where fkRole = :roleId";
    private String retrieveFunctionByAuthority = "from FunctionTreeNodeEntity where pkFunction in (:functions) and dr = 0";
    private String retrieveAllFunction = "from FunctionTreeNodeEntity where dr = 0";
    private String retrieveAuthorityByRoleFunction = "from AuthorityEntity where fkRole = :roleId and fkFunction = :functionId";

    @RequestMapping(value = "/base/authority", method = RequestMethod.GET)
    public String page() {
        return "base/authority";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/authority/createRole", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse createRole (String roleName) {
        Session session = sessionFactory.openSession();
        List<RoleEntity> roleEntityList = session.createQuery(retrieveRoleByName).setParameter(0, roleName).list();
        if (roleEntityList.size() > 0)
            return new JsonResponse(false);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);
        session.save(roleEntity);
        session.flush();
        session.close();
        return new JsonResponse(true);
    }

    @RequestMapping(value = "/base/authority/deleteRole", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse deleteRole (int roleId) {
        Session session = sessionFactory.openSession();
        RoleEntity roleEntity = (RoleEntity)session.get(RoleEntity.class, roleId);
        if (roleEntity == null)
            return new JsonResponse(false);

        session.delete(roleEntity);
        session.flush();
        session.close();
        return new JsonResponse(true);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/authority/getAuthority", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAuthority (int roleId) {
        Session session = sessionFactory.openSession();
        List<Integer> functionList = session.createQuery(retrieveAuthority).setInteger("roleId", roleId).list();
        if (functionList.size() == 0)
            return new JsonResponse(true);

        List<FunctionTreeNodeEntity> functionTreeNodeEntityList = session.createQuery(retrieveFunctionByAuthority).setParameterList("functions", functionList).list();
        JsonResponse jsonResponse = new JsonResponse(true);
        for (FunctionTreeNodeEntity functionTreeNodeEntity : functionTreeNodeEntityList) {
            jsonResponse.addResponseMsg(dataService.entityToJson(functionTreeNodeEntity));
            jsonResponse.setTotal(jsonResponse.getTotal() + 1);
        }
        return jsonResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/authority/getAvailableAuthority", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAvailableAuthority (int roleId) {
        Session session = sessionFactory.openSession();
        Set<Integer> functionList = new HashSet<Integer>(session.createQuery(retrieveAuthority).setInteger("roleId", roleId).list());

        JsonResponse jsonResponse = new JsonResponse(true);
        List<FunctionTreeNodeEntity> allFunctionTreeNodeEntity = session.createQuery(retrieveAllFunction).list();
        for (FunctionTreeNodeEntity functionTreeNodeEntity : allFunctionTreeNodeEntity) {
            if (!functionList.contains(functionTreeNodeEntity.getPkFunction())) {
                jsonResponse.addResponseMsg(dataService.entityToJson(functionTreeNodeEntity));
                jsonResponse.setTotal(jsonResponse.getTotal() + 1);
            }
        }
        session.close();
        return jsonResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/authority/addAuthority", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addAuthority (int roleId, int functionId) {
        Session session = sessionFactory.openSession();
        JsonResponse jsonResponse = new JsonResponse(true);
        List<AuthorityEntity> authorityEntityList = session.createQuery(retrieveAuthorityByRoleFunction).setInteger("roleId", roleId).setInteger("functionId", functionId).list();
        if (authorityEntityList.size() > 0)
            return jsonResponse;

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setFkRole(roleId);
        authorityEntity.setFkFunction(functionId);
        session.save(authorityEntity);
        session.flush();
        session.close();
        return jsonResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/authority/deleteAuthority", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse deleteAuthority (int roleId, int functionId) {
        Session session = sessionFactory.openSession();
        JsonResponse jsonResponse = new JsonResponse(true);
        List<AuthorityEntity> authorityEntityList = session.createQuery(retrieveAuthorityByRoleFunction).setInteger("roleId", roleId).setInteger("functionId", functionId).list();
        if (authorityEntityList.size() == 0)
            return new JsonResponse(false);

        for (AuthorityEntity authorityEntity : authorityEntityList)
            session.delete(authorityEntity);

        session.flush();
        session.close();
        return jsonResponse;
    }
}
