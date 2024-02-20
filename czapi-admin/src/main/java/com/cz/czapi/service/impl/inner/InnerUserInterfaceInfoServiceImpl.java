package com.cz.czapi.service.impl.inner;

import com.cz.czapi.exception.BusinessException;
import com.cz.czapi.service.UserInterfaceInfoService;
import com.cz.czapi.service.UserService;
import com.cz.czapicommon.common.ErrorCode;
import com.cz.czapicommon.model.entity.UserInterfaceInfo;
import com.cz.czapicommon.service.InnerUserInterfaceInfoService;

import javax.annotation.Resource;

public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 修改调用次数
     * @param interfaceInfoId 接口ID
     * @param userId          用户ID
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //查询用户接口信息是否存在
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.lambdaQuery()
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId, userId)
                .one();
        if(userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口不存在");
        }
        return userInterfaceInfoService.lambdaUpdate()
                .eq(UserInterfaceInfo::getInterfaceInfoId,interfaceInfoId)
                .eq(UserInterfaceInfo::getUserId,userId)
                .set(UserInterfaceInfo::getTotalNum,userInterfaceInfo.getTotalNum()+1)
                .set(UserInterfaceInfo::getLeftNum,userInterfaceInfo.getLeftNum()-1)
                .update();
    }

    @Override
    public UserInterfaceInfo hasLeftNum(Long interfaceId, Long userId) {
        return null;
    }

    @Override
    public Boolean addDefaultUserInterfaceInfo(Long interfaceId, Long userId) {
        return null;
    }

    @Override
    public UserInterfaceInfo checkUserHasInterface(long interfaceId, long userId) {
        return null;
    }
}
