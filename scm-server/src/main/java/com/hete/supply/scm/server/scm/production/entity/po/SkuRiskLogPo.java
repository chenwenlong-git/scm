package com.hete.supply.scm.server.scm.production.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 商品风险日志表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sku_risk_log")
@ApiModel(value = "SkuRiskLogPo对象", description = "商品风险日志表")
public class SkuRiskLogPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sku_risk_log_id", type = IdType.ASSIGN_ID)
    private Long skuRiskLogId;


    @ApiModelProperty(value = "sku编码")
    private String sku;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "属性风险id")
    private Long skuRiskId;


    @ApiModelProperty(value = "供应链属性主键id")
    private Long attributeId;


    @ApiModelProperty(value = "供应链属性名称")
    private String attributeName;


    @ApiModelProperty(value = "风险系数")
    private BigDecimal coefficient;


    @ApiModelProperty(value = "供应链属性可选项主键id")
    private Long attributeOptionId;


    @ApiModelProperty(value = "供应链属性可选项值")
    private String attributeOptionValue;


    @ApiModelProperty(value = "风险评分")
    private BigDecimal score;


}
