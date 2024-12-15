package com.hete.supply.scm.server.scm.entity.po;

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
 * 生产信息详情表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_item")
@ApiModel(value = "ProduceDataItemPo对象", description = "生产信息详情表")
public class ProduceDataItemPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_item_id", type = IdType.ASSIGN_ID)
    private Long produceDataItemId;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "关联单据号")
    private String businessNo;


    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "样品价格")
    private BigDecimal samplePrice;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "大货价格")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "BOM名称")
    private String bomName;

}
