package com.hete.supply.scm.server.scm.supplier.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hete.support.mybatis.plus.entity.po.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商账号信息
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_account")
@ApiModel(value = "SupplierAccountPo对象", description = "供应商账号信息")
@Deprecated
public class SupplierAccountPo extends BasePo {


    @ApiModelProperty(value = "主键ID")
    @TableId(value = "supplier_account_id", type = IdType.ASSIGN_ID)
    private Long supplierAccountId;


    @ApiModelProperty(value = "网点")
    private String networkAddress;


    @ApiModelProperty(value = "开户人")
    private String registrationPeople;


    @ApiModelProperty(value = "账号")
    private String account;


    @ApiModelProperty(value = "开户行")
    private String accountBank;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;


    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;


}
