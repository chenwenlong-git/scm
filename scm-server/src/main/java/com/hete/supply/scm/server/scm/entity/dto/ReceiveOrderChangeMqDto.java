package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveTypeAttribute;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/28 09:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReceiveOrderChangeMqDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "入库类型:bulk(大货入库);sample(样品入库); refund(退货入库);profit(盘盈入库);return(归还入库)")
    private ReceiveType receiveType;

    @ApiModelProperty(value = "收货类型属性")
    private ReceiveTypeAttribute receiveTypeAttribute;

    @ApiModelProperty(value = "采购子单号")
    private String scmBizNo;

    @ApiModelProperty(value = "收货单号")
    private String receiveOrderNo;

    @ApiModelProperty(value = "唯一单据号")
    private String unionKey;


    @ApiModelProperty(value = "收货单状态")
    private WmsEnum.ReceiveOrderState receiveOrderState;

    @ApiModelProperty(value = "退货类型")
    private WmsEnum.ReturnType returnType;


    @ApiModelProperty(value = "完成业务操作的时间")
    private LocalDateTime bizTime;

    @ApiModelProperty(value = "操作人")
    private String operator;


    @ApiModelProperty(value = "操作人")
    private String operatorName;

    @ApiModelProperty(value = "收货同步明细列表")
    @Valid
    private List<PurchaseReceiptSyncItemDto> purchaseReceiptSyncItemList;

    @ApiModelProperty(value = "质检结果")
    private List<BatchCodeDetail> batchCodeDetailList;

    @Data
    public static class PurchaseReceiptSyncItemDto {
        @NotBlank(message = "sku不能为空")
        @ApiModelProperty(value = "sku")
        private String skuCode;

        @NotBlank(message = "sku批次码不能为空")
        @ApiModelProperty(value = "sku批次码")
        private String batchCode;

        @NotNull(message = "收货数不能为空")
        @ApiModelProperty(value = "收货数")
        private Integer receiveAmount;

        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;

        @NotNull(message = "拒收数量不能为空")
        @ApiModelProperty(value = "拒收数量")
        private Integer rejectAmount;

    }


    @Data
    public static class BatchCodeDetail {
        @ApiModelProperty(value = "sku")
        private String skuCode;

        @ApiModelProperty(value = "批次码")
        private String batchCode;


        @ApiModelProperty(value = "正品数")
        private Integer passAmount;


        @ApiModelProperty(value = "次品数")
        private Integer notPassAmount;

        @ApiModelProperty(value = "丢货数")
        private Integer lossAmount;
    }
}
