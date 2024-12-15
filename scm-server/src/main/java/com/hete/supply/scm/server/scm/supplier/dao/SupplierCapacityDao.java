package com.hete.supply.scm.server.scm.supplier.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SupCapacityPageDto;
import com.hete.supply.scm.api.scm.entity.vo.SupplierCapacityExportVo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupCapDateRangeQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityQueryBo;
import com.hete.supply.scm.server.scm.supplier.entity.bo.SupplierCapacityResBo;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierCapacityPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierCapacityPageVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 供应商产能表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-08-05
 */
@Component
@Validated
public class SupplierCapacityDao extends BaseDao<SupplierCapacityMapper, SupplierCapacityPo> {


    /**
     * 根据供应商编码和日期范围查询供应商日产能信息列表
     *
     * @param supplierCode          供应商编码，唯一标识一个供应商
     * @param capacityDateBeginDate 容量日期开始范围，用于限定查询日期范围的起始点
     * @param capacityDateEndDate   容量日期结束范围，用于限定查询日期范围的结束点
     * @return 返回符合条件的供应商容量信息列表如果供应商编码为空或查询不到数据，则返回空列表
     */
    public List<SupplierCapacityPo> listBySupplierCodeAndDateRange(String supplierCode, LocalDate capacityDateBeginDate, LocalDate capacityDateEndDate) {
        // 检查供应商编码是否为空，如果为空则直接返回空列表，避免执行不必要的查询操作
        if (StrUtil.isBlank(supplierCode)) {
            return Collections.emptyList();
        }
        // 使用lambda查询方式，根据供应商编码和日期范围查询供应商容量信息列表
        return lambdaQuery()
                .eq(SupplierCapacityPo::getSupplierCode, supplierCode)
                .between(SupplierCapacityPo::getCapacityDate, capacityDateBeginDate, capacityDateEndDate).list();
    }

    /**
     * 根据供应商代码和日期获取供应商日产能信息
     * 如果存在多条记录，则返回第一条记录
     *
     * @param supplierCode 供应商代码
     * @param capacityDate 容量日期
     * @return 如果找到对应的供应商容量信息，则返回该信息；否则返回null
     */
    public SupplierCapacityPo getBySupplierCodeAndDate(String supplierCode, LocalDate capacityDate) {
        // 通过指定的供应商代码和日期范围（单个日期）查询供应商容量信息列表
        List<SupplierCapacityPo> supplierCapacityPos = listBySupplierCodeAndDateRange(supplierCode, capacityDate, capacityDate);

        // 如果列表为空，则返回null；否则返回列表中的第一个元素
        return supplierCapacityPos.isEmpty() ? null : supplierCapacityPos.stream().findFirst().orElse(null);
    }

    public List<SupplierCapacityResBo> listBySupplierCodeAndDate(List<SupplierCapacityQueryBo> queryParams) {
        if (CollectionUtils.isEmpty(queryParams)) {
            return Collections.emptyList();
        }
        return baseMapper.listBySupplierCodeAndDate(queryParams);
    }

    public void updateSupNorAvailCapacity(String supplierCode, LocalDate operationDate, BigDecimal operateValue) {
        baseMapper.updateSupNorAvailCapacity(supplierCode, operationDate, operateValue);
    }


    public List<String> filterSupplierCodes(BigDecimal restCap30PerStart, BigDecimal restCap30PerEnd,
                                            BigDecimal restCap60PerStart, BigDecimal restCap60PerEnd,
                                            BigDecimal restCap90PerStart, BigDecimal restCap90PerEnd) {
        return baseMapper.filterSupplierCodes(restCap30PerStart, restCap30PerEnd,
                restCap60PerStart, restCap60PerEnd,
                restCap90PerStart, restCap90PerEnd);
    }

    public int getSupCapacityExportTotal(SupCapacityPageDto dto) {
        return baseMapper.getSupCapacityExportTotal(dto);
    }

    public IPage<SupplierCapacityExportVo> getSupCapacityExportList(SupCapacityPageDto dto) {
        return baseMapper.getSupCapacityExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
    }

    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    public List<SupplierCapacityBo> listBySupCapWithDateRange(String supplierCode, LocalDate capacityBeginDate, LocalDate capacityEndDate) {
        return baseMapper.listBySupCapWithDateRange(supplierCode, capacityBeginDate, capacityEndDate);
    }
}
