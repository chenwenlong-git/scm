package com.hete.supply.scm.server.scm.sample.entity.vo;

import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rockyHuas
 * @date 2022/11/2 22:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SamplePurchasePrintVo extends SupplierSimpleVo {

    @ApiModelProperty(value = "id")
    private Long sampleChildOrderId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "样品采购子单号")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private List<String> warehouseTypeList;


    @ApiModelProperty(value = "采购数")
    private Integer purchaseCnt;

    @ApiModelProperty(value = "供应商生产信息")
    private String supplierProduction;

    @ApiModelProperty(value = "业务约定交期")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "参照图片")
    private List<String> contrastFileCode;

    @ApiModelProperty(value = "生产信息列表")
    private List<SampleChildOrderInfoVo> sampleChildOrderInfoList;

    @ApiModelProperty(value = "需求描述")
    private String demandDescribe;

    @ApiModelProperty(value = "样品工序列表")
    private List<SampleProcessVo> sampleChildProcessList;

    @ApiModelProperty(value = "样品工序描述列表")
    private List<SampleProcessDescVo> sampleProcessDescList;

    @ApiModelProperty(value = "原料bom表")
    private List<SampleRawVo> sampleRawList;


}
