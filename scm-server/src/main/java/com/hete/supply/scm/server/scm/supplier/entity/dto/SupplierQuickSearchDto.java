package com.hete.supply.scm.server.scm.supplier.entity.dto;

import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierQuickSearchDto extends ComPageDto {

    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

}
