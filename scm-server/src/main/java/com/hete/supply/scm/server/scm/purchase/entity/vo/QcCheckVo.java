package com.hete.supply.scm.server.scm.purchase.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/4/29.
 */
@Data
public class QcCheckVo {
    @ApiModelProperty(value = "采购子单单号")
    private String purchaseChildOrderNo;

    @ApiModelProperty(value = "质检来源(质检类型)")
    private QcOrigin qcOrigin;

    @ApiModelProperty(value = "质检来源属性(质检标识)")
    private QcOriginProperty qcOriginProperty;
}
