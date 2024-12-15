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
 * 供应链属性风险表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("attribute_risk")
@ApiModel(value = "AttributeRiskPo对象", description = "供应链属性风险表")
public class AttributeRiskPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "attribute_risk_id", type = IdType.ASSIGN_ID)
    private Long attributeRiskId;


    @ApiModelProperty(value = "供应链属性主键id")
    private Long attributeId;


    @ApiModelProperty(value = "供应链属性名称")
    private String attributeName;


    @ApiModelProperty(value = "风险系数")
    private BigDecimal coefficient;


}
