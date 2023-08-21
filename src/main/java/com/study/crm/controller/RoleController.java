package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.query.RoleQuery;
import com.study.crm.service.RoleService;
import com.study.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController  extends BaseController {
    @Resource
    private RoleService roleService;

    @ResponseBody
    @RequestMapping("queryAllRoles")
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @ResponseBody
    @PostMapping("add")
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功");
    }

    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer id, HttpServletRequest request){
        if(id!=null){
            Role role=roleService.selectByPrimaryKey(id);
            request.setAttribute("role",role);
        }
        return "role/add_update";
    }

    @ResponseBody
    @PostMapping("update")
    public ResultInfo update(Role role){
        roleService.updateRole(role);
        return success("更新角色成功");
    }

    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteUser(Integer id){
        roleService.deleteRole(id);
        return success("删除角色成功");
    }

    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId){
        return "role/grant";
    }

    @ResponseBody
    @PostMapping("addGrant")
    public ResultInfo addGrant(Integer roleId,Integer[] mIds){
//        for (Integer mId : mIds) {
//            System.out.println(mId);
//        }
        System.out.println(roleId);
        roleService.addGrant(roleId,mIds);
        return success("角色授权成功");
    }
}
