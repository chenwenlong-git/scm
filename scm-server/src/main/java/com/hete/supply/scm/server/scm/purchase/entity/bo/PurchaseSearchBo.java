package com.hete.supply.scm.server.scm.purchase.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/7/21 16:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseSearchBo {

    @ApiModelProperty(value = "查询的采购需求单号")
    private List<String> itemParentNoList;

    @ApiModelProperty(value = "查询的采购需求单号")
    private List<String> notInItemParentNoList;

}
