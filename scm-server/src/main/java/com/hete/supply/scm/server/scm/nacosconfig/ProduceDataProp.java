package com.hete.supply.scm.server.scm.nacosconfig;

import com.hete.supply.scm.server.scm.entity.bo.AttrVariantCompareBo;
import com.hete.supply.scm.server.scm.production.entity.bo.ColorAttrRuleBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/6 15:59
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.produce-data-attr")
@Data
public class ProduceDataProp {
    @ApiModelProperty(value = "蕾丝面积id")
    private Long laceAreaAttributeNameId;

    @ApiModelProperty(value = "完成长尺寸id")
    private Long completeLongSizeNameId;

    @ApiModelProperty(value = "材料id")
    private Long materialSizeNameId;

    @ApiModelProperty(value = "档长尺寸id")
    private Long crotchLengthAttributeNameId;

    @ApiModelProperty(value = "颜色属性id")
    private Long colorAttributeNameId;

    @ApiModelProperty(value = "颜色色系id")
    private Long colorSystemAttributeNameId;

    @ApiModelProperty(value = "颜色色系名称")
    private String colorSystemAttributeNameName;

    @ApiModelProperty(value = "复制属性的禁止复制的属性id")
    private List<Long> whitelistNameIds;

    @ApiModelProperty(value = "颜色色系对照规则")
    private List<ColorAttrRuleBo> colorAttrRuleList;

    @ApiModelProperty(value = "属性变体对照关系")
    private List<AttrVariantCompareBo> attrVariantCompareList;
}
