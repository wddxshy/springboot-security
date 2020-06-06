package org.shy.override;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.shy.beans.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/9 14:18
 * @Description  验证码过滤器
 */
@Component
public class VerifyCodeFilter extends GenericFilterBean {
    private String defaultFilterProcessUrl="/login";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if ("POST".equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {
            if (request.getContentType().equals(MediaType.APPLICATION_JSON)
                    || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
                // 验证码验证
                String accountKaptcha = null;
                try(InputStream is = request.getInputStream()) {
                    ObjectMapper mapper = new ObjectMapper();
                    Account account = mapper.readValue(is, Account.class);
                    accountKaptcha = account.getKaptcha();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String verifyKey = stringRedisTemplate.opsForValue().get("verifyKey");
                if (StringUtils.isEmpty(accountKaptcha))
                    throw new AuthenticationServiceException("验证码不能为空!");
            }
        }
        System.out.println("验证码验证成功");
        filterChain.doFilter(request, response);
    }
}
