package com.hete.supply.scm.server.scm.supplier.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.enums.SupplierWarehouse;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierWarehouseDao;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWareSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWarehouseBindDto;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierWarehouseEditDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierWarehousePo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierWareSearchItemVo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierWareSearchVo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2024/1/13 13:06
 */
@Service
@RequiredArgsConstructor
@Validated
public class SupplierWarehouseBizService {
    private final SupplierWarehouseDao supplierWarehouseDao;


    public CommonPageResult.PageInfo<SupplierWareSearchVo> searchSupplierWarehouse(SupplierWareSearchDto dto) {
        final CommonPageResult.PageInfo<SupplierWareSearchVo> supplierPageInfo = supplierWarehouseDao.searchSupplierWarehousePage(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<SupplierWareSearchVo> records = supplierPageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<String> supplierCodeList = records.stream()
                .map(SupplierWareSearchVo::getSupplierCode)
                .collect(Collectors.toList());
        final List<SupplierWarehousePo> supplierWarehousePoList = supplierWarehouseDao.getListBySupplierCodeList(supplierCodeList);
        final Map<String, List<SupplierWarehousePo>> supplierCodePoListMap = supplierWarehousePoList.stream()
                .collect(Collectors.groupingBy(SupplierWarehousePo::getSupplierCode));

        records.forEach(record -> {
            final List<SupplierWarehousePo> supplierWarehousePoList1 = supplierCodePoListMap.get(record.getSupplierCode());
            if (CollectionUtils.isEmpty(supplierWarehousePoList1)) {
                throw new BizException("供应商数据异常，请联系系统管理员");
            }
            final List<SupplierWareSearchItemVo> supplierWareSearchItemList = supplierWarehousePoList1.stream().map(po -> {
                final SupplierWareSearchItemVo supplierWareSearchItemVo = new SupplierWareSearchItemVo();
                supplierWareSearchItemVo.setSupplierWarehouseId(po.getSupplierWarehouseId());
                supplierWareSearchItemVo.setVersion(po.getVersion());
                supplierWareSearchItemVo.setWarehouseCode(po.getWarehouseCode());
                supplierWareSearchItemVo.setWarehouseName(po.getWarehouseName());
                supplierWareSearchItemVo.setSupplierWarehouse(po.getSupplierWarehouse());
                return supplierWareSearchItemVo;
            }).collect(Collectors.toList());
            record.setSupplierWareSearchItemList(supplierWareSearchItemList);
        });

        return supplierPageInfo;

    }

    @Transactional(rollbackFor = Exception.class)
    public void editSupplierWarehouse(SupplierWarehouseEditDto dto) {
        final SupplierWarehousePo supplierWarehousePo = supplierWarehouseDao.getByIdVersion(dto.getSupplierWarehouseId(), dto.getVersion());
        if (null == supplierWarehousePo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试");
        }
        final SupplierWarehousePo updatePo = new SupplierWarehousePo();
        updatePo.setSupplierWarehouseId(dto.getSupplierWarehouseId());
        updatePo.setVersion(dto.getVersion());
        updatePo.setWarehouseName(dto.getWarehouseName());
        supplierWarehouseDao.updateByIdVersion(updatePo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindSupplierWarehouse(SupplierWarehouseBindDto dto) {
        final List<SupplierWarehousePo> supplierWarehousePoList = supplierWarehouseDao.getListByWarehouseCode(dto.getWarehouseCode());
        if (CollectionUtils.isNotEmpty(supplierWarehousePoList)) {
            throw new ParamIllegalException("虚拟仓:{}，已经绑定了供应商，请勿重复绑定！", dto.getWarehouseCode());
        }
        final SupplierWarehousePo supplierWarehousePo = supplierWarehouseDao.getOneBySupplierCodeAndSupplierWarehouse(dto.getSupplierCode(),
                SupplierWarehouse.VIRTUAL_WAREHOUSE);
        if (null != supplierWarehousePo) {
            throw new ParamIllegalException("供应商:{}已经绑定虚拟仓:{}，请勿重复绑定！", supplierWarehousePo.getSupplierCode(),
                    supplierWarehousePo.getWarehouseCode());
        }

        final SupplierWarehousePo newSupplierWarehousePo = new SupplierWarehousePo();
        newSupplierWarehousePo.setSupplierCode(dto.getSupplierCode());
        newSupplierWarehousePo.setSupplierName(dto.getSupplierName());
        newSupplierWarehousePo.setWarehouseCode(dto.getWarehouseCode());
        newSupplierWarehousePo.setWarehouseName(dto.getWarehouseName());
        newSupplierWarehousePo.setSupplierWarehouse(SupplierWarehouse.VIRTUAL_WAREHOUSE);

        supplierWarehouseDao.insert(newSupplierWarehousePo);
    }
}
