package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.plm.api.developorder.enums.MaterialType;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.vo.ProcessOrderMaterialCompareVo;
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
@ApiModel("加工单生产信息")
public class ProcessOrderProductionInfoVo {

    @ApiModelProperty(value = "原料信息列表")
    @JsonProperty("materials")
    private List<ProcessOrderMaterialVo> processOrderMaterialVoList = Collections.emptyList();

    @ApiModelProperty(value = "工序信息列表")
    @JsonProperty("processes")
    private List<ProcessOrderProcedureVo> processOrderProcedureVoList = Collections.emptyList();

    @ApiModelProperty(value = "加工描述信息列表")
    @JsonProperty("processDescriptions")
    private List<ProcessOrderDescVo> processOrderDescVoList = Collections.emptyList();

    @ApiModelProperty("生产图片")
    private List<String> fileCodeList = Collections.emptyList();

    @Data
    @ApiModel("原料信息")
    public static class ProcessOrderMaterialVo {
        @ApiModelProperty(value = "SKU")
        private String sku;

        @ApiModelProperty(value = "出库数量（单个加工需求数）")
        private Integer deliveryNum;

        @ApiModelProperty(value = "原料SKU所属类型")
        private MaterialType materialSkuType;

        @ApiModelProperty(value = "商品对照关系")
        @JsonProperty("processOrderMaterialCompareList")
        private List<ProcessOrderMaterialCompareVo> processOrderMaterialCompareVoList;
    }

    @Data
    @ApiModel("工序信息")
    public static class ProcessOrderProcedureVo {

        @ApiModelProperty(value = "工序")
        private Long processId;

        @ApiModelProperty(value = "工序排序")
        private Integer sort;

        @ApiModelProperty(value = "人工提成")
        private BigDecimal commission;

        @ApiModelProperty(value = "二级工序代码")
        private String processSecondCode;

        @ApiModelProperty(value = "工序")
        private ProcessLabel processLabel;
    }

    @Data
    @ApiModel("加工描述信息")
    public static class ProcessOrderDescVo {
        @ApiModelProperty(value = "加工描述名称")
        private String processDescName;

        @ApiModelProperty(value = "加工描述值")
        private String processDescValue;
    }
}
