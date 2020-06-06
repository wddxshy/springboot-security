package org.shy.controller;

import lombok.extern.slf4j.Slf4j;
import org.shy.beans.StateResult;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 12:41
 * @Description
 */
@RestController
@Slf4j
public class HelloController {

    @RequestMapping("/hello")
    public StateResult hello(HttpServletRequest req, HttpServletResponse response){
        String verifyKey = (String) req.getSession().getAttribute("verifyKey");
        return new StateResult(1000,"Hello World!",verifyKey);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/admin/hello")
    public StateResult adminHello(){
        return new StateResult(1000,"Hello World!");
    }

    @RequestMapping("/user/hello")
    public StateResult userHello(){
        return new StateResult(1000,"Hello World!");
    }

}
