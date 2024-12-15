package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.enums.Measurement;
import com.hete.supply.scm.api.scm.entity.enums.UseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/18 15:46
 */
@NoArgsConstructor
@Data
public class SmCreateDto {
    @NotBlank(message = "辅料名称不能为空")
    @ApiModelProperty(value = "辅料名称")
    @Length(max = 128, message = "辅料名称长度不能超过128个字符")
    private String subsidiaryMaterialName;

    @NotBlank(message = "辅料类目代码不能为空")
    @ApiModelProperty(value = "辅料类目代码")
    @Length(max = 32, message = "辅料名称长度不能超过32个字符")
    private String categoryCode;


    @NotNull(message = "辅料类型不能为空")
    @ApiModelProperty(value = "辅料类型")
    private MaterialType materialType;

    @NotNull(message = "计量单位不能为空")
    @ApiModelProperty(value = "计量单位")
    private Measurement measurement;

    @NotBlank(message = "最小单位不能为空")
    @ApiModelProperty(value = "最小单位")
    @Length(max = 32, message = "最小单位长度不能超过32个字符")
    private String unit;

    @NotNull(message = "使用类型不能为空")
    @ApiModelProperty(value = "使用类型")
    private UseType useType;

    @ApiModelProperty(value = "辅料供应商信息")
    @Valid
    private List<SmSupplierDto> smSupplierList;

    @ApiModelProperty(value = "辅料组合产品信息")
    @Valid
    private List<SmProductDto> smProductList;
}
