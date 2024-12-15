package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/10 09:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SkuAttrPricePageDto extends ComPageDto {
    @ApiModelProperty(value = "蕾丝面积属性值")
    private List<String> laceAttrValueList;


    @ApiModelProperty(value = "档长尺寸属性值")
    private List<String> sizeAttrValueList;

    @ApiModelProperty(value = "材料属性值")
    private List<String> materialValueList;
}
