package com.hete.supply.scm.server.scm.sample.entity.bo;

import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderInfoPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/22 15:13
 */
@Data
@NoArgsConstructor
public class SampleParentLogBo {
    @ApiModelProperty(value = "更新人")
    final private String updateUsernameKey = "修改人";
    final private LogVersionValueType updateUsernameType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "更新时间")
    final private String updateTimeKey = "修改时间";
    final private LogVersionValueType updateTimeType = LogVersionValueType.DATE;
    @ApiModelProperty(value = "spu")
    final private String spuKey = "spu";
    final private LogVersionValueType spuType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "收货仓库名称")
    final private String warehouseNameKey = "收货仓库";
    final private LogVersionValueType warehouseNameType = LogVersionValueType.STRING;
    @ApiModelProperty(value = "业务约定交期")
    final private String deliverDateKey = "业务约定交期";
    final private LogVersionValueType deliverDateType = LogVersionValueType.DATE;
    @ApiModelProperty(value = "生产信息")
    final private String orderInfoListKey = "生产信息";
    final private LogVersionValueType orderInfoListType = LogVersionValueType.LIST;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "生产信息")
    private List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList;
}
