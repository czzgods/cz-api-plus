package com.cz.czapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.cz.czapiclientsdk.utils.SignUtils.genSign;

/**
 * API调用
 */
public class CzApiClient {
    //网关地址
    public static String GATEWAY_HOST = "http://localhost:8090";
    private String accessKey;
    private String secretKey;

    /**
     * 构造方法
     * @param accessKey
     * @param secretKey
     */
    public CzApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 设置网关地址
     * @param gatewayHost
     */
    public void setGatewayHost(String gatewayHost){
        GATEWAY_HOST = gatewayHost;
    }

    /**
     *用于发送Http请求，调用第三方接口
     * @param params 方法参数
     * @param url 请求路径
     * @param method 方法名称（POST,GET）
     * @return
     */
     /*header("Accept-Charset", CharsetUtil.UTF_8)//设置请求头中的 "Accept-Charset" 字段，
     指定客户端所能接受的字符集编码格式。在这里，它指定客户端能够接受的字符集编码为 UTF-8，
     这意味着客户端期望响应使用 UTF-8 编码格式进行字符编码。*/
    public String invokeInterface(String params,String url,String method) throws UnsupportedEncodingException {
        HttpResponse response = HttpRequest.post(GATEWAY_HOST+url)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(params, method))
                .body(params)
                .execute();
        //返回Http请求响应体重的数据
        /*JSON 格式化：
        toJsonStr()：将对象或数据转换为 JSON 字符串，并且不会进行额外的格式化或缩进，生成的 JSON 字符串可能是一行的形式，不易阅读。
        formatJsonStr()：将 JSON 字符串进行格式化和缩进，使其更具可读性，每个层级都会进行适当的缩进，方便开发者查看和理解。
        用途：
        toJsonStr()：适用于将对象或数据转换为 JSON 字符串，并且通常用于将对象序列化为 JSON 数据以便进行传输或存储。
        formatJsonStr()：适用于将 JSON 字符串进行格式化，通常用于日志输出、调试或展示给用户阅读。*/
        return JSONUtil.formatJsonStr(response.body());
    }

    /**
     * 该方法可以给我们要发送HTTP请求构建请求头信息
     * @param params
     * @param method
     * @return
     */
    private Map<String, String> getHeaderMap(String params, String method) throws UnsupportedEncodingException{
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        map.put("nonce", RandomUtil.randomNumbers(10));
        map.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign",genSign(params,secretKey));
        //下面这行代码可以对请求传来的params进行URL编码，能解决中文乱码的问题
        params = URLUtil.encode(params,CharsetUtil.CHARSET_UTF_8);
        map.put("body",params);
        map.put("method",method);
        return map;
    }
}
