package com.hete.supply.scm.api.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/5/24.
 */
@Data
@ApiModel(description = "获取结算单详情请求数据传输对象")
public class GetSettleOrderDetailDto extends ComPageDto {

    @NotBlank(message = "结算单号不能为空")
    @ApiModelProperty(value = "结算单号", example = "SETTLE123456")
    private String settleOrderNo;

    @JsonIgnore
    @ApiModelProperty(value = "结算单号列表", example = "SETTLE123456")
    private List<String> settleOrderNos;
}
