package com.hete.supply.scm.server.scm.process.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "解除工序参数", description = "解除工序参数")
public class GoodsProcessUnBindDto {

    @ApiModelProperty("商品工序")
    @Valid
    @NotEmpty(message = "商品工序不能为空")
    private List<GoodsProcess> goodsProcesses;

    @Data
    @ApiModel(value = "商品工序参数", description = "商品工序参数")
    public static class GoodsProcess {

        @ApiModelProperty(value = "商品工序 ID")
        @NotNull(message = "商品工序不能为空")
        private Long goodsProcessId;

        @ApiModelProperty("版本号")
        @NotNull(message = "版本号不能为空")
        private Integer version;
    }

    @ApiModelProperty("解除的工序")
    @Valid
    @NotEmpty(message = "工序不能为空")
    private List<Long> processIds;


}
