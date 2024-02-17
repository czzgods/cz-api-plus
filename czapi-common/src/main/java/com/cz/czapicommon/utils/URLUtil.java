package com.cz.czapicommon.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLUtil {

    /**
     * 解码
     * 将一串乱码的url转换成http://......
     * @param body
     * @param charset
     * @return
     */
    public static String decode(String body, String charset) {
        try {
            body = URLDecoder.decode(body, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return body;
    }

    /**
     * 编码
     * 将http://......转换成一串乱码
     * @param body
     * @param charset
     * @return
     */
    public static String encode(String body, String charset) {
        try {
            body = URLEncoder.encode(body, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return body;
    }

}
