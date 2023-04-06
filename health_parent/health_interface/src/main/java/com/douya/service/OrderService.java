package com.douya.service;
/**
 * 体检预约服务接口
 */
import com.douya.entity.Result;

import java.util.Map;

public interface OrderService {
    //体检预约
    Result order(Map map) throws Exception;
}
