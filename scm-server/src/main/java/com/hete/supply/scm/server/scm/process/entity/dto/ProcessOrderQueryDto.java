package com.hete.supply.scm.server.scm.process.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: RockyHuas
 * @date: 2022/11/1 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "加工单查询参数", description = "加工单查询参数")
public class ProcessOrderQueryDto extends ComPageDto {

    @ApiModelProperty(value = "加工单号")
    private List<String> processOrderNoList;

    @ApiModelProperty(value = "spu")
    private String spu;

    @ApiModelProperty(value = "sku")
    private List<String> skus;

    @ApiModelProperty(value = "原料sku列表")
    private List<String> materialSkuList;

    @ApiModelProperty(value = "质检单号")
    private String checkOrderNo;

    @ApiModelProperty(value = "收货单号")
    private String receiptOrderNo;

    @ApiModelProperty(value = "入库单号")
    private String storeOrderNo;

    @ApiModelProperty(value = "结算单号")
    private String settleOrderNo;

    @ApiModelProperty(value = "平台")
    private List<String> platformList;

    @ApiModelProperty(value = "商品类目id")
    @Size(max = 10, message = "商品类目搜索不能超过10个")
    private List<Long> categoryIdList;

    @ApiModelProperty(value = "最小加工数量")
    private Integer minProcessNum;

    @ApiModelProperty(value = "最大加工数量")
    private Integer maxProcessNum;

    @ApiModelProperty(value = "下单人名称")
    private String createUser;

    @ApiModelProperty(value = "下单人名称列表")
    private List<String> createUsers;

    @ApiModelProperty(value = "下单人名称")
    private String createUsername;

    @ApiModelProperty(value = "下单时间-开始时间")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "下单时间-结束时间")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "投产时间-开始时间")
    private LocalDateTime producedTimeStart;

    @ApiModelProperty(value = "投产时间-结束时间")
    private LocalDateTime producedTimeEnd;

    @ApiModelProperty(value = "质检时间-开始时间")
    private LocalDateTime checkedTimeStart;

    @ApiModelProperty(value = "质检时间-结束时间")
    private LocalDateTime checkedTimeEnd;

    @ApiModelProperty(value = "收货时间-开始时间")
    private LocalDateTime receiptTimeStart;

    @ApiModelProperty(value = "收货时间-结束时间")
    private LocalDateTime receiptTimeEnd;

    @ApiModelProperty(value = "入库时间-开始时间")
    private LocalDateTime storedStart;

    @ApiModelProperty(value = "入库时间-结束时间")
    private LocalDateTime storedEnd;

    @ApiModelProperty(value = "前处理时间-开始时间")
    private LocalDateTime waitHandleTimeStart;

    @ApiModelProperty(value = "完成交接时间-开始时间")
    private LocalDateTime checkingTimeStart;

    @ApiModelProperty(value = "完成交接时间-结束时间")
    private LocalDateTime checkingTimeEnd;

    @ApiModelProperty(value = "业务约定日期-开始时间")
    private LocalDateTime deliverDateStart;

    @ApiModelProperty(value = "业务约定日期-结束时间")
    private LocalDateTime deliverDateEnd;

    @ApiModelProperty(value = "答交时间-开始时间")
    private LocalDateTime promiseDateStart;

    @ApiModelProperty(value = "答交时间-结束时间")
    private LocalDateTime promiseDateEnd;

    @ApiModelProperty(value = "答交日期是否延期")
    private PromiseDateDelayed promiseDateDelayed;

    @ApiModelProperty(value = "类型")
    private List<ProcessOrderType> processOrderTypes;

    @ApiModelProperty(value = "状态")
    private List<ProcessOrderStatus> processOrderStatuses;

    @ApiModelProperty(value = "sku 属性,支持多个检索")
    private List<String> skuAttributes;

    @ApiModelProperty(value = "产品名称")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUser;

    @ApiModelProperty(value = "收货人名称")
    private String receiptUsername;

    @ApiModelProperty(value = "入库人名称")
    private String storeUser;

    @ApiModelProperty(value = "入库人名称")
    private String storeUsername;

    @ApiModelProperty(value = "完成扫码人")
    private String completeScanUser;

    @ApiModelProperty(value = "完成扫码人")
    private String completeScanUsername;

    @ApiModelProperty(value = "发货时间-开始时间")
    private LocalDateTime deliverTimeStart;

    @ApiModelProperty(value = "发货时间-结束时间")
    private LocalDateTime deliverTimeEnd;

    @ApiModelProperty(value = "当前工序进度")
    private List<ProcessLabel> currentProcessLabels;

    @ApiModelProperty(value = "原料归还状态")
    private List<MaterialBackStatus> materialBackStatuses;

    @ApiModelProperty(value = "次品记录")
    private DefectiveRecordStatus defectiveRecordStatus;

    @ApiModelProperty(value = "缺失信息")
    private List<MissingInformation> missingInformationList;

    @ApiModelProperty(value = "是否回料")
    private IsReceiveMaterial isReceiveMaterial;

    @ApiModelProperty(value = "是否超额")
    private OverPlan overPlan;

}
