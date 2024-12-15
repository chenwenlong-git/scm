package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/4/17 09:48
 */
@Data
@NoArgsConstructor
public class SampleProductDeliverDto {

    @ApiModelProperty(value = "仓库编码")
    @NotBlank(message = "仓库不能为空")
    @Length(max = 32, message = "仓库编码字符长度不能超过 32 位")
    private String warehouseCode;

    @ApiModelProperty(value = "明细列表")
    @Valid
    @NotEmpty(message = "明细列表不能为空")
    @Size(max = 20, message = "一次性选择数量不能超过 20 条")
    private List<SampleProductDeliverDetail> sampleProductDeliverDetailList;

}
