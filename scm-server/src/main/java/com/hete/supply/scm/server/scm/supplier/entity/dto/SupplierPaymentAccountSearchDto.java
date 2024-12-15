package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.supply.scm.server.scm.supplier.enums.SupplierPaymentAccountStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/12/5 17:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierPaymentAccountSearchDto extends ComPageDto {

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "状态")
    private List<SupplierPaymentAccountStatus> supplierPaymentAccountStatusList;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCode;

}
