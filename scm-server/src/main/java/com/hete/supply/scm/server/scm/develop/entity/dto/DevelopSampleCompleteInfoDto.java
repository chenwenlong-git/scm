package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleCompleteType;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/4 13:58
 */
@Data
@NoArgsConstructor
public class DevelopSampleCompleteInfoDto {

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "初始信息类型")
    @NotNull(message = "初始信息类型不能为空")
    private DevelopSampleCompleteType developSampleCompleteType;

}
