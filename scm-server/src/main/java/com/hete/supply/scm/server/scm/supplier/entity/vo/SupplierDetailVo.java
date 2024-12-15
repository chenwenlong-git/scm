package com.hete.supply.scm.server.scm.supplier.entity.vo;


import com.hete.supply.scm.api.scm.entity.enums.ReconciliationCycle;
import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierType;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author ChenWenLong
 * @date 2022/11/25 14:04
 */
@Data
@NoArgsConstructor
public class SupplierDetailVo {
    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商类型")
    private SupplierType supplierType;

    @ApiModelProperty(value = "供应商状态")
    private SupplierStatus supplierStatus;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "产能")
    private Integer capacity;

    @ApiModelProperty(value = "开发采购员")
    private String devUser;

    @ApiModelProperty(value = "开发采购员的名称")
    private String devUsername;

    @ApiModelProperty(value = "跟单采购员")
    private String followUser;

    @ApiModelProperty(value = "跟单采购员的名称")
    private String followUsername;


    @ApiModelProperty(value = "收货地址国家")
    private String shipToCountry;


    @ApiModelProperty(value = "收货地址省份/州")
    private String shipToProvince;


    @ApiModelProperty(value = "收货地址城市")
    private String shipToCity;


    @ApiModelProperty(value = "收货地址详细地址")
    private String shipToAddress;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;

    @ApiModelProperty(value = "物流时效")
    private Integer logisticsAging;

    @ApiModelProperty(value = "对账周期")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "结算日期")
    private String settleTime;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

    @ApiModelProperty(value = "收款账号列表")
    private List<SupplierPaymentAccountVo> supplierPaymentAccountList;

    @ApiModelProperty(value = "主体信息列表")
    private List<SupplierSubjectVo> supplierSubjectList;


}
