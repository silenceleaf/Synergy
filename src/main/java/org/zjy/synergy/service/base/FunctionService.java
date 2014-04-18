package org.zjy.synergy.service.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjy.synergy.entity.base.FunctionTreeNodeEntity;
import org.zjy.synergy.entity.base.ResponseFuncTreeEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by junyan Zhang on 14-2-8.
 */


@Service
public class FunctionService {
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private LogService logService;
    @Autowired
    private AuthorityService authorityService;

    private Boolean enableFunctionDivCache = false;
    private String divIdPrefix = "function_";
    private Map<Integer, String> functionBodyCache;
    private Map<Integer, FunctionTreeNodeEntity> funcTreeCache;
    private String webContentsRootPath;

    private String retrieveRootFuncNode = "from FunctionTreeNodeEntity where pkFunction = 1000 and dr = 0 order by pkFunction";
    private String getFuncNodeCount = "select count(*) from FunctionTreeNodeEntity where dr = 0";
    private String getNoneLeafNodeCount = "select count(*) from FunctionTreeNodeEntity where leaf = false and dr = 0";
    private String retrieveRoleAuthority = "select fkFunction from AuthorityEntity where fkRole = ? and dr = 0";

    public FunctionService() {
        functionBodyCache = new HashMap<>();
    }

    public List<ResponseFuncTreeEntity> dynamicGetFuncTree (int roleId, int nodeId) {
        reloadFuncTreeNode();
        Set<Integer> roleAuthoritySet = authorityService.getRoleAuthoritySet(roleId);
        int language = propertiesService.getAppLanguage();

        if (roleAuthoritySet.contains(nodeId) && funcTreeCache.containsKey(nodeId)) {
            List<ResponseFuncTreeEntity> responseFuncNodeList= new LinkedList<>();
            FunctionTreeNodeEntity currentNode = funcTreeCache.get(nodeId);
            for (FunctionTreeNodeEntity childNode : currentNode.getChildren()) {
                if (roleAuthoritySet.contains(childNode.getPkFunction()))
                    responseFuncNodeList.add(childNode.getResponseTreeNode(language));
            }
            return responseFuncNodeList;
        }
        return null;
    }

    public String getFunctionBody(int functionId) {
        if (enableFunctionDivCache)
            return (functionBodyCache.containsKey(functionId))? functionBodyCache.get(functionId) : null;

        if (webContentsRootPath == null) {
            String targetPath = "";
            try {
                targetPath = this.getClass().getClassLoader().getResource("").getPath();
            } catch (NullPointerException e) {
                logService.error("Get class path error");
            }
            String appName = propertiesService.getProperty("application.name");
            webContentsRootPath = targetPath.substring(0, targetPath.lastIndexOf(appName) + appName.length() + 1);
        }

        Session session = sessionFactory.openSession();
        FunctionTreeNodeEntity functionTreeNodeEntity = (FunctionTreeNodeEntity)session.get(FunctionTreeNodeEntity.class, functionId);
        String htmlPath;
        if (functionTreeNodeEntity != null && functionTreeNodeEntity.getFile() != null) {
            if (functionTreeNodeEntity.getFile().charAt(0) == '/')
                htmlPath = webContentsRootPath + propertiesService.getProperty("application.viewFilePath") + functionTreeNodeEntity.getFile().substring(1);
            else
                htmlPath = webContentsRootPath + propertiesService.getProperty("application.viewFilePath") + functionTreeNodeEntity.getFile();
        } else {
            logService.error("Get html path error");
            return null;
        }

        String result = "";
        File file=new File(htmlPath);
        try {
            FileInputStream in=new FileInputStream(file);
            int size=in.available();
            byte[] buffer=new byte[size];
            in.read(buffer);
            in.close();
            result = new String(buffer,"UTF8");
        } catch (IOException e) {
            logService.error("IO exception, when finding html file");
        }

        int bodyBeginIndex = 0;
        int bodyEndIndex = 0;
        bodyBeginIndex = result.indexOf("<body>") + 6;
        bodyEndIndex = result.indexOf("</body>");
        String htmlBody = result.substring(bodyBeginIndex, bodyEndIndex);
        functionBodyCache.put(functionId, htmlBody);
        return htmlBody;
    }

    @SuppressWarnings("unchecked")
    public void reloadFuncTreeNode() {
        FunctionTreeNodeEntity functionTreeNodeEntity;
        if (funcTreeCache == null) {
            funcTreeCache = new HashMap<>();
            Session session = sessionFactory.openSession();
            // cache do not contain leaf node
            int nodeCount = ((Long)session.createQuery(getNoneLeafNodeCount).iterate().next()).intValue();
            List<FunctionTreeNodeEntity> functionTreeList = new ArrayList<>(nodeCount);
            functionTreeList.add((FunctionTreeNodeEntity) session.get(FunctionTreeNodeEntity.class, 1000));
            for (int i = 0; i < nodeCount; i++) {
                FunctionTreeNodeEntity funcTreeNode = functionTreeList.get(i);
                if (funcTreeNode.getChildren().size() > 0) {
                    for (FunctionTreeNodeEntity childrenFuncNode : funcTreeNode.getChildren()) {
                        if (childrenFuncNode.getChildren().size() > 0)
                            functionTreeList.add(childrenFuncNode);
                    }
                    funcTreeCache.put(funcTreeNode.getPkFunction(), funcTreeNode);
                }
            }
            session.close();
        }
    }

    //if using extjs dynamic tree load, response node don't need children field
    // this method need to be checked, about response node's children node
    @SuppressWarnings("unchecked")
    public ResponseFuncTreeEntity getFuncTree () {
        Session session = sessionFactory.openSession();
        int nodeCount = ((Long)session.createQuery(getFuncNodeCount).iterate().next()).intValue();
        List<FunctionTreeNodeEntity> functionTreeList = new ArrayList<>(nodeCount);
        functionTreeList.addAll(session.createQuery(retrieveRootFuncNode).list());
        session.close();
        int language = propertiesService.getAppLanguage();
        ResponseFuncTreeEntity rootResponseNode = new ResponseFuncTreeEntity();
        rootResponseNode.setText("root");
        rootResponseNode.setRoot(true);

        if (rootResponseNode.getChildren() == null) {
            logService.error("Node should have children node.");
            return null;
        }

        rootResponseNode.getChildren().add(functionTreeList.get(0).getResponseTreeNode(language));
        ResponseFuncTreeEntity folderNode;
        if (functionTreeList.size() == 1) {
            // Traverse the Node Tree, generate response tree(UI tree)
            for (int i = 0; i < nodeCount; i++) {
                FunctionTreeNodeEntity funcTreeNode = functionTreeList.get(i);
                if (funcTreeNode.getChildren() != null && funcTreeNode.getChildren().size() > 0) {
                    folderNode = funcTreeNode.getResponseTreeNode(language);

                    for (FunctionTreeNodeEntity childrenFuncNode : funcTreeNode.getChildren()) {
                        folderNode.getChildren().add(childrenFuncNode.getResponseTreeNode(language));
                        functionTreeList.add(childrenFuncNode);
                    }
                }
            }
            return rootResponseNode;
        } else {
            logService.error("Get multiple root node or losing root node when retrieve function tree.");
            return null;
        }
    }
}
