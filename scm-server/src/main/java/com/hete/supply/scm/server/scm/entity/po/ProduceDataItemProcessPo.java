package com.hete.supply.scm.server.scm.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 生产信息详情工序
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("produce_data_item_process")
@ApiModel(value = "ProduceDataItemProcessPo对象", description = "生产信息详情工序")
public class ProduceDataItemProcessPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "produce_data_item_process_id", type = IdType.ASSIGN_ID)
    private Long produceDataItemProcessId;


    @ApiModelProperty(value = "生产信息详情ID")
    private Long produceDataItemId;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "工序代码")
    private String processCode;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "二级工序代码")
    private String processSecondCode;


    @ApiModelProperty(value = "二级工序名称")
    private String processSecondName;

    @ApiModelProperty(value = "工序环节")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "工序类别")
    private ProcessLabel processLabel;


}
