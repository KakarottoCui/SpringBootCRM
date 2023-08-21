package com.study.crm.dao;

import com.study.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
   //查询所有角色列表(只需要id和roleName)
    public List<Map<String,Object>> queryAllRoles(Integer userId);
}