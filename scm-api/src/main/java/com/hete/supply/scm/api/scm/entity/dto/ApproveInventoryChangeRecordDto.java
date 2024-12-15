package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.InventoryApproveResult;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2024/4/8.
 */
@Data
public class ApproveInventoryChangeRecordDto {
    @NotEmpty(message = "库存记录ID列表不能为空")
    private List<Long> supplierInventoryRecordIds;

    @NotNull(message = "库存变更结果不能为空")
    private InventoryApproveResult changeResult;
}
