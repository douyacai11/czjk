package com.douya.service;

import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.entity.Result;
import com.douya.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
