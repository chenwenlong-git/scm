package com.hete.supply.scm.server.scm.sample.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/4/17 11:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleSelectionDto extends SampleChildIdAndVersionDto {

    @ApiModelProperty(value = "成功数量")
    @NotNull(message = "成功数量不能为空")
    private Integer successNum;

    @ApiModelProperty(value = "退样数量")
    @NotNull(message = "退样数量不能为空")
    private Integer returnNum;

    @ApiModelProperty(value = "闪售数量")
    @NotNull(message = "闪售数量不能为空")
    private Integer saleNum;

    @ApiModelProperty(value = "正品sku")
    private String sku;

    @ApiModelProperty(value = "样品成本单价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "选中样实拍图")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "生产信息列表")
    @Valid
    private List<SampleChildOrderInfoDto> sampleChildOrderInfoList;

    @ApiModelProperty(value = "选样结果说明")
    @Length(max = 255, message = "选样结果说明长度不能超过255个字符")
    private String remark;

    @ApiModelProperty(value = "原料产品明细列表")
    @Valid
    private List<SampleRawDto> sampleRawList;

    @ApiModelProperty(value = "样品工序列表")
    @Valid
    private List<SampleProcessDto> sampleProcessList;

    @ApiModelProperty(value = "失败退样选样结果说明")
    @Length(max = 255, message = "失败退样选样结果说明长度不能超过255个字符")
    private String returnSampleReason;

    @ApiModelProperty(value = "失败闪售选样结果说明")
    @Length(max = 255, message = "失败闪售选样结果说明长度不能超过255个字符")
    private String saleSampleReason;

}
