package com.hete.supply.scm.server.scm.qc.entity.vo;

import com.hete.supply.scm.server.scm.process.entity.vo.ProduceDataSpecVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/11/6 10:21
 */
@Data
@NoArgsConstructor
public class QcDetailProduceDataVo {
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "销售图")
    private List<String> saleFileCodeList;

    @ApiModelProperty(value = "封样图片")
    private List<String> sealImageFileCodeList;

    @ApiModelProperty(value = "规格书信息详情列表")
    private List<ProduceDataSpecVo> produceDataSpecList;
}
