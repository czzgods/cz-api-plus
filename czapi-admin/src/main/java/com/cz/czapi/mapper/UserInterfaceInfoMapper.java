package com.cz.czapi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cz.czapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author 李钟意
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-02-17 21:29:09
* @Entity com.cz.czapi.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    /**
     * 获取接口调用排名前 n 的接口信息
     *
     * @param limit 前几名
     * @return List<InterfaceInfoVO>
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




