package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.server.scm.develop.enums.DevelopReviewRelated;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/8 15:26
 */
@Data
@NoArgsConstructor
public class DevelopReviewCreateDto {
    @NotNull(message = "审版关联单据类型不能为空")
    @ApiModelProperty(value = "审版关联单据类型")
    private DevelopReviewRelated developReviewRelated;

    @ApiModelProperty(value = "产前样单号")
    private String prenatalSampleOrderNo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @NotEmpty(message = "审版单明细不能为空")
    @ApiModelProperty(value = "审版单明细")
    private List<@Valid DevelopReviewCreateItemDto> developReviewCreateItemList;

    @ApiModelProperty(value = "审版详情")
    private List<@Valid DevelopReviewSampleInfoDto> developReviewSampleInfoList;
}
