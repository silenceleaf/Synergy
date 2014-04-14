package org.zjy.synergy.component;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zjy.entity.JsonResponse;
import org.zjy.entity.base.ResponseFuncTreeEntity;
import org.zjy.entity.base.TemplateEntity;
import org.zjy.entity.base.TemplateFieldEntity;
import org.zjy.service.base.FunctionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zjy on 14-1-9.
 */

@Controller
@RequestMapping(value="/test")
public class Test {
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public String test(HttpServletRequest request, HttpServletResponse response) {
        Session session = sessionFactory.openSession();
        String hql1 = "from TemplateFieldEntity where templateEntity.type = 2";
        List<TemplateFieldEntity> templateFieldEntityList = session.createQuery(hql1).list();
//        String hql2 = "select new TemplateEntity (templateEntity) from TemplateFieldEntity where templateEntity.type = 2";
//        List<TemplateEntity> templateEntityList = session.createQuery(hql2).list();
        session.close();
        return "test";
    }
}
