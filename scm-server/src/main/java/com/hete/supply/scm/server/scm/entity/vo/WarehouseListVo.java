package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author hejiaran
 * @Date 2022/11/16 19:39
 * @Version 1.0
 */
@Data
@ApiModel(description = "仓库列表信息")
public class WarehouseListVo {

    @ApiModelProperty("仓库id")
    private Long warehouseId;

    @ApiModelProperty("仓库编码")
    private String warehouseCode;

    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("仓库类型：DOMESTIC_SELF_RUN国内自营、DOMESTIC_THIRD_PARTY国内三方、FOREIGN_SELF_RUN海外自营、FOREIGN_THIRD_PARTY海外三方、MACHINING_WAREHOUSE加工仓")
    private WmsEnum.WarehouseType warehouseType;

}
