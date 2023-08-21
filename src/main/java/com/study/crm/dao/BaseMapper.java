package com.study.crm.dao;


import com.study.crm.base.BaseQuery;

import java.util.List;

public interface BaseMapper<T,ID> {
    /***
     * 添加记录返回行数
     * @param entity
     * @return
     */
    public Integer insertSelective(T entity);

    /***
     * 添加记录返回主键
     * @param entity
     * @return
     */
    public Integer insertHasKey(T entity);

    /***
     * 批量添加
     * @param entities
     * @return
     */

    public Integer insertBatch(List<T> entities);

    /***
     * 根据id 查询详情
     * @param id
     * @return
     */
    public T selectByPrimaryKey(ID id);

    /***
     * 多条件查询
     * @param baseQuery
     * @return
     */
    public List<T> selectByParams(BaseQuery baseQuery);

    /**
     * 更新单挑条记录
     * @param entity
     * @return
     */
    public Integer updateByPrimaryKeySelective(T entity);


    public Integer deleteBatch(ID[] ids);

}
