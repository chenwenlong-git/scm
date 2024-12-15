package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yanjiawei
 * Created on 2024/4/11.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessOrderPromiseDateImportationDto extends BaseImportationRowDto {
    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(value = "答交时间")
    private String promiseDate;
}
