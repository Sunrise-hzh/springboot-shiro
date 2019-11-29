package com.springboot.shiro.config.shiro;

import com.springboot.shiro.domain.Role;
import com.springboot.shiro.domain.User;
import com.springboot.shiro.service.RoleService;
import com.springboot.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleRealm extends AuthorizingRealm {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行 RoleRealm 的权限验证");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行 RoleRealm 的登录验证");
        System.out.println(authenticationToken.getPrincipal().toString());
        User user = userService.getByUsername((String) authenticationToken.getPrincipal());
        Role role = roleService.findById(user.getRoleId());
        if(!role.getRolename().equals("teacher"))
            throw new AuthenticationException();

        return new SimpleAuthenticationInfo("", user.getPassword(), "");
    }
}
