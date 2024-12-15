package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.plm.api.developorder.enums.DevelopCreateType;
import com.hete.supply.plm.api.goods.enums.SkuDevType;
import com.hete.supply.scm.api.scm.entity.enums.DevelopChildOrderStatus;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/3 15:26
 */
@Data
@NoArgsConstructor
public class DevelopChildBaseMsgVo {

    @ApiModelProperty(value = "id")
    private Long developChildOrderId;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "商品类目ID")
    private Long categoryId;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "开发子单状态")
    private DevelopChildOrderStatus developChildOrderStatus;

    @ApiModelProperty(value = "开发类型(PLM提供枚举)")
    private SkuDevType skuDevType;

    @ApiModelProperty(value = "开款需求(PLM提供枚举)")
    private DevelopCreateType developCreateType;

    @ApiModelProperty(value = "异常处理")
    private BooleanType hasException;

    @ApiModelProperty(value = "是否加急(PLM提供枚举)")
    private BooleanType isUrgent;

    @ApiModelProperty(value = "是否需要原料")
    private BooleanType isNeedRaw;

    @ApiModelProperty(value = "是否打样")
    private BooleanType isSample;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "平台ID")
    private Long platformId;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    @ApiModelProperty(value = "开发子单属性列表")
    private List<DevelopChildOrderAttrVo> developChildOrderAttrList;

    @ApiModelProperty(value = "开发子单渠道大货价格")
    private DevelopOrderPriceVo developOrderPrice;


}
