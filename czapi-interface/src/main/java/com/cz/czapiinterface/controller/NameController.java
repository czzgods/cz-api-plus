package com.cz.czapiinterface.controller;

import com.cz.czapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
public class NameController {
    @PostMapping("/api/name/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) throws IOException {
       /* BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String params = sb.toString();
        // 现在你可以从 params 中获取到请求体的参数
        return  "POST 你的用户名字是：" + params;*/
        return "POST 你的用户名字是：" + user.getUsername();
    }
    @GetMapping ("/get")
    public String getUserNameByGet(@RequestParam String userName){
        return "GET 你的用户名字是：" + userName;
    }
}
