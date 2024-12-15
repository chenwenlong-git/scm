package com.hete.supply.scm.server.scm.qc.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.rocketmq.entity.BaseMqMessageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "收货单质检")
public class ReceiveOrderQcOrderDto extends BaseMqMessageDto {
    @ApiModelProperty(value = "收货单号")
    @NotBlank(message = "收货单号不能为空")
    private String receiveOrderNo;

    @ApiModelProperty(value = "质检类型：全检、抽检、免检")
    @NotNull(message = "质检类型不能为空")
    private QcType qcType;

    @ApiModelProperty(value = "质检结果:PASSED(合格),FEW_NOT_PASSED(部分不合格),NOT_PASSED(不合格)")
    private QcResult qcResult;

    @ApiModelProperty(value = "收货类型")
    @NotNull(message = "收获类型不能为空")
    private WmsEnum.ReceiveType receiveType;

    @NotNull(message = "仓库编码不能为空")
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "出库单号")
    private String deliveryOrderNo;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "商品/辅料类目")
    private String goodsCategory;

    @ApiModelProperty(value = "创建人编号")
    private String createUser;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNo;

    @ApiModelProperty(value = "总收货数量")
    private Integer totalReceiveAmount;

    @ApiModelProperty(value = "供应链单据号")
    private String scmBizNo;

    @ApiModelProperty(value = "收货单质检订单明细列表")
    @NotEmpty(message = "收货单质检订单明细列表不能为空")
    private List<@Valid ReceiveOrderQcOrderDetailDto> receiveDetailList;
}
