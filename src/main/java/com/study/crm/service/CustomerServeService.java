package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.CustomerMapper;
import com.study.crm.dao.CustomerServeMapper;
import com.study.crm.dao.UserMapper;
import com.study.crm.enums.CustomerServeStatus;
import com.study.crm.query.CustomerServeQuery;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.CusDevPlan;
import com.study.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServeService extends BaseService<CustomerServe,Integer> {
    @Resource
    private CustomerServeMapper customerServeMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private UserMapper userMapper;


    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        Map<String ,Object> map=new HashMap<>();
        PageHelper.startPage(customerServeQuery.getPage(),customerServeQuery.getLimit());
        PageInfo<CustomerServe> pageInfo=new PageInfo<CustomerServe>(customerServeMapper.selectByParams(customerServeQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /***
     * 增加服务创建
     * 1. 参数校验
     *    customer非空且存在
     * 2. 设置默认值
     * 3. 调用mapper层方法
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerServe(CustomerServe customerServe){
        AssertUtil.isTrue(customerServe.getCustomer()==null,"客户姓名不能为空");
        AssertUtil.isTrue(null==customerMapper.queryCustomerByName(customerServe.getCustomer()),"客户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()),"请指定服务类型!");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()),"请指定服务请求内容!");
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());
        customerServe.setState(CustomerServeStatus.CREATED.getState());
        customerServe.setIsValid(1);
        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe)!=1,"添加服务创建失败");
    }

    /**
     * 1.参数校验
     *     id 记录必须存在
     * 2.如果状态为分配状态 fw_002
     *    分配人必须存在
     *    设置服务更新时间
     *    设置分配时间
     * 3.如果状态为 服务处理  fw_003
     *     服务处理内容非空
     *     设置服务处理时间
     *     服务更新时间
     * 4.如果服务状态为反馈状态 fw_004
     *     反馈内容非空
     *     满意度非空
     *     更新时间
     *     设置服务状态为归档状态
     * 5.执行更新操作 判断结果
     */
    //更新服务进程
    public void updateCustomerServe(CustomerServe customerServe){

        AssertUtil.isTrue(null == customerServeMapper.selectByPrimaryKey(customerServe.getId()),"待更新的服务记录不存在!");
        if(customerServe.getState().equals(CustomerServeStatus.ASSIGNED.getState())){
            // 执行分配
            AssertUtil.isTrue(null == userMapper.selectByPrimaryKey(Integer.parseInt(customerServe.getAssigner())),"待分配的用户不存在!");
            customerServe.setAssignTime(new Date());
        }else if(customerServe.getState().equals(CustomerServeStatus.PROCED.getState())){
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()),"请指定处理内容!");
            customerServe.setServiceProceTime(new Date());
        }else if(customerServe.getState().equals(CustomerServeStatus.FEED_BACK.getState())){
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()),"请指定反馈内容!");
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getSatisfaction()),"请指定满意度!");
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
        }
        customerServe.setUpdateDate(new Date());
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe)<1,"服务记录更新失败!");
    }

    public CustomerServe getById(Integer id) {
        return customerServeMapper.selectByPrimaryKey(id);
    }
}
