package com.springboot.shiro.service.impl;

import com.springboot.shiro.dao.UserDao;
import com.springboot.shiro.domain.User;
import com.springboot.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getByUsername(String username) {
        return userDao.selectByUsername(username);
    }
}
