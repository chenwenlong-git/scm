package com.hete.supply.scm.remote.dubbo;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.udb.api.entity.dto.OrgCodeDto;
import com.hete.supply.udb.api.entity.dto.OrgUsersQueryDto;
import com.hete.supply.udb.api.entity.dto.UserCodeDto;
import com.hete.supply.udb.api.entity.dto.UserCodeListDto;
import com.hete.supply.udb.api.entity.vo.OrgVo;
import com.hete.supply.udb.api.entity.vo.UserCodeNameVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.supply.udb.api.service.OrgFacade;
import com.hete.supply.udb.api.service.UserFacade;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.core.util.CollSplitUtil;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/7/31 11:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Validated
public class UdbRemoteService {
    @DubboReference(check = false)
    private UserFacade userFacade;

    @DubboReference(check = false)
    private OrgFacade orgFacade;

    /**
     * 根据组织编码获取部门下的所有员工
     *
     * @param dto
     * @return
     */
    public List<UserCodeNameVo> queryUserListByOrgCode(OrgUsersQueryDto dto) {
        final CommonResult<ResultList<UserCodeNameVo>> result = userFacade.getOrgUsers(dto);
        final ResultList<UserCodeNameVo> userVoResultList = DubboResponseUtil.checkCodeAndGetData(result);
        return userVoResultList.getList();
    }

    /**
     * 查询账号信息
     *
     * @param userCode:
     * @return UserVo
     * @author ChenWenLong
     * @date 2023/12/14 17:32
     */
    public UserVo getByUserCode(String userCode) {
        if (StringUtils.isBlank(userCode)) {
            return null;
        }
        UserCodeDto dto = new UserCodeDto();
        dto.setUserCode(userCode);
        CommonResult<UserVo> result = userFacade.getByUserCode(dto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    /**
     * 根据部门编码获取部门信息
     *
     * @param orgCode
     * @return
     */
    public OrgVo getOrgByCode(String orgCode) {
        final OrgCodeDto orgCodeDto = new OrgCodeDto();
        orgCodeDto.setOrgCode(orgCode);
        final CommonResult<OrgVo> result = orgFacade.getByCode(orgCodeDto);

        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    /**
     * 获取用户的信息LIST
     *
     * @param userCodeList:
     * @return List<UserVo>
     * @author ChenWenLong
     * @date 2024/5/31 11:12
     */
    public List<UserVo> getListByUserCodeList(List<String> userCodeList) {
        if (CollectionUtils.isEmpty(userCodeList)) {
            return Collections.emptyList();
        }
        return CollSplitUtil.collSplitExec(userCodeList, ScmConstant.SPLIT_SIZE, ScmConstant.THREAD_POOL_NAME, list -> {
            UserCodeListDto userCodeListDto = new UserCodeListDto();
            userCodeListDto.setUserCodeList(list);
            return userFacade.listByUserCode(userCodeListDto);
        }, result -> DubboResponseUtil.checkCodeAndGetData(result).getList(), "向UDP获取用户信息异常失败！");
    }

    /**
     * 获取用户的信息MAP
     *
     * @param userCodeList:
     * @return Map<String, UserVo>
     * @author ChenWenLong
     * @date 2024/5/31 11:12
     */
    public Map<String, UserVo> getMapByUserCodeList(List<String> userCodeList) {
        if (CollectionUtils.isEmpty(userCodeList)) {
            return Collections.emptyMap();
        }
        List<UserVo> listByUserCodeList = this.getListByUserCodeList(userCodeList);
        if (CollectionUtils.isEmpty(listByUserCodeList)) {
            return Collections.emptyMap();
        }
        return listByUserCodeList.stream()
                .collect(Collectors.toMap(UserVo::getUserCode, Function.identity(), (existing, replacement) -> existing));
    }

}
