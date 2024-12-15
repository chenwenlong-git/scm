package com.hete.supply.scm.server.supplier.sample.entity.vo;

import com.hete.supply.scm.server.scm.sample.entity.vo.SampleParentOrderInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/11 16:32
 */
@Data
@NoArgsConstructor
public class SampleSplitDetailVo {


    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "样品采购单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品拆分子项列表")
    private List<SampleSplitItemVo> sampleSplitItemList;

    @ApiModelProperty(value = "样品单生产信息")
    private List<SampleParentOrderInfoVo> sampleParentOrderInfoList;
}
