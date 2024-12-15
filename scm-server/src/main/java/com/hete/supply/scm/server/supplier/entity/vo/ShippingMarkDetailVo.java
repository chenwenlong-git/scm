package com.hete.supply.scm.server.supplier.entity.vo;

import com.hete.supply.scm.server.supplier.enums.ShippingMarkBizType;
import com.hete.supply.scm.server.supplier.enums.ShippingMarkStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/14 09:29
 */
@Data
@NoArgsConstructor
public class ShippingMarkDetailVo {
    @ApiModelProperty(value = "id")
    private Long shippingMarkId;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "箱唛号")
    private String shippingMarkNo;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "是否直发")
    private BooleanType isDirectSend;

    @ApiModelProperty(value = "总发货数")
    private Integer totalDeliver;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "箱唛状态")
    private ShippingMarkStatus shippingMarkStatus;

    @ApiModelProperty(value = "箱唛业务类型")
    private ShippingMarkBizType shippingMarkBizType;

    @ApiModelProperty(value = "打印人")
    private String printUsername;

    @ApiModelProperty(value = "箱数")
    private Integer boxCnt;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "发货装箱明细")
    private List<ShippingMarkItemDetailVo> shippingMarkItemList;

    @ApiModelProperty(value = "海外仓文件信息")
    private OverseasWarehouseMsgVo overseasWarehouseMsgVo;

    @ApiModelProperty(value = "是否加急")
    private BooleanType isUrgentOrder;
}
