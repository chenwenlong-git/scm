package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;

import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品工序关系表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_process_relation")
@ApiModel(value = "GoodsProcessRelationPo对象", description = "商品工序关系表")
public class GoodsProcessRelationPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "goods_process_relation_id", type = IdType.ASSIGN_ID)
    private Long goodsProcessRelationId;


    @ApiModelProperty(value = "工序 id")
    private Long processId;

    @ApiModelProperty(value = "序号")
    private Integer sort;


    @ApiModelProperty(value = "商品工序id")
    private Long goodsProcessId;


}
