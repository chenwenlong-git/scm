package com.hete.supply.scm.server.scm.qc.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.QcSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.api.scm.entity.enums.ReceiveType;
import com.hete.supply.scm.api.scm.entity.vo.QcDetailSkuVo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.entity.vo.QcSearchVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 质检单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-10-09
 */
@Mapper
interface QcOrderMapper extends BaseDataMapper<QcOrderPo> {

    IPage<QcSearchVo> searchQcOrderPage(Page<Void> page,
                                        @Param("qcSearchDto") QcSearchDto dto);

    IPage<QcOrderPo> getProcAndRepairQcNoByPage(Page<Object> page,
                                                ReceiveType receiveType);

    List<QcDetailSkuVo> listBySkuAndQcState(@Param("skuCodeList") List<String> skuCodeList,
                                            @Param("qcStateList") List<QcState> qcStateList,
                                            @Param("warehouseCodeList") List<String> warehouseCodeList);
}
