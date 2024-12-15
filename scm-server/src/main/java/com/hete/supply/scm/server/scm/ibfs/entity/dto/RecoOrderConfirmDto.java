package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/24 10:15
 */
@Data
@NoArgsConstructor
public class RecoOrderConfirmDto extends RecoOrderNoAndVersionDto {

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "审核状态")
    private BooleanType examine;

    @ApiModelProperty(value = "备注")
    @Length(max = 500, message = "备注字符长度不能超过 500 位")
    private String remarks;

}
