package com.hete.supply.scm.server.scm.feishu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lihaifei
 * 插入数据
 */
@Data
@NoArgsConstructor
public class PrependVo {

    @ApiModelProperty("spreadsheet 的 token")
    private String spreadsheetToken;

    @ApiModelProperty("写入的范围")
    private String tableRange;

    @ApiModelProperty("sheet 的版本号")
    private int revision;

    @ApiModelProperty("插入数据的范围、行列数等")
    private Updates spreadsheet;

    @Data
    public static class Updates {

        @ApiModelProperty("spreadsheet 的 token")
        private String spreadsheetToken;

        @ApiModelProperty("电子表格的token")
        private String updatedRange;

        @ApiModelProperty("写入的行数")
        private int updatedRows;

        @ApiModelProperty("写入的列数")
        private int updatedColumns;

        @ApiModelProperty("写入的单元格总数")
        private int updatedCells;

        @ApiModelProperty("sheet 的版本号")
        private int revision;
    }
}
