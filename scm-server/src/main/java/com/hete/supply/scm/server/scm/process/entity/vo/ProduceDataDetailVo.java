package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 15:20
 */
@Data
@NoArgsConstructor
public class ProduceDataDetailVo {

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "spu主图")
    private List<String> spuFileCodeList;

    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类目")
    private String categoryName;


    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "状态")
    private BindingProduceData bindingProduceData;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "生产信息详情列表")
    private List<ProduceDataItemVo> produceDataItemList;

    @ApiModelProperty(value = "生产属性")
    private List<ProduceDataAttrVo> produceDataAttrList;

    @ApiModelProperty(value = "id")
    private Long produceDataId;

    @ApiModelProperty(value = "封样图片")
    private List<String> sealImageFileCodeList;

    @ApiModelProperty(value = "规格书信息详情列表")
    private List<ProduceDataSpecVo> produceDataSpecList;

    @ApiModelProperty(value = "商品采购价格")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "原料管理")
    private BooleanType rawManage;

}
