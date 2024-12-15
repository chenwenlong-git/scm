package com.hete.supply.scm.server.scm.purchase.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2022/11/7 01:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseApproveDto extends PurchaseParentNoListDto {
    @ApiModelProperty(value = "审批原因")
    private String approveRemarks;

    @ApiModelProperty(value = "是否通过")
    @NotNull(message = "是否通过不能为空")
    private BooleanType isPass;
}
