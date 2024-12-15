package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessProgressStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 16:28
 */
@Data
public class ProcessOrderScanQueryByApiDto extends ComPageDto {

    @ApiModelProperty(value = "加工单编号")
    private String processOrderNo;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "接货人用户编码")
    private String receiptUser;

    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUser;

    @ApiModelProperty(value = "扫码完成人名称")
    private String completeUsername;

    @ApiModelProperty(value = "扫码创建时间-区间起始时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "扫码创建时间-区间结束时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "扫码完成时间-开始时间")
    private LocalDateTime completeTimeStart;

    @ApiModelProperty(value = "扫码完成时间-结束时间")
    private LocalDateTime completeTimeEnd;

    @ApiModelProperty(value = "扫码 ID")
    private List<Long> processOrderScanIdList;

    @ApiModelProperty(value = "加工单编号列表")
    private List<String> processOrderNoList;

    @ApiModelProperty(value = "工序状态")
    private List<ProcessProgressStatus> processProgressStatusList;
}
