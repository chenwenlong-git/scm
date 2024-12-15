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

/**
 * <p>
 * 生产资料原料对照关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_item_raw_compare")
@ApiModel(value = "ProduceDataItemRawComparePo对象", description = "生产资料原料对照关系表")
public class ProduceDataItemRawComparePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_item_raw_compare_id", type = IdType.ASSIGN_ID)
    private Long produceDataItemRawCompareId;


    @ApiModelProperty(value = "生产信息详情原料表id")
    private Long produceDataItemRawId;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;


}
