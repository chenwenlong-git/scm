package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/20 18:12
 */
@Data
@NoArgsConstructor
public class ScmImageBo {
    @NotNull(message = "imageBizId不能为空")
    @ApiModelProperty(value = "imageBizId")
    private Long imageBizId;


    @NotEmpty(message = "fileCodeList不能为空")
    @ApiModelProperty(value = "fileCodeList")
    private List<String> fileCodeList;


}
