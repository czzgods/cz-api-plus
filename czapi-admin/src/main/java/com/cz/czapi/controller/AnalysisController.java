package com.cz.czapi.controller;

import com.cz.czapi.annotation.AuthCheck;
import com.cz.czapi.exception.BusinessException;
import com.cz.czapi.service.InterfaceInfoService;
import com.cz.czapi.service.UserInterfaceInfoService;
import com.cz.czapicommon.common.BaseResponse;
import com.cz.czapicommon.common.ErrorCode;
import com.cz.czapicommon.common.ResultUtils;
import com.cz.czapicommon.constant.UserConstant;
import com.cz.czapicommon.model.entity.InterfaceInfo;
import com.cz.czapicommon.model.entity.UserInterfaceInfo;
import com.cz.czapicommon.model.vo.InterfaceInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计分析接口
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        //查询调用次数前三的接口
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.listTopInvokeInterfaceInfo(3);
        if(userInterfaceInfoList.isEmpty()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口信息不存在");
        }
        //根据接口id进行分组
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        //查询接口信息
        List<InterfaceInfo> list = interfaceInfoService.lambdaQuery()
                .in(InterfaceInfo::getId, interfaceInfoIdObjMap.keySet())
                .list();
        if(list.isEmpty()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口信息不存在");
        }
        //封装信息并返回
        List<InterfaceInfoVO> result = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
            Integer totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(result);
    }
}
