package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.supply.scm.server.scm.supplier.enums.SupplierSubjectType;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.mybatis.plus.entity.po.BaseSupplyPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 供应商主体信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_subject")
@ApiModel(value = "SupplierSubjectPo对象", description = "供应商主体信息")
public class SupplierSubjectPo extends BaseSupplyPo {


    @ApiModelProperty(value = "主键id")
    @TableId(value = "supplier_subject_id", type = IdType.ASSIGN_ID)
    private Long supplierSubjectId;


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


}
