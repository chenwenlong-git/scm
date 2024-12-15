package com.hete.supply.scm.server.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveTypeAttribute;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.enums.wms.SkuDevType;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 入库收货单
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ApiModel(value = "创建收货单dto", description = "创建收货单dto")
public class ReceiveOrderCreateMqDto extends BaseMqMessageDto {
    @NotEmpty(message = "采购子单信息不能为空")
    @ApiModelProperty(value = "创建收货单参数list")
    @Valid
    private List<ReceiveOrderCreateItem> receiveOrderCreateItemList;

    @Data
    public static class ReceiveOrderCreateItem {
        @NotNull(message = "入库类型不能为空")
        @ApiModelProperty(value = "入库类型:bulk(大货入库);sample(样品入库); refund(退货入库);profit(盘盈入库);return(归还入库)")
        private ReceiveType receiveType;

        @ApiModelProperty(value = "入库类型属性：入库类型下一级分类")
        private ReceiveTypeAttribute receiveTypeAttribute;

        @ApiModelProperty(value = "业务类型")
        private ScmBizReceiveOrderType scmBizReceiveOrderType;

        @ApiModelProperty(value = "sku 类型")
        private SkuDevType skuDevType;

        @NotNull(message = "质检类型不正确")
        @ApiModelProperty(value = "质检类型：全检、抽检、免检")
        private WmsEnum.QcType qcType;

        @ApiModelProperty(value = "唯一单号")
        private String unionKey;

        @NotBlank(message = "业务单号不能为空")
        @ApiModelProperty(value = "业务单号")
        private String scmBizNo;

        @ApiModelProperty(value = "箱唛号")
        private String shippingMarkNo;

        @ApiModelProperty(value = "箱唛箱号（序号）")
        private List<String> shippingMarkNumList;

        @ApiModelProperty(value = "采购子单单号")
        private String purchaseChildOrderNo;

        @ApiModelProperty(value = "发货物流单号")
        private String trackingNumber;

        @ApiModelProperty(value = "商品/辅料类目")
        private String goodsCategory;

        @NotBlank(message = "仓库编码不能为空")
        @ApiModelProperty(value = "仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "供应商代码")
        private String supplierCode;

        @ApiModelProperty(value = "供应商名称")
        private String supplierName;


        @NotNull(message = "是否首单不能为空")
        @ApiModelProperty(value = "是否首单")
        private PurchaseOrderType purchaseOrderType;

        @NotNull(message = "是否加急不能为空")
        @ApiModelProperty(value = "是否加急")
        private BooleanType isUrgentOrder;

        @NotNull(message = "是否正常采购不能为空")
        @ApiModelProperty(value = "是否正常采购")
        private BooleanType isNormalOrder;

        @NotNull(message = "是否直发不能为空")
        @ApiModelProperty(value = "是否直发")
        private BooleanType isDirectSend;

        @NotNull(message = "下单时间不能为空")
        @ApiModelProperty(value = "下单时间")
        private LocalDateTime placeOrderTime;

        @NotNull(message = "供应商发货时间不能为空")
        @ApiModelProperty(value = "供应商发货时间")
        private LocalDateTime sendTime;

        @ApiModelProperty(value = "操作人")
        private String operator;

        @ApiModelProperty(value = "操作人")
        private String operatorName;

        @ApiModelProperty(value = "质检单号")
        private String qcOrderNo;

        @Valid
        @NotEmpty(message = "采购商品详情不能为空")
        @ApiModelProperty(value = "采购商品详情")
        private List<ReceiveOrderDetail> detailList;
    }

    @Data
    public static class ReceiveOrderDetail {
        @NotBlank(message = "平台编码不能为空")
        @ApiModelProperty(value = "平台编码")
        private String platCode;

        @JsonIgnore
        @ApiModelProperty(value = "发货单号")
        private String deliverOrderNo;

        @NotBlank(message = "批次码不能为空")
        @ApiModelProperty(value = "批次码")
        private String batchCode;

        @ApiModelProperty(value = "旧的sku")
        private String oldSkuCode;

        @ApiModelProperty(value = "赫特spu")
        private String spu;

        @NotBlank(message = "赫特sku不能为空")
        @ApiModelProperty(value = "赫特sku")
        private String skuCode;

        @NotNull(message = "采购数量不能为空")
        @ApiModelProperty(value = "采购数量")
        private Integer purchaseAmount;

        @NotNull(message = "发货数量不能为空")
        @ApiModelProperty(value = "发货数量")
        private Integer deliveryAmount;

        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;
    }


}
