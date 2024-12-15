package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.qc.dao.QcOnShelvesOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOnShelvesOrderPo;
import com.hete.supply.scm.server.supplier.entity.dto.OnShelvesOrderCreateResultMqDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * QcOnShelvesOrderBaseService 是质检上架单基础服务类，用于保存质检上架单信息并提供去重的幂等性保证。
 * 该类标记为 Spring 服务，使用 Lombok 的 RequiredArgsConstructor 注解自动生成构造函数，并使用 Lombok 的 Log 注解实现日志记录。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QcOnShelvesOrderBaseService {
    private final QcOnShelvesOrderDao qcOnShelvesOrderDao;

    /**
     * 保存质检上架单信息，实现幂等性保证。
     *
     * @param dto 质检上架单的创建结果信息
     */
    public void saveOnShelvesOrderIdempotent(OnShelvesOrderCreateResultMqDto dto) {
        final WmsEnum.OnShelvesOrderCreateResult result = dto.getResult();
        if (Objects.equals(WmsEnum.OnShelvesOrderCreateResult.BUSSINESS_ERROR, result)) {
            return;
        }

        final String onShelvesOrderNo = dto.getOnShelvesOrderNo();
        if (StrUtil.isBlank(onShelvesOrderNo)) {
            throw new ParamIllegalException("保存上架单信息失败，原因：上架单号为空");
        }
        QcOnShelvesOrderPo existQcOnShelvesOrder = qcOnShelvesOrderDao.getOnShelvesOrdersByOnShelvesOrderNo(onShelvesOrderNo);
        if (Objects.nonNull(existQcOnShelvesOrder)) {
            log.info("跳过保存上架单信息，质检单信息已存在，上架单号：{}", onShelvesOrderNo);
            return;
        }

        final Integer planAmount = dto.getPlanAmount();
        final String qcOrderNo = dto.getQcOrderNo();
        final WmsEnum.OnShelvesOrderCreateType onShelvesOrderCreateType = dto.getOnShelvesOrderCreateType();

        QcOnShelvesOrderPo qcOnShelvesOrderPo = new QcOnShelvesOrderPo();
        // 设置上架单号
        qcOnShelvesOrderPo.setOnShelvesOrderNo(onShelvesOrderNo);
        // 设置计划上架数量
        qcOnShelvesOrderPo.setPlanAmount(planAmount);
        // 设置质检单号
        qcOnShelvesOrderPo.setQcOrderNo(qcOrderNo);
        // 设置上架类型
        qcOnShelvesOrderPo.setType(onShelvesOrderCreateType);
        qcOnShelvesOrderDao.insert(qcOnShelvesOrderPo);
    }
}










