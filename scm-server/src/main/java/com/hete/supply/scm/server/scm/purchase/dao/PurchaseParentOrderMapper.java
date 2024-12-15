package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSearchNewDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseParentExportVo;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseParentSkuExportVo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseSearchBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseSearchNewVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 采购需求母单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Mapper
interface PurchaseParentOrderMapper extends BaseDataMapper<PurchaseParentOrderPo> {

    IPage<PurchaseSearchNewVo> searchPurchaseNew(Page<Void> page, @Param("dto") PurchaseSearchNewDto dto,
                                                 @Param("purchaseSearchBo") PurchaseSearchBo purchaseSearchBo);

    Integer exportPurchaseParentTotals(@Param("dto") PurchaseSearchNewDto dto,
                                       @Param("purchaseSearchBo") PurchaseSearchBo purchaseSearchBo);

    IPage<PurchaseParentExportVo> exportPurchaseParent(Page<Void> page, @Param("dto") PurchaseSearchNewDto dto,
                                                       @Param("purchaseSearchBo") PurchaseSearchBo purchaseSearchBo);

    Integer exportPurchaseParentBySkuTotals(@Param("dto") PurchaseSearchNewDto dto,
                                            @Param("purchaseSearchBo") PurchaseSearchBo purchaseSearchBo);

    IPage<PurchaseParentSkuExportVo> exportPurchaseParentBySku(Page<Void> page, @Param("dto") PurchaseSearchNewDto dto,
                                                               @Param("purchaseSearchBo") PurchaseSearchBo purchaseSearchBo);

    List<PurchaseParentOrderPo> getCanCompletePurchase();
}
