package com.hete.supply.scm.server.scm.supplier.entity.vo;


import com.hete.supply.scm.server.scm.supplier.enums.SupplierSubjectType;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author ChenWenLong
 * @date 2024/5/15 17:00
 */
@Data
@NoArgsConstructor
public class SupplierSubjectVo {

    @ApiModelProperty(value = "主键id")
    private Long supplierSubjectId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "主体类型")
    private SupplierSubjectType supplierSubjectType;

    @ApiModelProperty(value = "主体")
    private String subject;

    @ApiModelProperty(value = "法定代表人")
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

    @ApiModelProperty(value = "进出口资质")
    private BooleanType supplierExport;

    @ApiModelProperty(value = "开票资质")
    private BooleanType supplierInvoicing;

    @ApiModelProperty(value = "税点")
    private BigDecimal taxPoint;

    @ApiModelProperty(value = "营业执照")
    private List<String> businessFileCodeList;

}
