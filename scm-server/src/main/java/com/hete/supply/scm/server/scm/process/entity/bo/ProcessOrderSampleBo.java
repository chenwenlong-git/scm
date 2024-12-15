package com.hete.supply.scm.server.scm.process.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2023/10/16.
 */
@Data
public class ProcessOrderSampleBo {

    @ApiModelProperty(value = "样品采购子单号，现无法获取待确认")
    private String sampleChildOrderNo;

    @ApiModelProperty(value = "来源单据号（PLM生产属性主键）")
    private String sourceDocumentNumber;

    @ApiModelProperty(value = "属性key")
    private String sampleInfoKey;

    @ApiModelProperty(value = "属性值")
    private String sampleInfoValue;

}
