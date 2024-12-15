package com.hete.supply.scm.server.scm.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.DeductOrderPurchaseType;
import com.hete.supply.scm.api.scm.entity.enums.DeductStatus;
import com.hete.supply.scm.api.scm.entity.enums.DeductType;
import com.hete.supply.scm.api.scm.importation.entity.dto.DeductOrderImportationDto;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmFormatUtil;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.common.util.StringUtil;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.settle.config.SettleConfig;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.service.base.DeductOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2023/11/6 18:10
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class DeductOrderImportService {
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final SupplierDao supplierDao;
    private final PlmSkuDao plmSkuDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final IdGenerateService idGenerateService;
    private final DeductOrderDao deductOrderDao;
    private final DeductOrderPurchaseDao deductOrderPurchaseDao;
    private final DeductOrderQualityDao deductOrderQualityDao;
    private final DeductOrderOtherDao deductOrderOtherDao;
    private final DeductOrderPayDao deductOrderPayDao;
    private final DeductOrderBaseService deductOrderBaseService;
    private final SettleConfig settleConfig;

    /**
     * 导入生成扣款单
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/6 18:14
     */
    @Transactional(rollbackFor = Exception.class)
    public void importDeductOrder(DeductOrderImportationDto dto) {
        log.info("批量创建扣款单入参：{}", dto);
        // 校验入参
        if (StringUtils.isBlank(dto.getDeductType())) {
            throw new ParamIllegalException("扣款类型不能为空");
        }
        if (StringUtils.isBlank(dto.getSupplierCode())) {
            throw new ParamIllegalException("供应商代码不能为空");
        }
        if (StringUtils.isBlank(dto.getAboutSettleTime())) {
            throw new ParamIllegalException("约定结算时间不能为空");
        }
        if (!ScmFormatUtil.isLocalDateTimeStandardFormat(dto.getAboutSettleTime(), DatePattern.NORM_DATETIME_PATTERN)) {
            throw new ParamIllegalException("约定结算时间日期格式不正确，请按2023/09/01的日期格式导入");
        }
        if (StringUtils.isBlank(dto.getDeductRemarks())) {
            throw new ParamIllegalException("扣款原因不能为空");
        }
        if (dto.getDeductRemarks().length() > DeductOrderBaseService.MAX_DEDUCT_REMARKS) {
            throw new ParamIllegalException("扣款原因字符长度不能超过 {} 位", DeductOrderBaseService.MAX_DEDUCT_REMARKS);
        }
        if (dto.getDeductPrice() == null) {
            throw new ParamIllegalException("扣款金额不能为空");
        }

        if (dto.getDeductPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("扣款金额必须是大于0的数字");
        }

        if (StringUtils.isNotBlank(dto.getSkuNum()) && !StringUtil.isNumeric(dto.getSkuNum())) {
            throw new ParamIllegalException("数量必须填写正整数数值，请调整后再导入");
        }

        final DeductType deductType = DeductType.getByDesc(dto.getDeductType());
        if (!DeductType.PRICE.equals(deductType)
                && !DeductType.QUALITY.equals(deductType)
                && !DeductType.PAY.equals(deductType)
                && !DeductType.OTHER.equals(deductType)) {
            throw new ParamIllegalException("导入失败，扣款类型填写不正确，请填写{}、{}、{}、{}", DeductType.PRICE.getRemark(),
                    DeductType.QUALITY.getRemark(), DeductType.PAY.getRemark(),
                    DeductType.OTHER.getRemark());
        }

        //价差扣款或品质扣款情况下
        boolean priceQuality = DeductType.PRICE.equals(deductType) || DeductType.QUALITY.equals(deductType);
        if (priceQuality) {
            if (StringUtils.isBlank(dto.getDeductOrderPurchaseType())) {
                throw new ParamIllegalException("扣款类型是{}和{}的单据类型不能为空", DeductType.PRICE.getRemark(), DeductType.QUALITY.getRemark());
            }
            if (StringUtils.isBlank(dto.getBusinessNo())) {
                throw new ParamIllegalException("扣款类型是{}和{}的单据号不能为空", DeductType.PRICE.getRemark(), DeductType.QUALITY.getRemark());
            }
        }

        final DeductOrderPurchaseType deductOrderPurchaseType = DeductOrderPurchaseType.getByDesc(dto.getDeductOrderPurchaseType());
        if (priceQuality
                && !DeductOrderPurchaseType.PRODUCT_PURCHASE.equals(deductOrderPurchaseType)
                && !DeductOrderPurchaseType.PROCESS_PURCHASE.equals(deductOrderPurchaseType)
                && !DeductOrderPurchaseType.PURCHASE_RETURN.equals(deductOrderPurchaseType)
                && !DeductOrderPurchaseType.SAMPLE_RETURN.equals(deductOrderPurchaseType)
                && !DeductOrderPurchaseType.SAMPLE.equals(deductOrderPurchaseType)
                && !DeductOrderPurchaseType.DELIVER.equals(deductOrderPurchaseType)) {
            throw new ParamIllegalException("单据类型填写错误，仅允许填写{}、{}、{}、{}、{}、{}", DeductOrderPurchaseType.PRODUCT_PURCHASE.getRemark(),
                    DeductOrderPurchaseType.PROCESS_PURCHASE.getRemark(), DeductOrderPurchaseType.PURCHASE_RETURN.getRemark(),
                    DeductOrderPurchaseType.SAMPLE_RETURN.getRemark(), DeductOrderPurchaseType.SAMPLE.getRemark(),
                    DeductOrderPurchaseType.DELIVER.getRemark());
        }

        PlmSkuPo plmSkuPo = null;
        if (StringUtils.isNotBlank(dto.getSku())) {
            plmSkuPo = plmSkuDao.getBySku(dto.getSku());
            if (plmSkuPo == null) {
                throw new ParamIllegalException("导入失败，原料sku：{}不存在，请确认后再导入", dto.getSku());
            }
        }

        final LocalDateTime aboutSettleTime = ScmTimeUtil.getLastSecondTimeOfDayForTime(ScmTimeUtil.dateStrToLocalDateTime(dto.getAboutSettleTime(), DatePattern.NORM_DATETIME_PATTERN));
        if (aboutSettleTime == null) {
            throw new ParamIllegalException("导入失败，约定结算时间格式错误，请使用日期格式：yyyy-MM-dd");
        }

        final SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
        if (supplierPo == null) {
            throw new ParamIllegalException("导入失败，供应商代码：{}不存在，请确认后再导入", dto.getSupplierCode());
        }


        // 根据单据类型选择不同的查询
        if (deductOrderPurchaseType != null) {
            switch (deductOrderPurchaseType) {
                case PRODUCT_PURCHASE:
                case PROCESS_PURCHASE:
                    //查询采购子单
                    PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getBusinessNo());
                    Assert.notNull(purchaseChildOrderPo,
                            () -> new ParamIllegalException("导入失败，单据号：{}与单据类型：{}或{}不匹配，请确认后再导入！",
                                    dto.getBusinessNo(), DeductOrderPurchaseType.PRODUCT_PURCHASE.getRemark(), DeductOrderPurchaseType.PROCESS_PURCHASE.getRemark()));
                    break;
                case PURCHASE_RETURN:
                    //查询采购退货单
                    PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderDao.getOneByNo(dto.getBusinessNo());
                    Assert.notNull(purchaseReturnOrderPo,
                            () -> new ParamIllegalException("导入失败，单据号：{}与单据类型：{}不匹配，请确认后再导入！",
                                    dto.getBusinessNo(), DeductOrderPurchaseType.PURCHASE_RETURN.getRemark()));
                    break;
                case SAMPLE_RETURN:
                    //样品退货单
                    SampleReturnOrderPo sampleReturnOrderPo = sampleReturnOrderDao.getOneByNo(dto.getBusinessNo());
                    Assert.notNull(sampleReturnOrderPo,
                            () -> new ParamIllegalException("导入失败，单据号：{}与单据类型：{}不匹配，请确认后再导入！",
                                    dto.getBusinessNo(), DeductOrderPurchaseType.SAMPLE_RETURN.getRemark()));
                    break;
                case SAMPLE:
                    //样品退货单
                    SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(dto.getBusinessNo());
                    Assert.notNull(sampleChildOrderPo,
                            () -> new ParamIllegalException("导入失败，单据号：{}与单据类型：{}不匹配，请确认后再导入！",
                                    dto.getBusinessNo(), DeductOrderPurchaseType.SAMPLE.getRemark()));
                    break;
                case DELIVER:
                    //样品发货单
                    PurchaseDeliverOrderPo purchaseDeliverOrderPo = purchaseDeliverOrderDao.getOneByNo(dto.getBusinessNo());
                    Assert.notNull(purchaseDeliverOrderPo,
                            () -> new ParamIllegalException("导入失败，单据号：{}与单据类型：{}不匹配，请确认后再导入！",
                                    dto.getBusinessNo(), DeductOrderPurchaseType.DELIVER.getRemark()));
                    break;
                default:
                    throw new ParamIllegalException("导入失败，单据类型无效！");
            }
        }

        //创建扣款单数据
        this.insertDeductOrder(dto, supplierPo,
                plmSkuPo, deductType,
                deductOrderPurchaseType,
                aboutSettleTime);

    }

    /**
     * 导入创建扣款单数据
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/7 11:01
     */
    private void insertDeductOrder(DeductOrderImportationDto dto,
                                   SupplierPo supplierPo,
                                   PlmSkuPo plmSkuPo,
                                   DeductType deductType,
                                   DeductOrderPurchaseType deductOrderPurchaseType,
                                   LocalDateTime aboutSettleTime) {
        DeductOrderPo deductOrderPo = new DeductOrderPo();
        String deductOrderNo = idGenerateService.getIncrCode(ScmConstant.DEDUCT_ORDER_NO_PREFIX, TimeType.CN_DAY_YYYY, 4);
        deductOrderPo.setSupplierCode(supplierPo.getSupplierCode());
        deductOrderPo.setSupplierName(supplierPo.getSupplierName());
        deductOrderPo.setDeductOrderNo(deductOrderNo);
        deductOrderPo.setDeductPrice(dto.getDeductPrice());
        deductOrderPo.setDeductType(deductType);
        deductOrderPo.setAboutSettleTime(aboutSettleTime);
        deductOrderPo.setDeductStatus(DeductStatus.WAIT_SUBMIT);
        deductOrderBaseService.updateHandleUser(deductOrderPo, settleConfig);
        deductOrderDao.insert(deductOrderPo);
        // 写入日志
        deductOrderBaseService.createStatusChangeLog(deductOrderPo, null);


        //创建采购明细
        if (DeductType.PRICE.equals(deductOrderPo.getDeductType())) {
            DeductOrderPurchasePo deductOrderPurchasePo = new DeductOrderPurchasePo();
            deductOrderPurchasePo.setDeductOrderId(deductOrderPo.getDeductOrderId());
            deductOrderPurchasePo.setBusinessNo(dto.getBusinessNo());
            deductOrderPurchasePo.setDeductOrderPurchaseType(deductOrderPurchaseType);
            if (plmSkuPo != null) {
                deductOrderPurchasePo.setSku(plmSkuPo.getSku());
                deductOrderPurchasePo.setSpu(plmSkuPo.getSpu());
            }
            if (StringUtils.isNotBlank(dto.getSkuNum())) {
                deductOrderPurchasePo.setSkuNum(Integer.valueOf(dto.getSkuNum()));
            }
            deductOrderPurchasePo.setDeductPrice(dto.getDeductPrice());
            deductOrderPurchasePo.setDeductRemarks(dto.getDeductRemarks());
            deductOrderPurchaseDao.insert(deductOrderPurchasePo);
        }


        //创建品质扣款明细
        if (DeductType.QUALITY.equals(deductOrderPo.getDeductType())) {
            DeductOrderQualityPo deductOrderQualityPo = new DeductOrderQualityPo();
            deductOrderQualityPo.setDeductOrderId(deductOrderPo.getDeductOrderId());
            deductOrderQualityPo.setBusinessNo(dto.getBusinessNo());
            deductOrderQualityPo.setDeductOrderPurchaseType(deductOrderPurchaseType);
            if (plmSkuPo != null) {
                deductOrderQualityPo.setSku(plmSkuPo.getSku());
                deductOrderQualityPo.setSpu(plmSkuPo.getSpu());
            }
            if (StringUtils.isNotBlank(dto.getSkuNum())) {
                deductOrderQualityPo.setSkuNum(Integer.valueOf(dto.getSkuNum()));
            }
            deductOrderQualityPo.setDeductPrice(dto.getDeductPrice());
            deductOrderQualityPo.setDeductRemarks(dto.getDeductRemarks());
            deductOrderQualityDao.insert(deductOrderQualityPo);
        }

        //创建其他明细
        if (DeductType.OTHER.equals(deductOrderPo.getDeductType())) {
            DeductOrderOtherPo deductOrderOtherPo = new DeductOrderOtherPo();
            deductOrderOtherPo.setDeductOrderId(deductOrderPo.getDeductOrderId());
            deductOrderOtherPo.setDeductOrderNo(deductOrderPo.getDeductOrderNo());
            deductOrderOtherPo.setDeductPrice(dto.getDeductPrice());
            deductOrderOtherPo.setDeductRemarks(dto.getDeductRemarks());
            deductOrderOtherDao.insert(deductOrderOtherPo);
        }

        //创建预付款明细
        if (DeductType.PAY.equals(deductOrderPo.getDeductType())) {
            DeductOrderPayPo deductOrderPayPo = new DeductOrderPayPo();
            deductOrderPayPo.setDeductOrderId(deductOrderPo.getDeductOrderId());
            deductOrderPayPo.setDeductOrderNo(deductOrderPo.getDeductOrderNo());
            deductOrderPayPo.setDeductPrice(dto.getDeductPrice());
            deductOrderPayPo.setDeductRemarks(dto.getDeductRemarks());
            deductOrderPayDao.insert(deductOrderPayPo);
        }

    }
}
