package com.hete.supply.scm.server.supplier.purchase.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author rockyHuas
 * @date 2023/06/29 17:07
 */
@Data
@NoArgsConstructor
public class PurchaseReturnPrintDto {

    @NotEmpty(message = "退货单号不能为空")
    @ApiModelProperty(value = "退货单号")
    private List<String> returnOrderNoList;

}
