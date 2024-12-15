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
 * 生产信息表
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_spu")
@ApiModel(value = "ProduceDataSpuPo对象", description = "生产信息表")
public class ProduceDataSpuPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_spu_id", type = IdType.ASSIGN_ID)
    private Long produceDataSpuId;


    @ApiModelProperty(value = "spu")
    private String spu;


}
