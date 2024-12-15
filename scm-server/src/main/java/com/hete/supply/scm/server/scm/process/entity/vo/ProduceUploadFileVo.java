package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/10/23 14:58
 */
@Data
@NoArgsConstructor
public class ProduceUploadFileVo {
    @ApiModelProperty(value = "id")
    private Long produceDataId;

    @ApiModelProperty(value = "产品规格书链接")
    private String productLink;

    @ApiModelProperty(value = "产品规格书图片")
    private List<String> productFileCode;
}
