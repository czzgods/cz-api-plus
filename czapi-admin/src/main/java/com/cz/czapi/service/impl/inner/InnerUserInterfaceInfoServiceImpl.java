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

    /**
     * 判断是否还有调用次数,用户是否拥有该接口
     * 可以从返回的UserInterfaceInfo对象里取出来调用次数并判断有没有
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return
     */
    @Override
    public UserInterfaceInfo hasLeftNum(Long interfaceId, Long userId) {
        return userInterfaceInfoService.lambdaQuery()
                .eq(UserInterfaceInfo::getInterfaceInfoId,interfaceId)
                .eq(UserInterfaceInfo::getUserId,userId)
                .one();
    }

    /**
     * 添加默认的用户接口信息
     *
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return Boolean 是否添加成功
     */
    @Override
    public Boolean addDefaultUserInterfaceInfo(Long interfaceId, Long userId) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setInterfaceInfoId(interfaceId);
        userInterfaceInfo.setLeftNum(99999999);
        return userInterfaceInfoService.save(userInterfaceInfo);
    }

    /**
     * 检查用户是否有接口
     *
     * @param interfaceId 接口id
     * @param userId     用户id
     * @return UserInterfaceInfo 用户接口信息
     */
    @Override
    public UserInterfaceInfo checkUserHasInterface(long interfaceId, long userId) {
        if(userId <= 0 || interfaceId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.lambdaQuery()
                .eq(UserInterfaceInfo::getInterfaceInfoId,interfaceId)
                .eq(UserInterfaceInfo::getUserId,userId)
                .one();
    }
}
