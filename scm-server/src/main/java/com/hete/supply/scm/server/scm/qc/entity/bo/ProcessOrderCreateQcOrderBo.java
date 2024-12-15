package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.api.scm.entity.enums.QcType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/11.
 */
@Data
@ApiModel(value = "质检主单信息", description = "质检主单信息")
public class ProcessOrderCreateQcOrderBo {


    @ApiModelProperty(name = "仓库编码")
    private String warehouseCode;


    @ApiModelProperty(name = "加工单号")
    private String processOrderNo;

    @ApiModelProperty(name = "加工单号")
    private ProcessOrderType processOrderType;


    @ApiModelProperty(name = "质检类型")
    private QcType qcType;


    @ApiModelProperty(name = "操作人编码")
    private String operator;


    @ApiModelProperty(name = "操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(name = "商品明细列表")
    private List<ProcessOrderCreateQcOrderDetailBo> qcOrderDetails;
}
