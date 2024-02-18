package com.cz.czapi.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cz.czapiclientsdk.client.CzApiClient;
import com.cz.czapicommon.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.cz.czapicommon.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.cz.czapicommon.model.entity.InterfaceInfo;
import com.cz.czapicommon.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 李钟意
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-02-17 21:29:01
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 参数校验
     * @param interfaceInfo
     * @param b
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    /**
     *修改接口信息
     * @param interfaceInfoUpdateRequest
     * @return
     *
     */
    boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest);

    /**
     *获取接口信息封装
     * @param interfaceInfo
     * @param request
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 分页获取接口信息封装
     *
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    /**
     * 根据用户ID 分页获取接口信息封装
     *
     * @param interfaceInfoPage 接口信息分页
     * @param request           当前会话
     * @return 接口信息分页
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOByUserIdPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    /**
     * 创建SDK客户端
     *
     * @param request 当前会话
     * @return SDK客户端
     */
    CzApiClient getCzApiClient(HttpServletRequest request);
}
