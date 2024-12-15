package com.hete.supply.scm.server.scm.settle.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderDto;
import com.hete.supply.scm.api.scm.entity.dto.SupplementOrderQueryByApiDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportSkuVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplementOrderExportVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.entity.bo.DeductSupplementBusinessBo;
import com.hete.supply.scm.server.scm.entity.dto.SupplementBusinessSettleDto;
import com.hete.supply.scm.server.scm.entity.dto.SupplementSkuDropDownDto;
import com.hete.supply.scm.server.scm.entity.vo.DeductSupplementBusinessSettleVo;
import com.hete.supply.scm.server.scm.entity.vo.SkuDropDownVo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.ibfs.service.base.RecoOrderBaseService;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.dto.PurchaseDropDownDto;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseDropDownVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.config.SettleConfig;
import com.hete.supply.scm.server.scm.settle.converter.*;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.dto.*;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementDeductOrderListVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderSettleDropDownVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.SupplementOrderVo;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.scm.settle.enums.SupplementDeduct;
import com.hete.supply.scm.server.scm.settle.enums.SupplementDeductType;
import com.hete.supply.scm.server.scm.settle.service.base.DeductOrderBaseService;
import com.hete.supply.scm.server.scm.settle.service.base.SupplementOrderBaseService;
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
public class SupplementOrderBizService {

    private final SupplementOrderDao supplementOrderDao;
    private final SupplementOrderPurchaseDao supplementOrderPurchaseDao;
    private final SupplementOrderProcessDao supplementOrderProcessDao;
    private final IdGenerateService idGenerateService;
    private final PurchaseBaseService purchaseBaseService;
    private final SampleBaseService sampleBaseService;
    private final ScmImageBaseService scmImageBaseService;
    private final SupplementOrderBaseService supplementOrderBaseService;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessSettleOrderDao processSettleOrderDao;
    private final SupplierDao supplierDao;
    private final AuthBaseService authBaseService;
    private final SupplementOrderOtherDao supplementOrderOtherDao;
    private final DeductOrderDao deductOrderDao;
    private final DeductOrderProcessDao deductOrderProcessDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final DeductOrderBaseService deductOrderBaseService;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final PlmRemoteService plmRemoteService;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final RecoOrderBaseService recoOrderBaseService;
    private final SettleConfig settleConfig;


    public CommonPageResult.PageInfo<SupplementOrderVo> searchSupplementOrder(SupplementOrderDto dto) {
        return supplementOrderBaseService.searchSupplementOrder(dto);
    }

    /**
     * 查询导出总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(SupplementOrderQueryByApiDto dto) {
        if (StringUtils.isNotBlank(dto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(dto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.REPLENISH);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return 0;
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return 0;
            }

            dto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(dto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(dto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return 0;
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.REPLENISH);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return 0;
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(processSettleOrderBillPoList.stream().map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return 0;
            }

            dto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }


        return supplementOrderDao.getExportTotals(dto);

    }

    /**
     * 供应商系统查询导出总数
     *
     * @param dto
     * @return
     */
    public Integer getSupplierExportTotals(SupplementOrderQueryByApiDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtil.isNotEmpty(supplierCodeList)) {
            List<SupplementStatus> notSupplementStatusList = new ArrayList<>();
            notSupplementStatusList.add(SupplementStatus.WAIT_SUBMIT);
            notSupplementStatusList.add(SupplementStatus.WAIT_PRICE);
            dto.setNotSupplementStatusList(notSupplementStatusList);
            List<SupplementType> supplementTypeList = new ArrayList<>();
            supplementTypeList.add(SupplementType.PRICE);
            supplementTypeList.add(SupplementType.OTHER);
            dto.setSupplementTypeList(supplementTypeList);
        }
        return this.getExportTotals(dto);
    }

    /**
     * 查询导出列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<SupplementOrderExportVo> getExportList(SupplementOrderQueryByApiDto dto) {

        if (StringUtils.isNotBlank(dto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(dto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.REPLENISH);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            dto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(dto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(dto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.REPLENISH);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementOrderNoList(processSettleOrderBillPoList.stream().map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(supplementOrderPoList)) {
                return new CommonPageResult.PageInfo<>();
            }

            dto.setSupplementOrderIds(supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList()));
        }


        CommonPageResult.PageInfo<SupplementOrderExportVo> exportList = supplementOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<SupplementOrderExportVo> records = exportList.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return exportList;
        }
        List<String> supplementOrderNos = records.stream().map(SupplementOrderExportVo::getSupplementOrderNo).collect(Collectors.toList());
        // 获取结算单号
        List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos = purchaseSettleOrderItemDao.getByBusinessNoList(supplementOrderNos);
        List<ProcessSettleOrderItemPo> processSettleOrderItemPos = new ArrayList<>();
        List<ProcessSettleOrderBillPo> processSettleOrderBillPos = processSettleOrderBillDao.getByBussinessNos(supplementOrderNos);
        if (CollectionUtil.isNotEmpty(processSettleOrderBillPos)) {
            processSettleOrderItemPos = processSettleOrderItemDao.getByProcessSettleOrderItemIdList(processSettleOrderBillPos.stream().map(ProcessSettleOrderBillPo::getProcessSettleOrderItemId).collect(Collectors.toList()));
        }
        List<ProcessSettleOrderItemPo> finalProcessSettleOrderItemPos = processSettleOrderItemPos;
        List<SupplementOrderExportVo> newRecords = records.stream().peek(item -> {
            if (CollectionUtil.isNotEmpty(purchaseSettleOrderItemPos)) {
                Optional<PurchaseSettleOrderItemPo> first = purchaseSettleOrderItemPos.stream().filter(it -> it.getBusinessNo().equals(item.getSupplementOrderNo())).findFirst();
                if (first.isPresent()) {
                    PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = first.get();
                    item.setSettleOrderNo(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo());
                }
            }
            if (StringUtils.isEmpty(item.getSettleOrderNo()) && CollectionUtil.isNotEmpty(finalProcessSettleOrderItemPos)) {
                Optional<ProcessSettleOrderBillPo> first = processSettleOrderBillPos.stream().filter(it -> it.getBusinessNo().equals(item.getSupplementOrderNo())).findFirst();
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
    public CommonPageResult.PageInfo<SupplementOrderExportVo> getSupplierExportList(SupplementOrderQueryByApiDto dto) {
        //供应商导出加权限限制
        List<String> supplierCodeList = authBaseService.getSupplierCodeList();
        dto.setAuthSupplierCode(supplierCodeList);
        if (CollectionUtil.isNotEmpty(supplierCodeList)) {
            List<SupplementStatus> notSupplementStatusList = new ArrayList<>();
            notSupplementStatusList.add(SupplementStatus.WAIT_SUBMIT);
            notSupplementStatusList.add(SupplementStatus.WAIT_PRICE);
            dto.setNotSupplementStatusList(notSupplementStatusList);
            List<SupplementType> supplementTypeList = new ArrayList<>();
            supplementTypeList.add(SupplementType.PRICE);
            supplementTypeList.add(SupplementType.OTHER);
            dto.setSupplementTypeList(supplementTypeList);
        }
        return this.getExportList(dto);
    }

    public SupplementOrderDetailVo getSupplementOrderDetail(SupplementOrderDetailDto dto) {
        return supplementOrderBaseService.getSupplementOrderDetail(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addSupplementOrder(SupplementOrderAddDto dto) {
        this.addParameterVerify(dto);

        // 计算补款总金额
        final List<SupplementOrderPurchaseDto> supplementOrderPurchaseList = dto.getSupplementOrderPurchaseList();
        final List<SupplementOrderProcessDto> supplementOrderProcessList = dto.getSupplementOrderProcessList();
        final List<SupplementOrderOtherDto> supplementOrderOtherList = dto.getSupplementOrderOtherList();
        BigDecimal supplementPrice = this.getSupplementPrice(dto.getSupplementType(), supplementOrderPurchaseList, supplementOrderProcessList, supplementOrderOtherList);

        SupplementOrderPo supplementOrderPo = SupplementOrderConverter.INSTANCE.create(dto);

        if (SupplementType.PRICE.equals(dto.getSupplementType()) ||
                SupplementType.OTHER.equals(dto.getSupplementType())) {
            SupplierPo supplierPo = supplierDao.getBySupplierCode(dto.getSupplierCode());
            if (supplierPo == null) {
                throw new ParamIllegalException("供应商查询不到信息");
            }
            supplementOrderPo.setSupplierName(supplierPo.getSupplierName());
        }

        // 返回自增序列号，返回格式为key+自增id，其中数字部分最少6位
        String today = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        String supplementOrderNo = idGenerateService.getConfuseCode(ScmConstant.SUPPLEMENT_ORDER_NO_PREFIX + today, ConfuseLength.L_4);
        supplementOrderPo.setSupplementOrderNo(supplementOrderNo);
        supplementOrderPo.setSupplementStatus(SupplementStatus.WAIT_SUBMIT);
        supplementOrderPo.setSupplementPrice(supplementPrice);
        supplementOrderBaseService.updateHandleUser(supplementOrderPo, settleConfig);
        supplementOrderDao.insert(supplementOrderPo);

        Long supplementOrderId = supplementOrderPo.getSupplementOrderId();

        //创建采购明细
        if (SupplementType.PRICE.equals(supplementOrderPo.getSupplementType())) {
            List<SupplementOrderPurchasePo> supplementOrderPurchasePos = SupplementOrderPurchaseConverter.INSTANCE.create(supplementOrderPurchaseList);
            List<SupplementOrderPurchasePo> supplementOrderPurchasePoList = supplementOrderPurchasePos.stream().peek(item -> item.setSupplementOrderId(supplementOrderId)).collect(Collectors.toList());
            supplementOrderPurchaseDao.insertBatch(supplementOrderPurchasePoList);
        }
        //创建加工明细
        if (SupplementType.PROCESS.equals(supplementOrderPo.getSupplementType())) {
            List<SupplementOrderProcessPo> supplementOrderProcessPos = SupplementOrderProcessConverter.INSTANCE.create(supplementOrderProcessList);
            List<SupplementOrderProcessPo> supplementOrderProcessPoList = supplementOrderProcessPos.stream().peek(item -> item.setSupplementOrderId(supplementOrderId)).collect(Collectors.toList());
            supplementOrderProcessDao.insertBatch(supplementOrderProcessPoList);
        }
        //创建其他明细
        if (SupplementType.OTHER.equals(supplementOrderPo.getSupplementType())) {
            List<SupplementOrderOtherPo> supplementOrderOtherPos = SupplementOrderOtherConverter.INSTANCE.create(supplementOrderOtherList);
            List<SupplementOrderOtherPo> supplementOrderOtherPoList = supplementOrderOtherPos.stream().peek(item -> {
                item.setSupplementOrderId(supplementOrderId);
                item.setSupplementOrderNo(supplementOrderNo);
            }).collect(Collectors.toList());
            supplementOrderOtherDao.insertBatch(supplementOrderOtherPoList);
        }
        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.SUPPLEMENT_ORDER, supplementOrderId);
        scmImageBaseService.insertBatchImage(dto.getDocumentCodeList(), ImageBizType.SUPPLEMENT_ORDER_FILE, supplementOrderId);
        supplementOrderBaseService.createStatusChangeLog(supplementOrderPo, null);
        return true;
    }

    private BigDecimal getSupplementPrice(SupplementType supplementType, List<SupplementOrderPurchaseDto> supplementOrderPurchaseList,
                                          List<SupplementOrderProcessDto> supplementOrderProcessList,
                                          List<SupplementOrderOtherDto> supplementOrderOtherList) {
        if (SupplementType.PRICE.equals(supplementType)) {
            return Optional.ofNullable(supplementOrderPurchaseList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(SupplementOrderPurchaseDto::getSupplementPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (SupplementType.PROCESS.equals(supplementType)) {
            return Optional.ofNullable(supplementOrderProcessList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(SupplementOrderProcessDto::getSupplementPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (SupplementType.OTHER.equals(supplementType)) {
            return Optional.ofNullable(supplementOrderOtherList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(SupplementOrderOtherDto::getSupplementPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 增加判断是否填写供应商或补款员工,并验证入参
     *
     * @author ChenWenLong
     * @date 2022/11/10 12:06
     */
    private void addParameterVerify(SupplementOrderAddDto dto) {
        // 判断是否存在重复单据号
        Set<String> businessNoSet = new HashSet<>();
        if (SupplementType.PRICE.equals(dto.getSupplementType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getSupplementOrderPurchaseList())) {
                throw new ParamIllegalException("请填写采购补款明细信息");
            }
            for (SupplementOrderPurchaseDto supplementOrderPurchaseDto : dto.getSupplementOrderPurchaseList()) {
                supplementOrderPurchaseVerify(supplementOrderPurchaseDto.getSupplementOrderPurchaseType(),
                        supplementOrderPurchaseDto.getBusinessNo(),
                        supplementOrderPurchaseDto.getSupplementPrice(),
                        supplementOrderPurchaseDto.getSupplementRemarks(),
                        businessNoSet);
            }
        }
        if (SupplementType.PROCESS.equals(dto.getSupplementType())) {
            if (StringUtils.isBlank(dto.getSupplementUsername())) {
                throw new ParamIllegalException("请选择对应补款员工");
            }
            if (CollectionUtil.isEmpty(dto.getSupplementOrderProcessList())) {
                throw new ParamIllegalException("请填写加工补款明细信息");
            }
            for (SupplementOrderProcessDto supplementOrderProcessDto : dto.getSupplementOrderProcessList()) {
                supplementOrderProcessVerify(supplementOrderProcessDto.getProcessOrderNo(),
                        supplementOrderProcessDto.getSupplementPrice(),
                        supplementOrderProcessDto.getSupplementRemarks(),
                        businessNoSet);
            }
        }
        if (SupplementType.OTHER.equals(dto.getSupplementType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getSupplementOrderOtherList())) {
                throw new ParamIllegalException("请填写其他明细信息");
            }
            for (SupplementOrderOtherDto supplementOrderOtherDto : dto.getSupplementOrderOtherList()) {
                supplementOrderOtherVerify(supplementOrderOtherDto.getSupplementPrice(), supplementOrderOtherDto.getSupplementRemarks());
            }
        }

        List<DeductSupplementBusinessBo> deductSupplementBusinessBoList = new ArrayList<>();
        Optional.ofNullable(dto.getSupplementOrderPurchaseList())
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    DeductSupplementBusinessBo deductSupplementBusinessBo = new DeductSupplementBusinessBo();
                    deductSupplementBusinessBo.setDeductOrderPurchaseType(DeductOrderPurchaseType.convertDeductType(item.getSupplementOrderPurchaseType()));
                    deductSupplementBusinessBo.setBusinessNo(item.getBusinessNo());
                    deductSupplementBusinessBo.setSku(item.getSku());
                    deductSupplementBusinessBoList.add(deductSupplementBusinessBo);
                });

        //补扣款单新增导入时验证数据逻辑一致
        deductOrderBaseService.verifyDeductSupplementBusiness(deductSupplementBusinessBoList, dto.getSupplierCode());

    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean editSupplementOrder(SupplementOrderEditDto dto) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getByIdVersion(dto.getSupplementOrderId(), dto.getVersion());
        if (null == supplementOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (supplementOrderPo.getSupplementStatus() != SupplementStatus.WAIT_SUBMIT) {
            throw new ParamIllegalException("状态处于{}时才能进行编辑", SupplementStatus.WAIT_SUBMIT.getRemark());
        }
        this.editParameterVerify(dto);
        SupplementOrderPo updateSupplementOrderPo = SupplementOrderConverter.INSTANCE.edit(dto);
        final List<SupplementOrderPurchaseDto> supplementOrderPurchaseList = dto.getSupplementOrderPurchaseList();
        final List<SupplementOrderProcessDto> supplementOrderProcessList = dto.getSupplementOrderProcessList();
        final List<SupplementOrderOtherDto> supplementOrderOtherList = dto.getSupplementOrderOtherList();
        BigDecimal supplementPrice = this.getSupplementPrice(dto.getSupplementType(), supplementOrderPurchaseList, supplementOrderProcessList, supplementOrderOtherList);
        updateSupplementOrderPo.setSupplementPrice(supplementPrice);
        supplementOrderDao.updateByIdVersion(updateSupplementOrderPo);
        Long supplementOrderId = dto.getSupplementOrderId();

        if (SupplementType.PRICE.equals(updateSupplementOrderPo.getSupplementType())) {
            //删除原来旧数据
            supplementOrderPurchaseDao.deleteBySupplementOrderId(supplementOrderId);
            //创建采购明细
            List<SupplementOrderPurchasePo> supplementOrderPurchasePos = SupplementOrderPurchaseConverter.INSTANCE.edit(dto.getSupplementOrderPurchaseList());
            List<SupplementOrderPurchasePo> supplementOrderPurchasePoList = supplementOrderPurchasePos.stream().peek(item -> item.setSupplementOrderId(supplementOrderId)).collect(Collectors.toList());
            supplementOrderPurchaseDao.insertBatch(supplementOrderPurchasePoList);
        }
        if (SupplementType.PROCESS.equals(updateSupplementOrderPo.getSupplementType())) {
            //删除原来旧数据
            supplementOrderProcessDao.deleteBySupplementOrderId(supplementOrderId);
            //创建加工明细
            List<SupplementOrderProcessPo> supplementOrderProcessPos = SupplementOrderProcessConverter.INSTANCE.edit(dto.getSupplementOrderProcessList());
            List<SupplementOrderProcessPo> supplementOrderProcessPoList = supplementOrderProcessPos.stream().peek(item -> item.setSupplementOrderId(supplementOrderId)).collect(Collectors.toList());
            supplementOrderProcessDao.insertBatch(supplementOrderProcessPoList);
        }

        if (SupplementType.OTHER.equals(updateSupplementOrderPo.getSupplementType())) {
            //删除原来旧数据
            supplementOrderOtherDao.deleteBySupplementOrderId(supplementOrderId);
            //创建其他明细
            List<SupplementOrderOtherPo> supplementOrderOtherPos = SupplementOrderOtherConverter.INSTANCE.edit(dto.getSupplementOrderOtherList());
            List<SupplementOrderOtherPo> supplementOrderOtherPoList = supplementOrderOtherPos.stream().peek(item -> {
                item.setSupplementOrderId(supplementOrderId);
                item.setSupplementOrderNo(supplementOrderPo.getSupplementOrderNo());
            }).collect(Collectors.toList());
            supplementOrderOtherDao.insertBatch(supplementOrderOtherPoList);
        }

        //获取凭证
        List<Long> longs = List.of(dto.getSupplementOrderId());
        List<String> scmImagePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLEMENT_ORDER, longs);
        scmImageBaseService.editImage(dto.getFileCodeList(), scmImagePos, ImageBizType.SUPPLEMENT_ORDER, dto.getSupplementOrderId());
        List<String> scmFilePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SUPPLEMENT_ORDER_FILE, longs);
        scmImageBaseService.editImage(dto.getDocumentCodeList(), scmFilePos, ImageBizType.SUPPLEMENT_ORDER_FILE, dto.getSupplementOrderId());
        return true;
    }

    /**
     * 编辑判断是否填写供应商或补款员工,并验证入参
     *
     * @author ChenWenLong
     * @date 2022/11/10 12:06
     */
    private void editParameterVerify(SupplementOrderEditDto dto) {
        // 判断是否存在重复单据号
        Set<String> businessNoSet = new HashSet<>();
        if (SupplementType.PRICE.equals(dto.getSupplementType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getSupplementOrderPurchaseList())) {
                throw new ParamIllegalException("请填写采购补款明细信息");
            }
            for (SupplementOrderPurchaseDto supplementOrderPurchaseDto : dto.getSupplementOrderPurchaseList()) {
                supplementOrderPurchaseVerify(supplementOrderPurchaseDto.getSupplementOrderPurchaseType(),
                        supplementOrderPurchaseDto.getBusinessNo(),
                        supplementOrderPurchaseDto.getSupplementPrice(),
                        supplementOrderPurchaseDto.getSupplementRemarks(),
                        businessNoSet);
            }
        }
        if (SupplementType.PROCESS.equals(dto.getSupplementType())) {
            if (StringUtils.isBlank(dto.getSupplementUsername())) {
                throw new ParamIllegalException("请选择对应补款员工");
            }
            if (CollectionUtil.isEmpty(dto.getSupplementOrderProcessList())) {
                throw new ParamIllegalException("请填写加工补款明细信息");
            }
            for (SupplementOrderProcessDto supplementOrderProcessDto : dto.getSupplementOrderProcessList()) {
                supplementOrderProcessVerify(supplementOrderProcessDto.getProcessOrderNo(),
                        supplementOrderProcessDto.getSupplementPrice(),
                        supplementOrderProcessDto.getSupplementRemarks(),
                        businessNoSet);
            }

        }
        if (SupplementType.OTHER.equals(dto.getSupplementType())) {
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("请选择对应供应商");
            }
            if (CollectionUtil.isEmpty(dto.getSupplementOrderOtherList())) {
                throw new ParamIllegalException("请填写采购补款明细信息");
            }
            for (SupplementOrderOtherDto supplementOrderOtherDto : dto.getSupplementOrderOtherList()) {
                supplementOrderOtherVerify(supplementOrderOtherDto.getSupplementPrice(), supplementOrderOtherDto.getSupplementRemarks());
            }
        }

        List<DeductSupplementBusinessBo> deductSupplementBusinessBoList = new ArrayList<>();
        Optional.ofNullable(dto.getSupplementOrderPurchaseList())
                .orElse(Collections.emptyList())
                .forEach(item -> {
                    DeductSupplementBusinessBo deductSupplementBusinessBo = new DeductSupplementBusinessBo();
                    deductSupplementBusinessBo.setDeductOrderPurchaseType(DeductOrderPurchaseType.convertDeductType(item.getSupplementOrderPurchaseType()));
                    deductSupplementBusinessBo.setBusinessNo(item.getBusinessNo());
                    deductSupplementBusinessBo.setSku(item.getSku());
                    deductSupplementBusinessBoList.add(deductSupplementBusinessBo);
                });

        //补扣款单新增导入时验证数据逻辑一致
        deductOrderBaseService.verifyDeductSupplementBusiness(deductSupplementBusinessBoList, dto.getSupplierCode());

    }

    /**
     * 入参详情中采购数据验证
     *
     * @author ChenWenLong
     * @date 2022/11/15 13:56
     */
    private void supplementOrderPurchaseVerify(SupplementOrderPurchaseType supplementOrderPurchaseType,
                                               String businessNo,
                                               BigDecimal supplementPrice,
                                               String supplementRemarks,
                                               Set<String> businessNoSet) {
        if (null == supplementOrderPurchaseType) {
            throw new ParamIllegalException("请填写单据类型");
        }
        if (StringUtils.isBlank(businessNo)) {
            throw new ParamIllegalException("请填写单据号");
        }
        if (businessNo.length() > 32) {
            throw new ParamIllegalException("单据号字符长度不能超过 32 位");
        }
        if (null == supplementPrice) {
            throw new ParamIllegalException("请填写补款金额");
        }
        if (supplementPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("请填写补款金额不能小于等于0");
        }
        if (StringUtils.isBlank(supplementRemarks)) {
            throw new ParamIllegalException("请填写补款原因");
        }
        if (supplementRemarks.length() > SupplementOrderBaseService.MAX_SUPPLEMENT_REMARKS) {
            throw new ParamIllegalException("补款原因字符长度不能超过 {} 位", SupplementOrderBaseService.MAX_SUPPLEMENT_REMARKS);
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
    private void supplementOrderProcessVerify(String processOrderNo,
                                              BigDecimal supplementPrice,
                                              String supplementRemarks,
                                              Set<String> businessNoSet) {
        if (StringUtils.isBlank(processOrderNo)) {
            throw new ParamIllegalException("请填写加工单号");
        }
        if (processOrderNo.length() > 32) {
            throw new ParamIllegalException("加工单号字符长度不能超过 32 位");
        }
        if (null == supplementPrice) {
            throw new ParamIllegalException("请填写补款金额");
        }
        if (supplementPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("请填写补款金额不能小于等于0");
        }
        if (StringUtils.isBlank(supplementRemarks)) {
            throw new ParamIllegalException("请填写补款原因");
        }
        if (supplementRemarks.length() > SupplementOrderBaseService.MAX_SUPPLEMENT_REMARKS) {
            throw new ParamIllegalException("补款原因字符长度不能超过 {} 位", SupplementOrderBaseService.MAX_SUPPLEMENT_REMARKS);
        }
        if (!businessNoSet.add(processOrderNo)) {
            throw new ParamIllegalException("单据号:{}存在重复，请修改单据号！", processOrderNo);
        }
    }

    /**
     * 入参详情中其他数据验证
     *
     * @author ChenWenLong
     * @date 2023/2/24 10:24
     */
    private void supplementOrderOtherVerify(BigDecimal supplementPrice, String supplementRemarks) {
        if (null == supplementPrice) {
            throw new ParamIllegalException("请填写补款金额");
        }
        if (supplementPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParamIllegalException("请填写补款金额不能小于等于0");
        }
        if (StringUtils.isBlank(supplementRemarks)) {
            throw new ParamIllegalException("请填写补款原因");
        }
        if (supplementRemarks.length() > SupplementOrderBaseService.MAX_SUPPLEMENT_REMARKS) {
            throw new ParamIllegalException("补款原因字符长度不能超过 {} 位", SupplementOrderBaseService.MAX_SUPPLEMENT_REMARKS);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean update(SupplementOrderVersionDto dto) {
        return true;
    }

    public Boolean examine(SupplementOrderUpdateDto dto) {
        return supplementOrderBaseService.examine(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(SupplementOrderVersionDto dto) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getByIdVersion(dto.getSupplementOrderId(), dto.getVersion());
        if (supplementOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        SupplementStatus supplementStatus = supplementOrderPo.getSupplementStatus();
        if (supplementStatus != SupplementStatus.WAIT_SUBMIT) {
            throw new ParamIllegalException("状态处于{}才能进行删除", SupplementStatus.WAIT_SUBMIT.getRemark());
        }
        supplementOrderDao.removeByIdVersion(dto.getSupplementOrderId(), dto.getVersion());
        return true;
    }

    public List<PurchaseDropDownVo> getPurchaseDropDown(PurchaseDropDownDto dto) {
        List<PurchaseDropDownVo> purchaseDropDownVoList = new ArrayList<>();
        //过滤掉采购的状态
        List<PurchaseOrderStatus> purchaseOrderStatusNotList = PurchaseOrderStatus.getSupplementDeductOrderNotStatus();
        switch (dto.getSupplementOrderPurchaseType()) {
            case PRODUCT_PURCHASE:
                List<PurchaseChildOrderPo> purchaseChildOrderPoCargo = purchaseBaseService.getListLikeByChildNoAndType(dto.getBusinessNo(), PurchaseBizType.PRODUCT, dto.getSupplierCode(), purchaseOrderStatusNotList);
                if (CollectionUtil.isNotEmpty(purchaseChildOrderPoCargo)) {
                    for (PurchaseChildOrderPo item : purchaseChildOrderPoCargo) {
                        PurchaseDropDownVo purchaseDropDownVo = new PurchaseDropDownVo();
                        purchaseDropDownVo.setBusinessNo(item.getPurchaseChildOrderNo());
                        purchaseDropDownVo.setSettlePrice(item.getTotalSettlePrice());
                        purchaseDropDownVoList.add(purchaseDropDownVo);
                    }
                }
                break;
            case PROCESS_PURCHASE:
                List<PurchaseChildOrderPo> purchaseChildOrderPoProcess = purchaseBaseService.getListLikeByChildNoAndType(dto.getBusinessNo(), PurchaseBizType.PROCESS, dto.getSupplierCode(), purchaseOrderStatusNotList);
                if (CollectionUtil.isNotEmpty(purchaseChildOrderPoProcess)) {
                    for (PurchaseChildOrderPo item : purchaseChildOrderPoProcess) {
                        PurchaseDropDownVo purchaseDropDownVo = new PurchaseDropDownVo();
                        purchaseDropDownVo.setBusinessNo(item.getPurchaseChildOrderNo());
                        purchaseDropDownVo.setSettlePrice(item.getTotalSettlePrice());
                        purchaseDropDownVoList.add(purchaseDropDownVo);
                    }
                }
                break;
            case SAMPLE:
                List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByLikeNo(dto.getBusinessNo(), dto.getSupplierCode(), List.of(DevelopSampleStatus.WAIT_RECEIVE, DevelopSampleStatus.ON_SHELVES));
                if (CollectionUtil.isNotEmpty(developSampleOrderPoList)) {
                    for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                        PurchaseDropDownVo purchaseDropDownVo = new PurchaseDropDownVo();
                        purchaseDropDownVo.setBusinessNo(developSampleOrderPo.getDevelopSampleOrderNo());
                        purchaseDropDownVo.setSettlePrice(developSampleOrderPo.getSkuBatchSamplePrice());
                        purchaseDropDownVoList.add(purchaseDropDownVo);
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
                        PurchaseDropDownVo purchaseDropDownVo = new PurchaseDropDownVo();
                        purchaseDropDownVo.setBusinessNo(item.getPurchaseDeliverOrderNo());
                        purchaseDropDownVo.setSettlePrice(purchaseChildOrderPriceMap.get(item.getPurchaseChildOrderNo()));
                        purchaseDropDownVoList.add(purchaseDropDownVo);
                    }
                }
                break;
            case PURCHASE_RETURN:
                List<PurchaseReturnOrderPo> purchaseReturnOrderPos = purchaseBaseService.getListLikeByPurchaseReturnOrderNo(dto.getBusinessNo(), dto.getSupplierCode(), List.of(ReceiptOrderStatus.WAIT_RECEIVE));
                if (CollectionUtil.isNotEmpty(purchaseReturnOrderPos)) {
                    for (PurchaseReturnOrderPo item : purchaseReturnOrderPos) {
                        PurchaseDropDownVo purchaseDropDownVo = new PurchaseDropDownVo();
                        purchaseDropDownVo.setBusinessNo(item.getReturnOrderNo());
                        purchaseDropDownVoList.add(purchaseDropDownVo);
                    }
                }
                break;
            default:
                throw new ParamIllegalException("请求单据类型错误！");
        }
        return purchaseDropDownVoList;
    }

    public List<SkuDropDownVo> getSkuDropDown(SupplementSkuDropDownDto dto) {
        List<SkuDropDownVo> skuDropDownVoList = new ArrayList<>();
        Map<SupplementOrderPurchaseType, List<SupplementSkuDropDownDto.SkuDropDownItemDto>> dtoTypeMap = dto.getSkuDropDownItemList()
                .stream()
                .collect(Collectors.groupingBy(SupplementSkuDropDownDto.SkuDropDownItemDto::getSupplementOrderPurchaseType));

        //采购查询详情
        List<SupplementSkuDropDownDto.SkuDropDownItemDto> productPurchaseList = new ArrayList<>();
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(SupplementOrderPurchaseType.PRODUCT_PURCHASE, new ArrayList<>()));
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(SupplementOrderPurchaseType.PROCESS_PURCHASE, new ArrayList<>()));
        if (CollectionUtils.isNotEmpty(productPurchaseList)) {
            List<String> childNoList = productPurchaseList.stream().map(SupplementSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
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
        List<SupplementSkuDropDownDto.SkuDropDownItemDto> sampleList = dtoTypeMap.get(SupplementOrderPurchaseType.SAMPLE);
        if (CollectionUtils.isNotEmpty(sampleList)) {
            List<String> sampleNoList = sampleList.stream().map(SupplementSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(sampleNoList);
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
        List<SupplementSkuDropDownDto.SkuDropDownItemDto> deliverList = dtoTypeMap.get(SupplementOrderPurchaseType.DELIVER);
        if (CollectionUtils.isNotEmpty(deliverList)) {
            List<String> deliverNoList = deliverList.stream().map(SupplementSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
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

        //采购退货单查询详情
        List<SupplementSkuDropDownDto.SkuDropDownItemDto> purchaseReturnList = dtoTypeMap.get(SupplementOrderPurchaseType.PURCHASE_RETURN);
        if (CollectionUtils.isNotEmpty(purchaseReturnList)) {
            List<String> returnNoList = purchaseReturnList.stream().map(SupplementSkuDropDownDto.SkuDropDownItemDto::getBusinessNo).collect(Collectors.toList());
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

    public List<SupplementOrderSettleDropDownVo> getSettleOrderDropDown(SupplementOrderSettleDropDownDto dto) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getBySupplementOrderNo(dto.getSupplementOrderNo());
        if (supplementOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<SupplementOrderSettleDropDownVo> list = new ArrayList<>();
        //针对采购类型进行查询采购结算单
        if (supplementOrderPo.getSupplementType().equals(SupplementType.PRICE) || supplementOrderPo.getSupplementType().equals(SupplementType.OTHER)) {
            List<PurchaseSettleOrderPo> purchaseSettleOrderPoList = purchaseSettleOrderDao.getListBySupplierCode(supplementOrderPo.getSupplierCode(), dto.getSettleOrderNo(), PurchaseSettleStatus.WAIT_CONFIRM);
            for (PurchaseSettleOrderPo purchaseSettleOrderPo : purchaseSettleOrderPoList) {
                SupplementOrderSettleDropDownVo supplementOrderSettleDropDownVo = new SupplementOrderSettleDropDownVo();
                supplementOrderSettleDropDownVo.setSettleOrderNo(purchaseSettleOrderPo.getPurchaseSettleOrderNo());
                list.add(supplementOrderSettleDropDownVo);
            }
        }

        //通过加工类型进行查询加工结算单
        if (supplementOrderPo.getSupplementType().equals(SupplementType.PROCESS)) {
            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getListByCompleteUser(supplementOrderPo.getSupplementUser(), dto.getSettleOrderNo());

            List<String> processSettleOrderNoList = processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderNo).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(processSettleOrderNoList)) {
                List<ProcessSettleOrderPo> processSettleOrderPoList = processSettleOrderDao.getByProcessSettleOrderNos(processSettleOrderNoList, ProcessSettleStatus.WAIT_SETTLE);
                for (ProcessSettleOrderPo processSettleOrderPo : processSettleOrderPoList) {
                    SupplementOrderSettleDropDownVo supplementOrderSettleDropDownVo = new SupplementOrderSettleDropDownVo();
                    supplementOrderSettleDropDownVo.setSettleOrderNo(processSettleOrderPo.getProcessSettleOrderNo());
                    list.add(supplementOrderSettleDropDownVo);
                }
            }

        }

        return list;

    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addSettleOrder(SupplementOrderAddSettleOrderDto dto) {
        SupplementOrderPo supplementOrderPo = supplementOrderDao.getBySupplementOrderNo(dto.getSupplementOrderNo(), dto.getVersion());
        if (supplementOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (!SupplementStatus.AUDITED.equals(supplementOrderPo.getSupplementStatus())) {
            throw new ParamIllegalException("补款单处于{}状态时，才能进行操作", SupplementStatus.AUDITED.getRemark());
        }

        if (supplementOrderPo.getSupplementType().equals(SupplementType.PRICE) || supplementOrderPo.getSupplementType().equals(SupplementType.OTHER)) {
            //修改结算单金额
            PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getByPurchaseSettleOrderNo(dto.getSettleOrderNo());
            if (!purchaseSettleOrderPo.getPurchaseSettleStatus().equals(PurchaseSettleStatus.WAIT_CONFIRM)) {
                throw new ParamIllegalException("采购结算单处于{}状态时，才能进行操作", PurchaseSettleStatus.WAIT_CONFIRM.getRemark());
            }
            purchaseSettleOrderPo.setTotalPrice(purchaseSettleOrderPo.getTotalPrice().add(supplementOrderPo.getSupplementPrice()));
            purchaseSettleOrderPo.setPayPrice((purchaseSettleOrderPo.getPayPrice()).add(supplementOrderPo.getSupplementPrice()));
            purchaseSettleOrderDao.updateByIdVersion(purchaseSettleOrderPo);

            //创建结算详情
            PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
            purchaseSettleOrderItemPo.setPurchaseSettleOrderId(purchaseSettleOrderPo.getPurchaseSettleOrderId());
            purchaseSettleOrderItemPo.setPurchaseSettleOrderNo(purchaseSettleOrderPo.getPurchaseSettleOrderNo());
            purchaseSettleOrderItemPo.setBusinessNo(supplementOrderPo.getSupplementOrderNo());
            purchaseSettleOrderItemPo.setPurchaseSettleItemType(PurchaseSettleItemType.REPLENISH);
            purchaseSettleOrderItemPo.setSettleTime(supplementOrderPo.getExamineTime());
            purchaseSettleOrderItemPo.setSkuNum(0);
            purchaseSettleOrderItemPo.setSettlePrice(supplementOrderPo.getSupplementPrice());
            purchaseSettleOrderItemPo.setStatusName(supplementOrderPo.getSupplementStatus().getRemark());
            purchaseSettleOrderItemDao.insert(purchaseSettleOrderItemPo);
        }


        if (supplementOrderPo.getSupplementType().equals(SupplementType.PROCESS)) {
            //修改加工单金额
            ProcessSettleOrderPo processSettleOrderPo = processSettleOrderDao.getByProcessSettleOrderNo(dto.getSettleOrderNo());
            if (!processSettleOrderPo.getProcessSettleStatus().equals(ProcessSettleStatus.WAIT_SETTLE)) {
                throw new ParamIllegalException("加工结算单处于{}状态时，才能进行操作", ProcessSettleStatus.WAIT_SETTLE.getRemark());
            }
            processSettleOrderPo.setTotalPrice(processSettleOrderPo.getTotalPrice().add(supplementOrderPo.getSupplementPrice()));
            processSettleOrderPo.setPayPrice((processSettleOrderPo.getPayPrice()).add(supplementOrderPo.getSupplementPrice()));
            processSettleOrderDao.updateByIdVersion(processSettleOrderPo);

            //修改详情记录
            List<ProcessSettleOrderItemPo> processSettleOrderItemPos = processSettleOrderItemDao.getListByCompleteUser(supplementOrderPo.getSupplementUser(), processSettleOrderPo.getProcessSettleOrderNo());
            if (CollectionUtil.isNotEmpty(processSettleOrderItemPos)) {
                ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemPos.get(0);
                processSettleOrderItemPo.setSettlePrice((processSettleOrderItemPo.getSettlePrice()).add(supplementOrderPo.getSupplementPrice()));
                processSettleOrderItemDao.updateByIdVersion(processSettleOrderItemPo);

                //创建记录
                ProcessSettleOrderBillPo processSettleOrderBillPo = new ProcessSettleOrderBillPo();
                processSettleOrderBillPo.setProcessSettleOrderItemId(processSettleOrderItemPo.getProcessSettleOrderItemId());
                processSettleOrderBillPo.setProcessSettleOrderBillType(ProcessSettleOrderBillType.REPLENISH);
                processSettleOrderBillPo.setBusinessNo(supplementOrderPo.getSupplementOrderNo());
                processSettleOrderBillPo.setSupplementDeductType(SupplementDeductType.getSupplementDeductBySupplement(supplementOrderPo.getSupplementType()));
                processSettleOrderBillPo.setExamineTime(supplementOrderPo.getExamineTime());
                processSettleOrderBillPo.setPrice(supplementOrderPo.getSupplementPrice());
                processSettleOrderBillDao.insert(processSettleOrderBillPo);
            }
        }

        supplementOrderPo.setSettleOrderNo(dto.getSettleOrderNo());
        supplementOrderDao.updateByIdVersion(supplementOrderPo);

        return true;

    }

    /**
     * 获取个人补扣款的记录
     *
     * @author ChenWenLong
     * @date 2023/3/28 09:52
     */
    public List<SupplementDeductOrderListVo> getSupplementDeductOrderList() {
        List<SupplementDeductOrderListVo> list = new ArrayList<>();
        String userKey = GlobalContext.getUserKey();
        if (StringUtils.isBlank(userKey)) {
            throw new BizException("获取不到当前登录用户");
        }
        List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getBySupplementUserAndType(userKey, SupplementType.PROCESS);
        List<Long> idList = supplementOrderPoList.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList());
        List<SupplementOrderProcessPo> supplementOrderProcessPoList = supplementOrderProcessDao.getBatchBySupplementOrderId(idList);
        Map<Long, List<SupplementOrderProcessPo>> itemPoGroup = supplementOrderProcessPoList.stream().collect(Collectors.groupingBy(SupplementOrderProcessPo::getSupplementOrderId));

        for (SupplementOrderPo supplementOrderPo : supplementOrderPoList) {
            SupplementDeductOrderListVo supplementDeductOrderListVo = new SupplementDeductOrderListVo();
            supplementDeductOrderListVo.setSupplementDeductOrderNo(supplementOrderPo.getSupplementOrderNo());
            supplementDeductOrderListVo.setSupplementDeduct(SupplementDeduct.SUPPLEMENT);
            supplementDeductOrderListVo.setStatus(supplementOrderPo.getSupplementStatus().getRemark());
            supplementDeductOrderListVo.setTotalPrice(supplementOrderPo.getSupplementPrice());
            supplementDeductOrderListVo.setCreateTime(supplementOrderPo.getCreateTime());

            List<SupplementDeductOrderListVo.ProcessOrder> processOrderList = new ArrayList<>();
            List<SupplementOrderProcessPo> supplementOrderProcessPos = itemPoGroup.get(supplementOrderPo.getSupplementOrderId());
            if (CollectionUtil.isNotEmpty(supplementOrderProcessPos)) {
                for (SupplementOrderProcessPo orderProcessPo : supplementOrderProcessPos) {
                    SupplementDeductOrderListVo.ProcessOrder processOrder = new SupplementDeductOrderListVo.ProcessOrder();
                    processOrder.setProcessOrderNo(orderProcessPo.getProcessOrderNo());
                    processOrder.setPrice(orderProcessPo.getSupplementPrice());
                    processOrder.setRemarks(orderProcessPo.getSupplementRemarks());
                    processOrderList.add(processOrder);
                }
            }
            supplementDeductOrderListVo.setProcessOrderList(processOrderList);
            list.add(supplementDeductOrderListVo);
        }

        List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductUserAndType(userKey, DeductType.PROCESS);
        List<Long> deductIdList = deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList());
        List<DeductOrderProcessPo> deductOrderProcessPoList = deductOrderProcessDao.getBatchByDeductOrderId(deductIdList);
        Map<Long, List<DeductOrderProcessPo>> deductItemPoGroup = deductOrderProcessPoList.stream().collect(Collectors.groupingBy(DeductOrderProcessPo::getDeductOrderId));

        for (DeductOrderPo deductOrderPo : deductOrderPoList) {
            SupplementDeductOrderListVo supplementDeductOrderListVo = new SupplementDeductOrderListVo();
            supplementDeductOrderListVo.setSupplementDeductOrderNo(deductOrderPo.getDeductOrderNo());
            supplementDeductOrderListVo.setSupplementDeduct(SupplementDeduct.DEDUCT);
            supplementDeductOrderListVo.setStatus(deductOrderPo.getDeductStatus().getRemark());
            supplementDeductOrderListVo.setTotalPrice(deductOrderPo.getDeductPrice());
            supplementDeductOrderListVo.setCreateTime(deductOrderPo.getCreateTime());

            List<SupplementDeductOrderListVo.ProcessOrder> processOrderList = new ArrayList<>();
            List<DeductOrderProcessPo> deductOrderProcessPos = deductItemPoGroup.get(deductOrderPo.getDeductOrderId());
            if (CollectionUtil.isNotEmpty(deductOrderProcessPos)) {
                for (DeductOrderProcessPo orderProcessPo : deductOrderProcessPos) {
                    SupplementDeductOrderListVo.ProcessOrder processOrder = new SupplementDeductOrderListVo.ProcessOrder();
                    processOrder.setProcessOrderNo(orderProcessPo.getProcessOrderNo());
                    processOrder.setPrice(orderProcessPo.getDeductPrice());
                    processOrder.setRemarks(orderProcessPo.getDeductRemarks());
                    processOrderList.add(processOrder);
                }
            }
            supplementDeductOrderListVo.setProcessOrderList(processOrderList);
            list.add(supplementDeductOrderListVo);
        }
        return list.stream().sorted(Comparator.comparing(SupplementDeductOrderListVo::getCreateTime).reversed()).collect(Collectors.toList());
    }

    /**
     * 补款单sku导出列表
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/7/6 14:23
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportSku(SupplementOrderDto dto) {
        //条件过滤
        if (null == supplementOrderBaseService.getSearchSupplementOrderWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportSkuTotals = supplementOrderDao.getExportSkuTotals(dto);
        Assert.isTrue(null != exportSkuTotals && 0 != exportSkuTotals, () -> new ParamIllegalException("导出数据为空"));

        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_SUPPLEMENT_ORDER_SKU_EXPORT.getCode(), dto));
    }

    /**
     * 补款单sku导出总数
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2023/7/5 17:07
     */
    public Integer getExportSkuTotals(SupplementOrderDto dto) {
        //条件过滤
        if (null == supplementOrderBaseService.getSearchSupplementOrderWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        return supplementOrderDao.getExportSkuTotals(dto);

    }

    /**
     * 补款单sku导出列表
     *
     * @param dto
     * @return
     */
    public CommonResult<ExportationListResultBo<SupplementOrderExportSkuVo>> getExportSkuList(SupplementOrderDto dto) {
        ExportationListResultBo<SupplementOrderExportSkuVo> resultBo = new ExportationListResultBo<>();
        List<SupplementOrderExportSkuVo> list = new ArrayList<>();
        //条件过滤
        if (null == supplementOrderBaseService.getSearchSupplementOrderWhere(dto)) {
            return CommonResult.success(resultBo);
        }

        CommonPageResult.PageInfo<SupplementOrderPo> exportList = supplementOrderDao.getExportSkuList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<SupplementOrderPo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        Map<Long, SupplementOrderPo> supplementOrderPoMap = records.stream().collect(Collectors.toMap(SupplementOrderPo::getSupplementOrderId, Function.identity()));

        List<Long> supplementOrderIdList = records.stream().map(SupplementOrderPo::getSupplementOrderId).collect(Collectors.toList());
        List<String> supplementOrderNoList = records.stream().map(SupplementOrderPo::getSupplementOrderNo).collect(Collectors.toList());

        //查询详情
        List<SupplementOrderOtherPo> supplementOrderOtherPoList = supplementOrderOtherDao.getByBatchSupplementOrderId(supplementOrderIdList);
        List<SupplementOrderProcessPo> supplementOrderProcessPoList = supplementOrderProcessDao.getBatchBySupplementOrderId(supplementOrderIdList);
        List<SupplementOrderPurchasePo> supplementOrderPurchasePoList = supplementOrderPurchaseDao.getByBatchSupplementOrderId(supplementOrderIdList);

        // 获取结算单号
        Map<String, List<PurchaseSettleOrderItemPo>> purchaseSettleOrderItemPoMap = purchaseSettleOrderItemDao.getByBusinessNoList(supplementOrderNoList)
                .stream().collect(Collectors.groupingBy(PurchaseSettleOrderItemPo::getBusinessNo));

        //组装数据
        //其他单据
        for (SupplementOrderOtherPo supplementOrderOtherPo : supplementOrderOtherPoList) {
            SupplementOrderPo supplementOrderPo = supplementOrderPoMap.get(supplementOrderOtherPo.getSupplementOrderId());
            SupplementOrderExportSkuVo otherVo = SupplementOrderDetailConverter.supplementOrderPoToVo(supplementOrderPo, purchaseSettleOrderItemPoMap);
            otherVo.setSupplementRemarks(supplementOrderOtherPo.getSupplementRemarks());
            otherVo.setSupplementPrice(supplementOrderOtherPo.getSupplementPrice());
            list.add(otherVo);
        }

        //加工明细
        for (SupplementOrderProcessPo supplementOrderProcessPo : supplementOrderProcessPoList) {
            SupplementOrderPo supplementOrderPo = supplementOrderPoMap.get(supplementOrderProcessPo.getSupplementOrderId());
            SupplementOrderExportSkuVo processVo = SupplementOrderDetailConverter.supplementOrderPoToVo(supplementOrderPo, purchaseSettleOrderItemPoMap);

            processVo.setBusinessNo(supplementOrderProcessPo.getProcessOrderNo());
            processVo.setSettlePrice(supplementOrderProcessPo.getSettlePrice());
            processVo.setSupplementRemarks(supplementOrderProcessPo.getSupplementRemarks());
            processVo.setSupplementPrice(supplementOrderProcessPo.getSupplementPrice());
            list.add(processVo);
        }

        //采购明细
        for (SupplementOrderPurchasePo supplementOrderPurchasePo : supplementOrderPurchasePoList) {
            SupplementOrderPo supplementOrderPo = supplementOrderPoMap.get(supplementOrderPurchasePo.getSupplementOrderId());
            SupplementOrderExportSkuVo purchaseVo = SupplementOrderDetailConverter.supplementOrderPoToVo(supplementOrderPo, purchaseSettleOrderItemPoMap);

            purchaseVo.setSupplementOrderPurchaseTypeName(supplementOrderPurchasePo.getSupplementOrderPurchaseType().getRemark());
            purchaseVo.setBusinessNo(supplementOrderPurchasePo.getBusinessNo());
            purchaseVo.setSettlePrice(supplementOrderPurchasePo.getSettlePrice());
            purchaseVo.setSku(supplementOrderPurchasePo.getSku());
            purchaseVo.setSpu(supplementOrderPurchasePo.getSpu());
            purchaseVo.setSupplementNum(supplementOrderPurchasePo.getSkuNum());
            purchaseVo.setSupplementRemarks(supplementOrderPurchasePo.getSupplementRemarks());
            purchaseVo.setSupplementPrice(supplementOrderPurchasePo.getSupplementPrice());
            list.add(purchaseVo);
        }

        resultBo.setRowDataList(list);

        return CommonResult.success(resultBo);

    }

    /**
     * 批量获取单据结算金额
     *
     * @param dto:
     * @return List<DeductSupplementBusinessSettleVo>
     * @author ChenWenLong
     * @date 2023/11/15 17:59
     */
    public List<DeductSupplementBusinessSettleVo> getBusinessSettle(SupplementBusinessSettleDto dto) {
        List<DeductSupplementBusinessSettleVo> voList = new ArrayList<>();
        Map<SupplementOrderPurchaseType, List<SupplementBusinessSettleDto.SupplementBusinessSettleItemDto>> dtoTypeMap = dto.getSupplementBusinessSettleItemList()
                .stream()
                .collect(Collectors.groupingBy(SupplementBusinessSettleDto.SupplementBusinessSettleItemDto::getSupplementOrderPurchaseType));

        //采购
        List<SupplementBusinessSettleDto.SupplementBusinessSettleItemDto> productPurchaseList = new ArrayList<>();
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(SupplementOrderPurchaseType.PRODUCT_PURCHASE, new ArrayList<>()));
        productPurchaseList.addAll(dtoTypeMap.getOrDefault(SupplementOrderPurchaseType.PROCESS_PURCHASE, new ArrayList<>()));
        if (CollectionUtils.isNotEmpty(productPurchaseList)) {
            List<String> childNoList = productPurchaseList.stream().map(SupplementBusinessSettleDto.SupplementBusinessSettleItemDto::getBusinessNo).collect(Collectors.toList());
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
        List<SupplementBusinessSettleDto.SupplementBusinessSettleItemDto> sampleList = dtoTypeMap.get(SupplementOrderPurchaseType.SAMPLE);
        if (CollectionUtils.isNotEmpty(sampleList)) {
            List<String> sampleNoList = sampleList.stream()
                    .map(SupplementBusinessSettleDto.SupplementBusinessSettleItemDto::getBusinessNo)
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
        List<SupplementBusinessSettleDto.SupplementBusinessSettleItemDto> deliverList = dtoTypeMap.get(SupplementOrderPurchaseType.DELIVER);
        if (CollectionUtils.isNotEmpty(deliverList)) {
            List<String> deliverNoList = deliverList.stream().map(SupplementBusinessSettleDto.SupplementBusinessSettleItemDto::getBusinessNo).collect(Collectors.toList());
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

        //采购退货单
        List<SupplementBusinessSettleDto.SupplementBusinessSettleItemDto> purchaseReturnList = dtoTypeMap.get(SupplementOrderPurchaseType.PURCHASE_RETURN);
        if (CollectionUtils.isNotEmpty(purchaseReturnList)) {
            List<String> returnNoList = purchaseReturnList.stream().map(SupplementBusinessSettleDto.SupplementBusinessSettleItemDto::getBusinessNo).collect(Collectors.toList());
            List<PurchaseReturnOrderPo> purchaseReturnOrderPos = purchaseReturnOrderDao.getListByNoListAndSupplierCode(returnNoList, dto.getSupplierCode(), ReceiptOrderStatus.WAIT_RECEIVE);
            if (CollectionUtil.isNotEmpty(purchaseReturnOrderPos)) {
                for (PurchaseReturnOrderPo item : purchaseReturnOrderPos) {
                    DeductSupplementBusinessSettleVo deductSupplementBusinessSettleVo = new DeductSupplementBusinessSettleVo();
                    deductSupplementBusinessSettleVo.setBusinessNo(item.getReturnOrderNo());
                    voList.add(deductSupplementBusinessSettleVo);
                }
            }
        }

        return voList;

    }
}
