package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/5 14:27
 */
@Data
public class ProcessOrderImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "加工单号")
        private String processOrderNo;

        @ApiModelProperty(value = "加工单状态")
        private Integer processOrderStatus;

        @ApiModelProperty(value = "平台")
        private String platform;

        @ApiModelProperty(value = "需求编号")
        private String requireNo;

        @ApiModelProperty(value = "订单编号")
        private String orderNo;

        @ApiModelProperty(value = "仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "收货仓库标签")
        private String warehouseTypes;

        @ApiModelProperty(value = "客户姓名")
        private String customerName;

        @ApiModelProperty(value = "创建人")
        private String createUsername;

        @ApiModelProperty(value = "创建时间")
        private LocalDateTime createTime;

        @ApiModelProperty(value = "加工单备注")
        private String processOrderNote;

        @ApiModelProperty(value = "出库备注")
        private String deliveryNote;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "加工数量")
        private Integer processNum;

        @ApiModelProperty(value = "采购单价")
        private BigDecimal purchasePrice;
    }
}
