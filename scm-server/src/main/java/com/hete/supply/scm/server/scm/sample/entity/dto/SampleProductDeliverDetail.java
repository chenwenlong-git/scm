package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ChenWenLong
 * @date 2023/4/17 09:48
 */
@Data
@NoArgsConstructor
public class SampleProductDeliverDetail {

    @NotBlank(message = "样品采购子单号不能为空")
    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    @Length(max = 100, message = "原料sku长度不能超过100个字符")
    private String sku;

    @NotBlank(message = "sku批次码不能为空")
    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @NotNull(message = "收货数量不能为空")
    @ApiModelProperty(value = "收货数量")
    private Integer receiptCnt;

    @NotNull(message = "发货数(入库数量)不能为空")
    @ApiModelProperty(value = "发货数(入库数量)")
    @Min(value = 1, message = "发货数不能小于0")
    private Integer deliveryCnt;

    @NotNull(message = "采购数不能为空")
    @ApiModelProperty(value = "采购数")
    @Min(value = 1, message = "采购数不能小于0")
    private Integer purchaseCnt;

    @NotBlank(message = "样品结果编号不能为空")
    @ApiModelProperty(value = "样品结果编号")
    private String sampleResultNo;

}
