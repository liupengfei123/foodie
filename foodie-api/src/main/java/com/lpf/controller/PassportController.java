package com.lpf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 用于注册登录的相关 controller
 * @author liupf
 * @date 2020-09-19 22:22
 */
@RestController
public class PassportController {


    @GetMapping("test")
    public String test(){
        return "ok";
    }


}
