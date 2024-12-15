package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.SupplierGrade;
import com.hete.supply.scm.api.scm.entity.enums.SupplierStatusSearch;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/25 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SupplierSearchDto extends ComPageDto {

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商代码批量")
    private List<String> supplierCodeList;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "SPU批量")
    private List<String> spuList;

    @ApiModelProperty(value = "供应商等级")
    private SupplierGrade supplierGrade;

    @ApiModelProperty(value = "入驻时间开始")
    private LocalDateTime joinTimeStart;

    @ApiModelProperty(value = "入驻时间结束")
    private LocalDateTime joinTimeEnd;

    @ApiModelProperty(value = "跟单采购员")
    private String followUser;

    @ApiModelProperty(value = "跟单采购员名称")
    private String followUsername;

    @ApiModelProperty(value = "开发采购员")
    private String devUser;

    @ApiModelProperty(value = "开发采购员名称")
    private String devUsername;

    @ApiModelProperty(value = "产能开始")
    private Integer capacityStart;

    @ApiModelProperty(value = "产能结束")
    private Integer capacityEnd;

    @ApiModelProperty(value = "供应商别称批量")
    private List<String> supplierAliasList;

    @ApiModelProperty(value = "供应商状态")
    private SupplierStatusSearch supplierStatus;

    @ApiModelProperty(value = "供应商状态批量")
    private List<SupplierStatusSearch> supplierStatusList;

    @ApiModelProperty(value = "收款账号是否绑定")
    private BooleanType supplierPayAccountBinding;

    @ApiModelProperty(value = "主体信息是否维护")
    private BooleanType supplierSubjectMaintain;

}
