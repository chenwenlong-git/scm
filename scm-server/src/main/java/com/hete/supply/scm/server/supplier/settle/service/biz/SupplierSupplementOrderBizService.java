package com.hete.supply.scm.server.supplier.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import com.hete.supply.scm.server.scm.settle.dao.SupplementOrderDao;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.SupplementOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
import com.hete.supply.scm.server.scm.settle.service.base.SupplementOrderBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 13:44
 */
@Service
@RequiredArgsConstructor
public class SupplierSupplementOrderBizService {
    private final SupplementOrderBaseService supplementOrderBaseService;
    private final SupplementOrderDao supplementOrderDao;
    private final AuthBaseService authBaseService;
    private final ConsistencySendMqService consistencySendMqService;

    public CommonPageResult.PageInfo<SupplementOrderVo> searchSupplementOrder(SupplementOrderDto dto) {
        SupplementOrderDto supplementOrderDto = this.searchSupplementOrderSupplierWhere(dto);
        if (null == supplementOrderDto) {
            return new CommonPageResult.PageInfo<>();
        }
        return supplementOrderBaseService.searchSupplementOrder(dto);
    }

    public SupplementOrderDetailVo getSupplementOrderDetail(SupplementOrderDetailDto dto) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getBySupplementOrderNo(dto.getSupplementOrderNo());
        if (supplementOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        verifyAuth(supplementOrderPo.getSupplementOrderId());
        return supplementOrderBaseService.getSupplementOrderDetail(dto);
    }

    public Boolean examine(SupplementOrderUpdateDto dto) {
        verifyAuth(dto.getSupplementOrderId());
        return supplementOrderBaseService.examine(dto);
    }

    /**
     * 验证供应商
     *
     * @author ChenWenLong
     * @date 2022/11/17 16:21
     */
    public void verifyAuth(Long supplementOrderId) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getById(supplementOrderId);
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }
        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(supplementOrderPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }
    }

    /**
     * 按sku导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/7/6 14:35
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSku(SupplementOrderDto dto) {
        SupplementOrderDto supplementOrderDto = this.searchSupplementOrderSupplierWhere(dto);
        if (null == supplementOrderDto) {
            throw new ParamIllegalException("导出数据为空");
        }
        //条件过滤
        if (null == supplementOrderBaseService.getSearchSupplementOrderWhere(supplementOrderDto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportSkuTotals = supplementOrderDao.getExportSkuTotals(supplementOrderDto);
        Assert.isTrue(null != exportSkuTotals && 0 != exportSkuTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SPM_SUPPLEMENT_ORDER_SKU_EXPORT.getCode(), supplementOrderDto));

    }

    /**
     * 搜索增加供应商权限条件
     *
     * @param dto:
     * @return SupplementOrderDto
     * @author ChenWenLong
     * @date 2023/7/6 11:16
     */
    public SupplementOrderDto searchSupplementOrderSupplierWhere(SupplementOrderDto dto) {
        // 供应商入参筛选。供应商只能看到价差补款类型
        List<SupplementType> supplementTypeList = new ArrayList<>();
        supplementTypeList.add(SupplementType.PRICE);
        supplementTypeList.add(SupplementType.OTHER);
        dto.setSupplementTypeList(supplementTypeList);
        if (CollectionUtils.isEmpty(dto.getSupplementStatusList())) {
            dto.setSupplementStatusList(SupplementStatus.getSupplierAllStatusList());
        }
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        List<SupplementStatus> notSupplementStatusList = new ArrayList<>();
        notSupplementStatusList.add(SupplementStatus.WAIT_SUBMIT);
        notSupplementStatusList.add(SupplementStatus.WAIT_PRICE);
        dto.setNotSupplementStatusList(notSupplementStatusList);
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return null;
        }
        return dto;
    }

}
