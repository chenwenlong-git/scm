package com.hete.supply.scm.server.scm.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/5/24 14:34
 */
@Data
@NoArgsConstructor
public class WmsDetailVo {
    @ApiModelProperty(value = "收货信息")
    private List<WmsReceiveDetailVo> wmsReceiveDetailList;

    @ApiModelProperty(value = "质检信息")
    private List<WmsQcDetailVo> wmsQcDetailList;

    @ApiModelProperty(value = "上架信息")
    private List<WmsOnShelfDetailVo> wmsOnShelfDetailList;
}
