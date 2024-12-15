package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/19 22:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "辅料名称")
        private String subsidiaryMaterialName;

        @ApiModelProperty(value = "辅料SKU")
        private String smSku;

        @ApiModelProperty(value = "所属类目")
        private String categoryName;

        @ApiModelProperty(value = "辅料类型")
        private String materialType;

        @ApiModelProperty(value = "计量单位")
        private String measurement;

        @ApiModelProperty(value = "最小颗粒度")
        private String unit;

        @ApiModelProperty(value = "使用类型")
        private String useType;

        @ApiModelProperty(value = "组合产品sku")
        private String sku;

        @ApiModelProperty(value = "数量")
        private Integer count;


    }
}
