package com.hete.supply.scm.server.scm.develop.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopReviewOrderType;
import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import com.hete.supply.scm.server.scm.develop.enums.DevelopReviewRelated;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/7 17:14
 */
@Data
@NoArgsConstructor
public class DevelopReviewDetailVo {
    @ApiModelProperty(value = "审版单号")
    private String developReviewOrderNo;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "审版单状态")
    private DevelopReviewOrderStatus developReviewOrderStatus;

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


    @ApiModelProperty(value = "打版次数")
    private Integer pamphletTimes;

    @ApiModelProperty(value = "商品类目id")
    private Long categoryId;

    @ApiModelProperty(value = "商品类目")
    private String category;

    @ApiModelProperty(value = "产前样采购单号")
    private String prenatalSampleOrderNo;

    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    @ApiModelProperty(value = "审版关联单据类型")
    private DevelopReviewRelated developReviewRelated;

    @ApiModelProperty(value = "开发子单生产属性信息")
    private List<DevelopChildOrderAttrVo> developChildOrderAttrList;

    @ApiModelProperty(value = "款式参考图片")
    private List<String> styleReferenceFileCodeList;

    @ApiModelProperty(value = "颜色参考图片")
    private List<String> colorReferenceFileCodeList;

    @ApiModelProperty(value = "样品信息")
    private List<DevelopReviewSampleVo> developReviewSampleList;

    @ApiModelProperty(value = "产前样的样单号")
    private String sampleOrderNoJoining;

    @ApiModelProperty(value = "版单原料信息")
    private List<DevelopPamphletRawDetailVo> developPamphletRawDetailList;

    @ApiModelProperty(value = "版单的需求描述")
    private String demandDesc;

}
