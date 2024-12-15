package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DeliveryTypeAttribute;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/28 09:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ProcessMaterialReceiptCreateMqDto extends BaseMqMessageDto {

    /**
     * 关联的加工单/返修单
     */
    @NotBlank(message = "关联的加工单不能为空")
    @Length(max = 32, message = "加工单字符长度不能超过 32 位")
    private String processOrderNo;

    /**
     * 出库单属性枚举
     */
    private DeliveryTypeAttribute deliveryTypeAttribute;

    /**
     * 出库单编号
     */
    @NotBlank(message = "关联的出库单编号不能为空")
    @Length(max = 32, message = "出库单编号字符长度不能超过 32 位")
    private String deliveryNo;

    /**
     * 发货数量
     */
    @NotNull(message = "发货数量不能为空")
    @Positive(message = "发货数量必须为正整数")
    private Integer deliveryNum;


    /**
     * 发货时间
     */
    @NotNull(message = "发货时间不能为空")
    private LocalDateTime deliveryTime;


    /**
     * 发货人
     */
    @NotBlank(message = "发货人不能为空")
    private String deliveryUser;

    /**
     * 发货人
     */
    @NotBlank(message = "发货人不能为空")
    private String deliveryUsername;

    /**
     * 出库备注
     */
    private String deliveryNote;


    /**
     * 发货仓库编码
     */
    @NotBlank(message = "发货仓库编码不能为空")
    @Length(max = 32, message = "发货仓库编号字符长度不能超过 32 位")
    private String deliveryWarehouseCode;


    /**
     * 发货仓库名称
     */
    @NotBlank(message = "发货仓库名称不能为空")
    @Length(max = 32, message = "发货仓库名称字符长度不能超过 32 位")
    private String deliveryWarehouseName;

    /**
     * 加工原料收货明细
     */
    @Valid
    private List<MaterialReceiptItem> materialReceiptItems;

    @Data
    public static class MaterialReceiptItem {

        /**
         * sku
         */
        @NotBlank(message = "sku 不能为空")
        @Length(max = 100, message = "sku字符长度不能超过 100 位")
        private String sku;

        /**
         * sku 批次码
         */
        @Length(max = 32, message = "sku批次码字符长度不能超过 32 位")
        private String skuBatchCode;

        /**
         * 出库数量
         */
        @NotNull(message = "出库数不能为空")
        @Positive(message = "出库数必须为正整数")
        private Integer deliveryNum;


    }

}
