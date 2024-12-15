package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/5.
 */
@Data
@ApiModel("批量打印返修单批次码返回VO")
public class RepairBatchCodePageVo {

    @ApiModelProperty("返修单号")
    private String repairOrderNumber;

    @ApiModelProperty("批次码列表")
    private List<String> batchCodeList;
}
