package com.hete.supply.scm.server.scm.supplier.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/8/7.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupOpCapacityBatchBo {
    private List<SupOpCapacityBo> supOpCapacityBos;
}
