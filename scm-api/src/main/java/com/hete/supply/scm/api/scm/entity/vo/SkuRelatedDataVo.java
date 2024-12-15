package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通过sku获取采购或质检等信息
 *
 * @author ChenWenLong
 * @date 2023/12/26 13:46
 */
@Data
@NoArgsConstructor
public class SkuRelatedDataVo {

    @ApiModelProperty("sku")
    private String skuCode;

    @ApiModelProperty("采购未交数量")
    private Integer purchaseUndeliveredCnt;

    @ApiModelProperty("采购在途数量")
    private Integer purchaseInTransitCnt;

    @ApiModelProperty("总待质检数量")
    private Integer totalToCheckAmount;


    @ApiModelProperty("sku在对应仓库的数量信息")
    private List<WarehouseInventory> inventoryList;

    @Data
    public static class WarehouseInventory {

        @ApiModelProperty("仓库编码")
        private String warehouseCode;

        @ApiModelProperty("仓库名称")
        private String warehouseName;

        @ApiModelProperty("待质检数量")
        private Integer toCheckAmount;


    }

}
