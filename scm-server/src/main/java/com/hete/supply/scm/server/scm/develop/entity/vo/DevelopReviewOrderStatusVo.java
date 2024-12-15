package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2023/9/12 10:58
 */
@Data
@NoArgsConstructor
public class DevelopReviewOrderStatusVo {
    @ApiModelProperty(value = "审版单状态")
    private DevelopReviewOrderStatus developReviewOrderStatus;

    @ApiModelProperty(value = "数据量")
    private Integer dataCnt;
}
