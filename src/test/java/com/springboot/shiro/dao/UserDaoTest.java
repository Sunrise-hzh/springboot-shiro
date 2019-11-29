package com.springboot.shiro.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testDeleteAll(){
        List<String> usernameList = new ArrayList<>();
        usernameList.add("admin");
        usernameList.add("admin2");
        usernameList.add("admin3");
        userDao.deleteAll(usernameList);
    }
}
