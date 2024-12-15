package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SampleResult;
import com.hete.supply.scm.api.scm.entity.enums.SampleResultStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/4/14 14:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SampleChildOrderResultSearchDto extends ComPageDto {

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "样品需求母单号")
    private String sampleParentOrderNo;

    @ApiModelProperty(value = "供应商代码")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "平台")
    private List<String> platformList;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTimeStart;

    @ApiModelProperty(value = "选样时间")
    private LocalDateTime sampleTimeEnd;

    @ApiModelProperty(value = "选样结果批量")
    private List<SampleResult> sampleResultList;

    @ApiModelProperty(value = "处理状态")
    private SampleResultStatus sampleResultStatus;

    @ApiModelProperty(value = "处理状态批量")
    private List<SampleResultStatus> sampleResultStatusList;

    @ApiModelProperty(value = "ID批量")
    private List<Long> sampleChildOrderResultIdList;

    @ApiModelProperty(value = "样品采购子单号批量")
    private List<String> sampleChildOrderNoList;

    @ApiModelProperty(value = "样品采购母单号批量")
    private List<String> sampleParentOrderNoList;

    @ApiModelProperty(value = "样品采购子单ID批量")
    private List<Long> sampleChildOrderIdList;

    @ApiModelProperty(value = "样品结果ID批量")
    private List<String> sampleResultNoList;

}
