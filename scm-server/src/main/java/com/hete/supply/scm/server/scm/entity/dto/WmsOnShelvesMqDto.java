package com.hete.supply.scm.server.scm.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/26 17:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WmsOnShelvesMqDto extends BaseMqMessageDto {

    @NotBlank(message = "容器码不能为空")
    private String containerCode;

    /**
     * 收货单号
     */
    @NotBlank(message = "收货单号不能为空")
    private String receiveOrderNo;

    /**
     * 质检单号
     */
    @NotBlank(message = "质检单号不能为空")
    private String qcOrderNo;


    @ApiModelProperty(value = "scm操作人编码")
    @NotBlank(message = "scm操作人编码不能为空")
    private String operator;

    @ApiModelProperty(value = "scm操作人名称")
    @NotBlank(message = "scm操作人名称不能为空")
    private String operatorName;

    /**
     * 上架明细列表
     */
    @NotEmpty(message = "上架明细不能为空")
    @Valid
    private List<OnShelvesDetail> onShelvesDetailList;

    /**
     * 上架明细
     */
    @Data
    public static class OnShelvesDetail {

        @NotBlank(message = "次品处理单号不能为空")
        @ApiModelProperty(value = "次品处理单号")
        private String defectHandlingNo;

        @NotNull(message = "明细id不能为空")
        @ApiModelProperty(value = "明细id")
        private Long bizDetailId;
        /**
         * sku编码
         */
        @NotBlank(message = "sku编码不能为空")
        private String skuCode;

        /**
         * 批次码
         */
        @NotBlank(message = "批次码不能为空")
        private String batchCode;

        /**
         * 上架数量
         */
        @NotNull(message = "上架数量不能为空")
        private Integer amount;

        @JsonProperty(value = "platCode")
        @NotBlank(message = "平台不能为空")
        @ApiModelProperty(value = "平台")
        private String platform;
    }
}
