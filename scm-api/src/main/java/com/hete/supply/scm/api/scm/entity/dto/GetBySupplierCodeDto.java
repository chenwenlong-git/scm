package com.hete.supply.scm.api.scm.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/19 11:02
 */
@Data
public class GetBySupplierCodeDto {

    @NotEmpty(message = "供应商代码不能为空")
    private List<String> supplierCodeList;

}
