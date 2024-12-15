package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/12/12 20:14
 */
@Data
@NoArgsConstructor
public class SmExportVo {
    @ApiModelProperty(value = "辅料sku")
    private String smSku;

    @ApiModelProperty(value = "辅料名称")
    private String subsidiaryMaterialName;

    @ApiModelProperty(value = "辅料类目名称")
    private String categoryName;

    @ApiModelProperty(value = "辅料类型")
    private String materialType;

    @ApiModelProperty(value = "使用类型")
    private String useType;

    @ApiModelProperty(value = "计量单位")
    private String measurement;


    @ApiModelProperty(value = "最小颗粒度")
    private String unit;

}
