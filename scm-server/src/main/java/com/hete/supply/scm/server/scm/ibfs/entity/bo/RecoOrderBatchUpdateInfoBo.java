package com.hete.supply.scm.server.scm.ibfs.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ChenWenLong
 * @date 2024/7/4 13:45
 */
@Data
@NoArgsConstructor
public class RecoOrderBatchUpdateInfoBo {

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;

    @ApiModelProperty(value = "删除")
    private Long delTimestamp;

}
