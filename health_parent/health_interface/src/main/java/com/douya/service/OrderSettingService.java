package com.douya.service;


import com.douya.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //批量导入文件的数据
    void add(List<OrderSetting> data);

    List<Map> getOrderSettingByMonth(String date);  //参数格式为：2019-03

    void editNumberByDate(OrderSetting orderSetting);
}
