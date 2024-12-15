package com.hete.supply.scm.server.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/13 19:42
 */
@Data
@NoArgsConstructor
public class OverseasWarehouseMsgVo {
    @ApiModelProperty(value = "id")
    private Long overseasWarehouseMsgId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "海外仓箱唛号")
    private String overseasShippingMarkNo;

    @ApiModelProperty(value = "海外仓箱唛PDF")
    private List<String> overseasShippingFileCode;

    @ApiModelProperty(value = "海外仓条码PDF")
    private List<String> overseasBarCodeFileCode;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "运单号PDF")
    private List<String> trackingNoFileCode;

    @ApiModelProperty(value = "海外仓条码信息")
    private List<OverseasWarehouseMsgItemVo> overseasWarehouseMsgItemList;

}
