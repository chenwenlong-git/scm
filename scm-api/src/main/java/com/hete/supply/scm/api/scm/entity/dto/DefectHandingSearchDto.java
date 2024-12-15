package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingProgramme;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingStatus;
import com.hete.supply.scm.api.scm.entity.enums.DefectHandlingType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/6/21 11:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DefectHandingSearchDto extends ComPageDto {
    @ApiModelProperty(value = "次品处理单号")
    private String defectHandlingNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "次品来源单据号：采购单号、加工单号")
    private String defectBizNo;

    @ApiModelProperty(value = "关联单号：采购发货单号、加工次品记录单号")
    private String relatedOrderNo;

    @ApiModelProperty(value = "次品方案（次品退供、次品报废、次品换货、次品让步）")
    private List<DefectHandlingProgramme> defectHandlingProgrammeList;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTimeStart;

    @ApiModelProperty(value = "确认时间")
    private LocalDateTime confirmTimeEnd;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "次品处理")
    private List<DefectHandlingStatus> defectHandlingStatusList;

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "次品处理单号批量")
    private List<String> defectHandlingNoList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

    @ApiModelProperty(value = "次品类型（比如大货、库内、加工）")
    private List<DefectHandlingType> defectHandlingTypeList;

}
