package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.QcResult;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@ApiModel(description = "收货单质检")
@Data
public class ReceiveOrderQcOrderBo {
    @ApiModelProperty(value = "收货单号", required = true)
    private String receiveOrderNo;

    @ApiModelProperty(value = "质检类型：全检、抽检、免检", required = true)
    private QcType qcType;

    @ApiModelProperty(value = "质检结果:PASSED(合格),FEW_NOT_PASSED(部分不合格),NOT_PASSED(不合格)")
    private QcResult qcResult;

    @ApiModelProperty(value = "质检数量（实际收货数量）", required = true)
    private int qcAmount;

    @ApiModelProperty(value = "收货类型", required = true)
    private WmsEnum.ReceiveType receiveType;

    @ApiModelProperty(value = "仓库编码", required = true)
    private String warehouseCode;

    @ApiModelProperty(value = "出库单号", required = true)
    private String deliveryOrderNo;

    @ApiModelProperty(value = "发货单号")
    private String deliverOrderNo;

    @ApiModelProperty(value = "供应商编码", required = true)
    private String supplierCode;

    @ApiModelProperty(value = "商品/辅料类目", required = true)
    private String goodsCategory;

    @ApiModelProperty(value = "创建人编号")
    private String createUser;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNo;

    @ApiModelProperty(value = "供应链单据号")
    private String scmBizNo;

    @ApiModelProperty(value = "收货单质检订单明细列表")
    private List<ReceiveOrderQcOrderDetailBo> qcOrderDetails;
}
