package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleDestination;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2023/8/12 16:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopCompleteInfoMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "操作人")
    private String userKey;

    @ApiModelProperty(value = "操作人名称")
    private String username;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "样品去向")
    private DevelopSampleDestination developSampleDestination;

    @ApiModelProperty(value = "平台code")
    private String platCode;

    @ApiModelProperty(value = "商品类目id(最末级)")
    private Long categoryId;

    @ApiModelProperty(value = "中文标题")
    private String titleCn;

    @ApiModelProperty(value = "商品描述")
    private String description;


}
