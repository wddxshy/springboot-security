package org.shy.config;

import lombok.extern.slf4j.Slf4j;
import org.shy.override.CustomAuthenticationFilter;
import org.shy.override.VerifyCodeFilter;
import org.shy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 12:07
 * @Description
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountServiceImpl;

    @Autowired
    VerifyCodeFilter verifyCodeFilter;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("开始账号认证");
        auth.userDetailsService(accountServiceImpl)
                .passwordEncoder(getPasswordEncoder());
    }



    //注册自定义认证filter
    @Bean
    public CustomAuthenticationFilter CustomAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> log.info("使用JSON，认证成功"));
        filter.setAuthenticationFailureHandler((httpServletRequest, httpServletResponse, e) -> log.info("使用JSON，认证失败"));
        //重用WebSecurityConfigAdapter配置的AuthenticationManager,不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }


    //忽略资源
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/druid/**","/verifyImg");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(verifyCodeFilter,CustomAuthenticationFilter.class);
        //自定义过滤器链实现JSON认证
        http.addFilterAt(CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/hello")
                .hasRole("ADMIN")
                /**
                 *   在连接数据库认证时
                 *   hasRole()中不能用小写，不能以"ROLE_"前缀开头，因为SpringSecurity会默认加；如果加了会报
                 *              throw new IllegalArgumentException("role should not start with 'ROLE_' since it is automatically inserted. Got '" + role + "'");
                 *   但在数据库的权限表中必须以 "ROLE_" 为前缀，且后面跟的权限字符与 hasRole()保持一致(只代指大小写)
                 */
                .antMatchers("/user/hello")
                .hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")    //登录接口
//                .usernameParameter("aUsername")                 //TODO    对自定义认证filter无效
//                .passwordParameter("aPassword")
                //.successForwardUrl("/hello")
                .successHandler((req,resp,auth)->{
                    Object principal = auth.getPrincipal();
                    String name = auth.getName();
                    log.info(name);
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)    //清空session
                .permitAll();
    }
}
