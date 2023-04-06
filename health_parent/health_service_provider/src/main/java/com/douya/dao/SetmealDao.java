package com.douya.dao;

import com.douya.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    //新增套装的 两个方法
    void add(Setmeal setmeal);
    void setSetmealAndCheckGroup(Map<String, Integer> map);  //设置套餐和检查组的多对多关联的方法


    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
