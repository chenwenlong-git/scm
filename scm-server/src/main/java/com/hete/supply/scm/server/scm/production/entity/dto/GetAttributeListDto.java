package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.server.scm.production.enums.AttributeInputType;
import com.hete.supply.scm.server.scm.production.enums.AttributeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/9/14.
 */
@Data
public class GetAttributeListDto {
    @ApiModelProperty(value = "属性id列表")
    private List<Long> attrIds;

    @ApiModelProperty(value = "录入类型")
    private List<AttributeInputType> attributeInputTypeList;

    @ApiModelProperty(value = "状态")
    private AttributeStatus attributeStatus;
}
