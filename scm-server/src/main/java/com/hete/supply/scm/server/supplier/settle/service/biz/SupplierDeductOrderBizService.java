package com.hete.supply.scm.server.supplier.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.supply.scm.server.scm.settle.dao.DeductOrderDao;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.po.DeductOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.supply.scm.server.scm.settle.service.base.DeductOrderBaseService;
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
public class SupplierDeductOrderBizService {
    private final DeductOrderBaseService deductOrderBaseService;
    private final DeductOrderDao deductOrderDao;
    private final AuthBaseService authBaseService;
    private final ConsistencySendMqService consistencySendMqService;

    public CommonPageResult.PageInfo<DeductOrderVo> searchDeductOrder(DeductOrderDto dto) {
        DeductOrderDto deductOrderDto = this.searchDeductOrderSupplierWhere(dto);
        if (null == deductOrderDto) {
            return new CommonPageResult.PageInfo<>();
        }
        return deductOrderBaseService.searchDeductOrder(deductOrderDto);
    }

    public DeductOrderDetailVo getDeductOrderDetail(DeductOrderDetailDto dto) {
        DeductOrderPo deductOrderPo = deductOrderDao.getByDeductOrderNo(dto.getDeductOrderNo());
        if (deductOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        verifyAuth(deductOrderPo.getDeductOrderId());
        return deductOrderBaseService.getDeductOrderDetail(dto);
    }

    public Boolean examine(DeductOrderUpdateDto dto) {
        verifyAuth(dto.getDeductOrderId());
        return deductOrderBaseService.examine(dto);
    }

    /**
     * 验证供应商
     *
     * @author ChenWenLong
     * @date 2022/11/17 16:21
     */
    public void verifyAuth(Long deductOrderId) {
        DeductOrderPo deductOrderPo = deductOrderDao.getById(deductOrderId);
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            throw new BizException("禁止非法请求");
        }
        if (CollectionUtil.isNotEmpty(supplierCodeList) && !supplierCodeList.contains(deductOrderPo.getSupplierCode())) {
            throw new BizException("禁止非法请求");
        }
    }

    /**
     * 扣款单sku导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/7/6 11:12
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSku(DeductOrderDto dto) {
        //供应商权限
        DeductOrderDto deductOrderDto = this.searchDeductOrderSupplierWhere(dto);
        if (null == deductOrderDto) {
            throw new ParamIllegalException("导出数据为空");
        }
        //条件过滤
        if (null == deductOrderBaseService.getSearchDeductOrderWhere(deductOrderDto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportSkuTotals = deductOrderDao.getExportSkuTotals(deductOrderDto);
        Assert.isTrue(null != exportSkuTotals && 0 != exportSkuTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SPM_DEDUCT_ORDER_SKU_EXPORT.getCode(), deductOrderDto));

    }

    /**
     * 搜索增加供应商权限条件
     *
     * @param dto:
     * @return DeductOrderDto
     * @author ChenWenLong
     * @date 2023/7/6 11:16
     */
    public DeductOrderDto searchDeductOrderSupplierWhere(DeductOrderDto dto) {
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        List<DeductStatus> notDeductStatusList = new ArrayList<>();
        notDeductStatusList.add(DeductStatus.WAIT_SUBMIT);
        notDeductStatusList.add(DeductStatus.WAIT_PRICE);
        dto.setAuthSupplierCode(supplierCodeList);
        dto.setNotDeductStatusList(notDeductStatusList);
        List<DeductType> deductTypeList = new ArrayList<>();
        deductTypeList.add(DeductType.PRICE);
        deductTypeList.add(DeductType.QUALITY);
        deductTypeList.add(DeductType.OTHER);
        deductTypeList.add(DeductType.PAY);
        deductTypeList.add(DeductType.DEFECTIVE_RETURN);
        dto.setDeductTypeList(deductTypeList);
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return null;
        }
        return dto;
    }
}
