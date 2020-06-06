package org.shy.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.shy.beans.Account;
import org.shy.beans.Role;
import org.shy.mapper.AccountMapper;
import org.shy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 17:53
 * @Description
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        Account account = accountMapper.queryAccountByUsername(username);
        log.info(account.toString());
        List<GrantedAuthority> authorities=new ArrayList<>();
        List<Role> roles = account.getRoles();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRName()));
        }
        System.out.println(authorities);
        UserDetails user = new User(account.getUsername(),account.getPassword(),authorities);
        System.out.println(user.toString());
        return user;
    }
}
