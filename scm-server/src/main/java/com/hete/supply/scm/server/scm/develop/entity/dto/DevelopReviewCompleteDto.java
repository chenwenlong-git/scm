package com.hete.supply.scm.server.scm.develop.entity.dto;

import com.hete.supply.scm.api.scm.entity.enums.ReviewResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/3/22 15:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DevelopReviewCompleteDto extends DevelopReviewNoAndVersionDto {

    @ApiModelProperty(value = "审版结果")
    private ReviewResult reviewResult;

    @NotEmpty(message = "审版样品列表不能为空")
    @ApiModelProperty(value = "审版样品")
    @Valid
    private List<DevelopReviewSampleDto> developReviewSampleList;
}
