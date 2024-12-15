package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 15:10
 */
@Data
@NoArgsConstructor
public class ProduceDataItemDto {

    @ApiModelProperty(value = "id")
    private Long produceDataItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "来源单据号")
    private String businessNo;

    @ApiModelProperty(value = "生产信息详情原料BOM列表")
    private List<@Valid ProduceDataItemRawListDto> produceDataItemRawList;

    @ApiModelProperty(value = "生产信息详情工序列表")
    private List<ProduceDataItemProcessListDto> produceDataItemProcessList;

    @ApiModelProperty(value = "生产信息详情工序描述列表")
    private List<ProduceDataItemProcessDescListDto> produceDataItemProcessDescList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @ApiModelProperty(value = "BOM名称")
    @NotBlank(message = "BOM名称不能为空")
    @Length(max = 15, message = "BOM名称长度不能超过15个字符")
    private String bomName;

    @ApiModelProperty(value = "关联供应商列表")
    private List<ProduceDataItemSupplierDto> produceDataItemSupplierList;

}
