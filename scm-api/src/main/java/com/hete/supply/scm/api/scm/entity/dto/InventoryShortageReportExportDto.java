package com.hete.supply.scm.api.scm.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.support.api.entity.dto.ComPageDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/12/4.
 */
@Data
@ApiModel(value = "缺货报表导出查询参数", description = "缺货报表导出查询参数")
public class InventoryShortageReportExportDto extends ComPageDto {
    private List<ProcessOrderStatus> processOrderStatuses;
}
