package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.server.scm.develop.enums.DevelopChildExceptionalType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author ChenWenLong
 * @date 2023/8/18 11:18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DevelopChildExceptionalDto extends DevelopChildIdAndVersionDto {

    @ApiModelProperty(value = "处理方式")
    DevelopChildExceptionalType developChildExceptionalType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @Length(max = 30, message = "取消原因不能超过30个字符")
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    @Length(max = 200, message = "需求描述不能超过200个字符")
    @ApiModelProperty(value = "需求描述")
    private String demandDesc;


}
