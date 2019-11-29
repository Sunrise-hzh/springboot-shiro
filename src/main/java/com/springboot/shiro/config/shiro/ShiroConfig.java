package com.springboot.shiro.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.security.Permission;
import java.util.*;

@Configuration
public class ShiroConfig {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     *      * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     *      * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *      * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //必须设置SecurityManager，安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //获取ShiroFilterFactoryBean的Filters
//        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        //将自定义的权限验证失败的过滤器ShiroFilterFactoryBean注入shiroFilter
//        filters.put("perms", new ShiroPermissionsFilter());



        //配置Shiro过滤器
        /**
         * 内置Shiro过滤器实现相关拦截功能
         *      常用的过滤器有：
         *          anon  : 无需认证（登录）可以访问
         *          authc : 必须认证才能访问
         *          user  : 如果使用rememberMe的功能可以直接访问
         *          perms : 该资源必须得到资源访问权限才可以使用
         *          role  : 该资源必须得到角色授权才可以使用
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        //配置退出过滤器，其中具体的退出代码，Shiro已经实现了。
//        filterMap.put("/logout","logout");
        //允许访问静态资源
        filterMap.put("/static/**","anon");
        filterMap.put("/testThymeleaf", "user");    //rememberMe可直接访问
        filterMap.put("/login", "anon");
        //从数据库获取所有的权限，然后配置到过滤器中
//        List<Permission> permissionList = permissionService.findAllPermission();
//        filterMap.put("/xxx","role[permission]");
        //最后配置/**
        filterMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        //设置登录页面路由，如果不设置则默认寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        //登录成功后要跳转的页面
//        shiroFilterFactoryBean.setSuccessUrl("/index");

        return shiroFilterFactoryBean;
    }

    /**
     * 创建SecurityManager
     * SecurityManager安全管理器是Shiro架构的核心，通过它来连接Realm和Subject（即用户）。
     */
    @Bean
    public SecurityManager securityManager() {
        //新建securityMananger
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        //配置modularRealmAuthenticator，配置多Realm验证管理器
        // *注意：该配置代码必须放在配置realm的前面。
//        securityManager.setAuthenticator(modularRealmAuthenticator());

        //配置多realm
//        Collection<Realm> realmCollection = new ArrayList<>();
//        realmCollection.add(userRealm());
//        realmCollection.add(roleRealm());
//        securityManager.setRealms(realmCollection);

        //配置单realm
        securityManager.setRealm(userRealm());

        //配置缓存
        securityManager.setCacheManager(ehCacheManager());  //注入缓存对象

        //配置rememberMe管理器
        securityManager.setRememberMeManager(cookieRememberMeManager());

        return securityManager;
    }

    /**
     * UserRealm
     */
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
//        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());    //设置加密规则
        return userRealm;
    }

    /**
     * RoleRealm
     */
    @Bean
    public RoleRealm roleRealm(){
        RoleRealm roleRealm = new RoleRealm();
        return roleRealm;
    }

    /**
     * 系统自带的Realm管理器，主要针对多realm
     */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
//        //自定义重写ModularRealmAuthenticator
//        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();

        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();

        //配置多Realm验证策略
        //  1.FirstSuccessfulStrategy：只要有一个Realm验证成功即可，只返回第一个Realm身份验证成功的认证信息，其他忽略；
        //  2.AtLeastOneSuccessfulStrategy：只要有一个Realm验证成功即可，和FirstSuccessfulStrategy不同，返回所有Realm身份验证成功的认证信息；
        //  3.AllSuccessfulStrategy：所有Realm验证成功才算成功，且返回所有Realm身份验证成功的认证信息，如果有一个失败则全失败。
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    // 因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。
    // 同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密的Hex编码；false时表示Base64编码
//        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 开启Shiro AOP注解支持，权限验证
     * Controller才能使用@RequiresPermissions
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 添加shiro ehcache 缓存
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:mybatis/ehcache-shiro.xml");
        return ehCacheManager;
    }

    /**
     * 配置Cookie对象
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //该构造器参数为cookie的名称，对应前端的checkbox的name=rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //rememberMe的生效时间，单位秒
//        simpleCookie.setMaxAge(259200);
        simpleCookie.setMaxAge(10);
        return simpleCookie;
    }

    /**
     * 配置Cookie管理器。
     * 过滤链需设置“user”权限
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //将配置的Cookie对象注入到Cookie管理器中
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

}
