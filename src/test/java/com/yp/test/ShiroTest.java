package com.yp.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class ShiroTest {

    /**
     * jdbcRealmTest
     */
    @Test
    public void shiroTests(){
        //创建Realm
       JdbcRealm realm = new JdbcRealm();
        //创建数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///shiro?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        realm.setDataSource(dataSource);
        //自定义sql
        realm.setAuthenticationQuery("select password from users where username = ?");
        //手动开启权限校验
        realm.setPermissionsLookupEnabled(true);
        //创建安全管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm);
        //创建subject
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //认证
        subject.login(new UsernamePasswordToken("admin","admin"));
        //校验角色
        subject.checkRoles("超级管理员","人事");
        //校验权限
        subject.checkPermissions("product:insert");
    }
}
