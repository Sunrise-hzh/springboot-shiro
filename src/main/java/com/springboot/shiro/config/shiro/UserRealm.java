package com.springboot.shiro.config.shiro;

import com.springboot.shiro.domain.Permission;
import com.springboot.shiro.domain.Role;
import com.springboot.shiro.domain.User;
import com.springboot.shiro.service.PermissionService;
import com.springboot.shiro.service.RoleService;
import com.springboot.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;

    /**
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("【权限验证.UserRealm】开始......");

        //1.获取用户信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        //2.根据用户信息获取其角色列表
        Role role = roleService.findById(user.getRoleId());
        simpleAuthorizationInfo.addRole(role.getRolename());
        //3.获取其权限列表
        for (Permission permission:permissionService.findByRoleId(role.getId())){
            simpleAuthorizationInfo.addStringPermission(role.getRolename()+":"+permission.getPermissionname());
        }

        return simpleAuthorizationInfo;
    }

    /**
     * 执行认证逻辑
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("【登录验证.UserRealm】开始......");

        //1、从数据库获取 用户名称和密码
        User user = userService.getByUsername((String) authenticationToken.getPrincipal());
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;

        //2、判断用户名称是否存在
        if (!user.getUsername().equals(usernamePasswordToken.getUsername())) {
            //用户名称不存在，Shiro底层会抛出UnknowAccountException
            return null;
        }

        //3、判断密码是否正确
        //交给AuthenticatingRealm使用CredentialsMather进行密码匹配。（可自定义实现）
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(),
                user.getPassword(),
                getName()       //ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时,不需要此句
        );

        //4.Session中不需要密码
        user.setPassword(null);
        //5.将user信息放进session中
        SecurityUtils.getSubject().getSession().setAttribute("userinfo",user);
        return authenticationInfo;
    }
}
