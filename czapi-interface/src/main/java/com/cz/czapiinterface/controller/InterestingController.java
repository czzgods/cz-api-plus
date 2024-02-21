package com.cz.czapiinterface.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 有意思的接口
 */
@RestController
public class InterestingController {
    /**
     * 获取随机毒鸡汤
     * @param request
     * @return
     */
    @PostMapping("/yan/api.php")
    public String poisonChicken(HttpServletRequest request) {
        String url = "http://api.btstu.cn/yan/api.php";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        return httpResponse.body();
    }
    /*/api/rand.avatar---获取随机头像*/
    @RequestMapping("/api/rand.avatar")
    public String randAvatar(HttpServletRequest request) {
        String url = "https://api.uomg.com/api/rand.avatar";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        System.out.println(JSONUtil.formatJsonStr(httpResponse.body()));
        return httpResponse.body();
    }
   /*
   sort=男&format=json
   @PostMapping("/api/rand.avatar")
    public String randAvatar(HttpServletRequest request) {
        String url = "https://api.uomg.com/api/rand.avatar";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.post(url)
                .header("Content-Type", "application/json") // 设置请求头
                .body(body)
                .execute();
        return httpResponse.body();
    }*/

    /**
     * 随机壁纸
     * @param request
     * @return
     */
   @PostMapping("/sjbz/api.php")
   public String randImages(HttpServletRequest request) {
       String url = "http://api.btstu.cn/sjbz/api.php";
       String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
       HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
               .execute();
       return httpResponse.body();
   }

    /**
     * 网易云音乐随机
     * @param request
     * @return
     */
    @PostMapping("/api/rand.music")
    public String randMusic(HttpServletRequest request) {
        String url = "https://api.uomg.com/api/rand.music";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        HttpResponse httpResponse = HttpRequest.get(url + "?" + body)
                .execute();
        return httpResponse.body();
    }

    /**
     * 网易云音乐热门评论随机API接口
     * @param request
     * @return
     */
    @PostMapping("/api/comments.163")
    public String hotComments(HttpServletRequest request) {
        String url = "https://api.uomg.com/api/comments.163";
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        System.out.println(body);
        HttpResponse httpResponse = HttpRequest.post(url)
                .body(body)
                .execute();
        return httpResponse.body();
    }
}
