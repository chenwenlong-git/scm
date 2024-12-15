package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/1/19 10:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RepairOrderNoPageDto extends ComPageDto {

    @ApiModelProperty(value = "返修单号批量")
    @NotEmpty(message = "返修单号不能为空")
    private List<String> repairOrderNoList;

}
