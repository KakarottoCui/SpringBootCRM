package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.model.TreeModel;
import com.study.crm.service.ModuleService;
import com.study.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    @ResponseBody
    @RequestMapping("queryAllModules")
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    @RequestMapping("index")
    public String index(){
        return "module/module";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryModuleList(){
        return moduleService.queryAllModuleList();
    }

    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade, Integer parentId, HttpServletRequest request){
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }
    @ResponseBody
    @PostMapping("add")
    public ResultInfo addModule(Module module){
        moduleService.addModule(module);
        return success("添加资源模块成功");
    }

    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("更新资源模块成功");
    }
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteModule(Integer id){
        System.out.println(id);
        moduleService.deleteModule(id);
        return success("删除资源模块成功");
    }

}
