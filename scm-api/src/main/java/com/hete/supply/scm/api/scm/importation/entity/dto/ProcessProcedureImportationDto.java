package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/2/5 18:24
 */
@Data
public class ProcessProcedureImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "关联的加工单")
        private String processOrderNo;

        @ApiModelProperty(value = "工序名称")
        private String processName;


        @ApiModelProperty(value = "人工提成")
        private BigDecimal commission;
    }
}
