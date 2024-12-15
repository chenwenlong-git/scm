package com.hete.supply.scm.server.scm.sample.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessDescVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleProcessVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleRawVo;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/3/30 09:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleProcessAndRawBo {

    @ApiModelProperty(value = "样品采购母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "生产标签")
    private SampleProduceLabel sampleProduceLabel;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTime;

    @ApiModelProperty(value = "是否有库存")
    private BooleanType inventory;

    @ApiModelProperty(value = "生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "样品bom原料信息")
    private List<SampleRawVo> sampleRawBoList;

    @ApiModelProperty(value = "样品加工描述信息")
    private List<SampleProcessDescVo> sampleProcessDescBoList;

    @ApiModelProperty(value = "样品加工信息")
    private List<SampleProcessVo> sampleProcessBoList;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;
}
