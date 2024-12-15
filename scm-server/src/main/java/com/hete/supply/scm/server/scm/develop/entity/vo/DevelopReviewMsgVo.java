package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/8/3 15:52
 */
@Data
@NoArgsConstructor
public class DevelopReviewMsgVo {

    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "审版单状态")
    private DevelopReviewOrderStatus developReviewOrderStatus;

    @ApiModelProperty(value = "审版单结果")
    private ReviewResult reviewResult;
}
