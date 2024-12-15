package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.PrepaymentOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/10 17:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PrepaymentSearchDto extends ComPageDto {

    @ApiModelProperty(value = "预付款对象(供应商code)")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "预付款单号")
    private String prepaymentOrderNo;

    @ApiModelProperty(value = "预付款单号")
    private List<String> prepaymentOrderNoList;

    @ApiModelProperty(value = "预付款单状态")
    private List<PrepaymentOrderStatus> prepaymentOrderStatusList;

    @ApiModelProperty(value = "授权供应商代码(前端忽略该参数)")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "当前审批人(前端忽略该参数)")
    private String approveUser;

    @ApiModelProperty(value = "当前审批人（数据权限）(前端忽略该参数)")
    private String ctrlUser;

    @ApiModelProperty(value = "spm授权供应商代码(前端忽略该参数)")
    private List<String> spmAuthSupplierCode;

    @ApiModelProperty(value = "申请日期开始")
    private LocalDateTime applyDateStart;

    @ApiModelProperty(value = "申请日期结束")
    private LocalDateTime applyDateEnd;
}
