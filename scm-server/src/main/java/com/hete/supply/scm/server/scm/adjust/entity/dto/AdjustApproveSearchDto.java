package com.hete.supply.scm.server.scm.adjust.entity.dto;

import com.hete.supply.scm.server.scm.adjust.enums.ApproveStatus;
import com.hete.supply.scm.server.scm.adjust.enums.ApproveType;
import com.hete.support.api.entity.dto.ComPageDto;
import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/6/13 14:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AdjustApproveSearchDto extends ComPageDto {
    @ApiModelProperty(value = "审批单号")
    private String adjustPriceApproveNo;

    @ApiModelProperty(value = "审批类型")
    private ApproveType approveType;

    @ApiModelProperty(value = "申请人")
    private String applyUser;

    @ApiModelProperty(value = "当前审批人")
    private String approveUser;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTimeStart;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTimeEnd;

    @ApiModelProperty(value = "sku")
    private List<String> skuList;

    @ApiModelProperty(value = "产品名称")
    private List<String> skuEncodeList;

    @ApiModelProperty(value = "采购订单号")
    private List<String> purchaseChildOrderNoList;

    @ApiModelProperty(value = "标记是否查询item表(前端忽略该参数)")
    private BooleanType isItemSearch;

    @ApiModelProperty(value = "当前审批人（数据权限）(前端忽略该参数)")
    private String ctrlUser;

    @ApiModelProperty(value = "审批状态")
    private List<ApproveStatus> approveStatusList;

    @ApiModelProperty(value = "数据权限（前端忽略该参数）")
    private String dataUser;
}
