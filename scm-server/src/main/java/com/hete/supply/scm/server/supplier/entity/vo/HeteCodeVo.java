package com.hete.supply.scm.server.supplier.entity.vo;

import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 01:11
 */
@Data
@NoArgsConstructor
public class HeteCodeVo {
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "赫特码列表")
    private List<HeteCodeItem> heteCodeItemList;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @Data
    @ApiModel(value = "赫特码列表项")
    public static class HeteCodeItem {
        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "采购数量")
        private Integer purchaseCnt;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "生产信息列表")
        private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;
    }
}
