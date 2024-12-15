package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yanjiawei
 * Created on 2023/12/4.
 */
@Data
@ApiModel("缺货报表")
public class InventoryShortageReportExportVo {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "SPU", example = "SPU123")
    private String spu;

    @ApiModelProperty(value = "一级类目名称", example = "")
    private String levelOneCategoryCnName;

    @ApiModelProperty(value = "二级类目名称", example = "")
    private String levelTwoCategoryCnName;

    @ApiModelProperty(value = "三级类目名称", example = "")
    private String levelThreeCategoryCnName;

    @ApiModelProperty(value = "四级类目名称", example = "")
    private String levelFourCategoryCnName;

    @ApiModelProperty(value = "商品类目末级名称", example = "假发")
    private String finalCategoryName;

    @ApiModelProperty(value = "SKU", example = "SKU789")
    private String sku;

    @ApiModelProperty(value = "加工单类型", example = "LIMITED")
    private String processOrderType;

    @ApiModelProperty(value = "加工单状态", example = "In Progress")
    private String processOrderStatus;

    @ApiModelProperty(value = "下单时间", example = "2023-01-01 08:00:00")
    private String createTimeStr;

    @ApiModelProperty(value = "下单数量", example = "30")
    private Integer processNum;

    @ApiModelProperty(value = "未交天数", example = "5")
    private Long daysToDelivery;

    @ApiModelProperty(value = "产品名称", example = "Product A")
    private String productName;

    @ApiModelProperty(value = "可用数量", example = "100")
    private Integer availableQuantity;

    @ApiModelProperty(value = "已订购数量", example = "50")
    private Integer orderedQuantity;

    @ApiModelProperty(value = "缺货数量", example = "20")
    private Integer shortageQuantity;

}
