package com.hete.supply.scm.server.scm.process.service.base;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.hete.supply.mc.api.msg.entity.dto.DingTalkOtoMsgDto;
import com.hete.supply.mc.api.msg.util.DingTalkMsgUtil;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.MaterialBackStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.entity.bo.MaterialBackBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.handler.DingTalkHandler;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptConfirmDto;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FreemarkerUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rockyHuas
 * @date 2023/06/06 11:22
 */
@Service
@RequiredArgsConstructor
@Validated
public class ProcessMaterialReceiptBaseService {

    private final IdGenerateService idGenerateService;
    private final ProcessOrderDao processOrderDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final ProcessMaterialReceiptItemDao processMaterialReceiptItemDao;
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ProcessOrderExtraDao processOrderExtraDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProcessMaterialDetailDao processMaterialDetailDao;

    /**
     * 确认收货
     *
     * @param dto
     * @param queriedProcessOrderPo
     * @param processDeliveryOrderVoList
     * @param containerCode
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceiptByMaterialReceiptPo(ProcessMaterialReceiptPo processMaterialReceiptPo,
                                                  ProcessMaterialReceiptConfirmDto dto,
                                                  ProcessOrderPo queriedProcessOrderPo,
                                                  List<ProcessDeliveryOrderVo> processDeliveryOrderVoList,
                                                  String containerCode) {
        String processOrderNo = processMaterialReceiptPo.getProcessOrderNo();
        ProcessOrderExtraPo processOrderExtraPo = processOrderExtraDao.getByProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
        if (processOrderExtraPo == null) {
            processOrderExtraPo = new ProcessOrderExtraPo();
            processOrderExtraPo.setProcessOrderNo(queriedProcessOrderPo.getProcessOrderNo());
            processOrderExtraDao.insert(processOrderExtraPo);
        }

        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos = dto.getProcessMaterialReceiptItems().stream().map(item -> {
            ProcessMaterialReceiptItemPo processMaterialReceiptItemPo = new ProcessMaterialReceiptItemPo();
            processMaterialReceiptItemPo.setProcessMaterialReceiptItemId(item.getProcessMaterialReceiptItemId());
            processMaterialReceiptItemPo.setReceiptNum(item.getReceiptNum());
            processMaterialReceiptItemPo.setVersion(item.getVersion());
            return processMaterialReceiptItemPo;
        }).collect(Collectors.toList());

        processMaterialReceiptItemDao.updateBatchByIdVersion(processMaterialReceiptItemPos);

        processMaterialReceiptPo.setReceiptUsername(GlobalContext.getUsername());
        processMaterialReceiptPo.setReceiptTime(new DateTime().toLocalDateTime());
        processMaterialReceiptPo.setProcessMaterialReceiptStatus(ProcessMaterialReceiptStatus.RECEIVED);
        processMaterialReceiptPo.setVersion(dto.getVersion());
        processMaterialReceiptDao.updateByIdVersion(processMaterialReceiptPo);

        // 判断是否需要归还原料
        List<ProcessOrderMaterialPo> processOrderMaterialPoList = processOrderMaterialDao.getByProcessOrderNo(processOrderNo);
        List<String> skuBatchCodes = processOrderMaterialPoList.stream()
                .map(ProcessOrderMaterialPo::getSkuBatchCode)
                .collect(Collectors.toList());
        // 原料收货单的收货数-已归还数
        List<ProcessMaterialReceiptPo> processMaterialReceiptPoList = processMaterialReceiptDao.getByProcessOrderNo(processOrderNo);
        int totalDeliveryNum = 0;
        List<MaterialBackBo.MaterialBackSku> materialBackSkus = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processMaterialReceiptPoList)) {
            List<Long> processMaterialReceiptIds = processMaterialReceiptPoList.stream()
                    .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                    .collect(Collectors.toList());
            List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds);
            totalDeliveryNum = processMaterialReceiptItemPoList.stream()
                    .filter(item -> skuBatchCodes.contains(item.getSkuBatchCode()))
                    .mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum).sum();

            materialBackSkus = processOrderMaterialPoList.stream().map(item -> {
                MaterialBackBo.MaterialBackSku materialBackSku = new MaterialBackBo.MaterialBackSku();
                materialBackSku.setSkuBatchCode(item.getSkuBatchCode());
                int totalDeliveryNumBySku = processMaterialReceiptItemPoList.stream()
                        .filter(it -> item.getSkuBatchCode().equals(it.getSkuBatchCode()))
                        .mapToInt(ProcessMaterialReceiptItemPo::getReceiptNum)
                        .sum();
                Integer backNum = item.getBackNum();
                int availableNum = totalDeliveryNumBySku - backNum;
                materialBackSku.setAvailableBackNum(availableNum);
                return materialBackSku;

            }).collect(Collectors.toList());
        }
        int totalBackNum = processOrderMaterialPoList.stream().mapToInt(ProcessOrderMaterialPo::getBackNum).sum();

        MaterialBackBo materialBackBo = new MaterialBackBo();
        if (totalDeliveryNum > 0 && totalBackNum == 0) {
            materialBackBo.setMaterialBackStatus(MaterialBackStatus.UN_BACK);
        } else if (totalDeliveryNum > 0 && totalBackNum != totalDeliveryNum) {
            materialBackBo.setMaterialBackStatus(MaterialBackStatus.PARTIAL_BACK);
        } else {
            materialBackBo.setMaterialBackStatus(MaterialBackStatus.NO_BACK);
        }
        int availableNum = totalDeliveryNum - totalBackNum;
        materialBackBo.setAvailableBackNum(availableNum);
        materialBackBo.setMaterialBackSkus(materialBackSkus);
        queriedProcessOrderPo.setMaterialBackStatus(materialBackBo.getMaterialBackStatus());

        // 仓库未签出的数量
        int deliverySize = (int) processDeliveryOrderVoList.stream().filter(it -> !it.getDeliveryState().equals(WmsEnum.DeliveryState.SIGNED_OFF)
                && !it.getDeliveryState().equals(WmsEnum.DeliveryState.FINISHED)
                && !it.getDeliveryState().equals(WmsEnum.DeliveryState.CANCELING)
                && !it.getDeliveryState().equals(WmsEnum.DeliveryState.CANCELED)).count();

        // 找出未收货的原料收货单
        int receiptSize = (int) processMaterialReceiptPoList.stream()
                .filter(it -> ProcessMaterialReceiptStatus.WAIT_RECEIVE.equals(it.getProcessMaterialReceiptStatus()))
                .count();

        // 原料全部签出并且全部收货时更新加工单的回料状态为已回料
        if (deliverySize == 0 && receiptSize == 0) {
            processOrderExtraPo.setProducedUser(GlobalContext.getUserKey());
            processOrderExtraPo.setProducedUsername(GlobalContext.getUsername());
            processOrderExtraDao.updateByIdVersion(processOrderExtraPo);

            queriedProcessOrderPo.setIsReceiveMaterial(IsReceiveMaterial.TRUE);
            queriedProcessOrderPo.setReceiveMaterialTime(new DateTime().toLocalDateTime());
        }
        queriedProcessOrderPo.setContainerCode(containerCode);
        final ProcessMaterialDetailPo processMaterialDetailPo = processMaterialDetailDao.getByDeliveryNo(processMaterialReceiptPo.getDeliveryNo());

        Integer availableProductNum = processMaterialDetailPo.getAvailableProductNum();
        Integer hasAvailableProductNum = queriedProcessOrderPo.getAvailableProductNum();
        Integer totalAvailableProductNum = availableProductNum + hasAvailableProductNum;
        queriedProcessOrderPo.setAvailableProductNum(totalAvailableProductNum);

        processOrderDao.updateByIdVersion(queriedProcessOrderPo);

        // 创建原料收货单日志
        this.createStatusChangeLog(processMaterialReceiptPo);
    }

    /**
     * 创建日志
     *
     * @param processMaterialReceiptPo
     */
    public void createStatusChangeLog(ProcessMaterialReceiptPo processMaterialReceiptPo) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.PMRSTATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.PROCESS_MATERIAL_RECEIPT_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.PMRSTATUS.name());
        bizLogCreateMqDto.setBizCode(processMaterialReceiptPo.getDeliveryNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        if (ProcessMaterialReceiptStatus.WAIT_RECEIVE.equals(processMaterialReceiptPo.getProcessMaterialReceiptStatus())) {
            bizLogCreateMqDto.setOperateUser(processMaterialReceiptPo.getDeliveryUsername());
            bizLogCreateMqDto.setOperateUsername(processMaterialReceiptPo.getDeliveryUsername());
        } else {
            bizLogCreateMqDto.setOperateUser(GlobalContext.getUserKey());
            bizLogCreateMqDto.setOperateUsername(GlobalContext.getUsername());
        }

        bizLogCreateMqDto.setContent(processMaterialReceiptPo.getProcessMaterialReceiptStatus().getDesc());

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 发送原料收货通知
     *
     * @param processMaterialReceiptPo
     */
    public void sendReceiveDingTalkMessage(ProcessMaterialReceiptPo processMaterialReceiptPo) {

        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processMaterialReceiptPo.getProcessOrderNo());
        if (null == processOrderPo) {
            throw new BizException("原料收货单关联的加工单不存在");
        }
        ArrayList<Long> materialReceiptIds = new ArrayList<>();
        materialReceiptIds.add(processMaterialReceiptPo.getProcessMaterialReceiptId());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos = processMaterialReceiptItemDao.getByMaterialReceiptIds(materialReceiptIds);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNo", processMaterialReceiptPo.getProcessOrderNo());
        hashMap.put("deliveryNo", processMaterialReceiptPo.getDeliveryNo());
        hashMap.put("status", processMaterialReceiptPo.getProcessMaterialReceiptStatus().getDesc());
        hashMap.put("operatorUsername", processMaterialReceiptPo.getReceiptUsername());
        String formatTime = LocalDateTimeUtil.format(TimeUtil.zoned(processMaterialReceiptPo.getReceiptTime(), TimeZoneId.CN).toLocalDateTime(), DatePattern.NORM_DATETIME_PATTERN);
        hashMap.put("operatorTime", formatTime);

        hashMap.put("warehouseName", processMaterialReceiptPo.getDeliveryWarehouseName());
        long totalDeliveryNum = processMaterialReceiptItemPos.stream().mapToLong(ProcessMaterialReceiptItemPo::getDeliveryNum).sum();
        hashMap.put("totalDeliveryNum", String.valueOf(totalDeliveryNum));
        long totalReceiveNum = processMaterialReceiptItemPos.stream().mapToLong(ProcessMaterialReceiptItemPo::getReceiptNum).sum();
        hashMap.put("totalReceiveNum", String.valueOf(totalReceiveNum));
        String file = FreemarkerUtil.processByFile("ding_talk_process_material_receive.ftl", hashMap);
        DingTalkOtoMsgDto msgDto = DingTalkMsgUtil.toO2oMdMsg(Lists.newArrayList(processOrderPo.getCreateUser()), "processMaterialReceive", file);
        consistencySendMqService.execSendMq(DingTalkHandler.class, msgDto);


    }

}
