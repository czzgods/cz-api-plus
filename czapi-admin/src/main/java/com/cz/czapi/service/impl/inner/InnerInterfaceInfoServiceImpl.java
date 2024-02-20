package com.cz.czapi.service.impl.inner;

import com.cz.czapi.service.InterfaceInfoService;
import com.cz.czapicommon.model.entity.InterfaceInfo;
import com.cz.czapicommon.service.InnerInterfaceInfoService;

import javax.annotation.Resource;

public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        InterfaceInfo interfaceInfo = interfaceInfoService.lambdaQuery()
                .eq(InterfaceInfo::getUrl, path)
                .eq(InterfaceInfo::getMethod, method)
                .one();
        return interfaceInfo;
    }
}
