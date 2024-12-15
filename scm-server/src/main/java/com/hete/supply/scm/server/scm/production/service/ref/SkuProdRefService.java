package com.hete.supply.scm.server.scm.production.service.ref;

import com.hete.supply.scm.api.scm.entity.dto.PlmSkuSearchDto;
import com.hete.supply.scm.server.scm.production.service.base.SkuProdBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ChenWenLong
 * @date 2024/10/10 17:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SkuProdRefService {

    private final SkuProdBaseService skuProdBaseService;

    /**
     * 提供旧生产资料查询条件，用于导出时条件能匹配
     *
     * @param dto:
     * @return PlmSkuSearchDto
     * @author ChenWenLong
     * @date 2024/10/10 17:55
     */
    public PlmSkuSearchDto getSearchPlmSkuWhere(PlmSkuSearchDto dto) {
        return skuProdBaseService.getSearchPlmSkuWhere(dto);
    }
}
