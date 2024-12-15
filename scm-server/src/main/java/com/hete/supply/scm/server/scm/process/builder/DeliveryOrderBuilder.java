package com.hete.supply.scm.server.scm.process.builder;

import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderMaterialPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.leave.entity.dto.DeliveryOrderCreateDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 构建 DeliveryOrderCreateDto 对象的建造者类。
 *
 * @author yanjiawei
 * Created on 2024/1/5.
 */
public class DeliveryOrderBuilder {

    /**
     * 构建 DeliveryOrderCreateDto 对象。
     *
     * @param repairOrderNo         返修单号
     * @param processOrderMaterials 加工单原料列表
     * @return 构建的 DeliveryOrderCreateDto 对象
     */
    public static DeliveryOrderCreateDto buildDeliveryOrderCreateDto(String repairOrderNo,
                                                                     String platformCode,
                                                                     List<ProcessOrderMaterialPo> processOrderMaterials) {
        DeliveryOrderCreateDto deliveryOrderCreateDto = new DeliveryOrderCreateDto();

        ProcessOrderMaterialPo oneOfMaterial = ParamValidUtils.requireNotNull(processOrderMaterials.stream()
                        .filter(processOrderMaterial -> StrUtil.isNotBlank(
                                processOrderMaterial.getWarehouseCode()))
                        .findFirst()
                        .orElse(null),
                "创建出库单失败！仓库编码为空");
        String warehouseCode = oneOfMaterial.getWarehouseCode();
        deliveryOrderCreateDto.setDeliveryType(WmsEnum.DeliveryType.PROCESS);
        deliveryOrderCreateDto.setDeliveryTypeAttribute(WmsEnum.DeliveryTypeAttribute.PROCESS_REPAIR);
        deliveryOrderCreateDto.setRelatedOrderNo(repairOrderNo);
        deliveryOrderCreateDto.setTargetWarehouseCode(warehouseCode);
        deliveryOrderCreateDto.setWarehouseCode(warehouseCode);
        deliveryOrderCreateDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
        deliveryOrderCreateDto.setPlatCode(platformCode);

        List<DeliveryOrderCreateDto.DeliveryDetail> deliveryDetails = processOrderMaterials.stream()
                .map(item -> {
                    DeliveryOrderCreateDto.DeliveryDetail deliveryDetail = new DeliveryOrderCreateDto.DeliveryDetail();
                    deliveryDetail.setSkuCode(item.getSku());
                    deliveryDetail.setPlanDeliveryAmount(item.getDeliveryNum());
                    deliveryDetail.setWarehouseLocationCode(item.getShelfCode());
                    return deliveryDetail;
                })
                .collect(Collectors.toList());
        deliveryOrderCreateDto.setDeliveryDetails(deliveryDetails);
        return deliveryOrderCreateDto;
    }

}
