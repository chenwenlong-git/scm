package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/5/12 15:49
 */
@Data
@NoArgsConstructor
public class SampleAddProduceLabelDto {

    @NotEmpty(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private List<String> sampleChildOrderNoList;

    @NotNull(message = "生产标签不能为空")
    @ApiModelProperty(value = "生产标签")
    private SampleProduceLabel sampleProduceLabel;

}
