package com.hete.supply.scm.server.scm.entity.dto;

import com.hete.supply.scm.server.scm.enums.ProduceDataCreateType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/7/3 09:41
 */
@Data
@NoArgsConstructor
public class ProduceDataSaveBodyDto {

    @NotBlank(message = "sku不能为空")
    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "版本")
    private int version;

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
