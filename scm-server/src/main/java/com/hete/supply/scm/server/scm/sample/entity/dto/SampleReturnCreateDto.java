package com.hete.supply.scm.server.scm.sample.entity.dto;

import com.hete.supply.scm.server.scm.entity.dto.SimpleReturnItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/18 10:13
 */
@Data
@NoArgsConstructor
public class SampleReturnCreateDto {

    @NotBlank(message = "物流不能为空")
    @ApiModelProperty(value = "物流")
    @Length(max = 32, message = "物流长度不能超过32个字符")
    private String logistics;

    @NotBlank(message = "运单号不能为空")
    @ApiModelProperty(value = "运单号")
    @Length(max = 32, message = "运单号不能超过32个字符")
    private String trackingNo;

    @ApiModelProperty("样品退货项")
    @NotEmpty(message = "样品退货项不能为空")
    @Valid
    private List<SimpleReturnItemDto> sampleReturnItemList;

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    @Length(max = 32, message = "供应商代码不能超过32个字符")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    @Length(max = 32, message = "供应商名称不能超过32个字符")
    private String supplierName;
}
