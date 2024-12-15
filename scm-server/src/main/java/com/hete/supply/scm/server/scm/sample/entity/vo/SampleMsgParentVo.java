package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SampleDevType;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/13 09:30
 */
@Data
@NoArgsConstructor
public class SampleMsgParentVo {
    @ApiModelProperty(value = "参照图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "开发类型")
    private SampleDevType sampleDevType;

    @ApiModelProperty(value = "样品单状态")
    private SampleOrderStatus sampleOrderStatus;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleParentOrderInfoVo> sampleParentOrderInfoList;

    @ApiModelProperty(value = "样品开发子单")
    private List<SampleMsgChildVo> sampleMsgChildList;
}
