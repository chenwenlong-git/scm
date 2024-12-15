package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatus;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 供应商信息表
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier")
@ApiModel(value = "SupplierPo对象", description = "供应商信息表")
public class SupplierPo extends BasePo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_id", type = IdType.ASSIGN_ID)
    private Long supplierId;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "供应商类型：自营供应商(ONESELF_BUSINESS)、合作供应商(COOPERATION)")
    private SupplierType supplierType;


    @ApiModelProperty(value = "供应商状态：启用(ENABLED)、禁用(DISABLED)")
    private SupplierStatus supplierStatus;


    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;


    @ApiModelProperty(value = "产能")
    private Integer capacity;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "开票资质：否(NO)、是(YES)")
    private SupplierInvoicing supplierInvoicing;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "税点")
    private BigDecimal taxPoint;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "社会信用代码")
    private String creditCode;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "公司名称")
    private String corporateName;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "法定代表人")
    private String legalPerson;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "营业期限开始")
    private LocalDateTime businessTimeStart;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "营业期限结束")
    private LocalDateTime businessTimeEnd;


    @ApiModelProperty(value = "开发采购员")
    private String devUser;


    @ApiModelProperty(value = "开发采购员的名称")
    private String devUsername;


    @ApiModelProperty(value = "跟单采购员")
    private String followUser;


    @ApiModelProperty(value = "跟单采购员名称")
    private String followUsername;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "进出口资质：否(NO)、是(YES)")
    private SupplierExport supplierExport;


    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;


    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "国家")
    private String country;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "省份/州")
    private String province;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 不在进行维护
     */
    @Deprecated
    @ApiModelProperty(value = "详细地址")
    private String address;


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

    @ApiModelProperty(value = "物流时效")
    private Integer logisticsAging;

    @ApiModelProperty(value = "供应商别称")
    private String supplierAlias;

    @ApiModelProperty(value = "对账周期")
    private ReconciliationCycle reconciliationCycle;

    @ApiModelProperty(value = "结算日期")
    private String settleTime;

}
