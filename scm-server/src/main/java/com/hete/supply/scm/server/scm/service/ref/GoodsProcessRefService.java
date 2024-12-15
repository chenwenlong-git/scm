package com.hete.supply.scm.server.scm.service.ref;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.server.scm.entity.po.GoodsProcessPo;
import com.hete.supply.scm.server.scm.process.dao.GoodsProcessDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * @date 2023年06月27日 11:58
 */
@Service
@RequiredArgsConstructor
@Validated
public class GoodsProcessRefService {
    private final GoodsProcessDao goodsProcessDao;

    /**
     * 返回加工工序存在的sku
     *
     * @param skuList
     * @return
     */
    public Set<String> filterExistSkus(List<String> skuList) {
        if (CollectionUtils.isEmpty(skuList)) {
            return Collections.emptySet();
        }
        List<GoodsProcessPo> goodsProcessPoList = goodsProcessDao.getBySkuList(skuList);
        if (CollectionUtils.isEmpty(goodsProcessPoList)) {
            return Collections.emptySet();
        }
        Set<String> existSkuList = goodsProcessPoList.stream().map(GoodsProcessPo::getSku).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(existSkuList)) {
            return Collections.emptySet();
        }
        return skuList.stream().filter(existSkuList::contains).collect(Collectors.toSet());
    }
}
