package com.hete.supply.scm.server.scm.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@NoArgsConstructor
public class PurchaseChildNoListDto {

    @NotEmpty(message = "采购子单单号不能为空")
    @ApiModelProperty(value = "采购子单单号")
    private List<String> purchaseChildOrderNoList;

}
