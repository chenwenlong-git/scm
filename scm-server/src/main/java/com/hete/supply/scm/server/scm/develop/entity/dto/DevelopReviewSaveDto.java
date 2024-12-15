package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2023/8/7 18:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopReviewSaveDto extends DevelopReviewNoDto {
    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    @ApiModelProperty(value = "审版样品")
    @Valid
    private List<DevelopReviewSampleDto> developReviewSampleList;

}
