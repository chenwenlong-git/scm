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
 * 供应商联系人
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("supplier_contact")
@ApiModel(value = "SupplierContactPo对象", description = "供应商联系人")
@Deprecated
public class SupplierContactPo extends BasePo {


    @ApiModelProperty(value = "主键ID")
    @TableId(value = "supplier_contact_id", type = IdType.ASSIGN_ID)
    private Long supplierContactId;


    @ApiModelProperty(value = "联系人姓名")
    private String name;


    @ApiModelProperty(value = "联系人电话")
    private String phone;


    @ApiModelProperty(value = "职位")
    private String position;


    @ApiModelProperty(value = "备注")
    private String remarks;


    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;


    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


    @ApiModelProperty(value = "创建人名称")
    private String createUsername;


    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;


}
