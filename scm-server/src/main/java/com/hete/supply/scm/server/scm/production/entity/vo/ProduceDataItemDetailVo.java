package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.support.api.enums.BooleanType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/24 10:42
 */
@Data
public class ProduceDataItemDetailVo {

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "生产资料的版本号")
    private Integer version;

    @ApiModelProperty(value = "是否需管理")
    private BooleanType rawManage;

    @ApiModelProperty(value = "原料&工序详情列表")
    private List<ProduceDataItemInfoVo> produceDataItemInfoList;


}
