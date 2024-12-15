package com.hete.supply.scm.server.supplier.entity.dto;

import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/28 11:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReturnOrderEventMqDto extends BaseMqMessageDto {
    @NotBlank(message = "采购退货单号不能为空")
    @ApiModelProperty(value = "采购退货单号")
    private String returnOrderNo;

    @ApiModelProperty(value = "操作人用户编码")
    private String operator;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "物流方式")
    private String logistics;

    @NotBlank(message = "运单号不能为空")
    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @NotNull(message = "退货数不能为空")
    @ApiModelProperty(value = "退货数")
    private Integer returnCnt;

    @NotNull(message = "退货单状态不能为空")
    @ApiModelProperty(value = "退货单状态")
    private WmsEnum.ReturnState returnState;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "采购退货明细列表")
    @Valid
    private List<PurchaseReturnItemDto> purchaseReturnCreateItemList;

    @Data
    public static class PurchaseReturnItemDto {
        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "退货数量")
        private Integer returnCnt;

        @ApiModelProperty(value = "质检明细id")
        private Long bizDetailId;

        @NotBlank(message = "次品处理单号不能为空")
        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;
    }
}
