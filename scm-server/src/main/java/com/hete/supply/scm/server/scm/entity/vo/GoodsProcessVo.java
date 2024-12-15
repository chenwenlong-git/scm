package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.plm.api.goods.entity.vo.PlmCategoryVo;
import com.hete.supply.scm.api.scm.entity.enums.GoodsProcessStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/7 10:10
 */
@Data
@NoArgsConstructor
public class GoodsProcessVo {

    @ApiModelProperty(value = "工序 ID")
    private Long goodsProcessId;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商品类目")
    private List<PlmCategoryVo> plmCategoryVoList;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "状态")
    private GoodsProcessStatus goodsProcessStatus;

    @ApiModelProperty("工序明细")
    private List<GoodsProcessItemVo> processes;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
