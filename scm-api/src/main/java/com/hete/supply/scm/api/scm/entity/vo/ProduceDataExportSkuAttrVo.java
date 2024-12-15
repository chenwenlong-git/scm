package com.hete.supply.scm.api.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导出的生产属性字段（字段顺序和命名需要遵循导出文件标题，以及ProduceDataBizService.setFieldValue方法设置规则）
 *
 * @author ChenWenLong
 * @date 2024/6/26 15:46
 */
@Data
public class ProduceDataExportSkuAttrVo extends ProduceDataSkuVo {

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "蕾丝面积")
    private String laceArea;

    @ApiModelProperty(value = "档长尺寸")
    private String fileLengthSize;

    @ApiModelProperty(value = "完成长尺寸")
    private String completeLongSize;

    @ApiModelProperty(value = "网帽大小")
    private String netCapSize;

    @ApiModelProperty(value = "刘海分式")
    private String partedBangs;

    @ApiModelProperty(value = "Parting分式")
    private String parting;

    @ApiModelProperty(value = "材料")
    private String material;

    @ApiModelProperty(value = "廓形")
    private String contour;

    @ApiModelProperty(value = "颜色色系")
    private String colorSystem;

    @ApiModelProperty(value = "颜色混合分区")
    private String colorMixPartition;

    @ApiModelProperty(value = "左侧档长尺寸")
    private String leftSideLength;

    @ApiModelProperty(value = "左侧完成长")
    private String leftFinish;

    @ApiModelProperty(value = "右侧档长尺寸")
    private String rightSideLength;

    @ApiModelProperty(value = "右侧完成长")
    private String rightFinish;

    @ApiModelProperty(value = "是否对称")
    private String symmetry;

    @ApiModelProperty(value = "预剪蕾丝")
    private String preselectionLace;

}
