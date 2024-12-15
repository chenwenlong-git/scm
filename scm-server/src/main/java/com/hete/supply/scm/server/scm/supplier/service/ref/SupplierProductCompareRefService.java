package com.hete.supply.scm.server.scm.supplier.service.ref;

import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierBindingListQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierProductComparePo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierProductCompareBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2024/9/29 10:06
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierProductCompareRefService {

    private final SupplierProductCompareBaseService supplierProductCompareBaseService;

    /**
     * 通过sku和供应商列表增加对照关系
     *
     * @param sku:
     * @param supplierCodeList:
     * @author ChenWenLong
     * @date 2024/9/29 10:36
     */
    public void insertSupplierProductCompareBySku(@NotBlank String sku, List<String> supplierCodeList) {
        supplierProductCompareBaseService.insertSupplierProductCompareBySku(sku, supplierCodeList);
    }

    /**
     * 创建供应商产品对照以及创建供应商库存信息
     *
     * @param supplierCode:
     * @param skuList:
     * @author ChenWenLong
     * @date 2024/10/9 16:13
     */
    public void insertSupplierProductCompareAndInventory(@NotBlank String supplierCode, List<String> skuList) {
        supplierProductCompareBaseService.insertSupplierProductCompareAndInventory(supplierCode, skuList);
    }

    /**
     * 提供各个模块公用通过条件获取可用供应商产品对照列表
     *
     * @param supplierBindingListQueryBo:
     * @return List<SupplierProductComparePo>
     * @author ChenWenLong
     * @date 2024/10/10 16:26
     */
    public List<SupplierProductComparePo> getBindingSupplierList(@NotNull SupplierBindingListQueryBo supplierBindingListQueryBo) {
        return supplierProductCompareBaseService.getBindingSupplierList(supplierBindingListQueryBo);
    }

}
