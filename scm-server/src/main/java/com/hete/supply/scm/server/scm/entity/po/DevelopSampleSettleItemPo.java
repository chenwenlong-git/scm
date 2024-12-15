package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
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
 * 样品结算单明细
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_sample_settle_item")
@ApiModel(value = "DevelopSampleSettleItemPo对象", description = "样品结算单明细")
public class DevelopSampleSettleItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_sample_settle_item_id", type = IdType.ASSIGN_ID)
    private Long developSampleSettleItemId;


    @ApiModelProperty(value = "结算单号")
    private String developSampleSettleOrderNo;


    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;


    @ApiModelProperty(value = "样品关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "单据时间(上架时间)")
    private LocalDateTime settleTime;


    @ApiModelProperty(value = "样品处理方式")
    private DevelopSampleMethod developSampleMethod;


    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;


}
