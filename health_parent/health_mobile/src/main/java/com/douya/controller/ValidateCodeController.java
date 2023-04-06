package com.douya.controller;

import com.aliyuncs.exceptions.ClientException;
import com.douya.constant.MessageConstant;
import com.douya.constant.RedisConstant;
import com.douya.constant.RedisMessageConstant;
import com.douya.entity.Result;
import com.douya.utils.SMSUtils;
import com.douya.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**验证码操作
 * */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //用户体检预约的发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成4为数字的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //发送给用户验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存刀redis(5分钟
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //手机登录的验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //生成4为数字的验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //发送给用户验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存刀redis(5分钟
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }



}
