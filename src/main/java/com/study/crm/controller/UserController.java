package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.model.UserModel;
import com.study.crm.query.UserQuery;
import com.study.crm.service.UserService;
import com.study.crm.utils.LoginUserUtil;
import com.study.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    /***
     * 如果有异常就捕获
     * 没有异常就说明登录成功
     * 将service生成的userModel赋给resultInfo
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo=new ResultInfo();
        UserModel userModel=userService.userLogin(userName,userPwd);
        resultInfo.setResult(userModel);
//        //通过try catch铺货service层的异常
//        try{
//
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败");
//            e.printStackTrace();
//        }
        return resultInfo;
    }

    @ResponseBody
    @PostMapping("updatePassword")
    public ResultInfo updatePassword(HttpServletRequest request,String oldPassword, String newPassword, String repeatPassword){
        ResultInfo resultInfo=new ResultInfo();
        System.out.println(oldPassword);
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updatePassword(userId,oldPassword,newPassword,repeatPassword);
//        try{
//
//
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败");
//            e.printStackTrace();
//        }
        return resultInfo;
    }
    @RequestMapping("toPasswordPage")
    public  String toPasswordPage(){
        return "user/password";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    @ResponseBody
    @PostMapping("add")
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("添加用户成功");
    }
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id,HttpServletRequest request){
        if(id!=null){
            User user=userService.selectByPrimaryKey(id);
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("更新用户成功");
    }

    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success("删除用户成功");
    }

    @PostMapping("queryAllCustomerManager")
    @ResponseBody
    public List<Map<String,Object>> queryAllCustomerManager(){
        return userService.queryAllCustomerManager();
    }
}
