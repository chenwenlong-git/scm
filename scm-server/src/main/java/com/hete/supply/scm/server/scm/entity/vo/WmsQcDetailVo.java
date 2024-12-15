package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.QcState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/24 14:34
 */
@Data
@NoArgsConstructor
public class WmsQcDetailVo {
    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "质检状态")
    private QcState qcState;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "质检数量")
    private Integer qcAmount;

    @ApiModelProperty(value = "质检不合格明细列表")
    private List<WmsNotPassQcDetail> notPassQcDetailList;
}
