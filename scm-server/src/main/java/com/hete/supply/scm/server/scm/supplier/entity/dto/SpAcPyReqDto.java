package com.hete.supply.scm.server.scm.supplier.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/5/16 17:34
 */
@Data
@NoArgsConstructor
public class SpAcPyReqDto {

    @ApiModelProperty(value = "供应商编码列表")
    @NotEmpty(message = "供应商编码列表不能为空")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "模糊搜索关键字")
    private String searchKey;
}
