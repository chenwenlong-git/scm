package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.GoodsProcessStatus;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品工序管理表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_process")
@ApiModel(value = "GoodsProcessPo对象", description = "商品工序管理表")
public class GoodsProcessPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "goods_process_id", type = IdType.ASSIGN_ID)
    private Long goodsProcessId;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "商品品类 ID")
    private Long goodsCategoryId;


    @ApiModelProperty(value = "商品品类名称")
    private String categoryName;


    @ApiModelProperty(value = "状态，BINDED：绑定、UNBINDED：未绑定")
    private GoodsProcessStatus goodsProcessStatus;

}
