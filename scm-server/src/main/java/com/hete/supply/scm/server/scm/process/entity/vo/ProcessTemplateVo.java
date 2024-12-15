package com.hete.supply.scm.server.scm.process.entity.vo;

import com.hete.supply.scm.server.scm.process.enums.ProcessTemplateType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author RockyHuas
 * @date 2022/11/1 14:04
 */
@Data
@NoArgsConstructor
public class ProcessTemplateVo {

    @ApiModelProperty(value = "工序模版 ID")
    private Long processTemplateId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型，CATEGORY：品类，SKU：商品sku")
    private ProcessTemplateType processTemplateType;

    @ApiModelProperty(value = "产品品类/SKU")
    private String typeValueName;

    @ApiModelProperty(value = "创建人")
    private String createUsername;

    @ApiModelProperty(value = "创建时间", notes = "排序字段")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "详情最后更新时间", notes = "排序字段")
    private LocalDateTime detailsLastUpdatedTime;

}
