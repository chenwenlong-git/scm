package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/7 11:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopReviewSearchDto extends ComPageDto {
    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "审版单号")
    private List<String> developReviewOrderNoList;

    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;


    @ApiModelProperty(value = "审版人")
    private String reviewUser;

    @ApiModelProperty(value = "审版人")
    private String reviewUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "审版时间")
    private LocalDateTime reviewDateStart;

    @ApiModelProperty(value = "审版时间")
    private LocalDateTime reviewDateEnd;

    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "审版单类型")
    private DevelopReviewOrderType developReviewOrderType;

    @ApiModelProperty(value = "审版单状态")
    private List<DevelopReviewOrderStatus> developReviewOrderStatusList;

    @ApiModelProperty(value = "产前样单号")
    private String prenatalSampleOrderNo;
}
