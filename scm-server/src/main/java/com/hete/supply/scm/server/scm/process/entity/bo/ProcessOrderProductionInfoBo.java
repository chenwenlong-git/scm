package com.hete.supply.scm.server.scm.process.entity.bo;

import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@Data
@ApiModel(description = "加工单生产信息")
public class ProcessOrderProductionInfoBo {

    @ApiModelProperty(value = "bom信息唯一键")
    private Long bomId;

    @ApiModelProperty(value = "加工成品SKU")
    private String processSku;

    @ApiModelProperty(value = "加工单优先级")
    private int priority;

    @ApiModelProperty(value = "加工单所需原料列表")
    private List<ProcessOrderMaterialBo> processOrderMaterialBoList = Collections.emptyList();

    @ApiModelProperty(value = "加工单工序列表")
    private List<ProcessOrderProcedureBo> processOrderProcedureBoList = Collections.emptyList();

    @ApiModelProperty(value = "加工单相关描述列表")
    private List<ProcessOrderDescBo> processOrderDescBoList = Collections.emptyList();

    @ApiModelProperty(value = "加工单加工单生产信息")
    private List<ProcessOrderSampleBo> processOrderSampleBoList = Collections.emptyList();

    @ApiModelProperty(value = "加工图片")
    private List<String> fileCodeList = Collections.emptyList();

    @Data
    @ApiModel("原料信息")
    public static class ProcessOrderMaterialBo {
        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "出库数量（单个加工需求数）")
        private Integer deliveryNum;

        @ApiModelProperty(value = "原料SKU所属类型")
        private MaterialType materialSkuType;

        @ApiModelProperty(value = "原料对照关系列表")
        private List<ProcessOrderMaterialCompareBo> procMaterialCompareBoList;
    }

    @Data
    @ApiModel("工序信息")
    public static class ProcessOrderProcedureBo {

        @ApiModelProperty(value = "工序")
        private Long processId;

        @ApiModelProperty(value = "工序排序")
        private Integer sort;

        @ApiModelProperty(value = "人工提成")
        private BigDecimal commission;

        @ApiModelProperty(value = "工序代码")
        private String processCode;

        @ApiModelProperty(value = "工序代码")
        private String processSecondCode;

        @ApiModelProperty(value = "工序")
        private ProcessLabel processLabel;
    }

    @Data
    @ApiModel("加工描述信息")
    public static class ProcessOrderDescBo {
        @ApiModelProperty(value = "加工描述名称")
        private String processDescName;

        @ApiModelProperty(value = "加工描述值")
        private String processDescValue;
    }
}
