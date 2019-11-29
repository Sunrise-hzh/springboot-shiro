package com.springboot.shiro.controller;

import com.springboot.shiro.domain.User;
import com.springboot.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
//@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/hello")
    @ResponseBody
    public String hello() {
        return "ok";
    }
    @RequestMapping(value = "/testThymeleaf")
    public String testThymeleaf(Model model) {
        //把数据存入model
        model.addAttribute("test", "测试Thymeleaf");
        //返回test.html
        return "test";
    }

    @RequestMapping(value = "/add")
    @RequiresPermissions("teacher:add")
    public String add(Model model) {
        //把数据存入model
        model.addAttribute("test", "添加用户页面");

        //返回test.html
        return "/user/add";
    }

    @RequestMapping(value = "/update")
    @RequiresPermissions("teacher:update")
    public String update(Model model) {
        //把数据存入model
        model.addAttribute("test", "修改用户页面");
        //返回test.html
        return "/user/update";
    }

    @RequestMapping(value = "/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login(String username, String password,boolean rememberMe, Model model) {
        System.out.println("【用户名】 " + username);
        System.out.println("【密码】 "+password);
        //1、获取 Subject
        Subject subject = SecurityUtils.getSubject();
        //2、封装用户数据
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        //3、执行登录方法
        try {
            //设置rememberMe
            usernamePasswordToken.setRememberMe(rememberMe);

            subject.login(usernamePasswordToken);
            return "redirect:/testThymeleaf";

        } catch (UnknownAccountException uae) {                   //用户名不存在
            model.addAttribute("msg", "用户名称不存在");
            return "login";

        } catch (IncorrectCredentialsException ice) {             //密码错误
            model.addAttribute("msg", "密码错误");
            return "login";

        } catch (LockedAccountException lae) {                   //用户被锁定
            model.addAttribute("msg", "账户已被锁住");
            return "login";

        } catch (AuthenticationException e){                    //测试异常
            model.addAttribute("msg","角色不匹配");
            return "login";
        } catch (Exception e){
            model.addAttribute("msg","输入异常");
            return "login";
        }
    }

}
