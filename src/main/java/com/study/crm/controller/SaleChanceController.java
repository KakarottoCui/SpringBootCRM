package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.enums.StateStatus;
import com.study.crm.query.SaleChanceQuery;
import com.study.crm.service.SaleChanceService;
import com.study.crm.utils.CookieUtil;
import com.study.crm.utils.LoginUserUtil;
import com.study.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
   @Autowired
    private SaleChanceService saleChanceService;

    /***
     * 营销机会数据查询（分页多条件查询）
     * 如果flag的值不为空为1则表示当前查询的是客户机会
     * @param saleChanceQuery 前端传的query参数
     * @param flag  用于判断查询的是客户机会还是营销机会
     * @param request 用于获取cookie中的userId
     * @return
     */
   @RequestMapping("list")
   @ResponseBody
   public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest request){
       if(flag!=null && flag==1){
           saleChanceQuery.setState(StateStatus.STATED.getType());
           Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
           saleChanceQuery.setAssignMan(userId);
       }
       return saleChanceService.querySaleChanceByParams(saleChanceQuery);
   }

    /**8
     * 返回营销机会信息的页面
     * @return
     */
   @RequestMapping("index")
   public String index(){
       return "saleChance/sale_chance";
   }

    /***
     * 添加营销机会信息
     * 就是前端按“确认”之后执行的操作
     * @param saleChance  前端传过来的saleChance实体类
     *                    往数据库中插入一条saleChance类数据
     * @param request
     * @return
     */
   @ResponseBody
   @PostMapping("add")
   public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
       String userName= CookieUtil.getCookieValue(request,"userName");
       saleChance.setCreateMan(userName);
       saleChanceService.addSaleChance(saleChance);
       return success("营销机会数据添加成功！");
   }

    /***
     * 去更新或添加记录的页面
     * 通过判断id是否为空来判断是更新还是添加
     * @param id
     * @param request
     * @return
     */
   @RequestMapping("addOrUpdateSaleChancePage")
   public String toSaleChancePage(Integer id,HttpServletRequest request){
       System.out.println(id+"saleChance");
       if(id!=null){
           SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
           request.setAttribute("saleChance",saleChance);
       }
       return "saleChance/add_update";
   }

    /***
     * 更新操作按更新的“确认”后执行的操作
     * @param saleChance
     * @return
     */
    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    /**
     * 删除操作
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 更新开发状态
     * @param id
     * @param devResult
     * @return
     */
    @ResponseBody
    @PostMapping("updateSaleChanceDevResult")
    private ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }

}
