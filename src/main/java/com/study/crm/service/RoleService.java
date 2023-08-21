package com.study.crm.service;

import com.study.crm.base.BaseService;
import com.study.crm.dao.ModuleMapper;
import com.study.crm.dao.PermissionMapper;
import com.study.crm.dao.RoleMapper;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.Permission;
import com.study.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    /***
     * 查询所有的角色名返回list
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /***
     * 增加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role)!=1,"角色添加失败");
    }

    /***
     * 更新角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(role.getId()==null,"待更新记录不存在");
        Role temp=roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp==null,"待更新记录不存在");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)!=1,"更新角色失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        AssertUtil.isTrue(null==roleId ,"待删除记录不存在");
        Role temp=roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(temp==null,"待删除记录不存在");
        temp.setUpdateDate(new Date());
        temp.setIsValid(0);
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(temp)!=1,"角色数据删除失败");
    }

    /***
     * 添加角色授权的资源
     * @param roleId  角色id
     * @param mIds  资源id
     *             主要是更新t_permission表
     *             将对应的角色ID和资源Id添加到permission表中
     *             先将之前的权限记录删除，然后再进行添加
     *                 1.通过角色ID查询权限记录返回权限记录数
     *                 2.删除这些权限
     *                3.添加后面的权限
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        Integer count=permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        if(mIds!=null && mIds.length>0){
            List<Permission> permissionList=new ArrayList<>();
            for (Integer mId : mIds) {
                Permission permission=new Permission();
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissionList)!=permissionList.size(),"角色授权失败");
        }

    }
}
