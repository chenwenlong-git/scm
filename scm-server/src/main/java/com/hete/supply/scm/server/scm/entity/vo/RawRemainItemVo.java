package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/15 20:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RawRemainItemVo extends RawProductItemVo {
    @ApiModelProperty(value = "预估未使用数")
    private Integer remainCnt;
}
