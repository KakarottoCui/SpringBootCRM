package com.study.crm.service;

import com.study.crm.base.BaseService;
import com.study.crm.dao.PermissionMapper;
import com.study.crm.vo.Permission;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;
}
