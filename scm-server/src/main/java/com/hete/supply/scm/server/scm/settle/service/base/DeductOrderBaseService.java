package com.hete.supply.scm.server.scm.settle.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DeductOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.UdbRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.entity.bo.DeductSupplementBusinessBo;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.bo.ReturnOrderDeductOrderBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.config.SettleConfig;
import com.hete.supply.scm.server.scm.settle.converter.*;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductOrderExportBo;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderDetailVo;
import com.hete.supply.scm.server.scm.settle.entity.vo.DeductOrderVo;
import com.hete.supply.scm.server.scm.settle.enums.DeductOrderExamine;
import com.hete.supply.scm.server.scm.settle.enums.ProcessSettleOrderBillType;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.udb.api.entity.vo.UserVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 补款单基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DeductOrderBaseService {

    private final DeductOrderDao deductOrderDao;
    private final DeductOrderPurchaseDao deductOrderPurchaseDao;
    private final DeductOrderProcessDao deductOrderProcessDao;
    private final DeductOrderQualityDao deductOrderQualityDao;
    private final ScmImageBaseService scmImageBaseService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final PurchaseSettleOrderItemBaseService purchaseSettleOrderItemBaseService;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final ProcessSettleOrderItemDao processSettleOrderItemDao;
    private final ProcessSettleOrderBillDao processSettleOrderBillDao;
    private final DeductOrderOtherDao deductOrderOtherDao;
    private final DeductOrderPayDao deductOrderPayDao;
    private final DeductOrderDefectiveDao deductOrderDefectiveDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;
    private final PurchaseReturnOrderItemDao purchaseReturnOrderItemDao;
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseReturnOrderDao purchaseReturnOrderDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final SettleConfig settleConfig;
    private final UdbRemoteService udbRemoteService;

    /**
     * 扣款备注字数限制
     */
    public final static int MAX_DEDUCT_REMARKS = 500;


    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/16 20:00
     */
    public CommonPageResult.PageInfo<DeductOrderVo> searchDeductOrder(DeductOrderDto deductOrderDto) {

        if (null == this.getSearchDeductOrderWhere(deductOrderDto)) {
            return new CommonPageResult.PageInfo<>();
        }

        return deductOrderDao.selectDeductOrderPage(PageDTO.of(deductOrderDto.getPageNo(), deductOrderDto.getPageSize()), deductOrderDto);
    }

    /**
     * 详情
     *
     * @author ChenWenLong
     * @date 2022/11/16 20:00
     */
    public DeductOrderDetailVo getDeductOrderDetail(DeductOrderDetailDto dto) {
        DeductOrderPo deductOrderDetail = deductOrderDao.getByDeductOrderNo(dto.getDeductOrderNo());
        if (deductOrderDetail == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        DeductOrderDetailVo detailVo = DeductOrderConverter.INSTANCE.convert(deductOrderDetail);

        //获取扣款单采购明细
        List<DeductOrderPurchasePo> purchasePos = deductOrderPurchaseDao.getByDeductOrderId(deductOrderDetail.getDeductOrderId());
        detailVo.setDeductOrderPurchaseList(DeductOrderPurchaseConverter.INSTANCE.deductOrderPurchaseList(purchasePos));

        //获取扣款单加工明细
        List<DeductOrderProcessPo> processPos = deductOrderProcessDao.getByDeductOrderId(deductOrderDetail.getDeductOrderId());
        detailVo.setDeductOrderProcessList(DeductOrderProcessConverter.INSTANCE.deductOrderProcessList(processPos));

        //获取品质扣款明细
        List<DeductOrderQualityPo> qualityPos = deductOrderQualityDao.getByDeductOrderId(deductOrderDetail.getDeductOrderId());
        detailVo.setDeductOrderQualityList(DeductOrderQualityConverter.INSTANCE.deductOrderQualityList(qualityPos));

        //获取其他明细
        List<DeductOrderOtherPo> otherPos = deductOrderOtherDao.getByDeductOrderId(deductOrderDetail.getDeductOrderId());
        detailVo.setDeductOrderOtherList(DeductOrderOtherConverter.INSTANCE.deductOrderOtherList(otherPos));

        //获取预付款明细
        List<DeductOrderPayPo> payPos = deductOrderPayDao.getByDeductOrderId(deductOrderDetail.getDeductOrderId());
        detailVo.setDeductOrderPayList(DeductOrderPayConverter.INSTANCE.deductOrderPayList(payPos));

        //获取次品退供明细
        List<DeductOrderDefectivePo> defectivePos = deductOrderDefectiveDao.getByDeductOrderId(deductOrderDetail.getDeductOrderId());
        detailVo.setDeductOrderDefectiveList(DeductOrderDefectiveConverter.INSTANCE.deductOrderDefectiveList(defectivePos));

        //获取凭证
        List<Long> longs = List.of(deductOrderDetail.getDeductOrderId());
        List<String> scmImagePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEDUCT_ORDER, longs);
        detailVo.setFileCodeList(scmImagePos);

        //获取文件凭证
        List<String> scmFilePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEDUCT_ORDER_FILE, longs);
        detailVo.setDocumentCodeList(scmFilePos);

        if (DeductType.PROCESS.equals(deductOrderDetail.getDeductType())) {
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getByBussinessNo(deductOrderDetail.getDeductOrderNo());
            if (CollectionUtil.isNotEmpty(processSettleOrderBillPoList)) {
                ProcessSettleOrderItemPo processSettleOrderItemPo = processSettleOrderItemDao.getById(processSettleOrderBillPoList.get(0).getProcessSettleOrderItemId());
                if (processSettleOrderItemPo != null) {
                    detailVo.setSettleOrderNo(processSettleOrderItemPo.getProcessSettleOrderNo());
                }
            }
        } else {
            PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = purchaseSettleOrderItemBaseService.getItemByBusinessNo(deductOrderDetail.getDeductOrderNo());
            if (purchaseSettleOrderItemPo != null) {
                detailVo.setSettleOrderNo(purchaseSettleOrderItemPo.getPurchaseSettleOrderNo());
            }
        }

        return detailVo;
    }

    /**
     * 审核
     *
     * @author ChenWenLong
     * @date 2022/11/16 20:00
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean examine(DeductOrderUpdateDto dto) {
        DeductOrderPo deductOrderPo = deductOrderDao.getByIdVersion(dto.getDeductOrderId(), dto.getVersion());
        if (deductOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        DeductOrderExamine deductOrderExamine = DeductOrderExamine.converterType(deductOrderPo.getDeductStatus(),
                deductOrderPo.getDeductType(), dto.getExamine());

        String user = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        LocalDateTime nowTime = new DateTime().toLocalDateTime();

        deductOrderPo.setDeductOrderId(deductOrderPo.getDeductOrderId());
        deductOrderPo.setVersion(deductOrderPo.getVersion());

        DeductStatus deductStatus = deductOrderPo.getDeductStatus();
        switch (deductOrderExamine) {
            case ALREADY_CONFIRM:
                // 如果是加工扣款类型，确认提交后，进入待审核状态
                DeductStatus confirm;
                if (DeductType.PROCESS.equals(deductOrderPo.getDeductType())) {
                    confirm = deductStatus.processToWaitExamine();
                } else if (DeductType.PRICE.equals(deductOrderPo.getDeductType())) {
                    confirm = deductStatus.priceToWaitExamine();
                } else {
                    confirm = deductStatus.toConfirm();
                }
                deductOrderPo.setDeductStatus(confirm);
                deductOrderPo.setSubmitUser(user);
                deductOrderPo.setSubmitUsername(username);
                deductOrderPo.setSubmitTime(nowTime);
                break;
            case CONFIRM_AGREE:
                DeductStatus audited = deductStatus.toExamine();
                deductOrderPo.setDeductStatus(audited);
                deductOrderPo.setConfirmUser(user);
                deductOrderPo.setConfirmRefuseRemarks(null);
                deductOrderPo.setConfirmUsername(username);
                deductOrderPo.setConfirmTime(nowTime);
                break;
            case CONFIRM_REFUSE:
                DeductStatus notAudited = deductStatus.toNotExamine();
                deductOrderPo.setDeductStatus(notAudited);
                if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}原因", DeductOrderExamine.CONFIRM_REFUSE.getRemark());
                }
                if (dto.getRefuseRemarks().length() > 500) {
                    throw new ParamIllegalException("拒绝原因长度不能超过 500 个字符");
                }
                deductOrderPo.setConfirmRefuseRemarks(dto.getRefuseRemarks());
                deductOrderPo.setConfirmUser(user);
                deductOrderPo.setConfirmUsername(username);
                deductOrderPo.setConfirmTime(nowTime);
                break;
            case EXAMINE_AGREE:
                DeductStatus examine = deductStatus.toAudited();
                deductOrderPo.setDeductStatus(examine);
                deductOrderPo.setExamineUser(user);
                deductOrderPo.setExamineRefuseRemarks(null);
                deductOrderPo.setExamineUsername(username);
                deductOrderPo.setExamineTime(nowTime);
                break;
            case EXAMINE_REFUSE:
                if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}原因", DeductOrderExamine.EXAMINE_REFUSE.getRemark());
                }
                if (dto.getRefuseRemarks().length() > 500) {
                    throw new ParamIllegalException("拒绝原因长度不能超过 500 个字符");
                }
                // 如果是加工补款类型，审核拒绝后，进入待提交状态
                DeductStatus notExamine;
                if (DeductType.PROCESS.equals(deductOrderPo.getDeductType())) {
                    notExamine = deductStatus.processToNotExamine();
                } else if (DeductType.DEFECTIVE_RETURN.equals(deductOrderPo.getDeductType())) {
                    notExamine = deductStatus.toNotAuditedReturn();
                } else {
                    notExamine = deductStatus.toNotAudited();
                }
                deductOrderPo.setDeductStatus(notExamine);
                deductOrderPo.setExamineRefuseRemarks(dto.getRefuseRemarks());
                deductOrderPo.setExamineUser(user);
                deductOrderPo.setExamineUsername(username);
                deductOrderPo.setExamineTime(nowTime);
                break;
            case PRICE_CONFIRM_AGREE:
                DeductStatus priceConfirm = deductStatus.toWaitConfirm();
                deductOrderPo.setDeductStatus(priceConfirm);
                deductOrderPo.setPriceConfirmUser(user);
                deductOrderPo.setPriceRefuseRemarks(null);
                deductOrderPo.setPriceConfirmUsername(username);
                deductOrderPo.setPriceConfirmTime(nowTime);
                break;
            case PRICE_CONFIRM_REFUSE:
                DeductStatus notPriceConfirm = deductStatus.toNotWaitConfirm();
                deductOrderPo.setDeductStatus(notPriceConfirm);
                if (StringUtils.isBlank(dto.getRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}原因", DeductOrderExamine.PRICE_CONFIRM_REFUSE.getRemark());
                }
                if (dto.getRefuseRemarks().length() > 500) {
                    throw new ParamIllegalException("拒绝原因长度不能超过 500 个字符");
                }
                deductOrderPo.setPriceRefuseRemarks(dto.getRefuseRemarks());
                deductOrderPo.setPriceConfirmUser(user);
                deductOrderPo.setPriceConfirmUsername(username);
                deductOrderPo.setPriceConfirmTime(nowTime);
                break;
            default:
                throw new BizException("请求类型错误！");
        }

        // 更新处理人信息
        this.updateHandleUser(deductOrderPo, settleConfig);

        deductOrderDao.updateByIdVersion(deductOrderPo);

        // 写入日志
        this.createStatusChangeLog(deductOrderPo, deductOrderExamine);

        return true;
    }

    /**
     * 日志
     *
     * @param deductOrderPo
     */
    public void createStatusChangeLog(DeductOrderPo deductOrderPo, DeductOrderExamine deductOrderExamine) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.DEDUCTSTATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.DEDUCT_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.DEDUCTSTATUS.name());
        bizLogCreateMqDto.setBizCode(deductOrderPo.getDeductOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
        bizLogCreateMqDto.setContent(deductOrderPo.getDeductStatus().getName());

        if (DeductOrderExamine.CONFIRM_REFUSE.equals(deductOrderExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(DeductOrderExamine.CONFIRM_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(deductOrderPo.getConfirmRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        if (DeductOrderExamine.EXAMINE_REFUSE.equals(deductOrderExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(DeductOrderExamine.EXAMINE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(deductOrderPo.getExamineRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 通过供应商和审核时间查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 17:50
     */
    public List<DeductOrderDetailVo> getBySupplierCodeAndExamineTime(List<String> supplierCodeList, LocalDateTime examineTime,
                                                                     LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        List<DeductOrderPo> poList = deductOrderDao.getBySupplierCodeAndExamineTime(supplierCodeList, examineTime, examineTimeStart, examineTimeEnd);
        return DeductOrderConverter.INSTANCE.poToListDetail(poList);
    }

    /**
     * 通过用户和审核时间查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/22 17:50
     */
    public List<DeductOrderPo> getByDeductUserAndExamineTime(String deductUser, LocalDateTime examineTime,
                                                             LocalDateTime examineTimeStart, LocalDateTime examineTimeEnd) {
        return deductOrderDao.getByDeductUserAndExamineTime(deductUser, examineTime, examineTimeStart, examineTimeEnd);
    }

    /**
     * 通过编号批量更新状态
     *
     * @author ChenWenLong
     * @date 2023/1/14 15:38
     */
    public void updateBatchDeductOrderNo(List<String> deductOrderNos, DeductStatus deductStatus) {
        deductOrderDao.updateBatchDeductOrderNo(deductOrderNos, deductStatus);
    }

    /**
     * 通过编号批量查询列表其他类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 14:59
     */
    public List<DeductOrderOtherPo> getOtherBatchDeductOrderNo(List<String> deductOrderNoList) {
        return deductOrderOtherDao.getOtherBatchDeductOrderNo(deductOrderNoList);
    }

    /**
     * 通过编号批量查询列表预付款类型详情
     *
     * @author ChenWenLong
     * @date 2023/3/7 14:59
     */
    public List<DeductOrderPayPo> getPayBatchDeductOrderNo(List<String> deductOrderNoList) {
        return deductOrderPayDao.getPayBatchDeductOrderNo(deductOrderNoList);
    }

    public List<DeductOrderExportBo> getPurchaseBatchDeductOrderNo(List<String> deductOrderNoList) {
        return deductOrderDao.getPurchaseBatchDeductOrderNo(deductOrderNoList);
    }

    public List<DeductOrderExportBo> getQualityBatchDeductOrderNo(List<String> deductOrderNoList) {
        return deductOrderDao.getQualityBatchDeductOrderNo(deductOrderNoList);
    }

    /**
     * 通过退货单创建扣款单
     *
     * @param bo:
     * @return DeductOrderPo
     * @author ChenWenLong
     * @date 2023/6/21 13:45
     */
    @Transactional(rollbackFor = Exception.class)
    public DeductOrderPo addReturnOrderDeductOrder(@NotNull ReturnOrderDeductOrderBo bo) {
        //创建的扣款单数据
        DeductOrderPo deductOrderPo = new DeductOrderPo();
        // 返回自增序列号，返回格式为key+自增id，其中数字部分最少6位
        String deductOrderNo = idGenerateService.getIncrCode(ScmConstant.DEDUCT_ORDER_NO_PREFIX, TimeType.CN_DAY_YYYY, 4);
        BigDecimal deductPrice = Optional.ofNullable(bo.getDeductOrderDefectiveList())
                .orElse(Collections.emptyList())
                .stream()
                .map(DeductOrderDefectiveDto::getDeductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        deductOrderPo.setSupplierCode(bo.getSupplierCode());
        deductOrderPo.setSupplierName(bo.getSupplierName());
        deductOrderPo.setDeductOrderNo(deductOrderNo);
        deductOrderPo.setDeductPrice(deductPrice);
        deductOrderPo.setDeductType(DeductType.DEFECTIVE_RETURN);
        deductOrderPo.setDeductStatus(DeductStatus.WAIT_EXAMINE);
        this.updateHandleUser(deductOrderPo, settleConfig);
        deductOrderDao.insert(deductOrderPo);
        Long deductOrderId = deductOrderPo.getDeductOrderId();

        //增加扣款单表次品退供明细
        List<DeductOrderDefectivePo> deductOrderDefectivePoList = DeductOrderDefectiveConverter.INSTANCE.convert(bo.getDeductOrderDefectiveList());
        List<DeductOrderDefectivePo> deductOrderDefectivePos = deductOrderDefectivePoList.stream().map(item -> item.setDeductOrderId(deductOrderId)).collect(Collectors.toList());
        deductOrderDefectiveDao.insertBatch(deductOrderDefectivePos);

        return deductOrderPo;
    }

    /**
     * 扣款单搜索列表和导出条件
     *
     * @param dto:
     * @return DeductOrderDto
     * @author ChenWenLong
     * @date 2023/7/5 16:23
     */
    public DeductOrderDto getSearchDeductOrderWhere(DeductOrderDto dto) {

        if (StringUtils.isNotBlank(dto.getPurchaseSettleOrderNo())) {

            List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = purchaseSettleOrderItemDao.getByPurchaseSettleOrderNo(dto.getPurchaseSettleOrderNo(), PurchaseSettleItemType.DEDUCT);
            if (CollectionUtil.isEmpty(purchaseSettleOrderItemPoList)) {
                return null;
            }
            List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(purchaseSettleOrderItemPoList.stream().map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(deductOrderPoList)) {
                return null;
            }

            dto.setDeductOrderIds(deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(dto.getProcessSettleOrderNo())) {

            List<ProcessSettleOrderItemPo> processSettleOrderItemPoList = processSettleOrderItemDao.getByProcessSettleOrderNo(dto.getProcessSettleOrderNo());
            if (CollectionUtil.isEmpty(processSettleOrderItemPoList)) {
                return null;
            }
            List<ProcessSettleOrderBillPo> processSettleOrderBillPoList = processSettleOrderBillDao.getBatchProcessSettleOrderItemId(processSettleOrderItemPoList.stream().map(ProcessSettleOrderItemPo::getProcessSettleOrderItemId).collect(Collectors.toList()), ProcessSettleOrderBillType.DEDUCT);
            if (CollectionUtil.isEmpty(processSettleOrderBillPoList)) {
                return null;
            }
            List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByDeductOrderNoList(processSettleOrderBillPoList.stream().map(ProcessSettleOrderBillPo::getBusinessNo).collect(Collectors.toList()));
            if (CollectionUtil.isEmpty(deductOrderPoList)) {
                return null;
            }

            dto.setDeductOrderIds(deductOrderPoList.stream().map(DeductOrderPo::getDeductOrderId).collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * 验证单据信息的数据
     *
     * @param deductSupplementBusinessBoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/16 17:57
     */
    public void verifyDeductSupplementBusiness(List<DeductSupplementBusinessBo> deductSupplementBusinessBoList,
                                               String supplierCode) {
        if (CollectionUtils.isEmpty(deductSupplementBusinessBoList)) {
            return;
        }
        log.info("验证单据信息的数据。bo={}", JacksonUtil.parse2Str(deductSupplementBusinessBoList));
        Map<DeductOrderPurchaseType, List<DeductSupplementBusinessBo>> deductOrderPurchaseMap = deductSupplementBusinessBoList.stream()
                .collect(Collectors.groupingBy(DeductSupplementBusinessBo::getDeductOrderPurchaseType));

        //采购大货
        List<DeductSupplementBusinessBo> productProductPurchaseList = deductOrderPurchaseMap.get(DeductOrderPurchaseType.PRODUCT_PURCHASE);
        if (CollectionUtils.isNotEmpty(productProductPurchaseList)) {
            List<String> childNoList = productProductPurchaseList.stream().map(DeductSupplementBusinessBo::getBusinessNo).collect(Collectors.toList());
            List<PurchaseChildOrderPo> purchaseChildOrderPos = purchaseChildOrderDao.getListByNoList(childNoList);
            for (PurchaseChildOrderPo purchaseChildOrderPo : purchaseChildOrderPos) {
                if (StringUtils.isNotBlank(supplierCode) && !supplierCode.equals(purchaseChildOrderPo.getSupplierCode())) {
                    throw new ParamIllegalException("单据号:{}关联不到对应供应商{}，请修改单据号！", purchaseChildOrderPo.getPurchaseChildOrderNo(), supplierCode);
                }
                if (!PurchaseBizType.PRODUCT.equals(purchaseChildOrderPo.getPurchaseBizType())) {
                    throw new ParamIllegalException("单据号:{}的采购类型与当前选择的类型：{}不一致，请确认后再提交！",
                            purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            PurchaseBizType.PRODUCT.getRemark());
                }
                List<PurchaseOrderStatus> purchaseOrderStatusNotList = PurchaseOrderStatus.getSupplementDeductOrderNotStatus();
                StringBuilder purchaseOrderStatusNotName = new StringBuilder();
                for (PurchaseOrderStatus purchaseOrderStatus : purchaseOrderStatusNotList) {
                    purchaseOrderStatusNotName.append(purchaseOrderStatus.getRemark()).append("、");
                }
                if (purchaseOrderStatusNotName.length() > 0) {
                    purchaseOrderStatusNotName.setLength(purchaseOrderStatusNotName.length() - 1);
                }
                if (purchaseOrderStatusNotList.contains(purchaseChildOrderPo.getPurchaseOrderStatus())) {
                    throw new ParamIllegalException("单据号:{}当前的采购状态为：{}，系统要求不能添加{}状态的订单！",
                            purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            purchaseChildOrderPo.getPurchaseOrderStatus().getRemark(),
                            purchaseOrderStatusNotName);
                }
            }
            List<String> childNoFilterList = purchaseChildOrderPos.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).collect(Collectors.toList());
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPos = purchaseChildOrderItemDao.getListByChildNoList(childNoList);
            for (DeductSupplementBusinessBo dtoPurchaseItem : productProductPurchaseList) {
                PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPos.stream()
                        .filter(itemPo -> childNoFilterList.contains(itemPo.getPurchaseChildOrderNo()))
                        .filter(itemPo -> {
                            if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                                return itemPo.getPurchaseChildOrderNo().equals(dtoPurchaseItem.getBusinessNo())
                                        && StringUtils.isNotBlank(itemPo.getSku())
                                        && itemPo.getSku().equals(dtoPurchaseItem.getSku());
                            } else {
                                return itemPo.getPurchaseChildOrderNo().equals(dtoPurchaseItem.getBusinessNo());
                            }
                        }).findAny().orElse(null);
                String errorMessage;
                if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                    errorMessage = StrUtil.format("sku和单据号不对应：明细的sku：{}与单据号：{}不一致，请选择正确的单据号或sku再提交！",
                            dtoPurchaseItem.getSku(), dtoPurchaseItem.getBusinessNo());
                } else {
                    errorMessage = StrUtil.format("单据号:{}查询不到对应{}的单据，请确认调整后再提交！",
                            dtoPurchaseItem.getBusinessNo(),
                            DeductOrderPurchaseType.PRODUCT_PURCHASE.getRemark());
                }
                Assert.notNull(purchaseChildOrderItemPo, () -> new ParamIllegalException(errorMessage));
            }

        }

        //采购加工
        List<DeductSupplementBusinessBo> productProcessPurchaseList = deductOrderPurchaseMap.get(DeductOrderPurchaseType.PROCESS_PURCHASE);
        if (CollectionUtils.isNotEmpty(productProcessPurchaseList)) {
            List<String> childNoList = productProcessPurchaseList.stream().map(DeductSupplementBusinessBo::getBusinessNo).collect(Collectors.toList());
            List<PurchaseChildOrderPo> purchaseChildOrderPos = purchaseChildOrderDao.getListByNoList(childNoList);
            for (PurchaseChildOrderPo purchaseChildOrderPo : purchaseChildOrderPos) {
                if (StringUtils.isNotBlank(supplierCode) && !supplierCode.equals(purchaseChildOrderPo.getSupplierCode())) {
                    throw new ParamIllegalException("单据号:{}关联不到对应供应商{}，请修改单据号！", purchaseChildOrderPo.getPurchaseChildOrderNo(), supplierCode);
                }

                if (!PurchaseBizType.PROCESS.equals(purchaseChildOrderPo.getPurchaseBizType())) {
                    throw new ParamIllegalException("{}的采购类型与当前选择的类型：{}不一致，请确认后再提交！",
                            purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            purchaseChildOrderPo.getPurchaseBizType().getRemark(),
                            PurchaseBizType.PROCESS.getRemark());
                }
                List<PurchaseOrderStatus> purchaseOrderStatusNotList = PurchaseOrderStatus.getSupplementDeductOrderNotStatus();
                StringBuilder purchaseOrderStatusNotName = new StringBuilder();
                for (PurchaseOrderStatus purchaseOrderStatus : purchaseOrderStatusNotList) {
                    purchaseOrderStatusNotName.append(purchaseOrderStatus.getRemark()).append("、");
                }
                if (purchaseOrderStatusNotName.length() > 0) {
                    purchaseOrderStatusNotName.setLength(purchaseOrderStatusNotName.length() - 1);
                }
                if (purchaseOrderStatusNotList.contains(purchaseChildOrderPo.getPurchaseOrderStatus())) {
                    throw new ParamIllegalException("{}当前的采购状态为：{}，系统要求不能增加{}状态的订单！",
                            purchaseChildOrderPo.getPurchaseChildOrderNo(),
                            purchaseChildOrderPo.getPurchaseOrderStatus().getRemark(),
                            purchaseOrderStatusNotName);
                }
            }

            List<String> childNoFilterList = purchaseChildOrderPos.stream().map(PurchaseChildOrderPo::getPurchaseChildOrderNo).collect(Collectors.toList());
            List<PurchaseChildOrderItemPo> purchaseChildOrderItemPos = purchaseChildOrderItemDao.getListByChildNoList(childNoList);
            for (DeductSupplementBusinessBo dtoPurchaseItem : productProcessPurchaseList) {
                PurchaseChildOrderItemPo purchaseChildOrderItemPo = purchaseChildOrderItemPos.stream()
                        .filter(itemPo -> childNoFilterList.contains(itemPo.getPurchaseChildOrderNo()))
                        .filter(itemPo -> {
                            if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                                return itemPo.getPurchaseChildOrderNo().equals(dtoPurchaseItem.getBusinessNo())
                                        && StringUtils.isNotBlank(itemPo.getSku())
                                        && itemPo.getSku().equals(dtoPurchaseItem.getSku());
                            } else {
                                return itemPo.getPurchaseChildOrderNo().equals(dtoPurchaseItem.getBusinessNo());
                            }
                        }).findAny().orElse(null);
                String errorMessage;
                if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                    errorMessage = StrUtil.format("sku和单据号不对应：明细的sku：{}与单据号：{}不一致，请选择正确的单据号或sku再提交！",
                            dtoPurchaseItem.getSku(), dtoPurchaseItem.getBusinessNo());
                } else {
                    errorMessage = StrUtil.format("单据号:{}查询不到对应{}的单据，请确认调整后再提交！",
                            dtoPurchaseItem.getBusinessNo(),
                            DeductOrderPurchaseType.PROCESS_PURCHASE.getRemark());
                }
                Assert.notNull(purchaseChildOrderItemPo, () -> new ParamIllegalException(errorMessage));
            }

        }

        //采购退货单查询详情
        List<DeductSupplementBusinessBo> purchaseReturnList = deductOrderPurchaseMap.get(DeductOrderPurchaseType.PURCHASE_RETURN);
        if (CollectionUtils.isNotEmpty(purchaseReturnList)) {
            List<String> returnNoList = purchaseReturnList.stream().map(DeductSupplementBusinessBo::getBusinessNo).collect(Collectors.toList());
            List<PurchaseReturnOrderPo> purchaseReturnOrderPoList = purchaseReturnOrderDao.getListByReturnBizNoList(returnNoList);
            for (PurchaseReturnOrderPo purchaseReturnOrderPo : purchaseReturnOrderPoList) {
                if (StringUtils.isNotBlank(supplierCode) && !supplierCode.equals(purchaseReturnOrderPo.getSupplierCode())) {
                    throw new ParamIllegalException("单据号:{}关联不到对应供应商{}，请修改单据号！", purchaseReturnOrderPo.getPurchaseChildOrderNo(), supplierCode);
                }
            }
            List<PurchaseReturnOrderItemPo> purchaseReturnOrderItemPos = purchaseReturnOrderItemDao.getListByReturnNoList(returnNoList);
            for (DeductSupplementBusinessBo dtoPurchaseItem : purchaseReturnList) {
                PurchaseReturnOrderItemPo purchaseReturnOrderItemPo = purchaseReturnOrderItemPos.stream()
                        .filter(itemPo -> {
                            if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                                return itemPo.getReturnOrderNo().equals(dtoPurchaseItem.getBusinessNo())
                                        && StringUtils.isNotBlank(itemPo.getSku())
                                        && itemPo.getSku().equals(dtoPurchaseItem.getSku());
                            } else {
                                return itemPo.getReturnOrderNo().equals(dtoPurchaseItem.getBusinessNo());
                            }
                        }).findAny().orElse(null);
                String errorMessage;
                if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                    errorMessage = StrUtil.format("sku和单据号不对应：明细的sku：{}与单据号：{}不一致，请选择正确的单据号或sku再提交！",
                            dtoPurchaseItem.getSku(), dtoPurchaseItem.getBusinessNo());
                } else {
                    errorMessage = StrUtil.format("单据号:{}查询不到对应{}的单据，请确认调整后再提交！",
                            dtoPurchaseItem.getBusinessNo(),
                            DeductOrderPurchaseType.PURCHASE_RETURN.getRemark());
                }
                Assert.notNull(purchaseReturnOrderItemPo, () -> new ParamIllegalException(errorMessage));
            }
        }

        //新样品查询
        List<DeductSupplementBusinessBo> sampleList = deductOrderPurchaseMap.get(DeductOrderPurchaseType.SAMPLE);
        if (CollectionUtils.isNotEmpty(sampleList)) {
            List<String> sampleNoList = sampleList.stream().map(DeductSupplementBusinessBo::getBusinessNo).collect(Collectors.toList());
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(sampleNoList);
            for (DeductSupplementBusinessBo dtoPurchaseItem : sampleList) {
                DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                        .filter(po -> {
                            if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                                return po.getDevelopSampleOrderNo().equals(dtoPurchaseItem.getBusinessNo())
                                        && StringUtils.isNotBlank(po.getSku())
                                        && po.getSku().equals(dtoPurchaseItem.getSku());
                            } else {
                                return po.getDevelopSampleOrderNo().equals(dtoPurchaseItem.getBusinessNo());
                            }
                        }).findAny().orElse(null);
                String errorMessage;
                if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                    errorMessage = StrUtil.format("sku和单据号不对应：明细的sku：{}与单据号：{}不一致，请选择正确的单据号或sku再提交！",
                            dtoPurchaseItem.getSku(), dtoPurchaseItem.getBusinessNo());
                } else {
                    errorMessage = StrUtil.format("单据号:{}查询不到对应{}的单据，请确认调整后再提交！",
                            dtoPurchaseItem.getBusinessNo(),
                            DeductOrderPurchaseType.SAMPLE.getRemark());
                }
                if (developSampleOrderPo == null) {
                    throw new ParamIllegalException(errorMessage);
                }
                if (StringUtils.isNotBlank(supplierCode) && !supplierCode.equals(developSampleOrderPo.getSupplierCode())) {
                    throw new ParamIllegalException("单据号:{}关联不到对应供应商{}，请修改单据号", developSampleOrderPo.getDevelopSampleOrderNo(), supplierCode);
                }
            }
        }

        //发货单查询
        List<DeductSupplementBusinessBo> deliverList = deductOrderPurchaseMap.get(DeductOrderPurchaseType.DELIVER);
        if (CollectionUtils.isNotEmpty(deliverList)) {
            List<String> deliverNoList = deliverList.stream().map(DeductSupplementBusinessBo::getBusinessNo).collect(Collectors.toList());
            List<PurchaseDeliverOrderPo> purchaseDeliverOrderPos = purchaseDeliverOrderDao.getListByNoList(deliverNoList);
            for (PurchaseDeliverOrderPo purchaseDeliverOrderPo : purchaseDeliverOrderPos) {
                if (StringUtils.isNotBlank(supplierCode) && !supplierCode.equals(purchaseDeliverOrderPo.getSupplierCode())) {
                    throw new ParamIllegalException("单据号:{}关联不到对应供应商{}，请修改单据号", purchaseDeliverOrderPo.getPurchaseDeliverOrderNo(), supplierCode);
                }
            }
            List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(deliverNoList);
            for (DeductSupplementBusinessBo dtoPurchaseItem : deliverList) {
                PurchaseDeliverOrderItemPo purchaseDeliverOrderItemPo = purchaseDeliverOrderItemPos.stream()
                        .filter(itemPo -> {
                            if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                                return itemPo.getPurchaseDeliverOrderNo().equals(dtoPurchaseItem.getBusinessNo())
                                        && StringUtils.isNotBlank(itemPo.getSku())
                                        && itemPo.getSku().equals(dtoPurchaseItem.getSku());
                            } else {
                                return itemPo.getPurchaseDeliverOrderNo().equals(dtoPurchaseItem.getBusinessNo());
                            }
                        }).findAny().orElse(null);
                String errorMessage;
                if (StringUtils.isNotBlank(dtoPurchaseItem.getSku())) {
                    errorMessage = StrUtil.format("sku和单据号不对应：明细的sku：{}与单据号：{}不一致，请选择正确的单据号或sku再提交！",
                            dtoPurchaseItem.getSku(), dtoPurchaseItem.getBusinessNo());
                } else {
                    errorMessage = StrUtil.format("单据号:{}查询不到对应{}的单据，请确认调整后再提交！",
                            dtoPurchaseItem.getBusinessNo(),
                            DeductOrderPurchaseType.DELIVER.getRemark());
                }
                Assert.notNull(purchaseDeliverOrderItemPo, () -> new ParamIllegalException(errorMessage));
            }
        }

        //样品退货单
        List<DeductSupplementBusinessBo> sampleReturnList = deductOrderPurchaseMap.get(DeductOrderPurchaseType.SAMPLE_RETURN);
        if (CollectionUtils.isNotEmpty(sampleReturnList)) {
            List<String> sampleReturnOrderNoList = sampleReturnList.stream().map(DeductSupplementBusinessBo::getBusinessNo).collect(Collectors.toList());
            List<SampleReturnOrderPo> sampleReturnOrderPoList = sampleReturnOrderDao.getListByNoListAndSupplierCode(supplierCode, sampleReturnOrderNoList);
            for (DeductSupplementBusinessBo deductSupplementBusinessBo : sampleReturnList) {
                SampleReturnOrderPo sampleReturnOrderPo = sampleReturnOrderPoList.stream()
                        .filter(po -> po.getSampleReturnOrderNo().equals(deductSupplementBusinessBo.getBusinessNo()))
                        .findAny()
                        .orElse(null);
                if (sampleReturnOrderPo == null) {
                    throw new ParamIllegalException("单据号:{}查询不到对应{}的单据，请确认调整后再提交！",
                            deductSupplementBusinessBo.getBusinessNo(),
                            DeductOrderPurchaseType.SAMPLE_RETURN.getRemark());
                }
                if (StringUtils.isNotBlank(supplierCode) && !supplierCode.equals(sampleReturnOrderPo.getSupplierCode())) {
                    throw new ParamIllegalException("单据号:{}关联不到对应供应商{}，请修改单据号", deductSupplementBusinessBo.getBusinessNo(), supplierCode);
                }
            }
        }
    }

    /**
     * 根据状态来更新处理人
     *
     * @param deductOrderPo:
     * @param settleConfig:
     * @return DeductOrderPo
     * @author ChenWenLong
     * @date 2024/8/31 21:05
     */
    public DeductOrderPo updateHandleUser(DeductOrderPo deductOrderPo, SettleConfig settleConfig) {
        log.info("根据状态来更新处理人的开始：po=>{}", JacksonUtil.parse2Str(deductOrderPo));
        if (DeductStatus.WAIT_SUBMIT.equals(deductOrderPo.getDeductStatus())) {
            if (StringUtils.isNotBlank(deductOrderPo.getCreateUser())) {
                deductOrderPo.setHandleUser(deductOrderPo.getCreateUser());
                if (StringUtils.isNotBlank(deductOrderPo.getCreateUsername())) {
                    deductOrderPo.setHandleUsername(deductOrderPo.getCreateUsername());
                } else {
                    final UserVo userVo = udbRemoteService.getByUserCode(deductOrderPo.getCreateUser());
                    if (null == userVo) {
                        throw new BizException("创建人的数据不存在，请联系系统管理员！", deductOrderPo.getCreateUser());
                    }
                    deductOrderPo.setHandleUsername(userVo.getUsername());
                    deductOrderPo.setCreateUsername(userVo.getUsername());
                }
            } else {
                deductOrderPo.setHandleUser(GlobalContext.getUserKey());
                deductOrderPo.setHandleUsername(GlobalContext.getUsername());
            }
            deductOrderPo.setHandleTime(LocalDateTime.now());
        } else if (DeductStatus.WAIT_PRICE.equals(deductOrderPo.getDeductStatus())) {
            deductOrderPo.setHandleUser(settleConfig.getConfirmUser());
            final UserVo userVo = udbRemoteService.getByUserCode(settleConfig.getConfirmUser());
            if (null == userVo) {
                throw new BizException("确认人的数据不存在，请联系系统管理员进行配置！", settleConfig.getConfirmUser());
            }
            deductOrderPo.setHandleUsername(userVo.getUsername());
            deductOrderPo.setHandleTime(LocalDateTime.now());
        } else if (DeductStatus.WAIT_CONFIRM.equals(deductOrderPo.getDeductStatus())) {
            deductOrderPo.setHandleUser(settleConfig.getSupplierName());
            deductOrderPo.setHandleUsername(settleConfig.getSupplierName());
            deductOrderPo.setHandleTime(LocalDateTime.now());
        } else if (DeductStatus.WAIT_EXAMINE.equals(deductOrderPo.getDeductStatus())) {
            deductOrderPo.setHandleUser(settleConfig.getExamineUser());
            final UserVo userVo = udbRemoteService.getByUserCode(settleConfig.getExamineUser());
            if (null == userVo) {
                throw new BizException("审核人的数据不存在，请联系系统管理员进行配置！", settleConfig.getExamineUser());
            }
            deductOrderPo.setHandleUsername(userVo.getUsername());
            deductOrderPo.setHandleTime(LocalDateTime.now());
        } else if (DeductStatus.AUDITED.equals(deductOrderPo.getDeductStatus())) {
            deductOrderPo.setHandleUser(deductOrderPo.getCreateUser());
            if (StringUtils.isNotBlank(deductOrderPo.getCreateUser())) {
                deductOrderPo.setHandleUser(deductOrderPo.getCreateUser());
                if (StringUtils.isNotBlank(deductOrderPo.getCreateUsername())) {
                    deductOrderPo.setHandleUsername(deductOrderPo.getCreateUsername());
                } else {
                    final UserVo userVo = udbRemoteService.getByUserCode(deductOrderPo.getCreateUser());
                    if (null == userVo) {
                        throw new BizException("创建人的数据不存在，请联系系统管理员！", deductOrderPo.getCreateUser());
                    }
                    deductOrderPo.setHandleUsername(userVo.getUsername());
                    deductOrderPo.setCreateUsername(userVo.getUsername());
                }
            } else {
                deductOrderPo.setHandleUser(GlobalContext.getUserKey());
                deductOrderPo.setHandleUsername(GlobalContext.getUsername());
            }
            deductOrderPo.setHandleTime(LocalDateTime.now());
        }
        log.info("根据状态来更新处理人的结果：po=>{}", JacksonUtil.parse2Str(deductOrderPo));
        return deductOrderPo;
    }

}
