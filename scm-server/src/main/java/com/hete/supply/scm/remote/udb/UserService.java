package com.hete.supply.scm.remote.udb;

import com.hete.supply.udb.api.entity.dto.UserCodeDto;
import com.hete.supply.udb.api.entity.dto.UsernameListDto;
import com.hete.supply.udb.api.entity.vo.UserCodeNameVo;
import com.hete.supply.udb.api.entity.vo.UserSupplierAuthInfoVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.supply.udb.api.service.OrgFacade;
import com.hete.supply.udb.api.service.UserFacade;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.dubbo.util.DubboResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户服务
 *
 * @author ChenWenLong
 * @date 2022/11/28 16:39
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserService {

    @DubboReference(check = false)
    private UserFacade userFacade;

    @DubboReference(check = false)
    private OrgFacade orgFacade;

    @Nullable
    public CommonResult<UserVo> getByUserCode(UserCodeDto dto) {
        return userFacade.getByUserCode(dto);
    }

    /**
     * 查询供应商编码
     *
     * @return
     */
    public UserSupplierAuthInfoVo getUserSupplierAuthInfo() {
        UserCodeDto userCodeDto = new UserCodeDto();
        String userCode = GlobalContext.getUserKey();
        userCodeDto.setUserCode(userCode);
        CommonResult<UserSupplierAuthInfoVo> result = orgFacade.getUserSupplierAuthInfo(userCodeDto);
        return DubboResponseUtil.checkCodeAndGetData(result);
    }

    /**
     * 根据Username查询用户信息
     *
     * @param dto
     * @return
     */
    public CommonResult<ResultList<UserCodeNameVo>> listDingTalkUserByUsername(@NotNull @Valid UsernameListDto dto) {
        return userFacade.listDingTalkUserByUsername(dto);
    }
}
