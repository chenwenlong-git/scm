package com.hete.supply.scm.server.scm.process.entity.po;

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
import java.time.LocalDateTime;

/**
 * <p>
 * 固定成本系数
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cost_coefficients")
@ApiModel(value = "CostCoefficientsPo对象", description = "固定成本系数")
public class CostCoefficientsPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "cost_coefficients_id", type = IdType.ASSIGN_ID)
    private Long costCoefficientsId;


    @ApiModelProperty(value = "生效日期")
    private LocalDateTime effectiveTime;


    @ApiModelProperty(value = "固定系数")
    private BigDecimal coefficient;
}
