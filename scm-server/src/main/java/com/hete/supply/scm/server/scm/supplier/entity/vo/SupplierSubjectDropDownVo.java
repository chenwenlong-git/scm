package com.hete.supply.scm.server.scm.supplier.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/16 17:40
 */
@Data
@NoArgsConstructor
public class SupplierSubjectDropDownVo {
    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "主体明细列表")
    public List<SupplierSubjectDropDownItemVo> supplierSubjectDropDownItemList;

    @Data
    public static class SupplierSubjectDropDownItemVo {
        @ApiModelProperty(value = "主体")
        private String subject;
    }

}
