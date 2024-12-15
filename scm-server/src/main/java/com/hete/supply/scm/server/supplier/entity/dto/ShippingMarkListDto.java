package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.scm.server.supplier.enums.ShippingMarkStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 01:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ShippingMarkListDto extends ComPageDto {
    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "发货人id")
    private String deliverUser;

    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "仓库编码")
    private List<String> warehouseCodeList;

    @ApiModelProperty(value = "仓库名称批量")
    private List<String> warehouseNameList;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;

    @ApiModelProperty(value = "箱唛状态")
    private List<ShippingMarkStatus> shippingMarkStatusList;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "关联业务子单单号批量")
    private List<String> bizChildOrderNoList;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

}
