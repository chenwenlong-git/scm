package com.hete.supply.scm.server.scm.production.entity.dto;

import com.hete.supply.scm.server.scm.process.entity.dto.ProcessDescInfoDto;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessInfoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/27 15:37
 */
@Data
@NoArgsConstructor
public class ProduceDataItemInfoDto {

    @ApiModelProperty(value = "id")
    private Long produceDataItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "来源单据号")
    private String businessNo;

    @ApiModelProperty(value = "生产信息详情原料BOM列表")
    private List<@Valid ProduceDataItemRawInfoDto> produceDataItemRawInfoList;

    @ApiModelProperty(value = "工序列表")
    private List<ProcessInfoDto> processInfoList;

    @ApiModelProperty(value = "生产信息详情工序描述列表")
    private List<ProcessDescInfoDto> processDescInfoList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @ApiModelProperty(value = "BOM名称")
    @NotBlank(message = "BOM名称不能为空")
    @Length(max = 15, message = "BOM名称长度不能超过15个字符")
    private String bomName;

    @ApiModelProperty(value = "关联供应商列表")
    private List<ProduceDataItemSupplierInfoDto> produceDataItemSupplierInfoList;

}
