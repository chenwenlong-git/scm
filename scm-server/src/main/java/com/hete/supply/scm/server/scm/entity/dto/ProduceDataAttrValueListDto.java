package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/4/10 10:27
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrValueListDto {

    @ApiModelProperty(value = "排除sku")
    private String notSku;

    @ApiModelProperty(value = "属性详情列表")
    @NotEmpty(message = "属性详情列表不能为空")
    private List<@Valid ProduceDataAttrValueListItemDto> produceDataAttrValueListItem;

    @Data
    public static class ProduceDataAttrValueListItemDto {
        @ApiModelProperty(value = "属性ID")
        @NotNull(message = "属性ID不能为空")
        private Long attributeNameId;

        @ApiModelProperty(value = "属性value")
        @NotEmpty(message = "属性value不能为空")
        private List<String> attrValueList;
    }

}
