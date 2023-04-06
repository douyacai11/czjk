package com.douya.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.douya.constant.MessageConstant;
import com.douya.constant.RedisMessageConstant;
import com.douya.entity.Result;
import com.douya.pojo.Order;
import com.douya.service.OrderService;
import com.douya.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**体检预约处理
 * */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;


    /**提交预约的跳转:
     * 从redis中获取保存的验证码 将用户输入的验证码和redis中保存的验证码比对。
     * 若成功测 调用服务完成预约处理，否则返回错误结果给页面提示*/
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //获取前端中输入的手机号
        String telephone = (String) map.get("telephone");
        //获取redis中保存的验证码
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取前端中输入的验证码
        String validateCode = (String) map.get("validateCode");

        if (validateCodeRedis!=null && validateCode!=null && validateCodeRedis.equals(validateCode)) {
            Result result = null;
            map.put("orderType", Order.ORDERTYPE_WEIXIN);   //设置预约类型 (分为微信预约 /电话预约
            try {
                result = orderService.order(map);

            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }

            if (result.isFlag()) {
                //预约成功，发送短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("orderDate"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        return null;
    }
}
