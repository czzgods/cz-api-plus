package com.cz.czapiinterface.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DyeDataInterceptor implements HandlerInterceptor {
    //流量染色数据的KEY
    private static final String DYE_DATA_HEADER = "X-Dye-Data";
    //流量染色数据的VALUE
    private static final String DYE_DATA_VALUE = "czzz";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的染色数据
        String dyeData = request.getHeader(DYE_DATA_HEADER);
        if(dyeData == null || !DYE_DATA_VALUE.equals(dyeData)){
            //染色数据不匹配，返回错误码
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        //放行
        return true;
    }
}
