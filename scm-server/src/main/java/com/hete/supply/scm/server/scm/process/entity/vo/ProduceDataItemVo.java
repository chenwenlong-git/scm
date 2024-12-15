package com.hete.supply.scm.server.scm.process.entity.vo;

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
public class ProduceDataItemVo {

    @ApiModelProperty(value = "id")
    private Long produceDataItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "来源单据号")
    private String businessNo;

    @ApiModelProperty(value = "生产信息详情原料BOM列表")
    private List<ProduceDataItemRawListVo> produceDataItemRawList;

    @ApiModelProperty(value = "生产信息详情工序列表")
    private List<ProduceDataItemProcessListVo> produceDataItemProcessList;

    @ApiModelProperty(value = "生产信息详情工序描述列表")
    private List<ProduceDataItemProcessDescListVo> produceDataItemProcessDescList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "BOM名称")
    private String bomName;

    @ApiModelProperty(value = "生产信息详情关联供应商")
    private List<ProduceDataItemSupplierVo> produceDataItemSupplierList;

}
