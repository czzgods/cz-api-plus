package com.cz.czapiinterface.controller;

import com.cz.czapicommon.model.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class NameController {
    @PostMapping("/api/name/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request){
        return  "POST 你的用户名字是：" + user.getUserName();
    }
    @GetMapping ("/get")
    public String getUserNameByGet(@RequestParam String userName){
        return "GET 你的用户名字是：" + userName;
    }
}
