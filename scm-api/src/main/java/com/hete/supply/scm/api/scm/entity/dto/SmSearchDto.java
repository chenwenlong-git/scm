package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.enums.ShelvesType;
import com.hete.supply.scm.api.scm.entity.enums.UseType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/18 18:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SmSearchDto extends ComPageDto {
    @ApiModelProperty(value = "辅料类目代码")
    private List<String> categoryCodeList;

    @ApiModelProperty(value = "辅料sku")
    private List<String> smSkuList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

    @ApiModelProperty(value = "辅料类型")
    private List<MaterialType> materialTypeList;

    @ApiModelProperty(value = "使用类型")
    private List<UseType> useTypeList;

    @ApiModelProperty(value = "上下架类型")
    private List<ShelvesType> shelvesTypeList;
}
