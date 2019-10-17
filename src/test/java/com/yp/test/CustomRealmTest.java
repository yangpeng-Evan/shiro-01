package com.yp.test;

import com.yp.entity.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {

    @Test
    public void custom(){

        //创建customRealm
        CustomRealm customRealm = new CustomRealm();
        //创建安全管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        //创建subject
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //认证
        subject.login(new UsernamePasswordToken("admin","admin"));
        //角色校验
        subject.checkRoles("超级管理员");
        //权限校验
        subject.checkPermissions("product:update");
    }
}
