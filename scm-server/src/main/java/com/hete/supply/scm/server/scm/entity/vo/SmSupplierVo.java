package com.hete.supply.scm.server.scm.entity.vo;

import com.hete.supply.scm.api.scm.entity.vo.SupplierSimpleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author weiwenxin
 * @date 2022/11/19 11:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SmSupplierVo extends SupplierSimpleVo {
    @ApiModelProperty(value = "id")
    private Long smSupplierId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    @ApiModelProperty(value = "产能")
    private Integer capacity;

    @ApiModelProperty(value = "入驻时间")
    private LocalDateTime joinTime;
}
