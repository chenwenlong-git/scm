package com.hete.supply.scm.server.scm.process.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/11/1 11:08
 */
@Data
@NoArgsConstructor
public class ProduceDataSpecVo {

    @ApiModelProperty(value = "id")
    private Long produceDataSpecId;

    @ApiModelProperty(value = "version")
    private Integer version;


    @ApiModelProperty(value = "sku")
    private String sku;


    @ApiModelProperty(value = "产品规格书链接")
    private String productLink;


    @ApiModelProperty(value = "产品规格书图片")
    private List<String> productFileCode;


    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "关联供应商")
    private List<ProduceDataSpecSupplierVo> produceDataSpecSupplierList;


}
