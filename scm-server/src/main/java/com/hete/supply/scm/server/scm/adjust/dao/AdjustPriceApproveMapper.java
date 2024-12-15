package com.hete.supply.scm.server.scm.adjust.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.adjust.entity.dto.AdjustApproveSearchDto;
import com.hete.supply.scm.server.scm.adjust.entity.po.AdjustPriceApprovePo;
import com.hete.supply.scm.server.scm.adjust.entity.vo.AdjustApproveVo;
import com.hete.support.mybatis.plus.mapper.BaseDataMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 调价审批表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2024-06-13
 */
@Mapper
interface AdjustPriceApproveMapper extends BaseDataMapper<AdjustPriceApprovePo> {

    IPage<AdjustApproveVo> searchAdjustApprove(Page<Void> page, AdjustApproveSearchDto dto);
}
