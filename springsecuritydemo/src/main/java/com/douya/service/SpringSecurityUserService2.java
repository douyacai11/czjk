package com.douya.service;

import com.douya.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService2 implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    //模拟数据库的用户数据
     public static Map<String, User> map=new HashMap<>();

     public void initData(){
         User user1 = new User();
         user1.setUsername("admin");
         user1.setPassword(passwordEncoder.encode("admin"));  //对密码进行加密

         User user2 = new User();
         user2.setUsername("xiaoming");
         user2.setPassword(passwordEncoder.encode("1234"));

         map.put(user1.getUsername(),user1);
         map.put(user2.getUsername(),user2);

     }



    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initData();
        System.out.println("用户输入的用户名为:"+username);

        //根据用户名查询数据库获取的用户信息(包含数据库存储的密码信息
        User user = map.get(username);
        if (user == null){
            return null;
        }else {
            //将用户信息返回给框架, 框架会进行密码比对

            List<GrantedAuthority> list=new ArrayList<>();
            //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色
            list.add(new SimpleGrantedAuthority("permission_A"));
            list.add(new SimpleGrantedAuthority("permission_B"));

            if (username.equals("admin")) {
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                list.add(new SimpleGrantedAuthority("add"));
            }

            org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);

            return securityUser;

        }
    }
}
