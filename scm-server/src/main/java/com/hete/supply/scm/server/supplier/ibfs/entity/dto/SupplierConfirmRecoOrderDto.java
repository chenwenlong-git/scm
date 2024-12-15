package com.hete.supply.scm.server.supplier.ibfs.entity.dto;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2024/5/23 17:59
 */
@Data
@NoArgsConstructor
public class SupplierConfirmRecoOrderDto extends SupplierRecoOrderNoAndVersionDto {


    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "审核状态")
    private BooleanType examine;


    @ApiModelProperty("确认意见")
    @Length(max = 500, message = "确认意见内容不能超过 500 个字")
    private String comment;

}
