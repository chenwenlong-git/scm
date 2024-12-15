package com.hete.supply.scm.server.scm.api.provider;

import com.hete.supply.scm.api.scm.importation.entity.dto.SkuAttrImportationDto;
import com.hete.supply.scm.api.scm.importation.facade.SkuAttrImportationFacade;
import com.hete.supply.scm.server.scm.service.biz.SkuAttrPriceBizService;
import com.hete.support.api.entity.dto.ImportationReqDto;
import com.hete.support.api.entity.vo.ImportationResultVo;
import com.hete.support.api.result.CommonResult;
import com.hete.support.core.importation.util.ImportationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author weiwenxin
 * @date 2024/11/1 17:53
 */
@DubboService
@RequiredArgsConstructor
public class SkuAttrImportationFacadeImpl implements SkuAttrImportationFacade {
    private final SkuAttrPriceBizService skuAttrPriceBizService;

    @Override
    public CommonResult<ImportationResultVo> importSkuAttr(ImportationReqDto<SkuAttrImportationDto> dto) {

        return ImportationUtil.importData(dto, skuAttrPriceBizService::importSkuAttr);
    }
}
