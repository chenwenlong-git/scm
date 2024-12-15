package com.hete.supply.scm.api.scm.importation.entity.dto;

import com.hete.support.api.entity.dto.BaseImportationRowDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author RockyHuas
 * @date 2023/2/7 18:24
 */
@Data
public class ProcessOrderDescImportationDto {
    /**
     * 导入数据
     */
    private List<ImportationDetail> detailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImportationDetail extends BaseImportationRowDto {
        @ApiModelProperty(value = "加工单号")
        private String processOrderNo;

        @ApiModelProperty(value = "描述名称")
        private String processDescName;


        @ApiModelProperty(value = "描述值")
        private String processDescValue;
    }
}
