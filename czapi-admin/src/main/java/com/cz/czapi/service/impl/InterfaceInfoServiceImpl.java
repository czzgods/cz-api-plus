package com.cz.czapi.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cz.czapi.exception.BusinessException;
import com.cz.czapi.exception.ThrowUtils;
import com.cz.czapi.service.InterfaceInfoService;
import com.cz.czapi.mapper.InterfaceInfoMapper;
import com.cz.czapi.service.UserInterfaceInfoService;
import com.cz.czapi.service.UserService;
import com.cz.czapi.utils.SqlUtils;
import com.cz.czapiclientsdk.client.CzApiClient;
import com.cz.czapicommon.common.ErrorCode;
import com.cz.czapicommon.constant.CommonConstant;
import com.cz.czapicommon.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.cz.czapicommon.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.cz.czapicommon.model.entity.InterfaceInfo;
import com.cz.czapicommon.model.entity.User;
import com.cz.czapicommon.model.entity.UserInterfaceInfo;
import com.cz.czapicommon.model.vo.InterfaceInfoVO;
import com.cz.czapicommon.model.vo.RequestParamsRemarkVO;
import com.cz.czapicommon.model.vo.ResponseParamsRemarkVO;
import com.cz.czapicommon.model.vo.UserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.JSONPatch.OperationType.add;

/**
* @author 李钟意
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-02-17 21:29:01
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Resource
    private UserService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestParams = interfaceInfo.getRequestParams();
        String host = interfaceInfo.getHost();
        String method = interfaceInfo.getMethod();

        // 创建时，参数不能为空
        if (b) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description, url, host, requestParams, method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public boolean updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        Long id = interfaceInfoUpdateRequest.getId();
        //判断接口是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest,interfaceInfo);
        interfaceInfo.setRequestParamsRemark(JSONUtil.toJsonStr(interfaceInfoUpdateRequest.getRequestParamsRemark()));
        interfaceInfo.setResponseParamsRemark(JSONUtil.toJsonStr(interfaceInfoUpdateRequest.getResponseParamsRemark()));
        //校验参数
        this.validInterfaceInfo(interfaceInfo,false);
        return this.updateById(interfaceInfo);
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        //使用写好的封装方法将实体类转封装类
        InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
        //获取用户id
        Long userId = interfaceInfoVO.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        interfaceInfoVO.setUser(userVO);
        // 封装请求参数说明 和 响应参数说明
        //因为这两个数据在存入数据库的时候转换为json格式了
        //所以显示的时候要转换回来
        /*JSONUtil.parseArray(interfaceInfo.getRequestParamsRemark()) 将 JSON 字符串解析成一个 JSON 数组，
        然后使用 JSONUtil.toList() 将 JSON 数组转换成一个列表对象，
        其中每个元素都被映射为 RequestParamsRemarkVO 类型的对象
        */
        List<RequestParamsRemarkVO> requestParamsRemarkVOList = JSONUtil.toList(JSONUtil.parseArray(interfaceInfo.getRequestParamsRemark()), RequestParamsRemarkVO.class);
        List<ResponseParamsRemarkVO> responseParamsRemarkVOList = JSONUtil.toList(JSONUtil.parseArray(interfaceInfo.getResponseParamsRemark()), ResponseParamsRemarkVO.class);
        interfaceInfoVO.setRequestParamsRemark(requestParamsRemarkVOList);
        interfaceInfoVO.setResponseParamsRemark(responseParamsRemarkVOList);
        return interfaceInfoVO;
    }

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if(interfaceInfoQueryRequest == null){
            return queryWrapper;
        }
        //获取查询条件
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String method = interfaceInfoQueryRequest.getMethod();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String searchText = interfaceInfoQueryRequest.getSearchText();
        Date createTime = interfaceInfoQueryRequest.getCreateTime();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        Long id = interfaceInfoQueryRequest.getId();
        Long userId = interfaceInfoQueryRequest.getUserId();
        //拼接查询条件
        if(StringUtils.isNotBlank(searchText)){
            queryWrapper.like("name",searchText).or().like("description",searchText);
        }
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.gt(ObjectUtils.isNotEmpty(createTime), "createTime", createTime);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        Page<InterfaceInfoVO>interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(),interfaceInfoPage.getTotal());
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        //获取原始分页列表数据
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        if(CollectionUtils.isEmpty(interfaceInfoList)){
            return interfaceInfoVOPage;
        }
        //关联查询用户信息
        Set<Long> userIdSet = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
        //根据用户id分组
        Map<Long, List<User>> userIdUserListMap  = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));
        //填充信息
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream()
                .map(interfaceInfo -> {
                    InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
                    //创建人的用户id
                    Long userId = interfaceInfo.getUserId();
                    //判断是否是当前用户拥有的接口
                    boolean hasInterfaceInfo = false;
                    //查询当前登录用户的接口调用次数
                    UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.lambdaQuery()
                            .eq(UserInterfaceInfo::getUserId, loginUser.getId())
                            .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfo.getId())
                            .one();
                    if (userInterfaceInfo != null) {
                        hasInterfaceInfo = true;
                        interfaceInfoVO.setTotalNum(userInterfaceInfo.getTotalNum());
                        interfaceInfoVO.setLeftNum(userInterfaceInfo.getLeftNum());
                    }
                    //获取用户信息
                    /*从 userIdUserListMap 中获取与指定的 userId 相关联的值，
                    如果 userId 在映射中不存在，则返回一个空列表 Collections.emptyList()。*/
                    User user = userIdUserListMap.getOrDefault(userId, Collections.emptyList()).stream().findFirst().orElse(null);
                    interfaceInfoVO.setUser(userService.getUserVO(user));
                    // 封装请求参数说明和响应参数说明
                    List<RequestParamsRemarkVO> requestParamsRemarkVOList = JSONUtil.toList(JSONUtil.parseArray(interfaceInfo.getRequestParamsRemark()), RequestParamsRemarkVO.class);
                    List<ResponseParamsRemarkVO> responseParamsRemarkVOList = JSONUtil.toList(JSONUtil.parseArray(interfaceInfo.getResponseParamsRemark()), ResponseParamsRemarkVO.class);
                    interfaceInfoVO.setRequestParamsRemark(requestParamsRemarkVOList);
                    interfaceInfoVO.setResponseParamsRemark(responseParamsRemarkVOList);
                    // 设置是否为当前用户拥有的接口
                    interfaceInfoVO.setIsOwnerByCurrentUser(hasInterfaceInfo);
                    return interfaceInfoVO;
                })
                .collect(Collectors.toList());
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOByUserIdPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(),interfaceInfoPage.getTotal());
        if(CollectionUtils.isEmpty(interfaceInfoList)){
            return interfaceInfoVOPage;
        }
        //获取当前登录用户id
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        //过滤掉不是当前用户的接口，并填充信息
        List<InterfaceInfoVO> interfaceInfoVOList  = interfaceInfoList.stream()
                .map(interfaceInfo -> {
                    InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
                    UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.lambdaQuery()
                            .eq(UserInterfaceInfo::getUserId, userId)
                            .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfo.getId())
                            .one();
                    if (userInterfaceInfo != null) {
                        interfaceInfoVO.setTotalNum(userInterfaceInfo.getTotalNum());
                        interfaceInfoVO.setLeftNum(userInterfaceInfo.getLeftNum());
                        // 封装请求参数说明和响应参数说明
                        List<RequestParamsRemarkVO> requestParamsRemarkVOList = JSONUtil.toList(JSONUtil.parseArray(interfaceInfo.getRequestParamsRemark()), RequestParamsRemarkVO.class);
                        List<ResponseParamsRemarkVO> responseParamsRemarkVOList = JSONUtil.toList(JSONUtil.parseArray(interfaceInfo.getResponseParamsRemark()), ResponseParamsRemarkVO.class);
                        interfaceInfoVO.setRequestParamsRemark(requestParamsRemarkVOList);
                        interfaceInfoVO.setResponseParamsRemark(responseParamsRemarkVOList);
                        return interfaceInfoVO;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        /*关于 filter(Objects::nonNull)，它的作用是过滤掉流中的空值元素，
        即过滤掉那些值为 null 的元素。这样做是为了去除在映射转换过程中未找到相关 UserInterfaceInfo 的情况，
        从而保证最终结果中不含有空值。*/
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public CzApiClient getCzApiClient(HttpServletRequest request) {
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        return new CzApiClient(accessKey, secretKey);
    }
}




