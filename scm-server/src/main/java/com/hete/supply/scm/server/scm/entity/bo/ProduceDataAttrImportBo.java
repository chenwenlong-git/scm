package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/3/20 16:49
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrImportBo {

    @ApiModelProperty(value = "属性key")
    private String attrName;

    @ApiModelProperty(value = "属性value")
    private String attrValue;

    public ProduceDataAttrImportBo(String attrName, String attrValue) {
        this.attrName = attrName;
        this.attrValue = attrValue;
    }

}
