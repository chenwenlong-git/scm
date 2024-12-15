package com.hete.supply.scm.server.scm.supplier.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierPaymentAccountSearchDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPaymentAccountPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierPaymentAccountSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 供应商收款账号 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-05
 */
@Mapper
interface SupplierPaymentAccountMapper extends BaseDataMapper<SupplierPaymentAccountPo> {
    IPage<SupplierPaymentAccountSearchVo> selectSupplierPaymentAccountPage(Page<Void> page, @Param("dto") SupplierPaymentAccountSearchDto dto);
}
