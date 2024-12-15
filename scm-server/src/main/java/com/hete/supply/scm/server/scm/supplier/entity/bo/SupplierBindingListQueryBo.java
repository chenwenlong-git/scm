package com.hete.supply.scm.server.scm.supplier.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/10/10 16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierBindingListQueryBo {

    @ApiModelProperty(value = "SKU列表")
    @NotEmpty(message = "SKU不能为空")
    private List<String> skuList;

}
