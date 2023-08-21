package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.SaleChanceMapper;
import com.study.crm.enums.DevResult;
import com.study.crm.enums.StateStatus;
import com.study.crm.query.SaleChanceQuery;
import com.study.crm.utils.AssertUtil;
import com.study.crm.utils.PhoneUtil;
import com.study.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer>  {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /***
     * 获取营销机会信息查询
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String ,Object> map=new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo=new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /**8
     * 1. 参数校验
     *    顾客名、联系人、联系电话不能为空
     * 2.设置相关参数的默认值：
     *      createMan：当前登录用户的名字
     *      assignMan :如果没有设置：默认state为0指派时间为null devResult开发状态：0
     *                有设置：state为1，指派时间为当前系统的时间  devResult为1
     *      isValid为1有效
     * 3. 执行添加操作，判断受影响行数
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public  void addSaleChance(SaleChance saleChance){
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setAssignTime(null);
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else{
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"添加营销机会失败");
    }

    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系号码不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系号码格式不正确");
    }

    /***
     * 更新营销机会
     * 1. 参数校验
     *    某些值不能为空
     * 2. 设置默认值
     *     与添加操作一致
     * 3. 执行更新方法（调用dao层）
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public  void updateSaleChance(SaleChance saleChance){
        AssertUtil.isTrue(null==saleChance.getId(),"待更新记录不存在");
        SaleChance temp=saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null==temp,"待更新记录不存在！");
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        saleChance.setUpdateDate(new Date());
        if(StringUtils.isBlank(temp.getAssignMan())){
          if(!StringUtils.isBlank(saleChance.getAssignMan())) {
              //修改前不存在，修改后有值
              saleChance.setAssignTime(new Date());
              saleChance.setState(StateStatus.STATED.getType());
              saleChance.setDevResult(DevResult.DEVING.getStatus());
          }
        }else{
            //修改前有值，修改后无值
            if(StringUtils.isBlank(saleChance.getAssignMan())){
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setAssignTime(null);
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else{
                //修改前后都有值
                //判断修改前后是否是同一个指派人
                if(!saleChance.getAssignMan().equals(temp.getAssignMan())){
                    saleChance.setAssignTime(new Date());
                }
            }
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"更新操作失败");
    }

    /***
     * 删除
     * 传的参数是id的数组
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        AssertUtil.isTrue(null==ids || ids.length<1,"待删除记录不存在");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!=ids.length,"营销机会数据删除失败");
    }

    /***
     * 更新saleChance表中的devResult
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id,Integer devResult){
        AssertUtil.isTrue(null==id,"待更新记录不存在");
        SaleChance saleChance=saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==saleChance,"待更新记录不存在");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"开发状态更新失败");
    }
}
