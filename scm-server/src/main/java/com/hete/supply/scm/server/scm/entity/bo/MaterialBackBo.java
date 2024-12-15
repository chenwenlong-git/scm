package com.hete.supply.scm.server.scm.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.MaterialBackStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author rockyHuas
 * @date 2022/11/20 18:12
 */
@Data
@NoArgsConstructor
public class MaterialBackBo {

    @ApiModelProperty(value = "归还状态")
    private MaterialBackStatus materialBackStatus;

    @ApiModelProperty(value = "可归还数量")
    private int availableBackNum;

    @ApiModelProperty("原料批次码信息")
    private List<MaterialBackSku> materialBackSkus;

    @Data
    public static class MaterialBackSku {

        @ApiModelProperty(value = "sku批次码")
        private String skuBatchCode;

        @ApiModelProperty(value = "可归还数量")
        private int availableBackNum;
    }

}
