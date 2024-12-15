package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/10/12 15:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduceDataDetailBo {

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu主图")
    private List<String> spuFileCodeList;

    @ApiModelProperty(value = "状态")
    private BindingProduceData bindingProduceData;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "原料管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "生产信息详情列表")
    private List<ProduceDataItemBo> produceDataItemBoList;

    @ApiModelProperty(value = "生产属性")
    private List<ProduceDataAttrBo> produceDataAttrBoList;

}
