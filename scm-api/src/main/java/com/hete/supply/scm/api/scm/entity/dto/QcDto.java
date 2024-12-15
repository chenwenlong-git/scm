package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.QcState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/12/12 15:12
 */
@Data
@NoArgsConstructor
public class QcDto {
    private List<String> skuCodeList;

    private List<QcState> qcStateList;

    private List<String> warehouseCodeList;
}
