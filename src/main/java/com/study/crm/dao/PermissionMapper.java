package com.study.crm.dao;

import com.study.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper  extends BaseMapper<Permission,Integer>{


    Integer countPermissionByRoleId(Integer roleId);

    void deletePermissionByRoleId(Integer roleId);

    List<Integer> queryRoleHasModuleIdByRoleId(Integer roleId);

    Integer countPermissionByModuleId(Integer id);

    void deletePermissionByModuleId(Integer id);
}