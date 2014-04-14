package org.zjy.synergy.component.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zjy.entity.JsonResponse;
import org.zjy.entity.base.ResponsePromptEntity;
import org.zjy.entity.base.UserEntity;
import org.zjy.service.base.LogService;
import org.zjy.service.base.TemplateService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zjy on 14-1-11.
 */
@Controller
@RequestMapping(value="/login")
public class Login {
    @Autowired
    private LogService logService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TemplateService templateService;

    private final String retrieveUserByName = "from UserEntity as user where user.userName = ? and user.dr = 0";

    @RequestMapping(method = RequestMethod.GET)
    public String loginPage (HttpServletRequest request, HttpServletResponse response) {
        return "base/login";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse loginCheck (String userName, String password) {
        Session session = sessionFactory.openSession();
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder("zjy");
        List<UserEntity> loginUser = session.createQuery(retrieveUserByName).setString(0, userName).list();

        JsonResponse jsonResponse = new JsonResponse(false);
        if (loginUser.size() == 1) {
            if (passwordEncoder.matches(password, loginUser.get(0).getPassword())) {
                if (loginUser.get(0).getRoleList().size() > 0)
                    jsonResponse.setSuccess(true);
                else
                    jsonResponse.addResponseMsg(new ResponsePromptEntity(logService, 103, "Login"));
            } else
                jsonResponse.addResponseMsg(new ResponsePromptEntity(logService, 102, "Login"));
        } else if (loginUser.size() < 1) {
            jsonResponse.addResponseMsg(new ResponsePromptEntity(logService, 101, "Login"));
        } else {
            logService.error("Exist duplicity user name.");
            jsonResponse.addResponseMsg(new ResponsePromptEntity(logService, 109, "Login"));
        }

        return jsonResponse;
    }
}