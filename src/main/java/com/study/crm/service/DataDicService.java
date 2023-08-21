package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.DataDicMapper;
import com.study.crm.query.DataDicQuery;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.DataDic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataDicService extends BaseService<DataDic,Integer> {
    @Resource
    private DataDicMapper dataDicMapper;


    //查询字典
    public Map<String,Object> queryDataDicByParams(DataDicQuery dataDicQuery){

        //把查询值放在map中
        Map<String,Object> map = new HashMap<String, Object>();

        //获取分页状态
        PageHelper.startPage(dataDicQuery.getPage(),dataDicQuery.getLimit());
        //使用了pageHelper查询结果并分页
        PageInfo<DataDic> pageInfo = new PageInfo<>( dataDicMapper.selectByParams(dataDicQuery));
        //将获取到的分页信息放入map中，然后返回
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        //返回结果集
        return map;
    }

    //添加
    public void saveDataDic(DataDic datadic){
        checkParams(datadic.getDataDicName(),datadic.getDataDicValue());
        //逻辑存在
        datadic.setIsValid(1);
        //写入创建和更新时间
        datadic.setCreateDate(new Date());
        datadic.setUpdateDate(new Date());
        //插入数据
        AssertUtil.isTrue(dataDicMapper.insertSelective(datadic) < 1,"数据添加失败!");
    }

    //检查插入参数
    private void checkParams(String DataDicName, String DataDicValue){
        AssertUtil.isTrue(StringUtils.isBlank(DataDicName),"数据类型不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(DataDicValue),"数据值不能为空!");
    }

    //更新记录
    public void updateDataDic(DataDic datadic){

        checkParams(datadic.getDataDicName(),datadic.getDataDicValue());

        //先查询将要更新的记录在不在，获取对象
        DataDic temp = dataDicMapper.selectByPrimaryKey(datadic.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在!");

        //写入当前记录的更新时间
        datadic.setUpdateDate(new Date());

        //查看更新结果
        AssertUtil.isTrue(dataDicMapper.updateByPrimaryKeySelective(datadic) < 1,"数据更新失败!");
    }


    //批量删除
    public void deleteDataDic(Integer[] ids){
        AssertUtil.isTrue(null == ids || ids.length == 0,"请选择待删除记录!");
        AssertUtil.isTrue(dataDicMapper.deleteBatch(ids) != ids.length,"记录删除失败!");
    }
}
