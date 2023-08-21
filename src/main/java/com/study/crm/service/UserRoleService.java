package com.study.crm.service;

import com.study.crm.base.BaseService;
import com.study.crm.dao.UserRoleMapper;
import com.study.crm.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Resource
    private UserRoleMapper userRoleMapper;
}
