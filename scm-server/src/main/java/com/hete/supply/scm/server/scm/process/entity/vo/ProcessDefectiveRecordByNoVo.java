package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author RockyHuas
 * @date 2023/06/01 10:52
 */
@Data
@NoArgsConstructor
public class ProcessDefectiveRecordByNoVo {

    @ApiModelProperty(value = "次品数量")
    private Integer defectiveGoodsCnt;

    @ApiModelProperty(value = "剩余可处理次品数量")
    private Integer availableDefectiveGoodsCnt;

    @ApiModelProperty(value = "关联的加工单号")
    private String processOrderNo;

    @ApiModelProperty("次品记录明细")
    private List<ProcessDefectiveRecordVo> processDefectiveRecordVoList;
}
