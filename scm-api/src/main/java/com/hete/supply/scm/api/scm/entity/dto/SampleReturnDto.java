package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ReceiptOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/3 10:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleReturnDto extends ComPageDto {
    @ApiModelProperty(value = "样品退货单号")
    private String sampleReturnOrderNo;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;


    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "退货单状态")
    private List<ReceiptOrderStatus> returnOrderStatusList;

    @ApiModelProperty(value = "运单号")
    private String trackingNo;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "样品退货单号批量")
    private List<String> sampleReturnOrderNoList;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private List<String> supplierNameList;

}
