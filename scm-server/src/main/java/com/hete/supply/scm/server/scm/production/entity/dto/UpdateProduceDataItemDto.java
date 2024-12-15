package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 16:06
 */
@Data
public class UpdateProduceDataItemDto {

    @NotBlank(message = "SKU不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "生产资料的版本号")
    private Integer version;

    @ApiModelProperty(value = "原料管理")
    @NotNull(message = "原料管理不能为空")
    private BooleanType rawManage;

    @ApiModelProperty(value = "原料&工序详情列表")
    private List<@Valid ProduceDataItemInfoDto> produceDataItemInfoList;

}
