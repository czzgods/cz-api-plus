package com.cz.czapicommon.service;


import com.cz.czapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author cz
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey accessKey
     * @return User 用户信息
     */
    User getInvokeUser(String accessKey);
}
