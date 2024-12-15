package com.hete.supply.scm.server.supplier.entity.vo;

import com.hete.supply.scm.server.supplier.enums.ShippingMarkStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 01:41
 */
@Data
@NoArgsConstructor
public class ShippingMarkListVo {
    @ApiModelProperty(value = "id")
    private Long shippingMarkId;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "箱唛状态")
    private ShippingMarkStatus shippingMarkStatus;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库类型")
    private List<String> warehouseTypeList;

    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;

    @ApiModelProperty(value = "箱数")
    private Integer boxCnt;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发货人id")
    private String deliverUser;


    @ApiModelProperty(value = "发货人名称")
    private String deliverUsername;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliverTime;


}
