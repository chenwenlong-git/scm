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

/**
 * <p>
 * 生产信息详情描述
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_item_process_desc")
@ApiModel(value = "ProduceDataItemProcessDescPo对象", description = "生产信息详情描述")
public class ProduceDataItemProcessDescPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_item_process_desc_id", type = IdType.ASSIGN_ID)
    private Long produceDataItemProcessDescId;


    @ApiModelProperty(value = "生产信息详情ID")
    private Long produceDataItemId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "描述值，多个以英文逗号分开")
    private String descValue;


}
