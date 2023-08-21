package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.ModuleMapper;
import com.study.crm.dao.PermissionMapper;
import com.study.crm.model.TreeModel;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.Module;
import com.study.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    /***
     * 角色管理中的资源授权
     * 主要是只需要返回treeModel中的三个数据
     * @param roleId
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId){
        List<TreeModel> treeModelList=moduleMapper.queryAllModules();
        List<Integer> permissionIds=permissionMapper.queryRoleHasModuleIdByRoleId(roleId);
        if(permissionIds!=null&&permissionIds.size()>0){
            for (TreeModel treeModel : treeModelList) {
                if(permissionIds.contains(treeModel.getId())){
                    treeModel.setChecked(true);
                }
            }
        }
        return moduleMapper.queryAllModules();
    }

    /**
     * 查询所有的资源列表
     * 并用map集合返回
     * @return
     */
    public Map<String,Object> queryAllModuleList(){
        Map<String ,Object> map=new HashMap<>();
       List<Module> moduleList=moduleMapper.queryAllModuleList();
        map.put("code",0);
        map.put("msg","success");
        map.put("data",moduleList);
        map.put("count",moduleList.size());
        return map;
    }

    /***
     * 添加资源
     * 传一个module
     * 思路：
     *    1.判断参数是否为空
     *        module_name   非空 同一层级下name唯一
     *        url
     *        parentId：
     *           一级菜单：grade=0，parentId为null
     *           二级：parentId一定存在
     *        grade 0|1|2
     *        optValue:非空不可重复
     *    2.设置参数的默认值
     *       createDate
     *       updateDate
     *       is_valid
     *    3.执行添加操作
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addModule(Module module){
        checkParams(module);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        module.setIsValid(1);
        AssertUtil.isTrue(moduleMapper.insertSelective(module)!=1,"添加资源失败");
    }

    private void checkParams(Module module) {
        Integer grade=module.getGrade();
        AssertUtil.isTrue(grade==null||!(grade==0 ||grade==1 ||grade==2),"菜单层级不合法");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName()),"该层级下模块名称已存在");
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url不能为空");
            AssertUtil.isTrue(null!=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl()),"url不可重复");
        }
        if(grade==0){
            module.setParentId(-1);
        }else{
            AssertUtil.isTrue(null==module.getParentId(),"父集菜单不能为空");
            AssertUtil.isTrue(null==moduleMapper.selectByPrimaryKey(module.getParentId()),"请指定父集菜单");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByOptValue(module.getOptValue()),"权限码已存在！");

    }

    /**
     * 修改更新资源
     * 1.参数校验
     *    id 非空且存在记录
     *    grade：
     *     非空 0|1|2
     *    moduleName:
     *      非空 同一个层级下name不能相同
     *     url：
     *       grade为1： 非空且不可重复
     *     optValue：
     *       非空，不可重复
     * 2.设置默认值
     *   updateDate
     * 3.调用更新方法
     * @param module
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        AssertUtil.isTrue(null==module.getId(),"待更新记录不存在");
        Module temp=moduleMapper.selectByPrimaryKey(module.getId());
        AssertUtil.isTrue(null==temp,"待更新记录不存在");
        Integer grade=module.getGrade();
        AssertUtil.isTrue(grade==null||!(grade==0 ||grade==1 ||grade==2),"菜单层级不合法");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"模块名称不能为空");
        temp=moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        if(temp!=null){
            AssertUtil.isTrue(!(temp.getModuleName().equals(module.getModuleName())),"该层级下模块名称已存在");
        }
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"url不能为空");
            temp=moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            if(temp!=null){
                AssertUtil.isTrue(!(temp.getUrl().equals(module.getUrl())),"url不可重复");
            }

        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"权限码不能为空");
        temp=moduleMapper.queryModuleByOptValue(module.getOptValue());
        if(temp!=null){
            AssertUtil.isTrue(!(temp.getOptValue().equals(module.getOptValue())),"权限码已存在！");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module)<1,"更新资源模块失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModule(Integer id){
        AssertUtil.isTrue(null==id,"待删除记录不存在");
        Module temp=moduleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==temp,"待删除记录不存在");
        //判断是否是父id
        Integer count=moduleMapper.queryModuleByParentId(id);
        AssertUtil.isTrue(count>0 ,"该资源存在子记录不能删除");
        count=permissionMapper.countPermissionByModuleId(id);
        if(count>0){
            permissionMapper.deletePermissionByModuleId(id);
        }
        temp.setIsValid(0);
        temp.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp)<1,"删除资源失败");

    }
}
