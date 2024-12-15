package com.hete.supply.scm.server.scm.sample.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 样品采购子单详细信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sample_child_order_info")
@ApiModel(value = "SampleChildOrderInfoPo对象", description = "样品采购子单详细信息")
public class SampleChildOrderInfoPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "sample_child_order_info_id", type = IdType.ASSIGN_ID)
    private Long sampleChildOrderInfoId;


    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;


    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;


    @ApiModelProperty(value = "属性值")
    private String sampleInfoValue;

}
