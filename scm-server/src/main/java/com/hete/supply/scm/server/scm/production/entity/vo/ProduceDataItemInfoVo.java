package com.hete.supply.scm.server.scm.production.entity.vo;

import com.hete.supply.scm.server.scm.process.entity.vo.ProcessDescInfoVo;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessInfoVo;
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
public class ProduceDataItemInfoVo {

    @ApiModelProperty(value = "id")
    private Long produceDataItemId;

    @ApiModelProperty(value = "version")
    private Integer version;

    @ApiModelProperty(value = "来源单据号")
    private String businessNo;

    @ApiModelProperty(value = "生产信息详情原料BOM列表")
    private List<ProduceDataItemRawInfoVo> produceDataItemRawInfoList;

    @ApiModelProperty(value = "工序列表")
    private List<ProcessInfoVo> processInfoList;

    @ApiModelProperty(value = "工序描述列表")
    private List<ProcessDescInfoVo> processDescInfoList;

    @ApiModelProperty(value = "效果图")
    private List<String> effectFileCodeList;

    @ApiModelProperty(value = "细节图")
    private List<String> detailFileCodeList;

    @ApiModelProperty(value = "BOM名称")
    private String bomName;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人名称")
    private String createUsername;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新人名称")
    private String updateUsername;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "生产信息详情关联供应商")
    private List<ProduceDataItemSupplierInfoVo> produceDataItemSupplierInfoList;

}
