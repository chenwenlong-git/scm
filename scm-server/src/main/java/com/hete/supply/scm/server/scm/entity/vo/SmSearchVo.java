package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.enums.Measurement;
import com.hete.supply.scm.api.scm.entity.enums.ShelvesType;
import com.hete.supply.scm.api.scm.entity.enums.UseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/18 18:39
 */
@Data
@NoArgsConstructor
public class SmSearchVo {
    @ApiModelProperty(value = "id")
    private Long subsidiaryMaterialId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "辅料sku")
    private String smSku;

    @ApiModelProperty(value = "上下架类型")
    private ShelvesType shelvesType;

    @ApiModelProperty(value = "辅料类目代码")
    private String categoryCode;


    @ApiModelProperty(value = "辅料类目名称")
    private String categoryName;

    @ApiModelProperty(value = "辅料名称")
    private String subsidiaryMaterialName;


    @ApiModelProperty(value = "辅料类型")
    private MaterialType materialType;

    @ApiModelProperty(value = "使用类型")
    private UseType useType;

    @ApiModelProperty(value = "最小单位")
    private String unit;

    @ApiModelProperty(value = "计量单位")
    private Measurement measurement;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
