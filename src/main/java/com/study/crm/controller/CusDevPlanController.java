//微信：egvh56ufy7hh ，QQ：821898835
import com.study.crm.enums.StateStatus;
import com.study.crm.query.CusDevPlanQuery;
import com.study.crm.query.SaleChanceQuery;
import com.study.crm.service.CusDevPlanService;
import com.study.crm.service.SaleChanceService;
import com.study.crm.utils.LoginUserUtil;
import com.study.crm.vo.CusDevPlan;
import com.study.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;

    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        System.out.println("customer"+id);
        SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
     //   System.out.println("customer");
       System.out.println(saleChance.getCustomerName()+"customerName");
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){

        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    @ResponseBody
    @PostMapping("add")
    private ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("添加计划项成功");
    }

    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId,HttpServletRequest request,Integer id){
        request.setAttribute("sId",sId);
        CusDevPlan cusDevPlan=cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan",cusDevPlan);

    @ResponseBody
    @PostMapping("delete")
    private ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("删除计划项成功");
    }



}
