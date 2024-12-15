package com.hete.supply.scm.server.scm.sample.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author weiwenxin
 * @date 2022/11/2
 */
@Data
@NoArgsConstructor
public class SampleBatchIdDto {
    @NotEmpty(message = "id列表不能为空")
    private Set<Long> sampleChildIdList;
}
