package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.scm.server.scm.process.entity.po.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/9/11 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessOrderAndItemListBo {
    @ApiModelProperty(value = "加工单列表")
    private List<ProcessOrderPo> processOrderPoList;

    @ApiModelProperty(value = "加工单明细列表")
    private List<ProcessOrderItemPo> processOrderItemPoList;

    @ApiModelProperty(value = "加工单额外信息列表")
    private List<ProcessOrderExtraPo> processOrderExtraPoList;

    @ApiModelProperty(value = "加工单原料对照关系列表")
    private List<ProcessOrderMaterialRefBo> processOrderMaterialRefBoList;

    @ApiModelProperty(value = "加工工序列表")
    private List<ProcessOrderProcedurePo> processOrderProcedurePoList;

    @ApiModelProperty(value = "加工工序描述列表")
    private List<ProcessOrderDescPo> processOrderDescPoList;

    @ApiModelProperty(value = "加工生产信息列表")
    private List<ProcessOrderSamplePo> processOrderSamplePoList;
}
