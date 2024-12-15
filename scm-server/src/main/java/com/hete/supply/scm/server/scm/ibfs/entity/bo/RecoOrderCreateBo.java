package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author ChenWenLong
 * @date 2024/5/28 16:53
 */
@Data
@NoArgsConstructor
public class RecoOrderCreateBo {

    @ApiModelProperty(value = "供应商代码")
    @NotBlank(message = "供应商代码不能为空")
    private String supplierCode;

    @ApiModelProperty(value = "供应商别称")
    @NotBlank(message = "供应商别称不能为空")
    private String supplierAlias;

    @ApiModelProperty(value = "对账周期")
    @NotNull(message = "对账周期不能为空")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "对账周期开始时间")
    @NotNull(message = "对账周期开始时间不能为空")
    private LocalDateTime reconciliationStartTime;

    @ApiModelProperty(value = "对账周期结束时间")
    @NotNull(message = "对账周期结束时间不能为空")
    private LocalDateTime reconciliationEndTime;

    @ApiModelProperty(value = "跟单人")
    @NotBlank(message = "跟单人不能为空")
    private String followUser;

    @ApiModelProperty(value = "跟单人的名称")
    @NotBlank(message = "跟单人的名称不能为空")
    private String followUsername;


}
