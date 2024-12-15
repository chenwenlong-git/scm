package com.hete.supply.scm.server.scm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2023/8/22 14:45
 */
@Data
@NoArgsConstructor
public class ProduceDataItemBo {

    @ApiModelProperty(value = "id")
    private Long produceDataItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "来源样品单号")
    private String businessNo;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "生产信息详情原料BOM列表")
    private List<ProduceDataItemRawListBo> produceDataItemRawBoList;

    @ApiModelProperty(value = "生产信息详情工序列表")
    private List<ProduceDataItemProcessListBo> produceDataItemProcessBoList;

    @ApiModelProperty(value = "生产信息详情工序描述列表")
    private List<ProduceDataItemProcessDescListBo> produceDataItemProcessDescBoList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
