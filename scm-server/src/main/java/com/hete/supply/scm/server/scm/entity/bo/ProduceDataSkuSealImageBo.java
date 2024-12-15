package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/12/15 17:03
 */
@Data
@NoArgsConstructor
public class ProduceDataSkuSealImageBo {

    @ApiModelProperty("来源单号")
    @NotBlank(message = "来源单号不能为空")
    private String sourceOrderNo;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "效果图")
    private List<String> fileCodeList;

}
