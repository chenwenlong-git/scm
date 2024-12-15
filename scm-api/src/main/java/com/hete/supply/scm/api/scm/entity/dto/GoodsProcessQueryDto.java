package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.GoodsProcessStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "商品工序查询参数", description = "商品工序查询参数")
public class GoodsProcessQueryDto extends ComPageDto {
    @ApiModelProperty("工序代码")
    private String processCode;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("sku")
    private List<String> skus;

    @JsonIgnore
    @ApiModelProperty("多个 sku")
    private List<String> skuByCategory;

    @JsonIgnore
    @ApiModelProperty("多个商品工序 id")
    private List<Long> goodsProcessIdsByProcessCode;

    @JsonIgnore
    @ApiModelProperty("多个商品工序 id")
    private List<Long> goodsProcessIdsByProcessName;

    @ApiModelProperty("商品品类 ID")
    private Long categoryId;

    @ApiModelProperty("状态，BINDED：绑定、UNBINDED：未绑定")
    private List<GoodsProcessStatus> goodsProcessStatusList;

    @ApiModelProperty("多个商品工序 id")
    private List<Long> goodsProcessIdList;

    @ApiModelProperty("是否绑定工序")
    private GoodsProcessStatus goodsProcessStatus;
}
