package com.douya.service;

import com.douya.pojo.User;

public interface UserService {

    public User findByUsername(String username);

}
