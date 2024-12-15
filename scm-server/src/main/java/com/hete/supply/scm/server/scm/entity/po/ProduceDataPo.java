package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.BindingProduceData;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 生产信息表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data")
@ApiModel(value = "ProduceDataPo对象", description = "生产信息表")
public class ProduceDataPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_id", type = IdType.ASSIGN_ID)
    private Long produceDataId;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;


    @ApiModelProperty(value = "重量")
    private BigDecimal weight;


    @ApiModelProperty(value = "绑定生产信息")
    private BindingProduceData bindingProduceData;


    @ApiModelProperty(value = "商品采购价格")
    private BigDecimal goodsPurchasePrice;

    @ApiModelProperty(value = "商品采购价格更新时间")
    private LocalDateTime goodsPurchasePriceTime;

    @ApiModelProperty(value = "原料管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "公差")
    private BigDecimal tolerance;
}
