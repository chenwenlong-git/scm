package com.hete.supply.scm.server.scm.process.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.process.enums.CommissionAttribute;
import com.hete.supply.scm.server.scm.process.enums.CommissionCategory;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 工序扫码提成明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("scan_commission_detail")
@ApiModel(value = "ScanCommissionDetailPo对象", description = "工序扫码提成明细")
public class ScanCommissionDetailPo extends BaseSupplyPo {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "scan_commission_detail_id", type = IdType.ASSIGN_ID)
    private Long scanCommissionDetailId;

    @ApiModelProperty(value = "扫码记录ID")
    private Long processOrderScanId;

    @ApiModelProperty(value = "提成类目")
    private CommissionCategory commissionCategory;

    @ApiModelProperty(value = "提成属性")
    private CommissionAttribute commissionAttribute;

    @ApiModelProperty(value = "提成规则")
    private String commissionRule;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "参考单位提成（四舍五入后结果）")
    private BigDecimal unitCommission;

    @ApiModelProperty(value = "提成总额")
    private BigDecimal totalAmount;
}
