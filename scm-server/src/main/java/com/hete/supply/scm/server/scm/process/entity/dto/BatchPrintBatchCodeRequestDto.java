package com.hete.supply.scm.server.scm.process.entity.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/5.
 */
@Data
@ApiModel("批量打印返修单批次码请求DTO")
public class BatchPrintBatchCodeRequestDto extends ComPageDto {

    @ApiModelProperty(value = "返修单号列表", required = true)
    private List<String> repairOrderNos;

}
