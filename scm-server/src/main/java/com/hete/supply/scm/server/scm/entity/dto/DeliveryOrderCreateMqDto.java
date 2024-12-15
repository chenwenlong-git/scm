package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.RawDeliverMode;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/12/28 15:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryOrderCreateMqDto extends BaseMqMessageDto {

    /**
     * 关联单号，如采购子单号
     */
    @NotBlank(message = "关联单号不能为空")
    private String relatedOrderNo;

    @NotNull(message = "出库单类型不能为空")
    private WmsEnum.DeliveryType deliveryType;

    /**
     * 计划发货时间
     */
    private LocalDateTime planDeliveryTime;

    /**
     * 仓库编码
     */
    @NotBlank(message = "仓库编码不能为空")
    private String warehouseCode;

    /**
     * 物流方式
     */
    private String logisticsType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String consigneePhoneNo;

    /**
     * 收货人地址
     */
    private String consigneeAddress;


    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人")
    private String operatorName;

    @ApiModelProperty(value = "原料出库方式")
    private RawDeliverMode rawDeliverMode;

    @ApiModelProperty(value = "产品质量")
    private WmsEnum.ProductQuality productQuality;

    @ApiModelProperty(value = "是否指定库位")
    private BooleanType particularLocation;

    @ApiModelProperty(value = "是否立即下发")
    private BooleanType dispatchNow;

    @ApiModelProperty(value = "平台")
    private String platCode;

    /**
     * 出库单详情
     */
    @NotEmpty(message = "出库单详情不能为空")
    private List<@Valid DeliveryDetail> deliveryDetails;

    /**
     * 出库单详情
     */
    @Data
    public static class DeliveryDetail {
        /**
         * sku编号
         */
        @NotBlank(message = "sku编号不能为空")
        private String skuCode;

        /**
         * 计划出库数量
         */
        @NotNull(message = "计划出库数量不能为空")
        @Positive(message = "计划出库数量必须为正整数")
        private Integer planDeliveryAmount;

        /**
         * 库位出库信息
         */
        private List<@Valid WareLocationDelivery> wareLocationDeliveryList;
    }

    @Data
    public static class WareLocationDelivery {
        @NotNull(message = "库位出库数量不能为空")
        @Positive(message = "库位出库数量必须为正整数")
        private Integer deliveryAmount;
        /**
         * 库位
         */
        @NotBlank(message = "库位不能为空")
        private String warehouseLocationCode;

        /**
         * 批次码
         */
        @NotBlank(message = "批次码不能为空")
        private String batchCode;
    }
}
