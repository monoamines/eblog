package com.teacherwl.eblog.shiro;

import com.teacherwl.eblog.service.MUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm  extends AuthorizingRealm {


    @Autowired
    MUserService mUserService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

      AccountProfile   primaryPrincipal = (AccountProfile) principalCollection.getPrimaryPrincipal();

      if(primaryPrincipal.getId()==7)
      {
          SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
          simpleAuthorizationInfo.addRole("admin");
          return simpleAuthorizationInfo;
      }
      return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken) authenticationToken;
     AccountProfile accountProfile=   mUserService.login(usernamePasswordToken.getUsername(),String.valueOf(usernamePasswordToken.getPassword()));
        SecurityUtils.getSubject().getSession().setAttribute("profile",accountProfile);
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(accountProfile,authenticationToken.getCredentials(),getName());
        return info;
    }
}
