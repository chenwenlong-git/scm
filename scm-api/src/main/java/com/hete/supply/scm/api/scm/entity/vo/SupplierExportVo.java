package com.hete.supply.scm.api.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.SupplierExport;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierInvoicing;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class SupplierExportVo {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;

    @ApiModelProperty(value = "开发采购员")
    private String devUsername;

    @ApiModelProperty(value = "跟单采购员")
    private String followUsername;

    @ApiModelProperty(value = "月产能")
    private Integer capacity;

    @ApiModelProperty(value = "进出口资质")
    private SupplierExport supplierExport;

    @ApiModelProperty(value = "开票资质")
    private SupplierInvoicing supplierInvoicing;

    @ApiModelProperty(value = "税点")
    private BigDecimal taxPoint;

    @ApiModelProperty(value = "社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "公司名称")
    private String corporateName;


    @ApiModelProperty(value = "法定代表人")
    private String legalPerson;


    @ApiModelProperty(value = "营业期限开始")
    private LocalDateTime businessTimeStart;


    @ApiModelProperty(value = "营业期限结束")
    private LocalDateTime businessTimeEnd;


    @ApiModelProperty(value = "国家")
    private String country;


    @ApiModelProperty(value = "省/州")
    private String province;


    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "物流时效")
    private Integer logisticsAging;


}
