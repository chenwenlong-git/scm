package com.hete.supply.scm.server.scm.process.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.server.scm.entity.vo.MaterialNumInformationVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author RockyHuas
 * @date 2022/11/11 14:04
 */
@Data
@NoArgsConstructor
public class ProcessOrderVo {

    @ApiModelProperty(value = "加工单 ID")
    private Long processOrderId;

    @ApiModelProperty(value = "加工单号")
    private String processOrderNo;


    @ApiModelProperty(value = "加工单状态")
    private ProcessOrderStatus processOrderStatus;


    @ApiModelProperty(value = "类型")
    private ProcessOrderType processOrderType;


    @ApiModelProperty(value = "spu")
    private String spu;


    @ApiModelProperty(value = "收货仓库编码")
    private String warehouseCode;


    @ApiModelProperty(value = "收货仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "收货仓库标签")
    private String warehouseTypes;

    @ApiModelProperty(value = "sku 列表")
    private List<ProcessOrderItemVo> processOrderItemVoList;

    @ApiModelProperty(value = "原料列表")
    private List<ProcessOrderMaterialVo> processOrderMaterialVoList;

    @ApiModelProperty(value = "spu 类目")
    private String categoryName;


    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "总加工数量")
    private Integer totalProcessNum;

    @ApiModelProperty(value = "下单人")
    private String createUsername;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "投产时间")
    private LocalDateTime producedTime;

    @ApiModelProperty(value = "质检时间")
    private LocalDateTime checkedTime;

    @ApiModelProperty(value = "收货时间")
    private LocalDateTime receiptTime;


    @ApiModelProperty(value = "入库时间")
    private LocalDateTime storedTime;

    @ApiModelProperty(value = "约定日期/期望上架时间")
    private LocalDateTime deliverDate;

    @ApiModelProperty(value = "答交日期", notes = "经过重新评估和调整后提出新的约定日期（deliverDate）")
    private LocalDateTime promiseDate;

    @ApiModelProperty(value = "答交日期是否延期")
    private PromiseDateDelayed promiseDateDelayed;

    @ApiModelProperty(value = "当前工序进度")
    private ProcessLabel currentProcessLabel;

    @ApiModelProperty(value = "原料归还状态")
    private MaterialBackStatus materialBackStatus;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "缺失信息remark")
    private Set<String> missingInformationRemarks;

    @ApiModelProperty(value = "缺失信息code")
    @JsonIgnore
    private String missingInformationEnums;

    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "是否超额")
    private OverPlan overPlan;

    @JsonIgnore
    @ApiModelProperty(value = "生产图片字符串")
    private String fileCodeStr;

    @ApiModelProperty(value = "生产图片")
    private List<String> fileCodeList;

    @ApiModelProperty(value = "原料信息")
    private List<MaterialNumInformationVo> materialNumInformationVos;

    @ApiModelProperty(value = "加工进度")
    private List<ProcessProgressVo> processProgressVos;

    @ApiModelProperty(value = "排产时间")
    private LocalDateTime processPlanTime;
}
