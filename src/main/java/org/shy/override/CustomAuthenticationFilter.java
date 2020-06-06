package org.shy.override;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.shy.beans.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/8 15:46
 * @Description  重写SpringSecurity认证filter
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("获取JSON前");
          //当内容类型是JSON时，尝试身份验证
        if (request.getContentType().equals(MediaType.APPLICATION_JSON)
                 || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            System.out.println("获取JSON");
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authenticationToken = null;

            try (InputStream is = request.getInputStream()){
                Account account = mapper.readValue(is, Account.class);
                log.info(account.toString());


                authenticationToken = new UsernamePasswordAuthenticationToken(
                        account.getUsername(), account.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
                authenticationToken = new UsernamePasswordAuthenticationToken("", "");
                log.info("账号，密码注入失败");
            } finally {
                setDetails(request,authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        }else {
            return super.attemptAuthentication(request, response);
        }
    }
}
