package com.hete.supply.scm.server.scm.defect.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author weiwenxin
 * @date 2023/6/26 20:48
 */
@Data
@NoArgsConstructor
public class DefectReturnSupplyDto {


    private BigDecimal deductPrice;

    private String defectHandlingNo;
}
