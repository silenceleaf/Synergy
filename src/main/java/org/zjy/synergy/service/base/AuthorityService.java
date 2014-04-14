package org.zjy.synergy.service.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by junyan Zhang on 14-2-14.
 */

@Service
public class AuthorityService {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LogService logService;

    private final int rootNodeId = 1000;

    private String retrieveRoleAuthority = "select fkFunction from AuthorityEntity where fkRole = ? and dr = 0";

    public AuthorityService() {
        authorityCache = new HashMap<>();
    }

    private Map<Integer, Set<Integer>> authorityCache;

    @SuppressWarnings("unchecked")
    public Set<Integer> getRoleAuthoritySet (int roleId) {
        if (!authorityCache.containsKey(roleId)) {
            Session session = sessionFactory.openSession();
            Set<Integer> roleAuthoritySet = new TreeSet<Integer>(session.createQuery(retrieveRoleAuthority).setInteger(0, roleId).list());
            // everyone have root node authority
            roleAuthoritySet.add(rootNodeId);
            authorityCache.put(roleId, roleAuthoritySet);
            return roleAuthoritySet;
        } else {
            return authorityCache.get(roleId);
        }
    }
}
