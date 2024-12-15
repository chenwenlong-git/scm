package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleType;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/3 14:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopSampleOrderSearchDto extends ComPageDto {
    @ApiModelProperty(value = "样品单号")
    private String developSampleOrderNo;

    @ApiModelProperty(value = "开发子单号")
    private String developChildOrderNo;

    @ApiModelProperty(value = "版单号")
    private String developPamphletOrderNo;

    @ApiModelProperty(value = "关联单据号")
    private String receiptOrderNo;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "sku批次码")
    private String skuBatchCode;

    @ApiModelProperty(value = "产品名称")
    private String skuEncode;

    @ApiModelProperty(value = "处理人")
    private String handleUsername;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "签收人")
    private String signUsername;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "处理时间开始")
    private LocalDateTime handleTimeStart;

    @ApiModelProperty(value = "处理时间结束")
    private LocalDateTime handleTimeEnd;

    @ApiModelProperty(value = "上架时间开始")
    private LocalDateTime shelvesTimeStart;

    @ApiModelProperty(value = "上架时间结束")
    private LocalDateTime shelvesTimeEnd;

    @ApiModelProperty(value = "签收时间开始")
    private LocalDateTime signTimeStart;

    @ApiModelProperty(value = "签收时间结束")
    private LocalDateTime signTimeEnd;

    @ApiModelProperty(value = "处理方式")
    private List<DevelopSampleMethod> developSampleMethodList;

    @ApiModelProperty(value = "状态")
    private List<DevelopSampleStatus> developSampleStatusList;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "平台批量")
    private List<String> platformList;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "授权供应商代码")
    private List<String> authSupplierCodeList;

    @ApiModelProperty(value = "样品单号批量")
    private List<String> developSampleOrderNoList;

    @ApiModelProperty(value = "sku批量")
    private List<String> skuList;

    @ApiModelProperty(value = "退样签收人")
    private String returnUsername;

    @ApiModelProperty(value = "退样签收时间开始")
    private LocalDateTime returnTimeStart;

    @ApiModelProperty(value = "退样签收时间结束")
    private LocalDateTime returnTimeEnd;

    @ApiModelProperty(value = "样品单类型批量")
    private List<DevelopSampleType> developSampleTypeList;

    @ApiModelProperty(value = "开发子单号批量")
    private List<String> developChildOrderNoList;

    @ApiModelProperty(value = "产品名称批量")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "sku批次码批量")
    private List<String> skuBatchCodeList;

}
