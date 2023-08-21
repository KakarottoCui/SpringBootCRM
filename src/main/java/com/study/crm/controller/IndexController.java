package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.service.UserService;
import com.study.crm.utils.LoginUserUtil;
import com.study.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/***
 * 登录账号：admin
 * password：12345678
 */
@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest request){
        System.out.println("user"+"user");
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        System.out.println("user"+userId);
        User user=userService.selectByPrimaryKey(userId);
      //  System.out.println("user"+user.getUserName());
        request.getSession().setAttribute("user",user);
        return "main1";
    }



}
