package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "返修单创建结果信息")
public class CreateRepairOrderResultMqDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "计划单号", example = "P2023001")
    private String planNo;

    @ApiModelProperty(value = "返修单号", example = "FX")
    private String repairNo;

    @ApiModelProperty(value = "返修单号关联SKU列表", example = "FX")
    private List<String> skuCodeList;
}
