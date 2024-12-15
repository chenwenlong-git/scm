package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/18 11:27
 */
@Data
@NoArgsConstructor
public class PurchaseParentNoListDto {

    @NotEmpty(message = "采购母单单号不能为空")
    @ApiModelProperty(value = "采购母单单号")
    private List<String> purchaseParentOrderNoList;
}
