package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author yanjiawei
 * Created on 2024/1/25.
 */
@Data
public class UpdateBatchCodePriceDto extends BaseMqMessageDto {

    private static final int WMS_UPDATE_LIMIT = 100;

    @ApiModelProperty(value = "批次码价格列表", notes = "批次码价格入参列表不能为空!批次码价格入参列表不能超过100条!")
    @NotEmpty(message = "批次码价格列表不能为空")
    private List<UpdateBatchCodeCostPrice> batchCodePriceList;

    @Data
    public static class UpdateBatchCodeCostPrice {
        @ApiModelProperty(value = "批次码", notes = "批次码不能为空")
        private String batchCode;

        @ApiModelProperty(value = "金额", notes = "金额必须大于0 且保留两位小数")
        private BigDecimal price;
    }

    public void valid() {
        // 校验批次码价格列表不能超过100条
        if (CollectionUtils.isEmpty(batchCodePriceList)) {
            throw new ParamIllegalException("批次码价格入参列表不能为空");
        }
        if (batchCodePriceList.size() > WMS_UPDATE_LIMIT) {
            throw new ParamIllegalException("批次码价格入参列表不能超过100条");
        }

        // 遍历校验价格
        for (UpdateBatchCodeCostPrice batchCodeCostPrice : batchCodePriceList) {
            BigDecimal price = batchCodeCostPrice.getPrice();

            if (Objects.isNull(price)) {
                throw new ParamIllegalException("批次码价格不能为空");
            }
            if (price.scale() > 2) {
                throw new ParamIllegalException("批次码价格价格必须保留两位小数");
            }
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParamIllegalException("批次码价格价格必须大于0");
            }
        }
    }

}
