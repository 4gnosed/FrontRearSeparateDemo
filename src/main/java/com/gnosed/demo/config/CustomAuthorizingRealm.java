package com.gnosed.demo.config;

import com.gnosed.demo.pojo.User;
import com.gnosed.demo.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Gnosed Lu
 * @Date 2020/1/16
 */
public class CustomAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private IUserService iUserService;

    /**
     * 简单重写获取授权信息方法
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 获取认证信息，即根据 token 中的用户名从数据库中获取密码、盐等并返回
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        User user = iUserService.getByName(username);
        if(user==null) return null;
        String encodedPassword = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(
                username, encodedPassword, ByteSource.Util.bytes(salt), getName());
    }
}
