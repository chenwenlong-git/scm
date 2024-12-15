package com.hete.supply.scm.server.scm.cost.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.CostTimeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeType;
import com.hete.supply.scm.api.scm.entity.enums.PolymerizeWarehouse;
import com.hete.supply.scm.api.scm.importation.entity.dto.CostImportDto;
import com.hete.supply.scm.server.scm.cost.service.base.CostBaseService;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.cost.dao.CostOfGoodsDao;
import com.hete.supply.scm.server.scm.cost.entity.po.CostOfGoodsPo;
import com.hete.supply.scm.server.scm.dao.PlmSkuDao;
import com.hete.supply.scm.server.scm.entity.po.PlmSkuPo;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.support.api.exception.ParamIllegalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author weiwenxin
 * @date 2024/2/26 10:29
 */
@Service
@RequiredArgsConstructor
@Validated
public class CostOfGoodsImportService {
    private final CostBaseService costBaseService;

    @Transactional(rollbackFor = Exception.class)
    public void importChangeMoData(@NotNull @Valid CostImportDto dto) {
        costBaseService.saveChangeMoData(dto);
    }
}
