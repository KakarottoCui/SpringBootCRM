package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseQuery;
import com.study.crm.base.BaseService;
import com.study.crm.dao.CustomerLossMapper;
import com.study.crm.dao.CustomerMapper;
import com.study.crm.dao.CustomerOrderMapper;
import com.study.crm.query.CustomerQuery;
import com.study.crm.utils.AssertUtil;
import com.study.crm.utils.PhoneUtil;
import com.study.crm.vo.Customer;
import com.study.crm.vo.CustomerLoss;
import com.study.crm.vo.CustomerOrder;
import com.study.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomerService extends BaseService<Customer,Integer> {
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CustomerOrderMapper customerOrderMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    /***
     * 查询顾客记录
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryByParamsForTable(CustomerQuery customerQuery){
        Map<String ,Object> map=new HashMap<>();
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        PageInfo<Customer> pageInfo=new PageInfo<Customer>(customerMapper.selectByParams(customerQuery));
        System.out.println(customerMapper.selectByParams(customerQuery).size());
        map.put("code",0);
        map.put("msg","success");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /***
     * 添加客户customer
     * 1.检验参数
     *     name 非空  且唯一
     *     legalPerson 非空
     *     手机号码 非空且格式正确
     *
     * 2.设置默认值
     *  is_valid
     *  createDate
     *  updateDate
     *  customerId 系统生成  KH+时间戳
        state 1  流失状态 1表示未流失 0表示已流失
     * 3.调用添加方法
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        checkParams(customer);
        Customer temp=customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(temp!=null,"客户名称已存在");
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        String khno= "KH"+System.currentTimeMillis();
        customer.setState(1);
        customer.setCustomerId(khno);
        AssertUtil.isTrue(customerMapper.insertSelective(customer)!=1,"客户信息添加失败");

    }

    private void checkParams(Customer customer) {
        AssertUtil.isTrue(StringUtils.isBlank(customer.getName()),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customer.getLegalPerson()),"法人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customer.getPhone()),"电话号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(customer.getPhone()),"电话号码格式不正确");
    }

    /***
     * 更新用户
     * 1.检查用户是否存在
     * 2.检查参数
     * 3.设置默认值
     * 4.调用mapper方法
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer) {
        AssertUtil.isTrue(null==customer.getId(),"待更新记录不存在");
        Customer temp=customerMapper.selectByPrimaryKey(customer.getId());
        AssertUtil.isTrue(null==temp,"待更新记录不存在！");
        checkParams(customer);
        customer.setUpdateDate(new Date());
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer)!=1,"客户信息更新失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer id){
        AssertUtil.isTrue(id==null,"待删除记录不存在");
        Customer customer=customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==customer,"待删除记录不存在");
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer)!=1,"删除用户失败");
    }

    /***
     * 更新用户流失状态
     * state为1表示未流失
     *    0表示已流失
     * 1. 查询待流失的客户数据
     * 2. 将流失客户批量添加到t_customer_loss表中
     * 3.更新state
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public  void updateCustomerState(){
        List<Customer> lossCustomerList=customerMapper.queryLossCustomer();
        if(lossCustomerList!=null && lossCustomerList.size()>0){
            List<CustomerLoss> customerLossList=new ArrayList<>();
            List<Integer> lossCustomerId=new ArrayList<>();
            for (Customer customer : lossCustomerList) {
               CustomerLoss customerLoss=new CustomerLoss();
               customerLoss.setCreateDate(new Date());
               customerLoss.setCusManager(customer.getCusManager());
               customerLoss.setCusName(customer.getName());
               customerLoss.setCusNo(customer.getAccountNumber());
               customerLoss.setIsValid(1);
               customerLoss.setUpdateDate(new Date());
               customerLoss.setState(0);
               //设置客户最后下单时间
                CustomerOrder customerOrder=customerOrderMapper.queryLossCustomerOrderByCustomerId(customer.getId());
                if(customerOrder!=null){
                    customerLoss.setConfirmLossTime(customerOrder.getOrderDate());
                }
                customerLossList.add(customerLoss);
                lossCustomerId.add(customer.getId());
            }
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLossList)!=customerLossList.size(),"添加流失客户失败");
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(lossCustomerId)!=lossCustomerId.size(),"客户流失状态更新失败");

        }
    }

    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        Map<String,Object> map=new HashMap<String,Object>();
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        PageInfo<Map<String,Object>> pageInfo=new PageInfo<Map<String,Object>>(customerMapper.queryCustomerContributionByParams(customerQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }


    //获取数据显示在echarts折线图上
    public Map<String,Object> countCustomerMake(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Map<String,Object>> list= customerMapper.countCustomerMake();
        List<String> data1=new ArrayList<String>();
        List<Integer> data2=new ArrayList<Integer>();
        /**
         * result
         *    data1:["大客户","合作伙伴"]
         *    data2:[10,20]
         */
        list.forEach(m->{
            data1.add(m.get("level").toString());
            data2.add(Integer.parseInt(m.get("total").toString()));
        });
        result.put("data1",data1);
        result.put("data2",data2);
        return result;
    }


    //获取数据显示在echarts玫瑰图上
    public Map<String,Object> countCustomerMake02(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Map<String,Object>> list= customerMapper.countCustomerMake();
        List<String> data1=new ArrayList<String>();
        List<Map<String,Object>> data2=new ArrayList<Map<String,Object>>();
        list.forEach(m->{
            data1.add(m.get("level").toString());
            Map<String,Object> temp=new HashMap<String,Object>();
            temp.put("name",m.get("level"));
            temp.put("value",m.get("total"));
            data2.add(temp);
        });
        result.put("data1",data1);
        result.put("data2",data2);
        return result;
    }

    //获取数据显示在echarts折线图上
    public Map<String,Object> countCustomerServe(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Map<String,Object>> list = customerMapper.countCustomerServe();
        List<String> data1 = new ArrayList<String>();
        List<Integer> data2 = new ArrayList<Integer>();
        /**
         * result
         *    data1:["建议","投诉"]
         *    data2:[1,2]
         */
        list.forEach(m->{
            data1.add(m.get("data_dic_value").toString());
            data2.add(Integer.parseInt(m.get("total").toString()));
        });
        result.put("data1",data1);
        result.put("data2",data2);
        return result;
    }


    //获取数据显示在echarts玫瑰图上
    public Map<String,Object> countCustomerServe02(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Map<String,Object>> list = customerMapper.countCustomerServe();
        List<String> data1 = new ArrayList<String>();
        List<Map<String,Object>> data2 = new ArrayList<Map<String,Object>>();
        list.forEach(m->{
            data1.add(m.get("data_dic_value").toString());
            Map<String,Object> temp=new HashMap<String,Object>();
            temp.put("name",m.get("data_dic_value"));
            temp.put("value",m.get("total"));
            data2.add(temp);
        });
        result.put("data1",data1);
        result.put("data2",data2);
        return result;
    }
}
