package com.hete.supply.scm.server.scm.process.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.supply.scm.server.scm.enums.MaterialSkuType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@Data
@ApiModel(value = "工序模板创建参数", description = "工序模版创建参数")
public class ProcessTemplateCreateDto {

    @ApiModelProperty("工序模版名称")
    @NotBlank(message = "工序模版名称不能为空")
    @Length(max = 255, message = "工序模版字符长度不能超过 255 位")
    private String name;

    @ApiModelProperty("工序明细")
    @Valid
    @NotEmpty(message = "工序明细不能为空")
    private List<@Valid Process> processes;

    @ApiModelProperty("原料明细")
    private List<@Valid Material> materials;

    @ApiModelProperty("工序描述信息")
    @JsonProperty("processDescriptions")
    private List<@Valid ProcessDescriptionInfoDto> processDescriptionInfoDtoList;

    @Data
    @ApiModel(value = "工序明细参数", description = "工序明细参数")
    public static class Process {

        @ApiModelProperty(value = "序号")
        @NotNull(message = "序号不能为空")
        private Integer sort;

        @ApiModelProperty(value = "工序 ID")
        @NotNull(message = "工序名称不能为空")
        private Long processId;
    }

    @Data
    @ApiModel(value = "原料明细参数", description = "原料明细参数")
    public static class Material {

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
        @NotNull(message = "原料SKU所属类型不能为空")
        private MaterialSkuType materialSkuType;
    }
}