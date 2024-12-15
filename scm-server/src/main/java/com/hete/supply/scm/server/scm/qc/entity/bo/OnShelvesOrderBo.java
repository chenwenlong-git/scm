package com.hete.supply.scm.server.scm.qc.entity.bo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2023/10/13.
 */
@Data
public class OnShelvesOrderBo {

    @ApiModelProperty(value = "质检单号")
    private String qcOrderNo;

    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNo;

    @ApiModelProperty(value = "计划上架数量")
    private Integer planAmount;

    @ApiModelProperty(value = "上架单生成类型：让步(CONCESSION)")
    private WmsEnum.OnShelvesOrderCreateType type;
}
