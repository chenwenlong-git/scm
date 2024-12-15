package com.hete.supply.scm.api.scm.entity.dto;

import lombok.Data;

/**
 * @author yanjiawei
 * Created on 2024/4/23.
 */
@Data
public class SkuExistenceResponseDto {
    private String sku;
    private boolean exists;
}
