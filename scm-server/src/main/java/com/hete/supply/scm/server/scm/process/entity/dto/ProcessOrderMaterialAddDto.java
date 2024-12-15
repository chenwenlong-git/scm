package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/12/6 17:32
 */
@Data
@ApiModel(value = "加工单补充原料参数", description = "加工单补充原料参数")
public class ProcessOrderMaterialAddDto {

    @ApiModelProperty(value = "加工单id")
    @NotNull(message = "加工单id不能为空")
    private Long processOrderId;

    @ApiModelProperty(value = "仓库编码")
    @Length(max = 32, message = "仓库编码字符长度不能超过 32 位")
    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    @ApiModelProperty(value = "产品质量")
    @NotNull(message = "产品质量不能为空")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty("版本号")
    @NotNull(message = "版本号不能为空")
    private Integer version;

    @ApiModelProperty("补充的原料skus")
    @Valid
    @NotEmpty(message = "原料skus 不能为空")
    private List<ProcessOrderMaterialDto> materialSkus;

}
