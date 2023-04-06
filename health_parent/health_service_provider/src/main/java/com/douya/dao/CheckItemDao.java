package com.douya.dao;

import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    /**这里是根据查询条件查询，使用的是分页助手 返回值类型固定为Page<>类
     * 虽然只有一个方法，mybatis底层也是这个方法，
     * 但是利用分页助手它会帮我们查询到rows+total*/
   Page<CheckItem> selectByCondition(String queryString);


    /** 删除操作： 因为检查项和检查组是关联的 ，是多对多的关系，所以
     *  1.先判断检查项 是否已经和检查组关联了 ，若关联则不可以删除检查项的数据
     *  2.若检查组中每页检查项的数据， 则可以执行删除操作*/
    long findCountByCheckItemId(Integer id);  //1.查询检查组中是否已经含有检查项的数据 判断操作
    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
