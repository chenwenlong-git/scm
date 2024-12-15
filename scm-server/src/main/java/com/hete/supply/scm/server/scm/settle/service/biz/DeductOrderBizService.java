package com.hete.supply.scm.server.scm.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.DeductOrderExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.entity.bo.DeductSupplementBusinessBo;
import com.hete.supply.scm.server.scm.entity.dto.DeductBusinessSettleDto;
import com.hete.supply.scm.server.scm.entity.vo.DeductSupplementBusinessSettleVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuDropDownVo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.ibfs.service.base.RecoOrderBaseService;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.config.SettleConfig;
import com.hete.supply.scm.server.scm.settle.converter.*;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.dto.*;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderSettleDropDownVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductReturnDropDownVo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.scm.settle.enums.SupplementDeductType;
import com.hete.supply.scm.server.scm.settle.service.base.DeductOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.service.base.AuthBaseService;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2022/11/1 13:44
 */
@Service
@RequiredArgsConstructor
public class DeductOrderBizService {

    private final DeductOrderDao deductOrderDao;
    private final DeductOrderPurchaseDao deductOrderPurchaseDao;
    private final DeductOrderProcessDao deductOrderProcessDao;
    private final IdGenerateService idGenerateService;
    private final DeductOrderQualityDao deductOrderQualityDao;
    private final ScmImageBaseService scmImageBaseService;
    private final DeductOrderBaseService deductOrderBaseService;
    private final PurchaseBaseService purchaseBaseService;
    private final SampleBaseService sampleBaseService;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;
    private final ProcessSettleOrderDao processSettleOrderDao;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessOrderDao processOrderDao;
    private final SupplierDao supplierDao;
    private final AuthBaseService authBaseService;
    private final DeductOrderOtherDao deductOrderOtherDao;
    private final DeductOrderPayDao deductOrderPayDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final DeductOrderDefectiveDao deductOrderDefectiveDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final SampleReturnOrderItemDao sampleReturnOrderItemDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final RecoOrderBaseService recoOrderBaseService;
    private final SettleConfig settleConfig;
    private final UdbRemoteService udbRemoteService;


    public CommonPageResult.PageInfo<DeductOrderVo> searchDeductOrder(DeductOrderDto dto) {
        return deductOrderBaseService.searchDeductOrder(dto);
    }

    /**
     * 查询导出总数
     *
     * @param deductOrderDto
     * @return
     */
    public Integer getExportTotals(DeductOrderQueryByApiDto deductOrderDto) {
        if (StringUtils.isNotBlank(deductOrderDto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(deductOrderDto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.DEDUCT);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return 0;
            }
            List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(deductOrderPoList)) {
                return 0;
            }

            deductOrderDto.setDeductOrderIds(deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(deductOrderDto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(deductOrderDto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return 0;
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.DEDUCT);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return 0;
            }
            List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(processSettleOrderBillPoList.stream().map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(deductOrderPoList)) {
                return 0;
            }

            deductOrderDto.setDeductOrderIds(deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList()));
        }


        return deductOrderDao.getExportTotals(deductOrderDto);

    }

    /**
     * 供应商系统查询导出总数
     *
     * @param dto
     * @return
     */
    public Integer getSupplierExportTotals(DeductOrderQueryByApiDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        List<DeductStatus> notDeductStatusList = new ArrayList<>();
        notDeductStatusList.add(DeductStatus.WAIT_SUBMIT);
        notDeductStatusList.add(DeductStatus.WAIT_PRICE);
        dto.setNotDeductStatusList(notDeductStatusList);
        List<DeductType> deductTypeList = new ArrayList<>();
        deductTypeList.add(DeductType.PRICE);
        deductTypeList.add(DeductType.QUALITY);
        deductTypeList.add(DeductType.OTHER);
        deductTypeList.add(DeductType.PAY);
        deductTypeList.add(DeductType.DEFECTIVE_RETURN);
        dto.setDeductTypeList(deductTypeList);
        return this.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param deductOrderDto
     * @return
     */
    public CommonPageResult.PageInfo<DeductOrderExportVo> getExportList(DeductOrderQueryByApiDto deductOrderDto) {

        if (StringUtils.isNotBlank(deductOrderDto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(deductOrderDto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.DEDUCT);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(deductOrderPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            deductOrderDto.setDeductOrderIds(deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(deductOrderDto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(deductOrderDto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.DEDUCT);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(processSettleOrderBillPoList.stream().map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(deductOrderPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            deductOrderDto.setDeductOrderIds(deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList()));
        }


        CommonPageResult.PageInfo<DeductOrderExportVo> exportList = deductOrderDao.getExportList(PageDTO.of(deductOrderDto.getPageNo(), deductOrderDto.getPageSize(), false), deductOrderDto);
        List<DeductOrderExportVo> records = exportList.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return exportList;
        }
        List<String> deductOrderNos = records.stream().map(DeductOrderExportVo::getDeductOrderNo).collect(Collectors.toList());
        // 获取结算单号
        List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos = purchaseSettleOrderItemDao.getByBusinessNoList(deductOrderNos);
        List<ProcessSettleOrderItemPo> processSettleOrderItemPos = new ArrayList<>();
        List<ProcessSettleOrderBillPo> processSettleOrderBillPos = processSettleOrderBillDao.getByBussinessNos(deductOrderNos);
        if (CollectionUtil.isNotEmpty(processSettleOrderBillPos)) {
            processSettleOrderItemPos = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(processSettleOrderBillPos.stream().map(ProcessSettleOrderBillPo::getProcessSettleOrderItemId).collect(Collectors.toList()));
        }
        List<ProcessSettleOrderItemPo> finalProcessSettleOrderItemPos = processSettleOrderItemPos;
        List<DeductOrderExportVo> newRecords = records.stream().peek(item -> {
            if (CollectionUtil.isNotEmpty(purchaseSettleOrderItemPos)) {
                Optional<PurchaseSettleOrderItemPo> first = purchaseSettleOrderItemPos.stream().filter(it -> it.getBusinessNo().equals(item.getDeductOrderNo())).findFirst();
                if (first.isPresent()) {
                    PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = first.get();
                    item.setSettleOrderNo(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo());
                }
            }
            if (StringUtils.isEmpty(item.getSettleOrderNo()) && CollectionUtil.isNotEmpty(finalProcessSettleOrderItemPos)) {
                Optional<ProcessSettleOrderBillPo> first = processSettleOrderBillPos.stream().filter(it -> it.getBusinessNo().equals(item.getDeductOrderNo())).findFirst();
                if (first.isPresent()) {
                    ProcessSettleOrderBillPo processSettleOrderBillPo = first.get();
                    Optional<ProcessSettleOrderItemPo> first1 = finalProcessSettleOrderItemPos.stream().filter(it -> it.getProcessSettleOrderItemId().equals(processSettleOrderBillPo.getProcessSettleOrderItemId())).findFirst();
                    if (first1.isPresent()) {
                        ProcessSettleOrderItemPo processSettleOrderItemPo = first1.get();
                        item.setSettleOrderNo(processSettleOrderItemPo.getProcessSettleOrderNo());
                    }
                }
            }
        }).collect(Collectors.toList());
        exportList.setRecords(newRecords);
        return exportList;

    }

    /**
     * 供应商系统查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<DeductOrderExportVo> getSupplierExportList(DeductOrderQueryByApiDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        List<DeductStatus> notDeductStatusList = new ArrayList<>();
        notDeductStatusList.add(DeductStatus.WAIT_SUBMIT);
        notDeductStatusList.add(DeductStatus.WAIT_PRICE);
        dto.setNotDeductStatusList(notDeductStatusList);
        List<DeductType> deductTypeList = new ArrayList<>();
        deductTypeList.add(DeductType.PRICE);
        deductTypeList.add(DeductType.QUALITY);
        deductTypeList.add(DeductType.OTHER);
        deductTypeList.add(DeductType.PAY);
        deductTypeList.add(DeductType.DEFECTIVE_RETURN);
        dto.setDeductTypeList(deductTypeList);
        return this.getExportList(dto);
    }

    public DeductOrderDetailVo getDeductOrderDetail(DeductOrderDetailDto dto) {
        return deductOrderBaseService.getDeductOrderDetail(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addDeductOrder(DeductOrderAddDto dto) {
        this.addParameterVerify(dto);

        // 计算扣款总金额
        final List<DeductOrderPurchaseDto> newDeductOrderPurchaseList = dto.getDeductOrderPurchaseList();
        final List<DeductOrderProcessDto> newDeductOrderProcessList = dto.getDeductOrderProcessList();
        final List<DeductOrderQualityDto> newDeductOrderQualityList = dto.getDeductOrderQualityList();
        final List<DeductOrderOtherDto> newDeductOrderOtherList = dto.getDeductOrderOtherList();
        final List<DeductOrderPayDto> newDeductOrderPayList = dto.getDeductOrderPayList();
        BigDecimal deductPrice = this.getDeductPrice(dto.getDeductType(),
                newDeductOrderPurchaseList,
                newDeductOrderProcessList,
                newDeductOrderQualityList,
                newDeductOrderOtherList,
                newDeductOrderPayList,
                Collections.emptyList());

        DeductOrderPo deductOrderPo = DeductOrderConverter.INSTANCE.create(dto);

        if (DeductType.PRICE.equals(dto.getDeductType()) ||
                DeductType.QUALITY.equals(dto.getDeductType()) ||
                DeductType.OTHER.equals(dto.getDeductType()) ||
                DeductType.PAY.equals(dto.getDeductType())) {
            SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
            if (supplierPo == null) {
                throw new ParamIllegalException("供应商查询不到信息");
            }
            deductOrderPo.setSupplierName(supplierPo.getSupplierName());
        }

        // 返回自增序列号，返回格式为key+自增id，其中数字部分最少6位
        String today = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        String deductOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEDUCT_ORDER_NO_PREFIX + today, ConfuseLength.L_4);
        deductOrderPo.setDeductOrderNo(deductOrderNo);
        deductOrderPo.setDeductStatus(DeductStatus.WAIT_SUBMIT);
        deductOrderPo.setDeductPrice(deductPrice);
        deductOrderBaseService.updateHandleUser(deductOrderPo, settleConfig);
        deductOrderDao.insert(deductOrderPo);
        Long deductOrderId = deductOrderPo.getDeductOrderId();

        //创建采购明细
        if (DeductType.PRICE.equals(deductOrderPo.getDeductType())) {
            List<DeductOrderPurchasePo> deductOrderPurchasePos = DeductOrderPurchaseConverter.INSTANCE.create(dto.getDeductOrderPurchaseList());
            List<DeductOrderPurchasePo> deductOrderPurchasePoList = deductOrderPurchasePos.stream().map(item -> item.setDeductOrderId(deductOrderId)).collect(Collectors.toList());
            deductOrderPurchaseDao.insertBatch(deductOrderPurchasePoList);
        }

        //创建加工明细
        if (DeductType.PROCESS.equals(deductOrderPo.getDeductType())) {
            List<DeductOrderProcessDto> deductOrderProcessList = dto.getDeductOrderProcessList();
            List<String> processOrderNos = deductOrderProcessList.stream().map(DeductOrderProcessDto::getProcessOrderNo).collect(Collectors.toList());
            List<ProcessOrderPo> byProcessOrderNos = processOrderDao.getByProcessOrderNos(processOrderNos);
            List<DeductOrderProcessPo> deductOrderProcessPos = DeductOrderProcessConverter.INSTANCE.create(dto.getDeductOrderProcessList());
            List<DeductOrderProcessPo> deductOrderProcessPoList = deductOrderProcessPos.stream().peek(item -> {
                Optional<ProcessOrderPo> first = byProcessOrderNos.stream().filter(item2 -> item2.getProcessOrderNo().equals(item.getProcessOrderNo())).findFirst();
                if (first.isPresent()) {
                    ProcessOrderPo processOrderPo = first.get();
                    item.setProcessOrderId(processOrderPo.getProcessOrderId());
                }
                item.setDeductOrderId(deductOrderId);

            }).collect(Collectors.toList());
            deductOrderProcessDao.insertBatch(deductOrderProcessPoList);
        }

        //创建品质扣款明细
        if (DeductType.QUALITY.equals(deductOrderPo.getDeductType())) {
            List<DeductOrderQualityPo> deductOrderQualityPos = DeductOrderQualityConverter.INSTANCE.create(dto.getDeductOrderQualityList());
            List<DeductOrderQualityPo> deductOrderQualityPoList = deductOrderQualityPos.stream().map(item -> item.setDeductOrderId(deductOrderId)).collect(Collectors.toList());
            deductOrderQualityDao.insertBatch(deductOrderQualityPoList);
        }

        //创建其他明细
        if (DeductType.OTHER.equals(deductOrderPo.getDeductType())) {
            List<DeductOrderOtherPo> deductOrderOtherPos = DeductOrderOtherConverter.INSTANCE.create(dto.getDeductOrderOtherList());
            List<DeductOrderOtherPo> deductOrderOtherPoList = deductOrderOtherPos.stream().peek(item -> {
                item.setDeductOrderId(deductOrderId);
                item.setDeductOrderNo(deductOrderNo);
            }).collect(Collectors.toList());
            deductOrderOtherDao.insertBatch(deductOrderOtherPoList);
        }

        //创建预付款明细
        if (DeductType.PAY.equals(deductOrderPo.getDeductType())) {
            List<DeductOrderPayPo> deductOrderPayPos = DeductOrderPayConverter.INSTANCE.create(dto.getDeductOrderPayList());
            List<DeductOrderPayPo> deductOrderPayPoList = deductOrderPayPos.stream().peek(item -> {
                item.setDeductOrderId(deductOrderId);
                item.setDeductOrderNo(deductOrderNo);
            }).collect(Collectors.toList());
            deductOrderPayDao.insertBatch(deductOrderPayPoList);
        }

        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.DEDUCT_ORDER, deductOrderId);
        scmImageBaseService.insertBatchImage(dto.getDocumentCodeList(), ImageBizType.DEDUCT_ORDER_FILE, deductOrderId);

        // 写入日志
        deductOrderBaseService.createStatusChangeLog(deductOrderPo, null);
        return true;
    }

    private BigDecimal getDeductPrice(DeductType deductType, List<DeductOrderPurchaseDto> deductOrderPurchaseList,
                                      List<DeductOrderProcessDto> deductOrderProcessList,
                                      List<DeductOrderQualityDto> deductOrderQualityList,
                                      List<DeductOrderOtherDto> deductOrderOtherList,
                                      List<DeductOrderPayDto> deductOrderPayList,
                                      List<DeductOrderDefectiveEditDto> deductOrderDefectiveEditList) {
        if (DeductType.PRICE.equals(deductType)) {
            return Optional.ofNullable(deductOrderPurchaseList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(DeductOrderPurchaseDto::getDeductPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (DeductType.PROCESS.equals(deductType)) {
            return Optional.ofNullable(deductOrderProcessList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(DeductOrderProcessDto::getDeductPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (DeductType.QUALITY.equals(deductType)) {
            return Optional.ofNullable(deductOrderQualityList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(DeductOrderQualityDto::getDeductPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (DeductType.OTHER.equals(deductType)) {
            return Optional.ofNullable(deductOrderOtherList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(DeductOrderOtherDto::getDeductPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (DeductType.PAY.equals(deductType)) {
            return Optional.ofNullable(deductOrderPayList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(DeductOrderPayDto::getDeductPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (DeductType.DEFECTIVE_RETURN.equals(deductType)) {
            return Optional.ofNullable(deductOrderDefectiveEditList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(DeductOrderDefectiveEditDto::getDeductPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 增加判断是否填写供应商或扣款员工,并验证入参
     *
     * @author ChenWenLong
     * @date 2022/11/10 12:06
     */
    private void addParameterVerify(DeductOrderAddDto dto) {
        // 判断是否存在重复单据号
        Set<String> businessNoSet = new HashSet<>();
        //价差扣款判断,品质扣款
        if (DeductType.PRICE.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderPurchaseList())) {
                throw new ParamIllegalException("请填写采购扣款明细信息");
            }
            for (DeductOrderPurchaseDto deductOrderPurchaseDto : dto.getDeductOrderPurchaseList()) {
                deductOrderPurchaseVerify(deductOrderPurchaseDto.getDeductOrderPurchaseType(),
                        deductOrderPurchaseDto.getBusinessNo(),
                        deductOrderPurchaseDto.getDeductPrice(),
                        deductOrderPurchaseDto.getDeductRemarks(),
                        businessNoSet);
            }

        }
        //加工扣款
        if (DeductType.PROCESS.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getDeductUsername())) {
                throw new ParamIllegalException("请选择对应扣款员工");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderProcessList())) {
                throw new ParamIllegalException("请填写加工扣款明细信息");
            }
            for (DeductOrderProcessDto deductOrderProcessDto : dto.getDeductOrderProcessList()) {
                deductOrderProcessVerify(deductOrderProcessDto.getProcessOrderNo(),
                        deductOrderProcessDto.getDeductPrice(),
                        deductOrderProcessDto.getDeductRemarks(),
                        businessNoSet);
            }

        }

        //品质扣款判断
        if (DeductType.QUALITY.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderQualityList())) {
                throw new ParamIllegalException("请填写采购扣款明细信息");
            }
            for (DeductOrderQualityDto deductOrderQualityDto : dto.getDeductOrderQualityList()) {
                deductOrderPurchaseVerify(deductOrderQualityDto.getDeductOrderPurchaseType(),
                        deductOrderQualityDto.getBusinessNo(), deductOrderQualityDto.getDeductPrice(),
                        deductOrderQualityDto.getDeductRemarks(), businessNoSet);
            }
        }

        //其他判断
        if (DeductType.OTHER.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderOtherList())) {
                throw new ParamIllegalException("请填写其他明细信息");
            }
            for (DeductOrderOtherDto deductOrderOtherDto : dto.getDeductOrderOtherList()) {
                deductOrderOtherVerify(deductOrderOtherDto.getDeductPrice(), deductOrderOtherDto.getDeductRemarks());
            }
        }

        //预支付判断
        if (DeductType.PAY.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderPayList())) {
                throw new ParamIllegalException("请填写预付款明细信息");
            }
            for (DeductOrderPayDto deductOrderPayDto : dto.getDeductOrderPayList()) {
                deductOrderOtherVerify(deductOrderPayDto.getDeductPrice(), deductOrderPayDto.getDeductRemarks());
            }
        }
        //次品退供判断
        if (DeductType.DEFECTIVE_RETURN.equals(dto.getDeductType())) {
            throw new BizException("禁止创建 {} 类型扣款单", DeductType.DEFECTIVE_RETURN.getRemark());
        }

        List<DeductSupplementBusinessBo> deductSupplementBusinessBoList = new ArrayList<>();
        Optional.ofNullable(dto.getDeductOrderPurchaseList())
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    DeductSupplementBusinessBo deductSupplementBusinessBo = new DeductSupplementBusinessBo();
                    deductSupplementBusinessBo.setDeductOrderPurchaseType(item.getDeductOrderPurchaseType());
                    deductSupplementBusinessBo.setBusinessNo(item.getBusinessNo());
                    deductSupplementBusinessBo.setSku(item.getSku());
                    deductSupplementBusinessBoList.add(deductSupplementBusinessBo);
                });
        Optional.ofNullable(dto.getDeductOrderQualityList())
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    DeductSupplementBusinessBo deductSupplementBusinessBo = new DeductSupplementBusinessBo();
                    deductSupplementBusinessBo.setDeductOrderPurchaseType(item.getDeductOrderPurchaseType());
                    deductSupplementBusinessBo.setBusinessNo(item.getBusinessNo());
                    deductSupplementBusinessBo.setSku(item.getSku());
                    deductSupplementBusinessBoList.add(deductSupplementBusinessBo);
                });

        deductOrderBaseService.verifyDeductSupplementBusiness(deductSupplementBusinessBoList, dto.getSupplierCode());

    }


    @Transactional(rollbackFor = Exception.class)
    public Boolean editDeductOrder(DeductOrderEditDto dto) {
        DeductOrderPo deductOrderPo = deductOrderDao.getByIdVersion(dto.getDeductOrderId(), dto.getVersion());
        if (null == deductOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        this.editParameterVerify(dto, deductOrderPo);
        DeductOrderPo updateDeductOrderPo = DeductOrderConverter.INSTANCE.edit(dto);

        // 计算扣款总金额
        final List<DeductOrderPurchaseDto> newDeductOrderPurchaseList = dto.getDeductOrderPurchaseList();
        final List<DeductOrderProcessDto> newDeductOrderProcessList = dto.getDeductOrderProcessList();
        final List<DeductOrderQualityDto> newDeductOrderQualityList = dto.getDeductOrderQualityList();
        final List<DeductOrderOtherDto> newDeductOrderOtherList = dto.getDeductOrderOtherList();
        final List<DeductOrderPayDto> newDeductOrderPayList = dto.getDeductOrderPayList();
        final List<DeductOrderDefectiveEditDto> newDeductOrderDefectiveEditList = dto.getDeductOrderDefectiveList();
        BigDecimal deductPrice = this.getDeductPrice(dto.getDeductType(),
                newDeductOrderPurchaseList,
                newDeductOrderProcessList,
                newDeductOrderQualityList,
                newDeductOrderOtherList,
                newDeductOrderPayList,
                newDeductOrderDefectiveEditList);
        updateDeductOrderPo.setDeductPrice(deductPrice);

        if (DeductType.PRICE.equals(updateDeductOrderPo.getDeductType())) {
            //删除原来旧数据
            deductOrderPurchaseDao.deleteByDeductOrderId(deductOrderPo.getDeductOrderId());
            //创建采购明细
            List<DeductOrderPurchasePo> deductOrderPurchasePos = DeductOrderPurchaseConverter.INSTANCE.edit(dto.getDeductOrderPurchaseList());
            List<DeductOrderPurchasePo> deductOrderPurchasePoList = deductOrderPurchasePos.stream().map(item -> item.setDeductOrderId(deductOrderPo.getDeductOrderId())).collect(Collectors.toList());
            deductOrderPurchaseDao.insertBatch(deductOrderPurchasePoList);
        }
        if (DeductType.PROCESS.equals(updateDeductOrderPo.getDeductType())) {
            //删除原来旧数据
            deductOrderProcessDao.deleteByDeductOrderId(deductOrderPo.getDeductOrderId());
            //创建加工明细
            List<DeductOrderProcessPo> deductOrderProcessPos = DeductOrderProcessConverter.INSTANCE.edit(dto.getDeductOrderProcessList());
            List<DeductOrderProcessPo> deductOrderProcessPoList = deductOrderProcessPos.stream().map(item -> item.setDeductOrderId(deductOrderPo.getDeductOrderId())).collect(Collectors.toList());
            deductOrderProcessDao.insertBatch(deductOrderProcessPoList);
        }
        if (DeductType.QUALITY.equals(updateDeductOrderPo.getDeductType())) {
            //删除原来旧数据
            deductOrderQualityDao.deleteByDeductOrderId(deductOrderPo.getDeductOrderId());
            //创建品质明细
            List<DeductOrderQualityPo> deductOrderQualityPos = DeductOrderQualityConverter.INSTANCE.edit(dto.getDeductOrderQualityList());
            List<DeductOrderQualityPo> deductOrderQualityPoList = deductOrderQualityPos.stream().map(item -> item.setDeductOrderId(deductOrderPo.getDeductOrderId())).collect(Collectors.toList());
            deductOrderQualityDao.insertBatch(deductOrderQualityPoList);
        }
        if (DeductType.OTHER.equals(updateDeductOrderPo.getDeductType())) {
            //删除原来旧数据
            deductOrderOtherDao.deleteByDeductOrderId(deductOrderPo.getDeductOrderId());
            //创建其他明细
            List<DeductOrderOtherPo> deductOrderOtherPos = DeductOrderOtherConverter.INSTANCE.edit(dto.getDeductOrderOtherList());
            List<DeductOrderOtherPo> deductOrderOtherPoList = deductOrderOtherPos.stream().peek(item -> {
                item.setDeductOrderId(deductOrderPo.getDeductOrderId());
                item.setDeductOrderNo(deductOrderPo.getDeductOrderNo());
            }).collect(Collectors.toList());
            deductOrderOtherDao.insertBatch(deductOrderOtherPoList);
        }
        if (DeductType.PAY.equals(updateDeductOrderPo.getDeductType())) {
            //删除原来旧数据
            deductOrderPayDao.deleteByDeductOrderId(deductOrderPo.getDeductOrderId());
            //创建预付款明细
            List<DeductOrderPayPo> deductOrderPayPos = DeductOrderPayConverter.INSTANCE.edit(dto.getDeductOrderPayList());
            List<DeductOrderPayPo> deductOrderPayPoList = deductOrderPayPos.stream().peek(item -> {
                item.setDeductOrderId(deductOrderPo.getDeductOrderId());
                item.setDeductOrderNo(deductOrderPo.getDeductOrderNo());
            }).collect(Collectors.toList());
            deductOrderPayDao.insertBatch(deductOrderPayPoList);
        }

        //修改次品退供单价
        if (DeductType.DEFECTIVE_RETURN.equals(updateDeductOrderPo.getDeductType())) {
            updateDeductOrderPo.setDeductStatus(DeductStatus.WAIT_EXAMINE);
            deductOrderBaseService.updateHandleUser(updateDeductOrderPo, settleConfig);
            deductOrderDefectiveDao.updateBatchByIdVersion(DeductOrderDefectiveConverter.INSTANCE.edit(newDeductOrderDefectiveEditList));
        }

        //更新扣款单数据
        deductOrderDao.updateByIdVersion(updateDeductOrderPo);

        //获取凭证
        List<Long> longs = List.of(dto.getDeductOrderId());
        List<String> scmImagePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEDUCT_ORDER, longs);
        scmImageBaseService.editImage(dto.getFileCodeList(), scmImagePos, ImageBizType.DEDUCT_ORDER, dto.getDeductOrderId());
        List<String> scmFilePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEDUCT_ORDER_FILE, longs);
        scmImageBaseService.editImage(dto.getDocumentCodeList(), scmFilePos, ImageBizType.DEDUCT_ORDER_FILE, dto.getDeductOrderId());
        return true;
    }

    /**
     * 编辑判断是否填写供应商或扣款员工,并验证入参
     *
     * @author ChenWenLong
     * @date 2022/11/10 12:06
     */
    private void editParameterVerify(DeductOrderEditDto dto, DeductOrderPo deductOrderPo) {
        // 判断是否存在重复单据号
        Set<String> businessNoSet = new HashSet<>();
        //次品退供判断
        if (DeductType.DEFECTIVE_RETURN.equals(dto.getDeductType())) {
            if (deductOrderPo.getDeductStatus() != DeductStatus.REFUSED) {
                throw new ParamIllegalException("状态处于{}时才能进行编辑", DeductStatus.REFUSED.getRemark());
            }
            for (DeductOrderDefectiveEditDto defectiveDto : dto.getDeductOrderDefectiveList()) {
                Assert.notNull(defectiveDto.getDeductUnitPrice(), () -> new BizException("扣款单价不能为空，请填写后重试！"));
                Assert.notNull(defectiveDto.getDeductNum(), () -> new BizException("扣款数量不能为空，请填写后重试！"));
                Assert.notNull(defectiveDto.getDeductPrice(), () -> new BizException("扣款总价不能为空，请填写后重试！"));
                if (0 != (defectiveDto.getDeductUnitPrice().multiply(new BigDecimal(defectiveDto.getDeductNum())).compareTo(defectiveDto.getDeductPrice()))) {
                    throw new BizException("扣款总价:{} 计算错误!", defectiveDto.getDeductPrice());
                }
            }
        } else {
            if (deductOrderPo.getDeductStatus() != DeductStatus.WAIT_SUBMIT) {
                throw new ParamIllegalException("状态处于{}时才能进行编辑", DeductStatus.WAIT_SUBMIT.getRemark());
            }
            if (null == dto.getAboutSettleTime()) {
                throw new ParamIllegalException("约定结算时间不能为空");
            }
        }
        //价差扣款判断,品质扣款
        if (DeductType.PRICE.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderPurchaseList())) {
                throw new ParamIllegalException("请填写采购扣款明细信息");
            }
            for (DeductOrderPurchaseDto deductOrderPurchaseDto : dto.getDeductOrderPurchaseList()) {
                deductOrderPurchaseVerify(deductOrderPurchaseDto.getDeductOrderPurchaseType(),
                        deductOrderPurchaseDto.getBusinessNo(),
                        deductOrderPurchaseDto.getDeductPrice(),
                        deductOrderPurchaseDto.getDeductRemarks(),
                        businessNoSet);
            }
        }
        //加工扣款判断
        if (DeductType.PROCESS.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getDeductUsername())) {
                throw new ParamIllegalException("请选择对应扣款员工");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderProcessList())) {
                throw new ParamIllegalException("请填写加工扣款明细信息");
            }
            for (DeductOrderProcessDto deductOrderProcessDto : dto.getDeductOrderProcessList()) {
                deductOrderProcessVerify(deductOrderProcessDto.getProcessOrderNo(),
                        deductOrderProcessDto.getDeductPrice(),
                        deductOrderProcessDto.getDeductRemarks(),
                        businessNoSet);
            }

        }
        //品质扣款判断
        if (DeductType.QUALITY.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderQualityList())) {
                throw new ParamIllegalException("请填写品质扣款明细信息");
            }
            for (DeductOrderQualityDto deductOrderQualityDto : dto.getDeductOrderQualityList()) {
                deductOrderPurchaseVerify(deductOrderQualityDto.getDeductOrderPurchaseType(),
                        deductOrderQualityDto.getBusinessNo(),
                        deductOrderQualityDto.getDeductPrice(),
                        deductOrderQualityDto.getDeductRemarks(),
                        businessNoSet);
            }
        }
        //其他判断
        if (DeductType.OTHER.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderOtherList())) {
                throw new ParamIllegalException("请填写其他明细信息");
            }
            for (DeductOrderOtherDto deductOrderOtherDto : dto.getDeductOrderOtherList()) {
                deductOrderOtherVerify(deductOrderOtherDto.getDeductPrice(), deductOrderOtherDto.getDeductRemarks());
            }
        }

        //预支付判断
        if (DeductType.PAY.equals(dto.getDeductType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getDeductOrderPayList())) {
                throw new ParamIllegalException("请填写预支付明细信息");
            }
            for (DeductOrderPayDto deductOrderPayDto : dto.getDeductOrderPayList()) {
                deductOrderOtherVerify(deductOrderPayDto.getDeductPrice(), deductOrderPayDto.getDeductRemarks());
            }
        }

        List<DeductSupplementBusinessBo> deductSupplementBusinessBoList = new ArrayList<>();
        Optional.ofNullable(dto.getDeductOrderPurchaseList())
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    DeductSupplementBusinessBo deductSupplementBusinessBo = new DeductSupplementBusinessBo();
                    deductSupplementBusinessBo.setDeductOrderPurchaseType(item.getDeductOrderPurchaseType());
                    deductSupplementBusinessBo.setBusinessNo(item.getBusinessNo());
                    deductSupplementBusinessBo.setSku(item.getSku());
                    deductSupplementBusinessBoList.add(deductSupplementBusinessBo);
                });
        Optional.ofNullable(dto.getDeductOrderQualityList())
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    DeductSupplementBusinessBo deductSupplementBusinessBo = new DeductSupplementBusinessBo();
                    deductSupplementBusinessBo.setDeductOrderPurchaseType(item.getDeductOrderPurchaseType());
                    deductSupplementBusinessBo.setBusinessNo(item.getBusinessNo());
                    deductSupplementBusinessBo.setSku(item.getSku());
                    deductSupplementBusinessBoList.add(deductSupplementBusinessBo);
                });

        deductOrderBaseService.verifyDeductSupplementBusiness(deductSupplementBusinessBoList, dto.getSupplierCode());


    }

    /**
     * 入参详情中采购数据验证
     *
     * @author ChenWenLong
     * @date 2022/11/15 13:56
     */
    private void deductOrderPurchaseVerify(DeductOrderPurchaseType deductOrderPurchaseType,
                                           String businessNo,
                                           BigDecimal deductPrice,
                                           String deductRemarks,
                                           Set<String> businessNoSet) {
        if (null == deductOrderPurchaseType) {
            throw new ParamIllegalException("请填写单据类型");
        }
        if (StringUtils.isBlank(businessNo)) {
            throw new ParamIllegalException("请填写单据号");
        }
        if (businessNo.length() > 32) {
            throw new ParamIllegalException("单据号字符长度不能超过 32 位");
        }
        if (null == deductPrice) {
            throw new ParamIllegalException("请填写扣款金额");
        }
        if (deductPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("请填写扣款金额不能小于等于0");
        }
        if (StringUtils.isBlank(deductRemarks)) {
            throw new ParamIllegalException("请填写扣款原因");
        }
        if (deductRemarks.length() > DeductOrderBaseService.MAX_DEDUCT_REMARKS) {
            throw new ParamIllegalException("扣款原因字符长度不能超过 {} 位", DeductOrderBaseService.MAX_DEDUCT_REMARKS);
        }
        if (!businessNoSet.add(businessNo)) {
            throw new ParamIllegalException("单据号:{}存在重复，请修改单据号！", businessNo);
        }
    }

    /**
     * 入参详情中加工数据验证
     *
     * @author ChenWenLong
     * @date 2022/11/15 13:56
     */
    private void deductOrderProcessVerify(String processOrderNo,
                                          BigDecimal deductPrice,
                                          String deductRemarks,
                                          Set<String> businessNoSet) {
        if (StringUtils.isBlank(processOrderNo)) {
            throw new ParamIllegalException("请填写加工单号");
        }
        if (processOrderNo.length() > 32) {
            throw new ParamIllegalException("加工单号字符长度不能超过 32 位");
        }
        if (null == deductPrice) {
            throw new ParamIllegalException("请填写扣款金额");
        }
        if (deductPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("请填写扣款金额不能小于等于0");
        }
        if (StringUtils.isBlank(deductRemarks)) {
            throw new ParamIllegalException("请填写扣款原因");
        }
        if (deductRemarks.length() > DeductOrderBaseService.MAX_DEDUCT_REMARKS) {
            throw new ParamIllegalException("扣款原因字符长度不能超过 {} 位", DeductOrderBaseService.MAX_DEDUCT_REMARKS);
        }
        if (!businessNoSet.add(processOrderNo)) {
            throw new ParamIllegalException("单据号:{}存在重复，请修改单据号！", processOrderNo);
        }
    }

    /**
     * 入参详情中其他或预付款数据验证
     *
     * @author ChenWenLong
     * @date 2023/2/24 10:45
     */
    private void deductOrderOtherVerify(BigDecimal deductPrice, String deductRemarks) {
        if (null == deductPrice) {
            throw new ParamIllegalException("请填写扣款金额");
        }
        if (deductPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("请填写扣款金额不能小于等于0");
        }
        if (StringUtils.isBlank(deductRemarks)) {
            throw new ParamIllegalException("请填写扣款原因");
        }
        if (deductRemarks.length() > DeductOrderBaseService.MAX_DEDUCT_REMARKS) {
            throw new ParamIllegalException("扣款原因字符长度不能超过 {} 位", DeductOrderBaseService.MAX_DEDUCT_REMARKS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean update(DeductOrderVersionDto dto) {
        return true;
    }

    public Boolean examine(DeductOrderUpdateDto dto) {
        return deductOrderBaseService.examine(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(DeductOrderVersionDto dto) {
        DeductOrderPo deductOrderPo = deductOrderDao.getByIdVersion(dto.getDeductOrderId(), dto.getVersion());
        if (deductOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        DeductStatus deductStatus = deductOrderPo.getDeductStatus();
        if (deductStatus != DeductStatus.WAIT_SUBMIT) {
            throw new ParamIllegalException("状态处于{}才能进行删除", DeductStatus.WAIT_SUBMIT.getRemark());
        }
        deductOrderDao.delete(dto.getDeductOrderId());
        return true;
    }

    public List<DeductReturnDropDownVo> getDeductReturnDropDown(DeductReturnDropDownDto dto) {
        List<DeductReturnDropDownVo> deductReturnDropDownVoList = new ArrayList<>();
        List<ReceiptOrderStatus> receiptOrderStatusNotList = new ArrayList<>();
        receiptOrderStatusNotList.add(ReceiptOrderStatus.WAIT_RECEIVE);
        switch (dto.getDeductOrderPurchaseType()) {
            case PURCHASE_RETURN:
                List<PurchaseReturnOrderPo> purchaseReturnOrderPos = purchaseBaseService.getListLikeByPurchaseReturnOrderNo(dto.getBusinessNo(), dto.getSupplierCode(), receiptOrderStatusNotList);
                if (CollectionUtil.isNotEmpty(purchaseReturnOrderPos)) {
                    for (PurchaseReturnOrderPo item : purchaseReturnOrderPos) {
                        DeductReturnDropDownVo deductReturnDropDownVo = new DeductReturnDropDownVo();
                        deductReturnDropDownVo.setBusinessNo(item.getReturnOrderNo());
                        deductReturnDropDownVoList.add(deductReturnDropDownVo);
                    }
                }
                break;
            case SAMPLE_RETURN:
                List<SampleReturnOrderPo> sampleReturnOrderPos = sampleBaseService.getBySampleReturnOrderNo(dto.getBusinessNo(), dto.getSupplierCode(), receiptOrderStatusNotList);
                if (CollectionUtil.isNotEmpty(sampleReturnOrderPos)) {
                    for (SampleReturnOrderPo item : sampleReturnOrderPos) {
                        DeductReturnDropDownVo deductReturnDropDownVo = new DeductReturnDropDownVo();
                        deductReturnDropDownVo.setBusinessNo(item.getSampleReturnOrderNo());
                        deductReturnDropDownVoList.add(deductReturnDropDownVo);
                    }
                }
                break;
            case PRODUCT_PURCHASE:
            case PROCESS_PURCHASE:
                final DeductOrderPurchaseType deductOrderPurchaseType = dto.getDeductOrderPurchaseType();
                PurchaseBizType purchaseBizType = deductOrderPurchaseType.equals(DeductOrderPurchaseType.PRODUCT_PURCHASE) ?
                        PurchaseBizType.PRODUCT : PurchaseBizType.PROCESS;
                List<PurchaseOrderStatus> purchaseOrderStatusNotList = PurchaseOrderStatus.getSupplementDeductOrderNotStatus();
                List<PurchaseChildOrderPo> purchaseChildOrderPo = purchaseBaseService.getListLikeByChildNoAndType(dto.getBusinessNo(), purchaseBizType, dto.getSupplierCode(), purchaseOrderStatusNotList);
                if (CollectionUtil.isNotEmpty(purchaseChildOrderPo)) {
                    for (PurchaseChildOrderPo item : purchaseChildOrderPo) {
                        DeductReturnDropDownVo deductReturnDropDownVo = new DeductReturnDropDownVo();
                        deductReturnDropDownVo.setBusinessNo(item.getPurchaseChildOrderNo());
                        deductReturnDropDownVo.setSettlePrice(item.getTotalSettlePrice());
                        deductReturnDropDownVoList.add(deductReturnDropDownVo);
                    }
                }
                break;
            case SAMPLE:
                List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByLikeNo(dto.getBusinessNo(), dto.getSupplierCode(), List.of(DevelopSampleStatus.WAIT_RECEIVE, DevelopSampleStatus.ON_SHELVES));
                if (CollectionUtil.isNotEmpty(developSampleOrderPoList)) {
                    for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                        DeductReturnDropDownVo deductReturnDropDownVo = new DeductReturnDropDownVo();
                        deductReturnDropDownVo.setBusinessNo(developSampleOrderPo.getDevelopSampleOrderNo());
                        deductReturnDropDownVo.setSettlePrice(developSampleOrderPo.getSkuBatchSamplePrice());
                        deductReturnDropDownVoList.add(deductReturnDropDownVo);
                    }
                }
                break;
            case DELIVER:
                List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByStatus(List.of(DeliverOrderStatus.WAREHOUSED),
                        List.of(dto.getSupplierCode()), null, null, dto.getBusinessNo());
                if (CollectionUtil.isNotEmpty(purchaseDeliverOrderPoList)) {
                    List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
                    Map<String, BigDecimal> purchaseChildOrderPriceMap = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList)
                            .stream()
                            .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getTotalSettlePrice));
                    for (PurchaseDeliverOrderPo item : purchaseDeliverOrderPoList) {
                        DeductReturnDropDownVo deductReturnDropDownVo = new DeductReturnDropDownVo();
                        deductReturnDropDownVo.setBusinessNo(item.getPurchaseDeliverOrderNo());
                        deductReturnDropDownVo.setSettlePrice(purchaseChildOrderPriceMap.get(item.getPurchaseChildOrderNo()));
                        deductReturnDropDownVoList.add(deductReturnDropDownVo);
                    }
                }
                break;
            default:
                throw new ParamIllegalException("请求单据类型错误！");
        }
        return deductReturnDropDownVoList;
    }

    public List<SkuDropDownVo> getSkuDropDown(DeductSkuDropDownDto dto) {
        List<SkuDropDownVo> skuDropDownVoList = new ArrayList<>();
        Map<DeductOrderPurchaseType, List<DeductSkuDropDownDto.SkuDropDownItemDto>> dtoTypeMap = dto.getSkuDropDownItemList()
                .stream()
                .collect(Collectors.groupingBy(DeductSkuDropDownDto.SkuDropDownItemDto::getDeductOrderPurchaseType));

        //采购查询详情
        List<DeductSkuDropDownDto.SkuDropDownItemDto> productPurchaseList = new ArrayList<>();
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(DeductOrderPurchaseType.PRODUCT_PURCHASE, new ArrayList<>()));
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(DeductOrderPurchaseType.PROCESS_PURCHASE, new ArrayList<>()));
        if (CollectionUtils.isNotEmpty(productPurchaseList)) {
            List<String> childNoList = productPurchaseList.stream().map(DeductSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
            List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(childNoList);
            Map<String, PurchaseChildOrderPo> purchaseChildOrderPoMap = purchaseChildOrderPoList.stream()
                    .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPos = purchaseChildOrderItemDao.getListByChildNoList(childNoList);
            purchaseChildOrderItemPos.stream()
                    .collect(Collectors.groupingBy(PurchaseChildOrderItemPo::getPurchaseChildOrderNo))
                    .forEach((key, value) -> {
                        SkuDropDownVo skuDropDownVo = new SkuDropDownVo();
                        skuDropDownVo.setBusinessNo(key);
                        skuDropDownVo.setSkuDropDownItemList(value.stream().map(item -> {
                            SkuDropDownVo.SkuDropDownItemVo itemVo = new SkuDropDownVo.SkuDropDownItemVo();
                            itemVo.setSku(item.getSku());
                            PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderPoMap.get(key);
                            if (purchaseChildOrderPo != null) {
                                itemVo.setSpu(purchaseChildOrderPo.getSpu());
                                itemVo.setSettlePrice(purchaseChildOrderPo.getTotalSettlePrice());
                            }
                            return itemVo;
                        }).collect(Collectors.toList()));
                        skuDropDownVoList.add(skuDropDownVo);
                    });
        }

        //新样品查询
        List<DeductSkuDropDownDto.SkuDropDownItemDto> sampleList = dtoTypeMap.get(DeductOrderPurchaseType.SAMPLE);
        if (CollectionUtils.isNotEmpty(sampleList)) {
            List<String> sampleNoList = sampleList.stream().map(DeductSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoListAndSupplierCode(sampleNoList, null, List.of(DevelopSampleStatus.WAIT_RECEIVE, DevelopSampleStatus.ON_SHELVES));
            for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                SkuDropDownVo skuDropDownVo = new SkuDropDownVo();
                skuDropDownVo.setBusinessNo(developSampleOrderPo.getDevelopSampleOrderNo());
                SkuDropDownVo.SkuDropDownItemVo itemVo = new SkuDropDownVo.SkuDropDownItemVo();
                itemVo.setSku(developSampleOrderPo.getSku());
                itemVo.setSpu(developSampleOrderPo.getSpu());
                itemVo.setSkuBatchCode(developSampleOrderPo.getSkuBatchCode());
                itemVo.setSettlePrice(developSampleOrderPo.getSkuBatchSamplePrice());
                skuDropDownVo.setSkuDropDownItemList(List.of(itemVo));
                skuDropDownVoList.add(skuDropDownVo);
            }
        }

        //发货单查询详情
        List<DeductSkuDropDownDto.SkuDropDownItemDto> deliverList = dtoTypeMap.get(DeductOrderPurchaseType.DELIVER);
        if (CollectionUtils.isNotEmpty(deliverList)) {
            List<String> deliverNoList = deliverList.stream().map(DeductSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(deliverNoList);

            // 查询采购单信息
            List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoList(deliverNoList);
            List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream()
                    .map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPoList = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList);
            Map<String, BigDecimal> purchaseChildOrderPriceMap = purchaseChildOrderItemPoList.stream()
                    .collect(Collectors.toMap(purchaseChildOrderItemPo -> purchaseChildOrderItemPo.getPurchaseChildOrderNo() + purchaseChildOrderItemPo.getSkuBatchCode(),
                            PurchaseChildOrderItemPo::getSettlePrice,
                            (existingValue, newValue) -> existingValue));

            for (PurchaseDeliverOrderPo purchaseDeliverOrderPo : purchaseDeliverOrderPoList) {
                SkuDropDownVo skuDropDownVo = new SkuDropDownVo();
                skuDropDownVo.setBusinessNo(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo());
                List<SkuDropDownVo.SkuDropDownItemVo> skuDropDownItemList = new ArrayList<>();
                purchaseDeliverOrderItemPos.stream()
                        .filter(itemPo -> itemPo.getPurchaseDeliverOrderNo().equals(purchaseDeliverOrderPo.getPurchaseDeliverOrderNo()))
                        .forEach(itemPo -> {
                            SkuDropDownVo.SkuDropDownItemVo skuDropDownItemVo = new SkuDropDownVo.SkuDropDownItemVo();
                            if (StringUtils.isNotBlank(purchaseDeliverOrderPo.getPurchaseChildOrderNo())
                                    && StringUtils.isNotBlank(itemPo.getSkuBatchCode())
                                    && purchaseChildOrderPriceMap.containsKey(purchaseDeliverOrderPo.getPurchaseChildOrderNo() + itemPo.getSkuBatchCode())) {
                                BigDecimal settlePrice = purchaseChildOrderPriceMap.get(purchaseDeliverOrderPo.getPurchaseChildOrderNo() + itemPo.getSkuBatchCode());
                                skuDropDownItemVo.setSettleUnitPrice(settlePrice);
                            }
                            if (null != skuDropDownItemVo.getSettleUnitPrice() && null != itemPo.getQualityGoodsCnt()) {
                                skuDropDownItemVo.setSettlePrice(skuDropDownItemVo.getSettleUnitPrice().multiply(new BigDecimal(itemPo.getQualityGoodsCnt())));
                            }
                            skuDropDownItemVo.setReceiveAmount(itemPo.getQualityGoodsCnt());
                            skuDropDownItemVo.setSku(itemPo.getSku());
                            skuDropDownItemVo.setSpu(itemPo.getSpu());
                            skuDropDownItemVo.setSkuBatchCode(itemPo.getSkuBatchCode());
                            skuDropDownItemList.add(skuDropDownItemVo);
                        });
                skuDropDownVo.setSkuDropDownItemList(skuDropDownItemList);
                skuDropDownVoList.add(skuDropDownVo);
            }
        }

        //样品退货单查询详情
        List<DeductSkuDropDownDto.SkuDropDownItemDto> sampleReturnList = dtoTypeMap.get(DeductOrderPurchaseType.SAMPLE_RETURN);
        if (CollectionUtils.isNotEmpty(sampleReturnList)) {
            List<String> sampleReturnNoList = sampleReturnList.stream().map(DeductSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
            List<SampleReturnOrderItemPo> sampleReturnOrderItemPos = sampleReturnOrderItemDao.getListBySampleReturnOrderNoList(sampleReturnNoList);
            sampleReturnOrderItemPos.stream()
                    .collect(Collectors.groupingBy(SampleReturnOrderItemPo::getSampleReturnOrderNo))
                    .forEach((key, value) -> {
                        SkuDropDownVo skuDropDownVo = new SkuDropDownVo();
                        skuDropDownVo.setBusinessNo(key);
                        skuDropDownVo.setSkuDropDownItemList(value.stream().map(item -> {
                            SkuDropDownVo.SkuDropDownItemVo itemVo = new SkuDropDownVo.SkuDropDownItemVo();
                            itemVo.setSpu(item.getSpu());
                            return itemVo;
                        }).collect(Collectors.toList()));
                        skuDropDownVoList.add(skuDropDownVo);
                    });
        }

        //采购退货单查询详情
        List<DeductSkuDropDownDto.SkuDropDownItemDto> purchaseReturnList = dtoTypeMap.get(DeductOrderPurchaseType.PURCHASE_RETURN);
        if (CollectionUtils.isNotEmpty(purchaseReturnList)) {
            List<String> returnNoList = purchaseReturnList.stream().map(DeductSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
            // 获取批次码结算单价
            Map<String, BigDecimal> purchaseReturnSettlePriceMap = recoOrderBaseService.getPurchaseReturnSettlePrice(returnNoList);
            List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPos = purchaseReturnOrderItemDao.getListByReturnNoList(returnNoList);
            List<String> purchaseReturnSkuList = purchaseReturnOrderItemPos.stream()
                    .map(PurchaseReturnOrderItemPo::getSku)
                    .distinct()
                    .collect(Collectors.toList());
            Map<String, String> skuSpuMap = plmRemoteService.getSpuMapBySkuList(purchaseReturnSkuList);
            purchaseReturnOrderItemPos.stream()
                    .collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getReturnOrderNo))
                    .forEach((key, valuePoList) -> {
                        SkuDropDownVo skuDropDownVo = new SkuDropDownVo();
                        skuDropDownVo.setBusinessNo(key);

                        Map<String, List<PurchaseReturnOrderItemPo>> skuBatchCodeMap = valuePoList.stream().collect(Collectors.groupingBy(PurchaseReturnOrderItemPo::getSkuBatchCode));

                        // 赋值VO
                        List<SkuDropDownVo.SkuDropDownItemVo> skuDropDownItemList = new ArrayList<>();
                        skuBatchCodeMap.forEach((skuBatchCode, itemPoList) -> {
                            BigDecimal settlePrice = BigDecimal.ZERO;
                            for (PurchaseReturnOrderItemPo purchaseReturnOrderItemPo : itemPoList) {
                                BigDecimal settleRecoOrderPrice = Optional.ofNullable(purchaseReturnSettlePriceMap.get(purchaseReturnOrderItemPo.getReturnOrderNo() + purchaseReturnOrderItemPo.getSkuBatchCode()))
                                        .orElse(BigDecimal.ZERO);
                                if (null != purchaseReturnOrderItemPo.getReceiptCnt()) {
                                    settlePrice = settlePrice.add(settleRecoOrderPrice.multiply(new BigDecimal(purchaseReturnOrderItemPo.getReceiptCnt())));
                                }
                            }
                            SkuDropDownVo.SkuDropDownItemVo itemVo = new SkuDropDownVo.SkuDropDownItemVo();
                            PurchaseReturnOrderItemPo purchaseReturnOrderItemPo = valuePoList.stream()
                                    .filter(po -> skuBatchCode.equals(po.getSkuBatchCode()))
                                    .findFirst()
                                    .orElse(null);
                            if (null != purchaseReturnOrderItemPo) {
                                itemVo.setSku(purchaseReturnOrderItemPo.getSku());
                                itemVo.setSpu(skuSpuMap.get(itemVo.getSku()));
                            }
                            itemVo.setSettlePrice(settlePrice);
                            itemVo.setSkuBatchCode(skuBatchCode);
                            skuDropDownItemList.add(itemVo);

                        });

                        skuDropDownVo.setSkuDropDownItemList(skuDropDownItemList);
                        skuDropDownVoList.add(skuDropDownVo);
                    });
        }

        return skuDropDownVoList;
    }

    public List<DeductOrderSettleDropDownVo> getSettleOrderDropDown(DeductOrderSettleDropDownDto dto) {
        DeductOrderPo deductOrderPo = deductOrderDao.getByDeductOrderNo(dto.getDeductOrderNo());
        if (deductOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<DeductOrderSettleDropDownVo> list = new ArrayList<>();
        //针对采购类型进行查询采购结算单
        if (deductOrderPo.getDeductType().equals(DeductType.PRICE) ||
                deductOrderPo.getDeductType().equals(DeductType.QUALITY) ||
                deductOrderPo.getDeductType().equals(DeductType.OTHER) ||
                deductOrderPo.getDeductType().equals(DeductType.PAY) ||
                deductOrderPo.getDeductType().equals(DeductType.DEFECTIVE_RETURN)
        ) {
            List<PurchaseSettleOrderPo> purchaseSettleOrderPoList = purchaseSettleOrderDao.getListBySupplierCode(deductOrderPo.getSupplierCode(), dto.getSettleOrderNo(), PurchaseSettleStatus.WAIT_CONFIRM);
            for (PurchaseSettleOrderPo purchaseSettleOrderPo : purchaseSettleOrderPoList) {
                DeductOrderSettleDropDownVo deductOrderSettleDropDownVo = new DeductOrderSettleDropDownVo();
                deductOrderSettleDropDownVo.setSettleOrderNo(purchaseSettleOrderPo.getPurchaseSettleOrderNo());
                list.add(deductOrderSettleDropDownVo);
            }
        }

        //通过加工类型进行查询加工结算单
        if (deductOrderPo.getDeductType().equals(DeductType.PROCESS)) {
            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getListByCompleteUser(deductOrderPo.getDeductUser(), dto.getSettleOrderNo());

            List<String> processSettleOrderNoList = processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderNo).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(processSettleOrderNoList)) {
                List<ProcessSettleOrderPo> processSettleOrderPoList = processSettleOrderDao.getByProcessSettleOrderNos(processSettleOrderNoList, ProcessSettleStatus.WAIT_SETTLE);
                for (ProcessSettleOrderPo processSettleOrderPo : processSettleOrderPoList) {
                    DeductOrderSettleDropDownVo deductOrderSettleDropDownVo = new DeductOrderSettleDropDownVo();
                    deductOrderSettleDropDownVo.setSettleOrderNo(processSettleOrderPo.getProcessSettleOrderNo());
                    list.add(deductOrderSettleDropDownVo);
                }
            }

        }

        return list;

    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addSettleOrder(DeductOrderAddSettleOrderDto dto) {
        DeductOrderPo deductOrderPo = deductOrderDao.getByDeductOrderNo(dto.getDeductOrderNo(), dto.getVersion());
        if (deductOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (!DeductStatus.AUDITED.equals(deductOrderPo.getDeductStatus())) {
            throw new ParamIllegalException("扣款单处于{}状态时，才能进行操作", DeductStatus.AUDITED.getRemark());
        }

        if (deductOrderPo.getDeductType().equals(DeductType.PRICE) ||
                deductOrderPo.getDeductType().equals(DeductType.QUALITY) ||
                deductOrderPo.getDeductType().equals(DeductType.OTHER) ||
                deductOrderPo.getDeductType().equals(DeductType.PAY) ||
                deductOrderPo.getDeductType().equals(DeductType.DEFECTIVE_RETURN)) {
            //修改结算单金额
            PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getByPurchaseSettleOrderNo(dto.getSettleOrderNo());
            if (!purchaseSettleOrderPo.getPurchaseSettleStatus().equals(PurchaseSettleStatus.WAIT_CONFIRM)) {
                throw new ParamIllegalException("采购结算单处于{}状态时，才能进行操作", PurchaseSettleStatus.WAIT_CONFIRM.getRemark());
            }
            purchaseSettleOrderPo.setDeductPrice(purchaseSettleOrderPo.getDeductPrice().add(deductOrderPo.getDeductPrice()));
            purchaseSettleOrderPo.setPayPrice((purchaseSettleOrderPo.getPayPrice()).subtract(deductOrderPo.getDeductPrice()));
            purchaseSettleOrderDao.updateByIdVersion(purchaseSettleOrderPo);

            //创建结算详情
            PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
            purchaseSettleOrderItemPo.setPurchaseSettleOrderId(purchaseSettleOrderPo.getPurchaseSettleOrderId());
            purchaseSettleOrderItemPo.setPurchaseSettleOrderNo(purchaseSettleOrderPo.getPurchaseSettleOrderNo());
            purchaseSettleOrderItemPo.setBusinessNo(deductOrderPo.getDeductOrderNo());
            purchaseSettleOrderItemPo.setPurchaseSettleItemType(PurchaseSettleItemType.DEDUCT);
            purchaseSettleOrderItemPo.setSettleTime(deductOrderPo.getExamineTime());
            purchaseSettleOrderItemPo.setSkuNum(0);
            purchaseSettleOrderItemPo.setSettlePrice(deductOrderPo.getDeductPrice());
            purchaseSettleOrderItemPo.setStatusName(deductOrderPo.getDeductStatus().getRemark());
            purchaseSettleOrderItemDao.insert(purchaseSettleOrderItemPo);
        }


        if (deductOrderPo.getDeductType().equals(DeductType.PROCESS)) {
            //修改加工单金额
            ProcessSettleOrderPo processSettleOrderPo = processSettleOrderDao.getByProcessSettleOrderNo(dto.getSettleOrderNo());
            if (!processSettleOrderPo.getProcessSettleStatus().equals(ProcessSettleStatus.WAIT_SETTLE)) {
                throw new ParamIllegalException("加工结算单处于{}状态时，才能进行操作", ProcessSettleStatus.WAIT_SETTLE.getRemark());
            }
            processSettleOrderPo.setDeductPrice(processSettleOrderPo.getDeductPrice().add(deductOrderPo.getDeductPrice()));
            processSettleOrderPo.setPayPrice((processSettleOrderPo.getPayPrice()).subtract(deductOrderPo.getDeductPrice()));
            processSettleOrderDao.updateByIdVersion(processSettleOrderPo);

            //修改详情记录
            List<ProcessSettleOrderItemPo> processSettleOrderItemPos = processSettleOrderItemDao.getListByCompleteUser(deductOrderPo.getDeductUser(), processSettleOrderPo.getProcessSettleOrderNo());
            if (CollectionUtil.isNotEmpty(processSettleOrderItemPos)) {
                ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemPos.get(0);
                processSettleOrderItemPo.setSettlePrice((processSettleOrderItemPo.getSettlePrice()).subtract(deductOrderPo.getDeductPrice()));
                processSettleOrderItemDao.updateByIdVersion(processSettleOrderItemPo);

                //创建记录
                ProcessSettleOrderBillPo processSettleOrderBillPo = new ProcessSettleOrderBillPo();
                processSettleOrderBillPo.setProcessSettleOrderItemId(processSettleOrderItemPo.getProcessSettleOrderItemId());
                processSettleOrderBillPo.setProcessSettleOrderBillType(ProcessSettleOrderBillType.DEDUCT);
                processSettleOrderBillPo.setBusinessNo(deductOrderPo.getDeductOrderNo());
                processSettleOrderBillPo.setSupplementDeductType(SupplementDeductType.getSupplementDeductByDeduct(deductOrderPo.getDeductType()));
                processSettleOrderBillPo.setExamineTime(deductOrderPo.getExamineTime());
                processSettleOrderBillPo.setPrice(deductOrderPo.getDeductPrice());
                processSettleOrderBillDao.insert(processSettleOrderBillPo);
            }
        }

        deductOrderPo.setSettleOrderNo(dto.getSettleOrderNo());
        deductOrderDao.updateByIdVersion(deductOrderPo);

        return true;

    }

    /**
     * 扣款单sku导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/7/5 16:55
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSku(DeductOrderDto dto) {
        //条件过滤
        if (null == deductOrderBaseService.getSearchDeductOrderWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportSkuTotals = deductOrderDao.getExportSkuTotals(dto);
        Assert.isTrue(null != exportSkuTotals && 0 != exportSkuTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_DEDUCT_ORDER_SKU_EXPORT.getCode(), dto));
    }

    /**
     * 扣款单sku导出总数
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2023/7/5 17:07
     */
    public Integer getExportSkuTotals(DeductOrderDto dto) {
        //条件过滤
        if (null == deductOrderBaseService.getSearchDeductOrderWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        return deductOrderDao.getExportSkuTotals(dto);

    }

    /**
     * 扣款单sku导出列表
     *
     * @param dto
     * @return
     */
    public CommonResult<ExportationListResultBo<DeductOrderExportSkuVo>> getExportSkuList(DeductOrderDto dto) {
        ExportationListResultBo<DeductOrderExportSkuVo> resultBo = new ExportationListResultBo<>();
        List<DeductOrderExportSkuVo> list = new ArrayList<>();
        //条件过滤
        if (null == deductOrderBaseService.getSearchDeductOrderWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<DeductOrderPo> exportList = deductOrderDao.getExportSkuList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<DeductOrderPo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        Map<Long, DeductOrderPo> deductOrderPoMap = records.stream().collect(Collectors.toMap(DeductOrderPo::getDeductOrderId, Function.identity()));

        List<Long> deductOrderIdList = records.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList());
        List<String> deductOrderNoList = records.stream().map(DeductOrderPo::getDeductOrderNo).collect(Collectors.toList());

        //查询详情
        List<DeductOrderDefectivePo> deductOrderDefectivePoList = deductOrderDefectiveDao.getByBatchDeductOrderId(deductOrderIdList);
        List<DeductOrderOtherPo> deductOrderOtherPoList = deductOrderOtherDao.getByBatchDeductOrderId(deductOrderIdList);
        List<DeductOrderPayPo> deductOrderPayPoList = deductOrderPayDao.getByBatchDeductOrderId(deductOrderIdList);
        List<DeductOrderProcessPo> deductOrderProcessPoList = deductOrderProcessDao.getBatchByDeductOrderId(deductOrderIdList);
        List<DeductOrderPurchasePo> deductOrderPurchasePoList = deductOrderPurchaseDao.getByBatchDeductOrderId(deductOrderIdList);
        List<DeductOrderQualityPo> deductOrderQualityPoList = deductOrderQualityDao.getByBatchDeductOrderId(deductOrderIdList);

        // sku列表
        List<String> skuList = new ArrayList<>();
        List<String> defectiveSkuList = deductOrderDefectivePoList.stream().map(DeductOrderDefectivePo::getSku).distinct().collect(Collectors.toList());
        List<String> purchaseSkuList = deductOrderPurchasePoList.stream().map(DeductOrderPurchasePo::getSku).distinct().collect(Collectors.toList());
        List<String> qualitySkuList = deductOrderQualityPoList.stream().map(DeductOrderQualityPo::getSku).distinct().collect(Collectors.toList());
        skuList.addAll(defectiveSkuList);
        skuList.addAll(purchaseSkuList);
        skuList.addAll(qualitySkuList);
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        //采购退货单查询详情
        List<DeductOrderPurchasePo> purchaseReturnList = deductOrderPurchasePoList.stream()
                .filter(purchasePo -> DeductOrderPurchaseType.PURCHASE_RETURN.equals(purchasePo.getDeductOrderPurchaseType()))
                .collect(Collectors.toList());

        List<String> returnNoList = purchaseReturnList.stream().map(DeductOrderPurchasePo::getBusinessNo).distinct().collect(Collectors.toList());
        List<String> returnNoDefectiveList = deductOrderDefectivePoList.stream().map(DeductOrderDefectivePo::getBusinessNo).distinct().collect(Collectors.toList());
        returnNoList.addAll(returnNoDefectiveList);
        Map<String, PurchaseReturnOrderPo> purchaseReturnOrderPoMap = purchaseReturnOrderDao.getMapByReturnOrderNoList(returnNoList);
        List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPos = purchaseReturnOrderItemDao.getListByReturnNoList(returnNoList);


        // 获取结算单号
        Map<String, List<PurchaseSettleOrderItemPo>> purchaseSettleOrderItemPoMap = purchaseSettleOrderItemDao.getByBusinessNoList(deductOrderNoList)
                .stream().collect(Collectors.groupingBy(PurchaseSettleOrderItemPo::getBusinessNo));

        //组装数据
        //次品退供
        for (DeductOrderDefectivePo deductOrderDefectivePo : deductOrderDefectivePoList) {
            DeductOrderPo deductOrderPo = deductOrderPoMap.get(deductOrderDefectivePo.getDeductOrderId());
            DeductOrderExportSkuVo defectiveVo = DeductOrderDetailConverter.deductOrderPoToVo(deductOrderPo, purchaseSettleOrderItemPoMap);

            defectiveVo.setBusinessNo(deductOrderDefectivePo.getBusinessNo());
            defectiveVo.setSettlePrice(deductOrderDefectivePo.getSettlePrice());
            defectiveVo.setSku(deductOrderDefectivePo.getSku());
            defectiveVo.setSkuEncode(skuEncodeMap.get(deductOrderDefectivePo.getSku()));
            defectiveVo.setDeductNum(deductOrderDefectivePo.getDeductNum());
            defectiveVo.setDeductRemarks(deductOrderDefectivePo.getDeductRemarks());
            defectiveVo.setDeductPrice(deductOrderDefectivePo.getDeductPrice());
            defectiveVo.setDeductUnitPrice(deductOrderDefectivePo.getDeductUnitPrice());
            if (purchaseReturnOrderPoMap.containsKey(deductOrderDefectivePo.getBusinessNo())) {
                PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderPoMap.get(deductOrderDefectivePo.getBusinessNo());
                defectiveVo.setPurchaseChildOrderNo(purchaseReturnOrderPo.getPurchaseChildOrderNo());
                String skuBatchCodeJoining = purchaseReturnOrderItemPos.stream()
                        .filter(itemPo -> itemPo.getReturnOrderNo().equals(purchaseReturnOrderPo.getReturnOrderNo()))
                        .map(PurchaseReturnOrderItemPo::getSkuBatchCode)
                        .distinct()
                        .collect(Collectors.joining(","));
                defectiveVo.setReturnSkuBatchCode(skuBatchCodeJoining);
            }
            list.add(defectiveVo);
        }

        //其他单据
        for (DeductOrderOtherPo deductOrderOtherPo : deductOrderOtherPoList) {
            DeductOrderPo deductOrderPo = deductOrderPoMap.get(deductOrderOtherPo.getDeductOrderId());
            DeductOrderExportSkuVo otherVo = DeductOrderDetailConverter.deductOrderPoToVo(deductOrderPo, purchaseSettleOrderItemPoMap);
            otherVo.setDeductRemarks(deductOrderOtherPo.getDeductRemarks());
            otherVo.setDeductPrice(deductOrderOtherPo.getDeductPrice());
            list.add(otherVo);
        }

        //预付款
        for (DeductOrderPayPo deductOrderPayPo : deductOrderPayPoList) {
            DeductOrderPo deductOrderPo = deductOrderPoMap.get(deductOrderPayPo.getDeductOrderId());
            DeductOrderExportSkuVo payVo = DeductOrderDetailConverter.deductOrderPoToVo(deductOrderPo, purchaseSettleOrderItemPoMap);
            payVo.setDeductRemarks(deductOrderPayPo.getDeductRemarks());
            payVo.setDeductPrice(deductOrderPayPo.getDeductPrice());
            list.add(payVo);
        }

        //加工明细
        for (DeductOrderProcessPo deductOrderProcessPo : deductOrderProcessPoList) {
            DeductOrderPo deductOrderPo = deductOrderPoMap.get(deductOrderProcessPo.getDeductOrderId());
            DeductOrderExportSkuVo processVo = DeductOrderDetailConverter.deductOrderPoToVo(deductOrderPo, purchaseSettleOrderItemPoMap);

            processVo.setBusinessNo(deductOrderProcessPo.getProcessOrderNo());
            processVo.setSettlePrice(deductOrderProcessPo.getSettlePrice());
            processVo.setDeductRemarks(deductOrderProcessPo.getDeductRemarks());
            processVo.setDeductPrice(deductOrderProcessPo.getDeductPrice());
            list.add(processVo);
        }

        //采购明细
        for (DeductOrderPurchasePo deductOrderPurchasePo : deductOrderPurchasePoList) {
            DeductOrderPo deductOrderPo = deductOrderPoMap.get(deductOrderPurchasePo.getDeductOrderId());
            DeductOrderExportSkuVo purchaseVo = DeductOrderDetailConverter.deductOrderPoToVo(deductOrderPo, purchaseSettleOrderItemPoMap);

            purchaseVo.setDeductOrderPurchaseTypeName(deductOrderPurchasePo.getDeductOrderPurchaseType().getRemark());
            purchaseVo.setBusinessNo(deductOrderPurchasePo.getBusinessNo());
            if (purchaseReturnOrderPoMap.containsKey(deductOrderPurchasePo.getBusinessNo())) {
                PurchaseReturnOrderPo purchaseReturnOrderPo = purchaseReturnOrderPoMap.get(deductOrderPurchasePo.getBusinessNo());
                purchaseVo.setPurchaseChildOrderNo(purchaseReturnOrderPo.getPurchaseChildOrderNo());
                String skuBatchCodeJoining = purchaseReturnOrderItemPos.stream()
                        .filter(itemPo -> itemPo.getReturnOrderNo().equals(purchaseReturnOrderPo.getReturnOrderNo()))
                        .map(PurchaseReturnOrderItemPo::getSkuBatchCode)
                        .distinct()
                        .collect(Collectors.joining(","));
                purchaseVo.setReturnSkuBatchCode(skuBatchCodeJoining);
            }
            purchaseVo.setSettlePrice(deductOrderPurchasePo.getSettlePrice());
            purchaseVo.setSku(deductOrderPurchasePo.getSku());
            purchaseVo.setSkuEncode(skuEncodeMap.get(deductOrderPurchasePo.getSku()));
            purchaseVo.setSpu(deductOrderPurchasePo.getSpu());
            purchaseVo.setDeductNum(deductOrderPurchasePo.getSkuNum());
            purchaseVo.setDeductRemarks(deductOrderPurchasePo.getDeductRemarks());
            purchaseVo.setDeductPrice(deductOrderPurchasePo.getDeductPrice());
            list.add(purchaseVo);
        }

        //品质扣款明细
        for (DeductOrderQualityPo deductOrderQualityPo : deductOrderQualityPoList) {
            DeductOrderPo deductOrderPo = deductOrderPoMap.get(deductOrderQualityPo.getDeductOrderId());
            DeductOrderExportSkuVo qualityVo = DeductOrderDetailConverter.deductOrderPoToVo(deductOrderPo, purchaseSettleOrderItemPoMap);

            qualityVo.setDeductOrderPurchaseTypeName(deductOrderQualityPo.getDeductOrderPurchaseType().getRemark());
            qualityVo.setBusinessNo(deductOrderQualityPo.getBusinessNo());
            qualityVo.setSettlePrice(deductOrderQualityPo.getSettlePrice());
            qualityVo.setSku(deductOrderQualityPo.getSku());
            qualityVo.setSkuEncode(skuEncodeMap.get(deductOrderQualityPo.getSku()));
            qualityVo.setSpu(deductOrderQualityPo.getSpu());
            qualityVo.setDeductNum(deductOrderQualityPo.getSkuNum());
            qualityVo.setDeductRemarks(deductOrderQualityPo.getDeductRemarks());
            qualityVo.setDeductPrice(deductOrderQualityPo.getDeductPrice());
            list.add(qualityVo);
        }


        resultBo.setRowDataList(list);

        return CommonResult.success(resultBo);

    }

    /**
     * 批量获取单据结算金额
     *
     * @param dto:
     * @return List<DeductBusinessSettleVo>
     * @author ChenWenLong
     * @date 2023/11/15 17:59
     */
    public List<DeductSupplementBusinessSettleVo> getBusinessSettle(DeductBusinessSettleDto dto) {
        List<DeductSupplementBusinessSettleVo> voList = new ArrayList<>();
        Map<DeductOrderPurchaseType, List<DeductBusinessSettleDto.DeductBusinessSettleItemDto>> dtoTypeMap = dto.getDeductBusinessSettleItemList()
                .stream()
                .collect(Collectors.groupingBy(DeductBusinessSettleDto.DeductBusinessSettleItemDto::getDeductOrderPurchaseType));

        //采购
        List<DeductBusinessSettleDto.DeductBusinessSettleItemDto> productPurchaseList = new ArrayList<>();
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(DeductOrderPurchaseType.PRODUCT_PURCHASE, new ArrayList<>()));
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(DeductOrderPurchaseType.PROCESS_PURCHASE, new ArrayList<>()));
        if (CollectionUtils.isNotEmpty(productPurchaseList)) {
            List<String> childNoList = productPurchaseList.stream().map(DeductBusinessSettleDto.DeductBusinessSettleItemDto::getBusinessNo).collect(Collectors.toList());
            List<PurchaseOrderStatus> purchaseOrderStatusNotList = new ArrayList<>();
            purchaseOrderStatusNotList.add(PurchaseOrderStatus.WAIT_APPROVE);
            purchaseOrderStatusNotList.add(PurchaseOrderStatus.WAIT_CONFIRM);
            purchaseOrderStatusNotList.add(PurchaseOrderStatus.WAIT_RECEIVE_ORDER);
            purchaseOrderStatusNotList.add(PurchaseOrderStatus.DELETE);
            purchaseOrderStatusNotList.add(PurchaseOrderStatus.RETURN);
            List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByChildNoListAndType(childNoList, dto.getSupplierCode(), purchaseOrderStatusNotList);
            for (PurchaseChildOrderPo purchaseChildOrderPo : purchaseChildOrderPoList) {
                DeductSupplementBusinessSettleVo deductSupplementBusinessSettleVo = new DeductSupplementBusinessSettleVo();
                deductSupplementBusinessSettleVo.setBusinessNo(purchaseChildOrderPo.getPurchaseChildOrderNo());
                deductSupplementBusinessSettleVo.setSettlePrice(purchaseChildOrderPo.getTotalSettlePrice());
                voList.add(deductSupplementBusinessSettleVo);
            }
        }

        //新样品
        List<DeductBusinessSettleDto.DeductBusinessSettleItemDto> sampleList = dtoTypeMap.get(DeductOrderPurchaseType.SAMPLE);
        if (CollectionUtils.isNotEmpty(sampleList)) {
            List<String> sampleNoList = sampleList.stream()
                    .map(DeductBusinessSettleDto.DeductBusinessSettleItemDto::getBusinessNo)
                    .collect(Collectors.toList());
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoListAndSupplierCode(sampleNoList, dto.getSupplierCode(), List.of(DevelopSampleStatus.WAIT_RECEIVE, DevelopSampleStatus.ON_SHELVES));
            for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                DeductSupplementBusinessSettleVo deductSupplementBusinessSettleVo = new DeductSupplementBusinessSettleVo();
                deductSupplementBusinessSettleVo.setBusinessNo(developSampleOrderPo.getDevelopSampleOrderNo());
                deductSupplementBusinessSettleVo.setSettlePrice(developSampleOrderPo.getSkuBatchSamplePrice());
                voList.add(deductSupplementBusinessSettleVo);
            }
        }

        //发货单
        List<DeductBusinessSettleDto.DeductBusinessSettleItemDto> deliverList = dtoTypeMap.get(DeductOrderPurchaseType.DELIVER);
        if (CollectionUtils.isNotEmpty(deliverList)) {
            List<String> deliverNoList = deliverList.stream().map(DeductBusinessSettleDto.DeductBusinessSettleItemDto::getBusinessNo).collect(Collectors.toList());
            List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByNoListAndStatus(deliverNoList, List.of(DeliverOrderStatus.WAREHOUSED),
                    List.of(dto.getSupplierCode()));
            if (CollectionUtil.isNotEmpty(purchaseDeliverOrderPoList)) {
                List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
                Map<String, BigDecimal> purchaseChildOrderPriceMap = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList)
                        .stream()
                        .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, PurchaseChildOrderPo::getTotalSettlePrice));
                for (PurchaseDeliverOrderPo item : purchaseDeliverOrderPoList) {
                    DeductSupplementBusinessSettleVo deductSupplementBusinessSettleVo = new DeductSupplementBusinessSettleVo();
                    deductSupplementBusinessSettleVo.setBusinessNo(item.getPurchaseDeliverOrderNo());
                    deductSupplementBusinessSettleVo.setSettlePrice(purchaseChildOrderPriceMap.get(item.getPurchaseChildOrderNo()));
                    voList.add(deductSupplementBusinessSettleVo);
                }
            }
        }

        return voList;

    }
}
