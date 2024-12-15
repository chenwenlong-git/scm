package com.hete.supply.scm.server.scm.process.dao;


import com.hete.supply.scm.server.scm.process.entity.bo.ProcessOrderScanRelateCountBo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderScanRelatePo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 工序扫码关联表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-02-27
 */
@Mapper
interface ProcessOrderScanRelateMapper extends BaseDataMapper<ProcessOrderScanRelatePo> {

    Integer calculateQualityGoodsCountTotal(@Param("processCode") String independentProcessCode,
                                            Long processOrderScanId,
                                            String completeUser,
                                            LocalDateTime completeTimeBegin,
                                            LocalDateTime completeTimeEnd);

    List<ProcessOrderScanRelateCountBo> countProcessOrderScanByIds(Collection<Long> ids);
}
