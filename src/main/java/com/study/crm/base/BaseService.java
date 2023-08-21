package com.study.crm.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.dao.BaseMapper;
import com.study.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseService<T,ID> {
   // @Resource
    @Autowired
    private BaseMapper<T,ID> baseMapper;

    /***
     * 添加记录返回行数
     * @param entity
     * @return
     */
    public Integer insertSelective(T entity){
        return  baseMapper.insertSelective(entity);
    }

    /***
     * 添加记录返回主键
     * @param entity
     * @return
     */
    public Integer insertHasKey(T entity){
        return baseMapper.insertHasKey(entity);
    }

    /***
     * 批量添加
     * @param entities
     * @return
     */

    public Integer insertBatch(List<T> entities){
        return baseMapper.insertBatch(entities);
    }

    /***
     * 根据id 查询详情
     * @param id
     * @return
     */
    public T selectByPrimaryKey(ID id){
        return baseMapper.selectByPrimaryKey(id);
    }

    /***
     * 多条件查询
     * @param baseQuery
     * @return
     */
    public List<T> selectByParams(BaseQuery baseQuery){
        return baseMapper.selectByParams(baseQuery);
    }

    /**
     * 更新单挑条记录
     * @param entity
     * @return
     */
    public Integer updateByPrimaryKeySelective(T entity){
        return baseMapper.updateByPrimaryKeySelective(entity);
    }


    public Map<String,Object> queryByParamsForTable(BaseQuery baseQuery){
        Map<String ,Object> map=new HashMap<>();
        PageHelper.startPage(baseQuery.getPage(),baseQuery.getLimit());
        PageInfo<T> pageInfo=new PageInfo<T>(selectByParams(baseQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        return map;
    }

}
