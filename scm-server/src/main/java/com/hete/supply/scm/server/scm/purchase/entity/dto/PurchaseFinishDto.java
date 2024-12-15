package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * @author weiwenxin
 * @date 2024/1/4 15:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PurchaseFinishDto extends PurchaseChildIdAndVersionDto {
    @ApiModelProperty(value = "终止来货备注")
    @Length(max = 255, message = "终止来货备注不能超过255个字符")
    private String finishRemark;
}
