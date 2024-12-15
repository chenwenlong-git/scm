package com.hete.supply.scm.server.scm.adjust.entity.vo;

import com.hete.supply.plm.api.goods.enums.SkuDevType;
import com.hete.supply.scm.api.scm.entity.enums.GoodsPriceMaintain;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/6/18 16:00
 */
@Data
@NoArgsConstructor
public class GoodsPriceSearchVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "开发类型")
    private SkuDevType skuDevType;

    @ApiModelProperty(value = "是否存在审批中的审批单")
    private BooleanType isApproval;

    @ApiModelProperty(value = "是否维护价格")
    private GoodsPriceMaintain goodsPriceMaintain;

    @ApiModelProperty(value = "在售平台")
    private List<String> platNameList;

    @ApiModelProperty(value = "渠道价格列表")
    private List<GoodsPriceInfoVo> goodsPriceInfoList;

}
