package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.entity.bo.ProduceDataAttrPriceUpdateBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/12 11:53
 */
@Data
@NoArgsConstructor
public class ProduceDataPriceByAttrDto {

    @NotEmpty(message = "SKU定价属性信息列表不能为空")
    @Valid
    @ApiModelProperty(value = "SKU定价属性信息列表")
    private List<ProduceDataAttrPriceUpdateBo> produceDataAttrPriceUpdateBoList;

}
