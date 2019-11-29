package com.springboot.shiro.service;

import com.springboot.shiro.domain.User;

public interface UserService {
    User getByUsername(String username);

}
