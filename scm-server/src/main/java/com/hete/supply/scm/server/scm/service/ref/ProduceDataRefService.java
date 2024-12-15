package com.hete.supply.scm.server.scm.service.ref;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.dto.ProduceDataSearchDto;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/18 11:41
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ProduceDataRefService {

    private final ProduceDataBaseService produceDataBaseService;
    private final PlmSkuDao plmSkuDao;

    /**
     * 创建plm_sku时更新生产属性信息
     *
     * @param plmSkuPoList:
     * @author ChenWenLong
     * @date 2024/9/18 11:28
     */
    public void createPlmSkuUpdateProduceDataAttr(List<PlmSkuPo> plmSkuPoList) {
        if (CollectionUtils.isEmpty(plmSkuPoList)) {
            return;
        }
        // 需要进行处理sku生产属性
        for (PlmSkuPo plmSkuPo : plmSkuPoList) {
            produceDataBaseService.updateSkuProduceDataAttrBySku(plmSkuPo);
        }
    }

    /**
     * 原料工序导出总数
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/1/2 11:52
     */
    public Integer getSkuProcessExportTotals(ProduceDataSearchDto dto) {
        //条件过滤
        if (null == produceDataBaseService.getSearchProduceDataWhere(dto)) {
            return 0;
        }
        return plmSkuDao.getExportSkuProcessTotals(dto);
    }
}
