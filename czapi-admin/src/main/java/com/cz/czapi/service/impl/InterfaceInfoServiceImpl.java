package com.cz.czapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cz.czapi.service.InterfaceInfoService;
import com.cz.czapi.mapper.InterfaceInfoMapper;
import com.cz.czapicommon.model.entity.InterfaceInfo;
import org.springframework.stereotype.Service;

/**
* @author 李钟意
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-02-17 21:29:01
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

}




