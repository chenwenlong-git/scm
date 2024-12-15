package com.hete.supply.scm.server.scm.adjust.service.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.remote.dubbo.McRemoteService;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.AdjustPriceApproveDao;
import com.hete.supply.scm.server.scm.adjust.entity.dto.AdjustApproveSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.dto.AdjustPriceApproveNoDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.AdjustApproveVo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/6/18 11:14
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdjustApproveBizService {
    private final AdjustPriceApproveDao adjustPriceApproveDao;
    private final PlmRemoteService plmRemoteService;
    private final McRemoteService mcRemoteService;
    private final UdbRemoteService udbRemoteService;

    public CommonPageResult.PageInfo<AdjustApproveVo> searchAdjustApprove(AdjustApproveSearchDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuList = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            dto.setSkuList(skuList);
        }

        if (CollectionUtils.isNotEmpty(dto.getSkuList()) || CollectionUtils.isNotEmpty(dto.getPurchaseChildOrderNoList())) {
            dto.setIsItemSearch(BooleanType.TRUE);
        }

        final CommonPageResult.PageInfo<AdjustApproveVo> pageInfo = adjustPriceApproveDao.searchAdjustApprove(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<AdjustApproveVo> records = pageInfo.getRecords();
        final List<String> applyUserList = records.stream()
                .map(AdjustApproveVo::getApplyUser)
                .distinct()
                .collect(Collectors.toList());

        final List<UserVo> userVoList = udbRemoteService.getListByUserCodeList(applyUserList);
        final Map<String, String> userAvatarMap = userVoList.stream()
                .collect(Collectors.toMap(UserVo::getUserCode, UserVo::getAvatar));
        records.forEach(record -> record.setAvatar(userAvatarMap.get(record.getApplyUser())));
        return pageInfo;
    }

    public void approveWorkFlow(AdjustPriceApproveNoDto dto) {
        final AdjustPriceApprovePo adjustPriceApprovePo = adjustPriceApproveDao.getOneByNo(dto.getAdjustPriceApproveNo());
        if (null == adjustPriceApprovePo) {
            throw new BizException("调价审批单:{}不存在，请联系系统管理员！", dto.getAdjustPriceApproveNo());
        }
        if (!GlobalContext.getUserKey().equals(adjustPriceApprovePo.getApproveUser())) {
            throw new ParamIllegalException("当前用户无法对调价审批单:{}进行审批操作，请刷新后重试！", dto.getAdjustPriceApproveNo());
        }

        mcRemoteService.approveWorkFlow(dto);

    }

    public void rejectWorkFlow(AdjustPriceApproveNoDto dto) {
        final AdjustPriceApprovePo adjustPriceApprovePo = adjustPriceApproveDao.getOneByNo(dto.getAdjustPriceApproveNo());
        if (null == adjustPriceApprovePo) {
            throw new BizException("调价审批单:{}不存在，请联系系统管理员！", dto.getAdjustPriceApproveNo());
        }
        if (!GlobalContext.getUserKey().equals(adjustPriceApprovePo.getApproveUser())) {
            throw new ParamIllegalException("当前用户无法对调价审批单:{}进行审批操作，请刷新后重试！", dto.getAdjustPriceApproveNo());
        }

        mcRemoteService.rejectWorkFlow(dto);
    }
}
