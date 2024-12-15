package com.hete.supply.scm.server.supplier.develop.entity.dto;

import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopPamphletIdAndVersionDto;
import com.hete.supply.scm.server.supplier.develop.enums.DevelopPamphletExamine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopPamphletExamineDto extends DevelopPamphletIdAndVersionDto {

    @ApiModelProperty(value = "版单审核")
    @NotNull(message = "版单审核不能为空")
    private DevelopPamphletExamine developPamphletExamine;

    @Length(max = 200, message = "审核拒绝备注不能超过200个字符")
    @ApiModelProperty(value = "审核拒绝备注")
    private String refuseReason;
}
