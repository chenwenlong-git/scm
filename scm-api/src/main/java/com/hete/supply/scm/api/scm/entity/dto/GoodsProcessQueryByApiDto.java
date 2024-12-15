package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.GoodsProcessStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 16:28
 */
@Data
public class GoodsProcessQueryByApiDto extends ComPageDto {

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "sku")
    private List<String> skus;

    @ApiModelProperty(value = "多个 sku")
    private List<String> skuByCategory;

    @ApiModelProperty(value = "商品品类 id")
    private Long categoryId;

    @ApiModelProperty(value = "状态，BINDED：绑定、UNBINDED：未绑定")
    private List<GoodsProcessStatus> goodsProcessStatusList;

    @ApiModelProperty("多个商品工序 id")
    private List<Long> goodsProcessIdList;
}
