package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.server.scm.enums.MaterialSkuType;
import com.hete.supply.scm.server.scm.process.enums.ProcessTemplateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class ProcessTemplateDetailVo {

    @ApiModelProperty(value = "工序模版 ID")
    private Long processTemplateId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型，CATEGORY：品类，SKU：商品sku")
    private ProcessTemplateType processTemplateType;

    @ApiModelProperty(value = "类型值 id")
    private String typeValue;


    @ApiModelProperty(value = "类型值名称")
    private String typeValueName;

    @ApiModelProperty("工序明细")
    private List<Process> processes;

    @ApiModelProperty("工序模版原料明细")
    private List<Material> materials;

    @ApiModelProperty("工序模版-工序描述")
    @JsonProperty("processDescriptions")
    private List<ProcessDescriptionInfoVo> processDescriptionInfoVos;

    @Data
    @ApiModel(value = "工序明细参数", description = "工序明细参数")
    public static class Process {

        @ApiModelProperty(value = "工序明细 ID")
        private Long processTemplateRelationId;

        @ApiModelProperty(value = "序号")
        private Integer sort;

        @ApiModelProperty(value = "工序 ID")
        private Long processId;

        @ApiModelProperty(value = "版本号")
        private Integer version;

        @ApiModelProperty(value = "工序")
        private ProcessLabel processLabel;

        @ApiModelProperty(value = "二级工序代码")
        private String processSecondCode;

    }

    @Data
    @ApiModel(value = "工序明细参数", description = "工序明细参数")
    public static class Material {

        @ApiModelProperty(value = "工序模版原料 id")
        private Long processTemplateMaterialId;

        @ApiModelProperty(value = "sku")
        @NotBlank(message = "sku不能为空")
        private String sku;

        @ApiModelProperty(value = "名称")
        @NotBlank(message = "名称不能为空")
        private String name;

        @ApiModelProperty(value = "数量")
        @NotNull(message = "数量不能为空")
        private Integer num;

        @ApiModelProperty(value = "原料SKU所属类型")
        private MaterialSkuType materialSkuType;

        @ApiModelProperty("版本号")
        private Integer version;

    }


    @ApiModelProperty(value = "版本号")
    private Integer version;

}
