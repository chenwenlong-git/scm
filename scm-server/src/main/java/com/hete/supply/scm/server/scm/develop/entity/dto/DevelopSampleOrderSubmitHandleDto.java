package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleSaleHandle;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/1 16:44
 */
@Data
@NoArgsConstructor
public class DevelopSampleOrderSubmitHandleDto {

    @ApiModelProperty(value = "样品单信息")
    @NotEmpty(message = "样品单信息不能为空")
    @Valid
    private List<DevelopSampleOrderSubmitHandleItemDto> developSampleOrderSubmitHandleItemList;

    @Data
    public static class DevelopSampleOrderSubmitHandleItemDto {

        @ApiModelProperty(value = "version")
        @NotNull(message = "version不能为空")
        private Integer version;

        @ApiModelProperty(value = "样品单号")
        @NotBlank(message = "样品单号不能为空")
        private String developSampleOrderNo;

        @ApiModelProperty(value = "样品处理方式")
        @NotNull(message = "样品处理方式不能为空")
        private DevelopSampleMethod developSampleMethod;

        @ApiModelProperty(value = "货物走向")
        @NotNull(message = "货物走向不能为空")
        private DevelopSampleDirection developSampleDirection;

        @ApiModelProperty(value = "sku")
        private String sku;

        @ApiModelProperty(value = "退样运单号")
        @Length(max = 15, message = "退样运单号的长度不能超过 15 位")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "退样运单号只能包含数字或大小写字母")
        private String returnTrackingNo;

        @ApiModelProperty(value = "样品价格")
        @DecimalMax(value = "99999999.99", message = "样品报价不能超过1亿")
        @Digits(integer = 8, fraction = 2, message = "样品报价小数位数不能超过两位")
        private BigDecimal skuBatchSamplePrice;

        @ApiModelProperty(value = "收货仓库编码")
        private String warehouseCode;

        @ApiModelProperty(value = "收货仓库名称")
        private String warehouseName;

        @ApiModelProperty(value = "闪售操作方式")
        private DevelopSampleSaleHandle developSampleSaleHandle;

        @ApiModelProperty(value = "渠道大货价格")
        private List<DevelopOrderPriceSaveDto> developOrderPriceList;

    }

}
