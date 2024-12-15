package com.hete.supply.scm.server.scm.nacosconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author weiwenxin
 * @date 2024/9/12 09:50
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "scm.category")
@Data
public class SkuCategoryProp {
    @ApiModelProperty(value = "工具配饰id")
    private Long toolAccessoryId;

    @ApiModelProperty(value = "假发id")
    private Long wigId;

    @ApiModelProperty(value = "化纤假发id")
    private Long syntheticHairId;

    @ApiModelProperty(value = "假发原料id")
    private Long wigRawId;
}
