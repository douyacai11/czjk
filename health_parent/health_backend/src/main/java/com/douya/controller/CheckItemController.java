package com.douya.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.douya.constant.MessageConstant;
import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.entity.Result;
import com.douya.pojo.CheckItem;
import com.douya.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 检查项管理
 *  */

@RestController  //包含了Controller ,直接返回对象  把返回的java对象转化为json对象响应到页面中
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference   //查找CheckItemService服务
    private CheckItemService checkItemService;

    //新增检查项
    @RequestMapping("/add")        //RequestBody 如果前端传过来的是一个json对象 则需要加
    public Result add(@RequestBody CheckItem checkItem){
       try {
           checkItemService.add(checkItem);
       }catch (Exception e){
           //服务器调用失败
           return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
       }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项的分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return  pageResult;
    }



    //删除检查项
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkItemService.deleteById(id);
        }catch (Exception e){
            //服务器调用失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    //编辑操作的数据回显 ,需要使用Result的第二个构造方法，传入返回的checkItem给data
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
          CheckItem checkItem=checkItemService.findById(id);
          return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){
            //服务器调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

    //编辑检查项
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){   ////RequestBody 如果前端传过来的是一个json对象 则需要加
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            //服务器调用失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }


    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<CheckItem> list = checkItemService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            //服务器调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
