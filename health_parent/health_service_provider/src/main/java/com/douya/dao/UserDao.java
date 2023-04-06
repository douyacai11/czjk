package com.douya.dao;

import com.douya.pojo.User;

public interface UserDao {

    User findByUsername(String username);
}
