package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/17.
 */
@Data
public class GetProcessSettleOrderDetailAndScanSettleDto extends ComPageDto {

    @NotNull(message = "结算单id不能为空")
    @ApiModelProperty(value = "结算单id", example = "1")
    private Long processSettleOrderId;

    @ApiModelProperty(value = "完成人编号", example = "U123456")
    private List<String> completeUsers;
}
