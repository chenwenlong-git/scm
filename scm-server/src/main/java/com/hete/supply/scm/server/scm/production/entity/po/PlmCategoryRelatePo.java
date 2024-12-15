package com.hete.supply.scm.server.scm.production.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.production.enums.PlmCategoryRelateBizType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类与业务关联表
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("plm_category_relate")
@ApiModel(value = "PlmCategoryRelatePo对象", description = "商品分类与业务关联表")
public class PlmCategoryRelatePo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "plm_category_relate_id", type = IdType.ASSIGN_ID)
    private Long plmCategoryRelateId;


    @ApiModelProperty(value = "plm商品分类id")
    private Long categoryId;


    @ApiModelProperty(value = "plm商品分类名称")
    private String categoryName;


    @ApiModelProperty(value = "业务id")
    private Long bizId;


    @ApiModelProperty(value = "业务类型")
    private PlmCategoryRelateBizType bizType;
}
