package com.hete.supply.scm.server.scm.settle.dao;

import com.hete.supply.scm.api.scm.entity.enums.SupplementStatus;
import com.hete.supply.scm.server.scm.settle.entity.bo.DeductSupplementAndStatusBo;
import com.hete.supply.scm.server.scm.settle.entity.po.SupplementOrderPurchasePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 补款单采购明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-03
 */
@Mapper
interface SupplementOrderPurchaseMapper extends BaseDataMapper<SupplementOrderPurchasePo> {

    List<DeductSupplementAndStatusBo> getListByBusinessNoAndStatus(@Param("businessNoList") List<String> businessNoList,
                                                                   @Param("statusList") List<SupplementStatus> statusList);
}
