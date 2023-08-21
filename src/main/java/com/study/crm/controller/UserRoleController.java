package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("")
public class UserRoleController extends BaseController {
    @Resource
    private UserRoleService userRoleService;
}
