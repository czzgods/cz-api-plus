package com.cz.czapi.service.impl.inner;

import com.cz.czapi.service.UserService;
import com.cz.czapicommon.model.entity.User;
import com.cz.czapicommon.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserService userService;
    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey accessKey
     * @return User 用户信息
     */
    @Override
    public User getInvokeUser(String accessKey) {
        return userService.lambdaQuery()
                .eq(User::getAccessKey, accessKey)
                .one();
    }
}
