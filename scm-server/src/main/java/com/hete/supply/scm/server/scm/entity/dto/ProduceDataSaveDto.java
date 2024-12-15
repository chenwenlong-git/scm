package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.enums.ProduceDataCreateType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 15:10
 */
@Data
@NoArgsConstructor
public class ProduceDataSaveDto extends ProduceDataSaveTopDto {

    @ApiModelProperty(value = "生产信息详情列表")
    private List<@Valid ProduceDataItemDto> produceDataItemList;


    @ApiModelProperty(value = "生产属性")
    private List<ProduceDataAttrDto> produceDataAttrList;

    @ApiModelProperty(value = "创建类型")
    private ProduceDataCreateType produceDataCreateType;

    @ApiModelProperty(value = "封样图")
    private List<String> sealImageFileCodeList;

    @ApiModelProperty(value = "规格书信息详情列表")
    private List<ProduceDataSpecDto> produceDataSpecList;


}
