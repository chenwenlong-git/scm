package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.entity.vo.RawProductItemVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author weiwenxin
 * @date 2022/11/16 09:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RawProductItemEditDto extends RawProductItemVo {
    @ApiModelProperty(value = "id")
    private Long purchaseChildOrderRawId;

    @ApiModelProperty(value = "version")
    private Integer version;
}
