package com.hete.supply.scm.server.scm.purchase.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.PurchaseOrderStatus;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseGetSuggestSupplierBo;
import com.hete.supply.scm.server.scm.purchase.entity.bo.PurchaseItemByReceiveTimeBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 采购需求子单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-01
 */
@Mapper
interface PurchaseChildOrderItemMapper extends BaseDataMapper<PurchaseChildOrderItemPo> {

    IPage<PurchaseChildOrderItemPo> getPage(Page<PurchaseChildOrderItemPo> page);

    List<PurchaseItemByReceiveTimeBo> getListByPurchaseItemAndReceiveTime(@NotEmpty @Param("skuList") List<String> skuList,
                                                                          @Param("purchaseOrderStatusExcludeList") List<PurchaseOrderStatus> purchaseOrderStatusExcludeList);

    List<PurchaseGetSuggestSupplierBo> getListBySkuListAndNotStatus(@NotEmpty @Param("skuList") List<String> skuList,
                                                                    @Param("notPurchaseOrderStatus") PurchaseOrderStatus notPurchaseOrderStatus,
                                                                    @Param("planConfirmTimeStart") LocalDateTime planConfirmTimeStart,
                                                                    @Param("planConfirmTimeEnd") LocalDateTime planConfirmTimeEnd);

}
