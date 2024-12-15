package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.wms.api.WmsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2023/5/24 14:34
 */
@Data
@NoArgsConstructor
public class WmsOnShelfDetailVo {
    @ApiModelProperty(value = "上架单号")
    private String onShelvesOrderNo;

    @ApiModelProperty(value = "上架单状态")
    private WmsEnum.OnShelfState onShelfState;

    @ApiModelProperty(value = "上架单创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "计划上架数量")
    private Integer onShelvesAmount;
}
