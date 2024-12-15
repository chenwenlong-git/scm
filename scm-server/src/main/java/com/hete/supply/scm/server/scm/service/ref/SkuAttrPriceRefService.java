package com.hete.supply.scm.server.scm.service.ref;

import com.hete.supply.scm.server.scm.dao.SkuAttrPriceDao;
import com.hete.supply.scm.server.scm.entity.dto.SkuAttrItemDto;
import com.hete.supply.scm.server.scm.entity.po.SkuAttrPricePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/9/10 16:57
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SkuAttrPriceRefService {
    private final SkuAttrPriceDao skuAttrPriceDao;

    /**
     * 根据蕾丝面积和档长尺寸获取定价表价格
     *
     * @param skuAttrItemDtoList
     * @return
     */
    public List<SkuAttrPricePo> getListByAttr(List<SkuAttrItemDto> skuAttrItemDtoList) {
        return skuAttrPriceDao.getListBySkuAndAttr(skuAttrItemDtoList);
    }
}
