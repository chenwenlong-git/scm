package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GoodsProcessMqDto extends BaseMqMessageDto {

    @ApiModelProperty("SPU")
    @NotBlank(message = "SPU不能为空")
    private String spuCode;

    @ApiModelProperty("skuCodeList")
    @NotEmpty(message = "skuCodeList不能为空")
    private List<String> skuCodeList;


}
