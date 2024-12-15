package com.hete.supply.scm.server.scm.feishu.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lihaifei
 * 插入数据
 */
@Data
public class AppendDto {

    @NotNull
    @ApiModelProperty("文件名称")
    private ValueRange valueRange;

    @Data
    public static class ValueRange {

        @NotBlank
        @ApiModelProperty("插入范围，<sheetId>!<开始列>:<结束列>，如：0b**12!A:B")
        private String range;

        @NotEmpty
        @ApiModelProperty("指定范围内的插入内容")
        private List<Object[]> values;
    }

    /*@Data
    public static class RowValue {
        @NotEmpty
        @ApiModelProperty("一行的内容")
        private Object[] value;
    }*/
}
