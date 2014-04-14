package org.zjy.synergy.component.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zjy.entity.AbstractEntity;
import org.zjy.entity.AbstractJsonEntity;
import org.zjy.entity.JsonResponse;
import org.zjy.entity.base.*;
import org.zjy.service.base.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zjy on 14-1-9.
 */

@Controller

public class User {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private DataService dataService;
    @Autowired
    private LogService logService;

    private PasswordEncoder passwordEncoder;

    private String retrieveAllRole = "from RoleEntity where dr = 0";

    public User() {
        passwordEncoder = new StandardPasswordEncoder("zjy");
    }

    @RequestMapping(value="/base/user", method = RequestMethod.GET)
    public String page (HttpServletRequest request, HttpServletResponse response) {
        return "base/user";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/data/{operation}/10002_1", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse writeUser(@PathVariable(value = "operation") String operation,
                                           @RequestBody String json) {
        Session session = sessionFactory.openSession();

        if (operation.equalsIgnoreCase("create")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10002);
            if (jsonEntityList == null) {
                session.close();
                return new JsonResponse(false);
            }

            List<AbstractEntity> abstractEntityList = dataService.mergeJsonWithEntity(jsonEntityList, 10002);
            for (AbstractEntity abstractEntity : abstractEntityList) {
                session.save(abstractEntity);
            }

        } else if (operation.equalsIgnoreCase("update")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10002);
            List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(jsonEntityList, 10002);
            for (AbstractEntity entity : entityList) {
                if (entity != null) {
                    UserEntity userEntity = (UserEntity)entity;
                    if (!userEntity.getPassword().equalsIgnoreCase("") && userEntity.getPassword() != null) {
                        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                    }
                    session.update(entity);
                } else {
                    session.close();
                    return new JsonResponse(false);
                }
            }
        } else if (operation.equalsIgnoreCase("destroy")) {
            List<AbstractJsonEntity> jsonEntityList = dataService.stringToJsonList(json, 10002);
            List<AbstractEntity> entityList = dataService.mergeJsonWithEntity(jsonEntityList, 10002);
            for (AbstractEntity entity : entityList) {
                UserEntity userEntity = (UserEntity) entity;
                if (entity != null && userEntity.getPkUser() != null) {
                    session.delete(userEntity);
                } else {
                    session.close();
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
    @RequestMapping(value = "/base/user/availableRoleList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAvailableRoleList (int userId) {
        Session session = sessionFactory.openSession();
        UserEntity userEntity = (UserEntity)session.get(UserEntity.class, userId);
        List<RoleEntity> allRoleList = session.createQuery(retrieveAllRole).list();
        JsonResponse jsonResponse = new JsonResponse(true);
        for (RoleEntity roleEntity : allRoleList) {
            boolean existFlag = false;
            for (RoleEntity userRole : userEntity.getRoleList()) {
                if (roleEntity.getPkRole() == userRole.getPkRole()) {
                    existFlag = true;
                    break;
                }
            }
            if (!existFlag) {
                RoleJson roleJson = new RoleJson(roleEntity.getPkRole(), roleEntity.getName());
                jsonResponse.addResponseMsg(roleJson);
                jsonResponse.setTotal(jsonResponse.getTotal() + 1);
            }
        }
        session.close();
        return jsonResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/user/userRoleList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getUserRoleList(int userId) {
        Session session = sessionFactory.openSession();
        UserEntity userEntity = (UserEntity) session.get(UserEntity.class, userId);
        JsonResponse jsonResponse = new JsonResponse(true);
        for (RoleEntity userRole : userEntity.getRoleList()) {
            RoleJson roleJson = new RoleJson(userRole.getPkRole(), userRole.getName());
            jsonResponse.addResponseMsg(roleJson);
            jsonResponse.setTotal(jsonResponse.getTotal() + 1);
        }
        session.close();
        return jsonResponse;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/user/deleteRole", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse deleteRole(int userId, int roleId) {
        Session session = sessionFactory.openSession();
        UserEntity userEntity = (UserEntity) session.get(UserEntity.class, userId);
        RoleEntity roleEntity = null;
        for (RoleEntity userRole : userEntity.getRoleList()) {
            if (userRole.getPkRole() == roleId)
                roleEntity = userRole;
        }

        if (roleEntity == null) {
            session.close();
            return new JsonResponse(false);
        } else {
            userEntity.getRoleList().remove(roleEntity);
            session.update(userEntity);
            session.flush();
            session.close();
            return new JsonResponse(true);
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/base/user/addRole", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addRole(int userId, int roleId) {
        Session session = sessionFactory.openSession();
        UserEntity userEntity = (UserEntity) session.get(UserEntity.class, userId);
        RoleEntity roleEntity = (RoleEntity) session.get(RoleEntity.class, roleId);
        if (userEntity == null || roleEntity == null)
            return new JsonResponse(false);

        userEntity.getRoleList().add(roleEntity);

        session.update(userEntity);
        session.flush();
        session.close();
        return new JsonResponse(true);
    }
}
