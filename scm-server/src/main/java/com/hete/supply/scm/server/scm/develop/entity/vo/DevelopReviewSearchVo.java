package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewSampleSource;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/7 11:42
 */
@Data
@NoArgsConstructor
public class DevelopReviewSearchVo {
    @ApiModelProperty(value = "打样样图")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "审版单状态")
    private DevelopReviewOrderStatus developReviewOrderStatus;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "样品数量")
    private Integer developSampleNum;

    @ApiModelProperty(value = "审版单类型")
    private DevelopReviewOrderType developReviewOrderType;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    // todo 需求、质量
    @ApiModelProperty(value = "样品信息")
    private List<DevelopSampleReviewVo> developSampleReviewList;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人")
    private String createUsername;


    @ApiModelProperty(value = "审版人")
    private String reviewUser;

    @ApiModelProperty(value = "审版人")
    private String reviewUsername;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "审版时间")
    private LocalDateTime reviewDate;

    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "开发需求单号")
    private String developParentOrderNo;

    @ApiModelProperty(value = "样品需求来源")
    private DevelopReviewSampleSource developReviewSampleSource;
}
