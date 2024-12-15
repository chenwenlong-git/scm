package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/11/21.
 */
@Data
@AllArgsConstructor
public class ProdDataAttrImportDto {
    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;
}
