package com.hete.supply.scm.server.scm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/11/1 16:18
 */
@Data
@NoArgsConstructor
public class ProduceDataSpecDto {

    @ApiModelProperty(value = "id")
    private Long produceDataSpecId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "规格书链接")
    private String productLink;

    @ApiModelProperty(value = "产品规格书图片")
    private List<String> productFileCode;

    @ApiModelProperty(value = "关联供应商列表")
    private List<ProduceDataSpecSupplierDto> produceDataSpecSupplierList;

}
