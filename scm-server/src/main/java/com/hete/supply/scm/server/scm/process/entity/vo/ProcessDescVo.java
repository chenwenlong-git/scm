package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.api.scm.entity.enums.ProcessFirst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RockyHuas
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class ProcessDescVo {
    @ApiModelProperty(value = "加工描述 ID")
    private Long processDescId;

    @ApiModelProperty(value = "版本")
    private int version;

    @ApiModelProperty(value = "工序环节")
    private ProcessFirst processFirst;

    @ApiModelProperty(value = "加工描述")
    private String name;

    @ApiModelProperty(value = "描述值")
    private String descValues;

    @ApiModelProperty(value = "描述值(list)")
    private List<String> descValuesAsList;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
