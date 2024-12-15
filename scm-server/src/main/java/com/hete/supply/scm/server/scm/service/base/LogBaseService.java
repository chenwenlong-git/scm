package com.hete.supply.scm.server.scm.service.base;

import cn.hutool.core.date.DateUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildCapacityLogBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseChildLogBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseParentLogBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.bo.SampleChildLogBo;
import com.hete.supply.scm.server.scm.sample.entity.bo.SampleParentLogBo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderInfoPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/12/21 14:26
 */
@Service
@RequiredArgsConstructor
public class LogBaseService {
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;


    public void simpleLog(LogBizModule logBizModule, Integer logVersion, String bizOrderNo,
                          String statusName, List<LogVersionBo> remark) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(StringUtils.isBlank(GlobalContext.getUserKey()) ?
                ScmConstant.SYSTEM_USER : GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(StringUtils.isBlank(GlobalContext.getUsername()) ?
                ScmConstant.SYSTEM_USER : GlobalContext.getUsername());
        bizLogCreateMqDto.setContent(statusName);
        bizLogCreateMqDto.setDetail(remark);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    public void simpleLog(LogBizModule logBizModule, Integer logVersion, String bizOrderNo,
                          String statusName, List<LogVersionBo> remark, String operateUser, String operateUsername) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(StringUtils.isBlank(operateUser) ?
                ScmConstant.MQ_DEFAULT_USER : operateUser);
        bizLogCreateMqDto.setOperateUsername(StringUtils.isBlank(operateUsername) ?
                ScmConstant.MQ_DEFAULT_USER : operateUsername);
        bizLogCreateMqDto.setContent(statusName);
        bizLogCreateMqDto.setDetail(remark);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    public void purchaseParentVersionLog(LogBizModule logBizModule, Integer logVersion, String bizOrderNo,
                                         PurchaseParentOrderPo purchaseParentOrderPo) {
        final PurchaseParentLogBo purchaseParentLogBo = new PurchaseParentLogBo();
        purchaseParentLogBo.setUpdateUsername(purchaseParentOrderPo.getUpdateUsername());
        purchaseParentLogBo.setUpdateTime(purchaseParentOrderPo.getUpdateTime());
        purchaseParentLogBo.setSpu(purchaseParentOrderPo.getSpu());
        purchaseParentLogBo.setWarehouseName(purchaseParentOrderPo.getWarehouseName());
        purchaseParentLogBo.setDeliverDate(purchaseParentOrderPo.getDeliverDate());
        purchaseParentLogBo.setPlatform(purchaseParentOrderPo.getPlatform());
        purchaseParentLogBo.setOrderRemarks(purchaseParentOrderPo.getOrderRemarks());

        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());
        List<LogVersionBo> logVersionBoList = this.getLogVersionBoList(purchaseParentLogBo);

        bizLogCreateMqDto.setDetail(logVersionBoList);
        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);

    }

    private List<LogVersionBo> getLogVersionBoList(PurchaseParentLogBo purchaseParentLogBo) {
        List<LogVersionBo> logVersionBoList = new ArrayList<>();
        final LogVersionBo logVersionBo1 = new LogVersionBo();
        logVersionBo1.setKey(purchaseParentLogBo.getUpdateUsernameKey());
        logVersionBo1.setValue(purchaseParentLogBo.getUpdateUsername());
        logVersionBo1.setValueType(purchaseParentLogBo.getUpdateUsernameType());
        final LogVersionBo logVersionBo2 = new LogVersionBo();
        logVersionBo2.setKey(purchaseParentLogBo.getUpdateTimeKey());
        logVersionBo2.setValue(purchaseParentLogBo.getUpdateTime());
        logVersionBo2.setValueType(purchaseParentLogBo.getUpdateTimeType());
        final LogVersionBo logVersionBo3 = new LogVersionBo();
        logVersionBo3.setKey(purchaseParentLogBo.getSpuKey());
        logVersionBo3.setValue(purchaseParentLogBo.getSpu());
        logVersionBo3.setValueType(purchaseParentLogBo.getSpuType());
        final LogVersionBo logVersionBo4 = new LogVersionBo();
        logVersionBo4.setKey(purchaseParentLogBo.getWarehouseNameKey());
        logVersionBo4.setValue(purchaseParentLogBo.getWarehouseName());
        logVersionBo4.setValueType(purchaseParentLogBo.getWarehouseNameType());
        final LogVersionBo logVersionBo5 = new LogVersionBo();
        logVersionBo5.setKey(purchaseParentLogBo.getDeliverDateKey());
        logVersionBo5.setValue(purchaseParentLogBo.getDeliverDate());
        logVersionBo5.setValueType(purchaseParentLogBo.getDeliverDateType());
        final LogVersionBo logVersionBo6 = new LogVersionBo();
        logVersionBo6.setKey(purchaseParentLogBo.getPlatformKey());
        logVersionBo6.setValue(purchaseParentLogBo.getPlatform());
        logVersionBo6.setValueType(purchaseParentLogBo.getPlatformType());
        final LogVersionBo logVersionBo7 = new LogVersionBo();
        logVersionBo7.setKey(purchaseParentLogBo.getOrderRemarksKey());
        logVersionBo7.setValue(purchaseParentLogBo.getOrderRemarks());
        logVersionBo7.setValueType(purchaseParentLogBo.getOrderRemarksType());
        logVersionBoList.add(logVersionBo1);
        logVersionBoList.add(logVersionBo2);
        logVersionBoList.add(logVersionBo3);
        logVersionBoList.add(logVersionBo4);
        logVersionBoList.add(logVersionBo5);
        logVersionBoList.add(logVersionBo6);
        logVersionBoList.add(logVersionBo7);

        return logVersionBoList;
    }

    public void sampleParentVersionLog(LogBizModule logBizModule, Integer logVersion,
                                       String bizOrderNo, SampleParentOrderPo sampleParentOrderPo,
                                       List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList) {
        final SampleParentLogBo sampleParentLogBo = new SampleParentLogBo();
        sampleParentLogBo.setUpdateUsername(sampleParentOrderPo.getUpdateUsername());
        sampleParentLogBo.setUpdateTime(sampleParentOrderPo.getUpdateTime());
        sampleParentLogBo.setSpu(sampleParentOrderPo.getSpu());
        sampleParentLogBo.setWarehouseName(sampleParentOrderPo.getWarehouseName());
        sampleParentLogBo.setDeliverDate(sampleParentOrderPo.getDeliverDate());
        sampleParentLogBo.setSampleParentOrderInfoPoList(sampleParentOrderInfoPoList);


        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());
        List<LogVersionBo> logVersionBoList = this.getLogVersionBoList(sampleParentLogBo);

        bizLogCreateMqDto.setDetail(logVersionBoList);
        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    private List<LogVersionBo> getLogVersionBoList(SampleParentLogBo sampleParentLogBo) {
        List<LogVersionBo> logVersionBoList = new ArrayList<>();
        final LogVersionBo logVersionBo1 = new LogVersionBo();
        logVersionBo1.setKey(sampleParentLogBo.getUpdateUsernameKey());
        logVersionBo1.setValue(sampleParentLogBo.getUpdateUsername());
        logVersionBo1.setValueType(sampleParentLogBo.getUpdateUsernameType());
        final LogVersionBo logVersionBo2 = new LogVersionBo();
        logVersionBo2.setKey(sampleParentLogBo.getUpdateTimeKey());
        logVersionBo2.setValue(sampleParentLogBo.getUpdateTime());
        logVersionBo2.setValueType(sampleParentLogBo.getUpdateTimeType());
        final LogVersionBo logVersionBo3 = new LogVersionBo();
        logVersionBo3.setKey(sampleParentLogBo.getSpuKey());
        logVersionBo3.setValue(sampleParentLogBo.getSpu());
        logVersionBo3.setValueType(sampleParentLogBo.getSpuType());
        final LogVersionBo logVersionBo4 = new LogVersionBo();
        logVersionBo4.setKey(sampleParentLogBo.getWarehouseNameKey());
        logVersionBo4.setValue(sampleParentLogBo.getWarehouseName());
        logVersionBo4.setValueType(sampleParentLogBo.getWarehouseNameType());
        final LogVersionBo logVersionBo5 = new LogVersionBo();
        logVersionBo5.setKey(sampleParentLogBo.getDeliverDateKey());
        logVersionBo5.setValue(sampleParentLogBo.getDeliverDate());
        logVersionBo5.setValueType(sampleParentLogBo.getDeliverDateType());
        final LogVersionBo logVersionBo6 = new LogVersionBo();
        logVersionBo6.setKey(sampleParentLogBo.getOrderInfoListKey());
        final List<LogVersionBo> infoBoList = Optional.ofNullable(sampleParentLogBo.getSampleParentOrderInfoPoList())
                .orElse(Collections.emptyList())
                .stream()
                .map(po -> {
                    final LogVersionBo logVersionBo = new LogVersionBo();
                    logVersionBo.setValueType(LogVersionValueType.STRING);
                    logVersionBo.setKey(po.getSampleInfoKey());
                    logVersionBo.setValue(po.getSampleInfoValue());
                    return logVersionBo;
                }).collect(Collectors.toList());
        logVersionBo6.setValue(infoBoList);
        logVersionBo6.setValueType(sampleParentLogBo.getOrderInfoListType());

        logVersionBoList.add(logVersionBo1);
        logVersionBoList.add(logVersionBo2);
        logVersionBoList.add(logVersionBo3);
        logVersionBoList.add(logVersionBo4);
        logVersionBoList.add(logVersionBo5);
        logVersionBoList.add(logVersionBo6);

        return logVersionBoList;
    }

    /**
     * 产能日志
     *
     * @param logBizModule
     * @param logVersion
     * @param bizOrderNo
     * @param purchaseChildOrderPo
     */
    public void purchaseCapacityLog(LogBizModule logBizModule, Integer logVersion, String bizOrderNo,
                                    PurchaseChildOrderPo purchaseChildOrderPo, BigDecimal capacity) {
        final PurchaseChildCapacityLogBo purchaseChildCapacityLogBo = new PurchaseChildCapacityLogBo();
        purchaseChildCapacityLogBo.setPurchaseStatus(purchaseChildOrderPo.getPurchaseOrderStatus().getRemark());
        purchaseChildCapacityLogBo.setSupplierCode(purchaseChildOrderPo.getSupplierCode());
        purchaseChildCapacityLogBo.setUpdateCapacity(capacity.toString());
        purchaseChildCapacityLogBo.setUpdateUsername(purchaseChildOrderPo.getUpdateUsername());

        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(StringUtils.isBlank(GlobalContext.getUserKey()) ?
                purchaseChildOrderPo.getPlaceOrderUser() : GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(StringUtils.isBlank(GlobalContext.getUsername()) ?
                purchaseChildOrderPo.getPlaceOrderUsername() : GlobalContext.getUsername());
        List<LogVersionBo> logVersionBoList = this.getLogVersionBoList(purchaseChildCapacityLogBo);

        bizLogCreateMqDto.setDetail(logVersionBoList);
        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    private List<LogVersionBo> getLogVersionBoList(PurchaseChildCapacityLogBo purchaseChildCapacityLogBo) {
        List<LogVersionBo> logVersionBoList = new ArrayList<>();
        final LogVersionBo logVersionBo1 = new LogVersionBo();
        logVersionBo1.setKey(purchaseChildCapacityLogBo.getPurchaseStatusKey());
        logVersionBo1.setValue(purchaseChildCapacityLogBo.getPurchaseStatus());
        logVersionBo1.setValueType(purchaseChildCapacityLogBo.getPurchaseStatusType());
        final LogVersionBo logVersionBo2 = new LogVersionBo();
        logVersionBo2.setKey(purchaseChildCapacityLogBo.getSupplierCodeKey());
        logVersionBo2.setValue(purchaseChildCapacityLogBo.getSupplierCode());
        logVersionBo2.setValueType(purchaseChildCapacityLogBo.getSupplierCodeType());
        final LogVersionBo logVersionBo4 = new LogVersionBo();
        logVersionBo4.setKey(purchaseChildCapacityLogBo.getUpdateCapacityKey());
        logVersionBo4.setValue(purchaseChildCapacityLogBo.getUpdateCapacity());
        logVersionBo4.setValueType(purchaseChildCapacityLogBo.getUpdateCapacityType());
        final LogVersionBo logVersionBo5 = new LogVersionBo();
        logVersionBo5.setKey(purchaseChildCapacityLogBo.getUpdateUsernameKey());
        logVersionBo5.setValue(purchaseChildCapacityLogBo.getUpdateUsername());
        logVersionBo5.setValueType(purchaseChildCapacityLogBo.getUpdateUsernameType());

        logVersionBoList.add(logVersionBo1);
        logVersionBoList.add(logVersionBo2);
        logVersionBoList.add(logVersionBo4);
        logVersionBoList.add(logVersionBo5);

        return logVersionBoList;
    }

    public void purchaseChildVersionLog(LogBizModule logBizModule, Integer logVersion,
                                        String bizOrderNo, PurchaseChildOrderPo purchaseChildOrderPo) {
        final PurchaseChildLogBo purchaseChildLogBo = new PurchaseChildLogBo();
        purchaseChildLogBo.setUpdateUsername(purchaseChildOrderPo.getUpdateUsername());
        purchaseChildLogBo.setUpdateTime(purchaseChildOrderPo.getUpdateTime());
        purchaseChildLogBo.setSpu(purchaseChildOrderPo.getSpu());
        purchaseChildLogBo.setWarehouseName(purchaseChildOrderPo.getWarehouseName());
        purchaseChildLogBo.setSupplierName(purchaseChildOrderPo.getSupplierName());
        purchaseChildLogBo.setDeliverDate(purchaseChildOrderPo.getExpectedOnShelvesDate());
        purchaseChildLogBo.setPlatform(purchaseChildOrderPo.getPlatform());
        purchaseChildLogBo.setOrderRemarks(purchaseChildOrderPo.getOrderRemarks());

        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());
        List<LogVersionBo> logVersionBoList = this.getLogVersionBoList(purchaseChildLogBo);

        bizLogCreateMqDto.setDetail(logVersionBoList);
        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);

    }

    private List<LogVersionBo> getLogVersionBoList(PurchaseChildLogBo purchaseChildLogBo) {
        List<LogVersionBo> logVersionBoList = new ArrayList<>();
        final LogVersionBo logVersionBo1 = new LogVersionBo();
        logVersionBo1.setKey(purchaseChildLogBo.getUpdateUsernameKey());
        logVersionBo1.setValue(purchaseChildLogBo.getUpdateUsername());
        logVersionBo1.setValueType(purchaseChildLogBo.getUpdateUsernameType());
        final LogVersionBo logVersionBo2 = new LogVersionBo();
        logVersionBo2.setKey(purchaseChildLogBo.getUpdateTimeKey());
        logVersionBo2.setValue(purchaseChildLogBo.getUpdateTime());
        logVersionBo2.setValueType(purchaseChildLogBo.getUpdateTimeType());
        final LogVersionBo logVersionBo3 = new LogVersionBo();
        logVersionBo3.setKey(purchaseChildLogBo.getSpuKey());
        logVersionBo3.setValue(purchaseChildLogBo.getSpu());
        logVersionBo3.setValueType(purchaseChildLogBo.getSpuType());
        final LogVersionBo logVersionBo4 = new LogVersionBo();
        logVersionBo4.setKey(purchaseChildLogBo.getWarehouseNameKey());
        logVersionBo4.setValue(purchaseChildLogBo.getWarehouseName());
        logVersionBo4.setValueType(purchaseChildLogBo.getWarehouseNameType());
        final LogVersionBo logVersionBo5 = new LogVersionBo();
        logVersionBo5.setKey(purchaseChildLogBo.getDeliverDateKey());
        logVersionBo5.setValue(purchaseChildLogBo.getDeliverDate());
        logVersionBo5.setValueType(purchaseChildLogBo.getDeliverDateType());
        final LogVersionBo logVersionBo6 = new LogVersionBo();
        logVersionBo6.setKey(purchaseChildLogBo.getPlatformKey());
        logVersionBo6.setValue(purchaseChildLogBo.getPlatform());
        logVersionBo6.setValueType(purchaseChildLogBo.getPlatformType());
        final LogVersionBo logVersionBo7 = new LogVersionBo();
        logVersionBo7.setKey(purchaseChildLogBo.getOrderRemarksKey());
        logVersionBo7.setValue(purchaseChildLogBo.getOrderRemarks());
        logVersionBo7.setValueType(purchaseChildLogBo.getOrderRemarksType());
        final LogVersionBo logVersionBo8 = new LogVersionBo();
        logVersionBo8.setKey(purchaseChildLogBo.getSupplierNameKey());
        logVersionBo8.setValue(purchaseChildLogBo.getSupplierName());
        logVersionBo8.setValueType(purchaseChildLogBo.getSupplierNameType());
        logVersionBoList.add(logVersionBo1);
        logVersionBoList.add(logVersionBo2);
        logVersionBoList.add(logVersionBo3);
        logVersionBoList.add(logVersionBo4);
        logVersionBoList.add(logVersionBo5);
        logVersionBoList.add(logVersionBo6);
        logVersionBoList.add(logVersionBo7);
        logVersionBoList.add(logVersionBo8);

        return logVersionBoList;
    }

    public void sampleChildVersionLog(LogBizModule logBizModule, Integer logVersion, String bizOrderNo,
                                      SampleChildOrderPo sampleChildOrderPo) {
        final SampleChildLogBo sampleChildLogBo = new SampleChildLogBo();
        sampleChildLogBo.setUpdateUsername(sampleChildOrderPo.getUpdateUsername());
        sampleChildLogBo.setUpdateTime(sampleChildOrderPo.getUpdateTime());
        sampleChildLogBo.setSpu(sampleChildOrderPo.getSpu());
        sampleChildLogBo.setWarehouseName(sampleChildOrderPo.getWarehouseName());
        sampleChildLogBo.setSupplierName(sampleChildOrderPo.getSupplierName());
        sampleChildLogBo.setDeliverDate(sampleChildOrderPo.getDeliverDate());
        sampleChildLogBo.setPlatform(sampleChildOrderPo.getPlatform());

        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(logBizModule.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(logVersion);
        bizLogCreateMqDto.setBizModule(logBizModule.name());
        bizLogCreateMqDto.setBizCode(bizOrderNo);
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());
        List<LogVersionBo> logVersionBoList = this.getLogVersionBoList(sampleChildLogBo);

        bizLogCreateMqDto.setDetail(logVersionBoList);
        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    private List<LogVersionBo> getLogVersionBoList(SampleChildLogBo sampleChildLogBo) {
        List<LogVersionBo> logVersionBoList = new ArrayList<>();
        final LogVersionBo logVersionBo1 = new LogVersionBo();
        logVersionBo1.setKey(sampleChildLogBo.getUpdateUsernameKey());
        logVersionBo1.setValue(sampleChildLogBo.getUpdateUsername());
        logVersionBo1.setValueType(sampleChildLogBo.getUpdateUsernameType());
        final LogVersionBo logVersionBo2 = new LogVersionBo();
        logVersionBo2.setKey(sampleChildLogBo.getUpdateTimeKey());
        logVersionBo2.setValue(sampleChildLogBo.getUpdateTime());
        logVersionBo2.setValueType(sampleChildLogBo.getUpdateTimeType());
        final LogVersionBo logVersionBo3 = new LogVersionBo();
        logVersionBo3.setKey(sampleChildLogBo.getSpuKey());
        logVersionBo3.setValue(sampleChildLogBo.getSpu());
        logVersionBo3.setValueType(sampleChildLogBo.getSpuType());
        final LogVersionBo logVersionBo4 = new LogVersionBo();
        logVersionBo4.setKey(sampleChildLogBo.getWarehouseNameKey());
        logVersionBo4.setValue(sampleChildLogBo.getWarehouseName());
        logVersionBo4.setValueType(sampleChildLogBo.getWarehouseNameType());
        final LogVersionBo logVersionBo5 = new LogVersionBo();
        logVersionBo5.setKey(sampleChildLogBo.getDeliverDateKey());
        logVersionBo5.setValue(sampleChildLogBo.getDeliverDate());
        logVersionBo5.setValueType(sampleChildLogBo.getDeliverDateType());
        final LogVersionBo logVersionBo6 = new LogVersionBo();
        logVersionBo6.setKey(sampleChildLogBo.getPlatformKey());
        logVersionBo6.setValue(sampleChildLogBo.getPlatform());
        logVersionBo6.setValueType(sampleChildLogBo.getPlatformType());
        final LogVersionBo logVersionBo8 = new LogVersionBo();
        logVersionBo8.setKey(sampleChildLogBo.getSupplierNameKey());
        logVersionBo8.setValue(sampleChildLogBo.getSupplierName());
        logVersionBo8.setValueType(sampleChildLogBo.getSupplierNameType());
        logVersionBoList.add(logVersionBo1);
        logVersionBoList.add(logVersionBo2);
        logVersionBoList.add(logVersionBo3);
        logVersionBoList.add(logVersionBo4);
        logVersionBoList.add(logVersionBo5);
        logVersionBoList.add(logVersionBo6);
        logVersionBoList.add(logVersionBo8);

        return logVersionBoList;
    }
}
