package com.cz.czapiclientsdk.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 主机号
     */
    private String host;
}
