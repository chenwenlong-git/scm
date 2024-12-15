package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/1/11.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "返修单入库变更消息")
public class RepairOrderInStorageDto extends BaseMqMessageDto {

    @ApiModelProperty(value = "返修单号", example = "R2023001", required = true)
    private String repairNo;

    @ApiModelProperty(value = "入库商品信息", required = true)
    private List<SkuInStorageInfo> inStorageSkus;

    @Data
    public static class SkuInStorageInfo {

        @ApiModelProperty(value = "sku编码")
        private String skuCode;

        @ApiModelProperty(value = "收货数")
        private Integer receiveAmount;
    }
}



