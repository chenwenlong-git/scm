package com.hete.supply.scm.server.scm.supplier.service.base;

import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierWarehouseDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weiwenxin
 * @date 2024/1/20 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class SupplierWarehouseBaseService {
    private final SupplierWarehouseDao supplierWarehouseDao;


    public List<SupplierWarehousePo> getWarehouseBySupplierCode(List<String> supplierCodeList, SupplierWarehouse supplierWarehouse) {
        return supplierWarehouseDao.getListBySupplierCodeListAndType(supplierCodeList, supplierWarehouse);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createWarehouse(String supplierCode, String supplierName) {
        final List<SupplierWarehousePo> supplierWarehousePoList = new ArrayList<>();

        // 备货仓
        final SupplierWarehousePo supplierWarehousePo1 = new SupplierWarehousePo();
        supplierWarehousePo1.setSupplierCode(supplierCode);
        supplierWarehousePo1.setSupplierName(supplierName);
        supplierWarehousePo1.setWarehouseCode(ScmConstant.SUPPLIER_WAREHOUSE_PREFIX + supplierCode
                + ScmConstant.STOCK_UP_WAREHOUSE_SUFFIX);
        supplierWarehousePo1.setWarehouseName(supplierCode + ScmConstant.STOCK_UP_WAREHOUSE_SUFFIX_NAME);
        supplierWarehousePo1.setSupplierWarehouse(SupplierWarehouse.STOCK_UP);

        supplierWarehousePoList.add(supplierWarehousePo1);


        // 自备仓
        final SupplierWarehousePo supplierWarehousePo2 = new SupplierWarehousePo();
        supplierWarehousePo2.setSupplierCode(supplierCode);
        supplierWarehousePo2.setSupplierName(supplierName);
        supplierWarehousePo2.setWarehouseCode(ScmConstant.SUPPLIER_WAREHOUSE_PREFIX + supplierCode
                + ScmConstant.SELF_PROVIDE_WAREHOUSE_SUFFIX);
        supplierWarehousePo2.setWarehouseName(supplierCode + ScmConstant.SELF_PROVIDE_WAREHOUSE_SUFFIX_NAME);
        supplierWarehousePo2.setSupplierWarehouse(SupplierWarehouse.SELF_PROVIDE);
        supplierWarehousePoList.add(supplierWarehousePo2);

        // 不良仓
        final SupplierWarehousePo supplierWarehousePo3 = new SupplierWarehousePo();
        supplierWarehousePo3.setSupplierCode(supplierCode);
        supplierWarehousePo3.setSupplierName(supplierName);
        supplierWarehousePo3.setWarehouseCode(ScmConstant.SUPPLIER_WAREHOUSE_PREFIX + supplierCode
                + ScmConstant.DEFECTIVE_WAREHOUSE_SUFFIX);
        supplierWarehousePo3.setWarehouseName(supplierCode + ScmConstant.DEFECTIVE_WAREHOUSE_SUFFIX_NAME);
        supplierWarehousePo3.setSupplierWarehouse(SupplierWarehouse.DEFECTIVE_WAREHOUSE);
        supplierWarehousePoList.add(supplierWarehousePo3);

        supplierWarehouseDao.insertBatch(supplierWarehousePoList);

    }
}
