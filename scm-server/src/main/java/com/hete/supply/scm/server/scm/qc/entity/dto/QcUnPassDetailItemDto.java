package com.hete.supply.scm.server.scm.qc.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/12 16:27
 */
@Data
@NoArgsConstructor
public class QcUnPassDetailItemDto {
    @NotNull(message = "合格的质检单详情id不能为空")
    @ApiModelProperty(value = "合格的质检单详情id（上级）")
    private Long qcDetailId;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @Length(max = 255, message = "备注不能超过255个字符")
    @ApiModelProperty(value = "备注")
    private String remark;

    @NotNull(message = "次品数不能为空")
    @ApiModelProperty(value = "次品数")
    private Integer notPassAmount;

    @NotEmpty(message = "质检不合格原因不能为空")
    @ApiModelProperty(value = "质检不合格原因")
    private List<@NotBlank(message = "质检不合格原因不能为空") String> qcNotPassedReasonList;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @NotEmpty(message = "问题图片不能为空")
    @ApiModelProperty(value = "问题图片")
    private List<String> problemFileCodeList;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;

    public static QcUnPassDetailItemDto copyObj(QcUnPassDetailItemDto dto, Long qcDetailId) {
        final QcUnPassDetailItemDto newDto = new QcUnPassDetailItemDto();
        newDto.setQcDetailId(qcDetailId);
        newDto.setSkuBatchCode(dto.getSkuBatchCode());
        newDto.setSku(dto.getSku());
        newDto.setRemark(dto.getRemark());
        newDto.setNotPassAmount(dto.getNotPassAmount());
        newDto.setQcNotPassedReasonList(dto.getQcNotPassedReasonList());
        newDto.setContainerCode(dto.getContainerCode());
        newDto.setProblemFileCodeList(dto.getProblemFileCodeList());

        return newDto;
    }
}
