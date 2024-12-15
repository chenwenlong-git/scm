package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.server.scm.settle.entity.dto.DeductOrderDefectiveDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/6/21 18:32
 */
@Data
@NoArgsConstructor
public class ReturnOrderDeductOrderBo {

    @NotBlank(message = "供应商代码不能为空")
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotEmpty(message = "扣款单表次品退供明细不能为空")
    @ApiModelProperty(value = "扣款单表次品退供明细列表")
    private List<DeductOrderDefectiveDto> deductOrderDefectiveList;

}
