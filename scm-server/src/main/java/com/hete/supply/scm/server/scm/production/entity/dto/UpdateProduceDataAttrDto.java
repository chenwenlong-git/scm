package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.server.scm.entity.dto.ProduceDataAttrDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 10:47
 */
@Data
public class UpdateProduceDataAttrDto {

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "SKU不能为空")
    private String sku;

    @ApiModelProperty(value = "生产属性")
    private List<@Valid ProduceDataAttrDto> produceDataAttrList;

}
