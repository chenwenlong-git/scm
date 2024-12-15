package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.entity.po.ProduceDataAttrPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/12 18:05
 */
@Data
@NoArgsConstructor
public class ProduceDataAttrCopyBo {

    @ApiModelProperty(value = "plm的产品信息ID")
    private Long plmSkuId;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "复制的属性列表")
    private List<ProduceDataAttrPo> produceDataAttrPoList;

}
