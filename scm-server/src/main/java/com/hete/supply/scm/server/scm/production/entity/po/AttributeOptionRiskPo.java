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
 * 供应链属性可选项风险表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("attribute_option_risk")
@ApiModel(value = "AttributeOptionRiskPo对象", description = "供应链属性可选项风险表")
public class AttributeOptionRiskPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "attribute_option_risk_id", type = IdType.ASSIGN_ID)
    private Long attributeOptionRiskId;


    @ApiModelProperty(value = "供应链属性风险配置表主键id")
    private Long attributeRiskId;


    @ApiModelProperty(value = "供应链属性可选项主键id")
    private Long attributeOptionId;


    @ApiModelProperty(value = "供应链属性可选项值")
    private String attributeOptionValue;


    @ApiModelProperty(value = "风险评分")
    private BigDecimal score;


}
