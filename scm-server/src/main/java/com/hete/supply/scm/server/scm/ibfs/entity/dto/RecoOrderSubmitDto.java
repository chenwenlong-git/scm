package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author ChenWenLong
 * @date 2024/7/16 09:42
 */
@Data
@NoArgsConstructor
public class RecoOrderSubmitDto extends RecoOrderNoAndVersionDto {

    @ApiModelProperty(value = "备注")
    @Length(max = 500, message = "备注字符长度不能超过 500 位")
    private String remarks;

}
