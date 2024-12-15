package com.hete.supply.scm.server.scm.ibfs.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/5/23 11:37
 */
@Data
@NoArgsConstructor
public class FinanceDataPermDto {
    @ApiModelProperty(value = "授权供应商代码(前端忽略该参数)")
    private List<String> authSupplierCode;

    @ApiModelProperty(value = "当前审批人（数据权限）(前端忽略该参数)")
    private String ctrlUser;

    @ApiModelProperty(value = "spm授权供应商代码(前端忽略该参数)")
    private List<String> spmAuthSupplierCode;
}
