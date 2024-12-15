package com.hete.supply.scm.server.scm.production.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.SkuRisk;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 商品风险表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_risk")
@ApiModel(value = "SkuRiskPo对象", description = "商品风险表")
public class SkuRiskPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sku_risk_id", type = IdType.ASSIGN_ID)
    private Long skuRiskId;


    @ApiModelProperty(value = "sku编码")
    private String sku;


    @ApiModelProperty(value = "风险评分")
    private BigDecimal score;


    @ApiModelProperty(value = "风险等级")
    private SkuRisk level;


}
