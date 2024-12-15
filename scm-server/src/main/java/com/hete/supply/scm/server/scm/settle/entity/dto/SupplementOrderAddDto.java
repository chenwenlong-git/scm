package com.hete.supply.scm.server.scm.settle.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplementType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/1 14:34
 */
@Data
@NoArgsConstructor
public class SupplementOrderAddDto {

    @NotNull(message = "补款类型不能为空")
    @ApiModelProperty(value = "补款类型")
    private SupplementType supplementType;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "补款员工")
    private String supplementUser;

    @ApiModelProperty(value = "补款员工名称")
    private String supplementUsername;

    @NotNull(message = "约定结算时间不能为空")
    @ApiModelProperty(value = "约定结算时间")
    private LocalDateTime aboutSettleTime;

    @ApiModelProperty(value = "支付图片凭证")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "文件凭证")
    private List<String> documentCodeList;


    @ApiModelProperty(value = "价差补款明细列表")
    private List<SupplementOrderPurchaseDto> supplementOrderPurchaseList;


    @ApiModelProperty(value = "加工补款明细列表")
    private List<SupplementOrderProcessDto> supplementOrderProcessList;


    @ApiModelProperty(value = "其他明细列表")
    private List<SupplementOrderOtherDto> supplementOrderOtherList;


}
