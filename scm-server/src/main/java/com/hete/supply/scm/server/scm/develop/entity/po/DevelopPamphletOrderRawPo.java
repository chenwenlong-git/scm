package com.hete.supply.scm.server.scm.develop.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.enums.SampleRawBizType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 开发版单原料表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("develop_pamphlet_order_raw")
@ApiModel(value = "DevelopPamphletOrderRawPo对象", description = "开发版单原料表")
public class DevelopPamphletOrderRawPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "develop_pamphlet_order_raw_id", type = IdType.ASSIGN_ID)
    private Long developPamphletOrderRawId;


    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;


    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;


    @ApiModelProperty(value = "开发母单号")
    private String developParentOrderNo;


    @ApiModelProperty(value = "原料类型")
    private MaterialType materialType;


    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;


    @ApiModelProperty(value = "sku数量")
    private Integer skuCnt;


    @ApiModelProperty(value = "原料业务类型")
    private SampleRawBizType sampleRawBizType;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;


}
