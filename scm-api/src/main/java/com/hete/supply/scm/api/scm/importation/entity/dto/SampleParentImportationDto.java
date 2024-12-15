package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/3/20 15:43
 */
@Data
public class SampleParentImportationDto {
    /**
     * 导入数据
     */
    private List<SampleParentImportationDto.ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "样品采购母单号")
        private String sampleParentOrderNo;

        @ApiModelProperty(value = "开发类型")
        private String sampleDevType;

        @ApiModelProperty(value = "spu")
        private String spu;

        @ApiModelProperty(value = "商品类目")
        private String categoryName;

        @ApiModelProperty(value = "平台")
        private String platform;

        @ApiModelProperty(value = "收货仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "收货仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "收货仓库标签")
        private String warehouseTypes;

        @ApiModelProperty(value = "业务约定交期")
        private LocalDateTime deliverDate;

        @ApiModelProperty(value = "是否首单")
        private String isFirstOrder;

        @ApiModelProperty(value = "是否加急")
        private String isUrgentOrder;

        @ApiModelProperty(value = "采购预估价")
        private BigDecimal purchasePredictPrice;

        @ApiModelProperty(value = "素材需求")
        private String sourceMaterial;

        @ApiModelProperty(value = "卖点描述")
        private String sampleDescribe;
    }
}
