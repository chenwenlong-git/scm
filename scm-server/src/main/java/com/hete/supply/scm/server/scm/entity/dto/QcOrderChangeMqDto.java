package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/28 09:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class QcOrderChangeMqDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;


    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;


    @ApiModelProperty(value = "完成质检时间")
    private LocalDateTime bizTime;

    @ApiModelProperty(value = "质检人")
    private String operator;

    @ApiModelProperty(value = "质检人")
    private String operatorName;


    @ApiModelProperty(value = "质检结果")
    private List<BatchCodeDetail> batchCodeDetailList;

    @Data
    public static class BatchCodeDetail {

        @ApiModelProperty(value = "批次码")
        private String batchCode;


        @ApiModelProperty(value = "正品数")
        private Integer passAmount;


        @ApiModelProperty(value = "次品数")
        private Integer notPassAmount;
    }

}
