package com.hete.supply.scm.server.supplier.purchase.service.biz;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseReturnDto;
import com.hete.supply.scm.api.scm.entity.enums.ReturnOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReturnType;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseReturnExportVo;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.ibfs.service.base.RecoOrderBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseReturnBaseService;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.util.TimeZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/14 00:05
 */
@Service
@RequiredArgsConstructor
public class PurchaseReturnExportService {
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseReturnBaseService purchaseReturnBaseService;
    private final AuthBaseService authBaseService;
    private final RecoOrderBaseService recoOrderBaseService;


    public Integer getExportTotals(PurchaseReturnDto dto) {
        return purchaseReturnOrderDao.getExportTotals(dto);
    }

    /**
     * 退货单导出
     *
     * @param dto
     * @return
     */
    public CommonResult<ExportationListResultBo<PurchaseReturnExportVo>> getExportList(PurchaseReturnDto dto) {
        ExportationListResultBo<PurchaseReturnExportVo> resultBo = new ExportationListResultBo<>();
        dto.setIsExport(true);
        CommonPageResult.PageInfo<PurchaseReturnExportVo> exportList = purchaseReturnOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<PurchaseReturnExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> returnOrderNoList = records.stream().map(PurchaseReturnExportVo::getReturnOrderNo)
                .distinct().collect(Collectors.toList());
        // 获取批次码结算单价
        Map<String, BigDecimal> purchaseReturnSettlePriceMap = recoOrderBaseService.getPurchaseReturnSettlePrice(returnOrderNoList);
        Map<String, PurchaseReturnOrderPo> purchaseReturnOrderPoMap = purchaseReturnOrderDao.getMapByReturnOrderNoList(returnOrderNoList);

        records = records.stream().peek(item -> {
            // 转换退货单状态
            String returnOrderStatus = item.getReturnOrderStatus();
            item.setReturnOrderStatus(ReturnOrderStatus.valueOf(returnOrderStatus).getRemark());

            // 转换退货单类型
            String returnType = item.getReturnType();
            item.setReturnType(ReturnType.valueOf(returnType).getRemark());

            // 转换时间类型
            item.setCreateTimeAsString(ScmTimeUtil.localDateTimeToStr(item.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setReceiptTimeAsString(ScmTimeUtil.localDateTimeToStr(item.getReceiptTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            item.setReturnTimeAsString(ScmTimeUtil.localDateTimeToStr(item.getReturnTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));


            BigDecimal settleRecoOrderPrice = Optional.ofNullable(purchaseReturnSettlePriceMap.get(item.getReturnOrderNo() + item.getSkuBatchCode()))
                    .orElse(BigDecimal.ZERO);
            item.setSettleRecoOrderPrice(settleRecoOrderPrice);
            if (null != item.getReceiptCnt()
                    && purchaseReturnOrderPoMap.containsKey(item.getReturnOrderNo())
                    && ReturnOrderStatus.RECEIPTED.equals(purchaseReturnOrderPoMap.get(item.getReturnOrderNo()).getReturnOrderStatus())) {
                item.setSettleRecoOrderPriceTotal(settleRecoOrderPrice.multiply(new BigDecimal(item.getReceiptCnt())));
            }

        }).collect(Collectors.toList());
        exportList.setRecords(records);


        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    public Integer getSupplierExportTotals(PurchaseReturnDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);

        return this.getExportTotals(dto);
    }

    public CommonResult<ExportationListResultBo<PurchaseReturnExportVo>> getSupplierExportList(PurchaseReturnDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);

        return this.getExportList(dto);
    }
}
