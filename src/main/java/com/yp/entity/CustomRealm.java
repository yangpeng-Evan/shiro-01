package com.yp.entity;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        this.setCredentialsMatcher(credentialsMatcher);
    }

    private User findByUsername(String name){
        if("admin".equals(name)){
            User user = new User();
            user.setUsername("admin");
            user.setPassword("8da6acb928eae1bcba142b9af285d961");
            user.setSalt("sdfkjhsdkjfshdf234234kj");
            return user;
        }
        return null;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户输入的用户名
        String username = (String) token.getPrincipal();
        User user = this.findByUsername(username);
        if(user == null){
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),"CustomRealm");
        info.setCredentialsSalt(ByteSource.Util.bytes(user.getSalt()));
        return info;
    }

    private Set<String> findRoleNameByUsername(String username){
        Set<String> roleName = new HashSet<>();
        roleName.add("超级管理员");
        roleName.add("普通用户");
        return roleName;
    }

    private Set<String> findPermissionsByRoleNames(Set<String> roleNames){
        Set<String> permissions = new HashSet<>();
        permissions.add("product:insert");
        permissions.add("product:update");
        permissions.add("product:delete");
        return permissions;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //        1. 获取当前登录用户的用户名.
        User user = (User) principals.getPrimaryPrincipal();
        String username = user.getUsername();
        //        2. 根据用户名查询全部的角色.
        Set<String> roleNames = this.findRoleNameByUsername(username);
        //        3. 根据角色查询全部的权限.
        Set<String> permissions = this.findPermissionsByRoleNames(roleNames);
        //        4. 创建返回结果info,并封装角色和权限.
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissions);
        //        5. 返回.
        return info;
    }

}
