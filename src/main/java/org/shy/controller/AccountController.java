package org.shy.controller;

import lombok.extern.slf4j.Slf4j;
import org.shy.beans.StateResult;
import org.shy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 18:13
 * @Description
 */
@RestController
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountServiceImpl;

    @RequestMapping("/accountlogin")
    @ResponseBody
    public StateResult login(){
       return new StateResult(200,"测试JSON");
    }

}
