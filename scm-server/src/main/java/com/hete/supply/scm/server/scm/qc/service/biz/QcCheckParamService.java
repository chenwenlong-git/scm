package com.hete.supply.scm.server.scm.qc.service.biz;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcCompletedQcDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcDetailHandItemDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcSubmitDto;
import com.hete.supply.scm.server.scm.qc.entity.dto.QcUnPassDetailItemDto;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.enums.QcOperate;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/10/16 09:23
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class QcCheckParamService {

    public void checkSubmitQcParam(QcSubmitDto dto, Map<Long, Integer> qcDetailIdUnPassedAmountMap,
                                   List<QcDetailPo> qcDetailPoList, boolean residentQc) {

        // 校验数量
        for (QcDetailHandItemDto handItemDto : dto.getQcDetailHandItemList()) {
            final Integer unPassedAmount = qcDetailIdUnPassedAmountMap.getOrDefault(handItemDto.getQcDetailId(), 0);
            if (handItemDto.getPassAmount() + unPassedAmount > handItemDto.getAmount()) {
                throw new ParamIllegalException("批次码:{}的合格与不合格数量之和超过了质检总数", handItemDto.getSkuBatchCode());
            }
        }

        if (residentQc) {
            return;
        }
        // 校验次品信息容器码
        if (CollectionUtils.isNotEmpty(dto.getQcUnPassDetailItemList())) {
            if (dto.getQcUnPassDetailItemList().stream()
                    .map(QcUnPassDetailItemDto::getContainerCode)
                    .distinct().count() != 1) {
                throw new BizException("质检次品信息的容器编码必须相同，数据错误，请联系系统管理员！");
            }

            // 次品容器
            final String containerCode = dto.getQcUnPassDetailItemList().get(0).getContainerCode();

            // 正品容器与次品容器不能相同
            qcDetailPoList.stream()
                    .filter(qcDetailPo -> QcResult.PASSED.equals(qcDetailPo.getQcResult()))
                    .forEach(qcDetailPo -> {
                        if (containerCode.equals(qcDetailPo.getContainerCode())) {
                            throw new ParamIllegalException("次品容器与正品容器不能相同，请重新选择容器:{}！", containerCode);
                        }
                    });
        }
    }

    public void checkCompletedQc(QcOrderPo qcOrderPo, List<QcDetailPo> qcDetailPoList, QcCompletedQcDto dto,
                                 Map<Long, Integer> qcDetailIdUnPassedAmountMap, boolean residentQc) {

        // 校验质检操作与质检详情信息是否符合
        if (QcOperate.PASSED.equals(dto.getQcOperate())) {
            if (CollectionUtils.isNotEmpty(dto.getQcUnPassDetailItemList())) {
                throw new ParamIllegalException("整单合格的提交不允许提交质检次品信息，请选择完成质检按钮提交或清空质检次品信息");
            }
        } else if (QcOperate.NOT_PASSED.equals(dto.getQcOperate())) {
            if (CollectionUtils.isEmpty(dto.getQcUnPassDetailItemList())) {
                throw new ParamIllegalException("整单不合格需要填写质检次品信息，请选择完成质检按钮提交或填写质检次品信息");
            }
            dto.getQcDetailHandItemList().forEach(qcDetailHandItemDto -> {
                if (0 != qcDetailHandItemDto.getPassAmount()) {
                    throw new ParamIllegalException("整单不合格的正品数应该都为0，当前批次码:{}对应的正品数不为0，请重新填写",
                            qcDetailHandItemDto.getSkuBatchCode());

                }
            });
        }

        // detail按id聚合
        final Map<Long, QcDetailPo> qcDetailIdPoMap = qcDetailPoList.stream()
                .collect(Collectors.toMap(QcDetailPo::getQcDetailId, Function.identity()));

        for (QcDetailHandItemDto qcDetailHandItemDto : dto.getQcDetailHandItemList()) {
            final int passedAndUnPassedAmount = qcDetailHandItemDto.getPassAmount()
                    + qcDetailIdUnPassedAmountMap.getOrDefault(qcDetailHandItemDto.getQcDetailId(), 0);
            final QcDetailPo qcDetailPo = qcDetailIdPoMap.get(qcDetailHandItemDto.getQcDetailId());
            if (null == qcDetailPo) {
                throw new BizException("质检详情id:{}找不到对应的详情数据，数据错误，请联系系统管理员！", qcDetailHandItemDto.getQcDetailId());
            }

            if (passedAndUnPassedAmount != qcDetailPo.getAmount()) {
                if (residentQc) {
                    throw new ParamIllegalException("合格与不合格数量之和:{}不等于质检总数:{}", passedAndUnPassedAmount,
                            qcDetailPo.getAmount());
                } else {
                    throw new ParamIllegalException("容器编码:{}的合格与不合格数量之和:{}不等于质检总数:{}",
                            qcDetailHandItemDto.getContainerCode(), passedAndUnPassedAmount, qcDetailPo.getAmount());
                }
            }
        }

        // 校验合格与不合格数
        if (CollectionUtils.isNotEmpty(dto.getQcUnPassDetailItemList())) {
            // 校验不合格入参的数量都大于0
            dto.getQcUnPassDetailItemList().forEach(qcUnPassDetailItemDto -> {
                if (qcUnPassDetailItemDto.getNotPassAmount() <= 0) {
                    throw new ParamIllegalException("质检次品信息的批次码:{}次品数维护不正确，请重新填写或删除后再提交",
                            qcUnPassDetailItemDto.getSkuBatchCode());
                }
            });

            if (residentQc) {
                return;
            }

            // 校验次品信息容器码
            if (dto.getQcUnPassDetailItemList().stream()
                    .map(QcUnPassDetailItemDto::getContainerCode)
                    .distinct().count() != 1) {
                throw new BizException("质检次品信息的容器编码必须相同，数据错误，请联系系统管理员！");
            }

            // 次品容器
            final String containerCode = dto.getQcUnPassDetailItemList().get(0).getContainerCode();

            // 正品容器与次品容器不能相同
            qcDetailPoList.stream()
                    .filter(qcDetailPo -> QcResult.PASSED.equals(qcDetailPo.getQcResult()))
                    .forEach(qcDetailPo -> {
                        if (containerCode.equals(qcDetailPo.getContainerCode())) {
                            throw new ParamIllegalException("次品容器与正品容器不能相同，请重新选择容器:{}！", containerCode);
                        }
                    });
        }
    }

    /**
     * 校验质检提交DTO中容器编码是否为空。
     *
     * @param dto 质检提交DTO对象
     * @return 如果容器编码不为空则返回 true，否则返回 false
     */
    public void validateContainerCodeNotEmpty(QcSubmitDto dto) {
        List<QcUnPassDetailItemDto> qcUnPassDetailItemList = dto.getQcUnPassDetailItemList();
        if (CollectionUtils.isNotEmpty(qcUnPassDetailItemList)) {
            ParamValidUtils.requireEquals(true,
                    qcUnPassDetailItemList.stream().allMatch(
                            qcUnPassDetailItemDto -> StrUtil.isNotBlank(qcUnPassDetailItemDto.getContainerCode())),
                    "容器编码不能为空");
        }
    }

}
