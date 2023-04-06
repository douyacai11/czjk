package com.douya.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.douya.dao.OrderSettingDao;
import com.douya.pojo.OrderSetting;
import com.douya.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass =OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置数据
    public void add(List<OrderSetting> list) {
       if (list!=null &&list.size()>0){
           for (OrderSetting orderSetting : list) {
               //判断日期是否已经进行了预约设置
               long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
               if (countByOrderDate>0){
                   //已经进行了预约设置，执行更新操作
                   orderSettingDao.editNumberByOrderDate(orderSetting);
               }else{
                   //没有进行预约设置 ，执行插入操作
                   orderSettingDao.add(orderSetting);
               }
           }
       }
    }

    //根据月份查询对应的预约设置信息
    public List<Map> getOrderSettingByMonth(String date) {  // date 格式:yyyy-MM
        String begin=date +"-1";   //2019-6-1
        String end=date +"-31";    //2019-6-31
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("begin",begin);
        map.put("end",end);

        /**根据dao调用方法，查询预约信息的数据
         *  但是返回的数据不和页面对应 所以这里需要转换类型
         * */
        List<OrderSetting> list= orderSettingDao.getOrderSettingByMonth(map);

        //把OrderSetting 类型的list 转换成 Map类型
        List<Map> result = new ArrayList<Map>();
        if (list!=null &&list.size()>0){
            for (OrderSetting orderSetting : list) {

                Map<String, Object> m = new HashMap<String, Object>();
                m.put("date",orderSetting.getOrderDate().getDate());  //获取日期数字  几号 7号
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    //根据月份 修改 对应的预约设置信息
    /** 根据日期查询 是否已经有了预约设置,
     * 1.如果已经有了预约设置 ，需要执行更新操作
     * 2.如果还没有进行预约设置  需要执行插入操作*/
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());  //根据日期查询 是否已经有了预约设置,
        if (count>0){
            //有了预约设置 ，需要执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //还没有进行预约设置  需要执行插入操作
            orderSettingDao.add(orderSetting);
        }
    }
}
