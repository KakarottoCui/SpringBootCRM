package com.study.crm.controller;

import com.study.crm.base.BaseController;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("report")
public class CustomerReportController extends BaseController {

    /***
     * 页面相关
     * @param type
     * @return
     */
    @RequestMapping("{type}")
    public String index(@PathVariable Integer type){
        if(type!=null){
            if(type==0){
                //贡献
                return "report/customer_contri";
            }else if(type==1){
                return "report/customer_make";
            } else if(type==2){
                return "report/customer_serve";
            }else if(type==3){
                return "report/customer_loss";
            }
        }
        return " ";
    }
}
