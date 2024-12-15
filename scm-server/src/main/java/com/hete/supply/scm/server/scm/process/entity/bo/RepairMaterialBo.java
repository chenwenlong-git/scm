package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/1/8.
 */
@Data
public class RepairMaterialBo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "原料仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "库位号/货架号")
    private String shelfCode;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty(value = "出库数量")
    private Integer deliveryNum;
}
