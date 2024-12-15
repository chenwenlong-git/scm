package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 导入供应商主体
 *
 * @author chenwenlong
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierSubjectImportationDto extends BaseImportationRowDto {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "主体类型")
    private String supplierSubjectType;

    @ApiModelProperty(value = "公司名称")
    private String subject;

    @ApiModelProperty(value = "法人")
    private String legalPerson;

    @ApiModelProperty(value = "联系人")
    private String contactsName;

    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;

    @ApiModelProperty(value = "注册资金")
    private String registerMoney;

    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    @ApiModelProperty(value = "经营地址")
    private String businessAddress;

    @ApiModelProperty(value = "社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "进出口资质：否、是")
    private String supplierExport;

    @ApiModelProperty(value = "开票资质：否、是")
    private String supplierInvoicing;

    @ApiModelProperty(value = "税点")
    private String taxPoint;

}
