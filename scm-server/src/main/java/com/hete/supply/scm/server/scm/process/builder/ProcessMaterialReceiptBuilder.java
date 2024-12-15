package com.hete.supply.scm.server.scm.process.builder;

import cn.hutool.core.date.DateTime;
import com.hete.supply.scm.api.scm.entity.dto.ProcessMaterialReceiptCreateDto;
import com.hete.supply.scm.api.scm.entity.enums.MaterialReceiptType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessMaterialReceiptStatus;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.process.converter.ProcessMaterialReceiptConverter;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessMaterialReceiptConfirmDto;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptItemPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialReceiptPo;
import com.hete.supply.scm.server.scm.process.entity.po.RepairOrderPo;
import com.hete.support.core.holder.GlobalContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/9.
 */
public class ProcessMaterialReceiptBuilder {
    /**
     * 构建 ProcessMaterialReceiptPo 对象
     *
     * @param createDto 创建原料入库单的 DTO
     * @return 构建的 ProcessMaterialReceiptPo 对象
     */
    public static ProcessMaterialReceiptPo buildProcessMaterialReceiptPo(RepairOrderPo repairOrderPo,
                                                                         ProcessMaterialReceiptCreateDto createDto) {
        ParamValidUtils.requireNotNull(createDto, "创建返修原料收货单失败，出库单信息为空！");
        ParamValidUtils.requireNotNull(repairOrderPo, "创建返修原料收货单失败，返修单信息为空！");

        ProcessMaterialReceiptPo processMaterialReceiptPo = ProcessMaterialReceiptConverter.INSTANCE.convert(createDto);
        processMaterialReceiptPo.setPlaceOrderTime(repairOrderPo.getPlanCreateTime());
        processMaterialReceiptPo.setPlaceOrderUser(repairOrderPo.getPlanCreateUser());
        processMaterialReceiptPo.setPlaceOrderUsername(repairOrderPo.getPlanCreateUsername());
        processMaterialReceiptPo.setPlatform(repairOrderPo.getPlatform());
        processMaterialReceiptPo.setMaterialReceiptType(MaterialReceiptType.REPAIR_MATERIAL);
        processMaterialReceiptPo.setProcessMaterialReceiptStatus(ProcessMaterialReceiptStatus.WAIT_RECEIVE);
        // 可以根据实际情况设置其他属性

        return processMaterialReceiptPo;
    }

    public static List<ProcessMaterialReceiptItemPo> buildProcessMaterialReceiptItemPos(Long processMaterialReceiptId,
                                                                                        List<ProcessMaterialReceiptCreateDto.MaterialReceiptItem> createReceiptItems) {
        ParamValidUtils.requireNotEmpty(createReceiptItems, "创建返修收货单明细失败，出库单明细为空！");

        return createReceiptItems.stream()
                .map(createReceiptItem -> {
                    ProcessMaterialReceiptItemPo processMaterialReceiptItemPo = new ProcessMaterialReceiptItemPo();
                    processMaterialReceiptItemPo.setProcessMaterialReceiptId(processMaterialReceiptId);
                    processMaterialReceiptItemPo.setSku(createReceiptItem.getSku());
                    processMaterialReceiptItemPo.setSkuBatchCode(createReceiptItem.getSkuBatchCode());
                    processMaterialReceiptItemPo.setDeliveryNum(createReceiptItem.getDeliveryNum());
                    // 可以根据实际情况设置其他属性
                    return processMaterialReceiptItemPo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建 ProcessMaterialReceiptItemPo 列表
     *
     * @param updateReceiptItemDtoList 原料入库单项的 DTO 列表
     * @return 构建的 ProcessMaterialReceiptItemPo 列表
     */
    public static List<ProcessMaterialReceiptItemPo> buildProcessMaterialReceiptItemPos(List<ProcessMaterialReceiptConfirmDto.ProcessMaterialReceiptItem> updateReceiptItemDtoList) {
        ParamValidUtils.requireNotEmpty(updateReceiptItemDtoList, "确认收货失败，提交原料收货单明细信息为空！");

        return updateReceiptItemDtoList.stream()
                .map(item -> {
                    ProcessMaterialReceiptItemPo itemPo = new ProcessMaterialReceiptItemPo();
                    itemPo.setProcessMaterialReceiptItemId(item.getProcessMaterialReceiptItemId());
                    itemPo.setReceiptNum(item.getReceiptNum());
                    itemPo.setVersion(item.getVersion());
                    // 可以根据实际情况设置其他属性
                    return itemPo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 复制并构建 ProcessMaterialReceiptPo 对象
     *
     * @param processMaterialReceiptPo 原始的 ProcessMaterialReceiptPo 对象
     * @return 新的 ProcessMaterialReceiptPo 对象
     */
    public static ProcessMaterialReceiptPo buildProcessMaterialReceiptPo(ProcessMaterialReceiptPo processMaterialReceiptPo,
                                                                         Integer version) {
        ParamValidUtils.requireNotNull(processMaterialReceiptPo, "确认收货失败！原料收货单信息不存在！");
        processMaterialReceiptPo.setReceiptUsername(GlobalContext.getUsername());
        processMaterialReceiptPo.setReceiptTime(new DateTime().toLocalDateTime());
        processMaterialReceiptPo.setProcessMaterialReceiptStatus(ProcessMaterialReceiptStatus.RECEIVED);
        processMaterialReceiptPo.setVersion(version);

        // 可以根据实际情况设置其他属性

        return processMaterialReceiptPo;
    }
}
