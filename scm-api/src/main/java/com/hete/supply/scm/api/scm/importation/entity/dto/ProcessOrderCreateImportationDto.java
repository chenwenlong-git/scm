package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2023/2/5 14:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessOrderCreateImportationDto extends BaseImportationRowDto {

    @ApiModelProperty(value = "加工单类型")
    @NotNull(message = "加工单类型不能为空")
    private String processOrderType;

    @NotBlank(message = "平台不能为空")
    @ApiModelProperty(value = "平台")
    private String platform;

    @NotBlank(message = "收货仓库编码为空")
    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "sku")
    @NotBlank(message = "sku 不能为空")
    private String sku;

    @ApiModelProperty(value = "加工数量")
    @NotNull(message = "加工数不能为空")
    private Integer processNum;

    @ApiModelProperty(value = "业务约定日期")
    @NotNull(message = "业务约定交期不能为空")
    private String deliverDate;

    @ApiModelProperty(value = "加工备注")
    private String processOrderNote;

    @ApiModelProperty(value = "原料发货仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "原料sku1")
    private String materialSku1;

    @ApiModelProperty(value = "单个加工需求数1")
    private Integer deliveryNum1;

    @ApiModelProperty(value = "原料sku2")
    private String materialSku2;

    @ApiModelProperty(value = "单个加工需求数2")
    private Integer deliveryNum2;

    @ApiModelProperty(value = "原料sku3")
    private String materialSku3;

    @ApiModelProperty(value = "单个加工需求数3")
    private Integer deliveryNum3;

    @ApiModelProperty(value = "原料sku4")
    private String materialSku4;

    @ApiModelProperty(value = "单个加工需求数4")
    private Integer deliveryNum4;

    @ApiModelProperty(value = "原料sku5")
    private String materialSku5;

    @ApiModelProperty(value = "单个加工需求数5")
    private Integer deliveryNum5;

    @ApiModelProperty(value = "工序名称1")
    private String processSecondName1;

    @ApiModelProperty(value = "工序类别1")
    private String processLabel1;

    @ApiModelProperty(value = "工序名称2")
    private String processSecondName2;

    @ApiModelProperty(value = "工序类别2")
    private String processLabel2;

    @ApiModelProperty(value = "工序名称3")
    private String processSecondName3;

    @ApiModelProperty(value = "工序类别3")
    private String processLabel3;

    @ApiModelProperty(value = "工序名称4")
    private String processSecondName4;

    @ApiModelProperty(value = "工序类别4")
    private String processLabel4;

    @ApiModelProperty(value = "工序名称5")
    private String processSecondName5;

    @ApiModelProperty(value = "工序类别5")
    private String processLabel5;

    @ApiModelProperty(value = "加工描述名称1")
    private String processDescName1;

    @ApiModelProperty(value = "加工描述值1")
    private String processDescValue1;

    @ApiModelProperty(value = "加工描述名称2")
    private String processDescName2;

    @ApiModelProperty(value = "加工描述值2")
    private String processDescValue2;

    @ApiModelProperty(value = "加工描述名称3")
    private String processDescName3;

    @ApiModelProperty(value = "加工描述值3")
    private String processDescValue3;

    @ApiModelProperty(value = "加工描述名称4")
    private String processDescName4;

    @ApiModelProperty(value = "加工描述值4")
    private String processDescValue4;

    @ApiModelProperty(value = "加工描述名称5")
    private String processDescName5;

    @ApiModelProperty(value = "加工描述值5")
    private String processDescValue5;


}
