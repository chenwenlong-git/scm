package com.hete.supply.scm.server.scm.process.dao;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.enums.ProcessLabel;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleDetailExportVo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessSettleDetailReportPo;
import com.hete.supply.scm.api.scm.entity.dto.GetProcessSettleOrderDetailAndScanSettleDto;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderItemPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderDetailAndScanSettleVo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.sql.Struct;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 加工结算详情报表 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-12-18
 */
@Component
@Validated
public class ProcessSettleDetailReportDao extends BaseDao<ProcessSettleDetailReportMapper, ProcessSettleDetailReportPo> {
    public IPage<ProcessSettleDetailReportPo> getByPage(IPage<ProcessSettleDetailReportPo> page,
                                                        GetProcessSettleOrderDetailAndScanSettleDto dto) {
        LambdaQueryWrapper<ProcessSettleDetailReportPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(dto.getProcessSettleOrderId()), ProcessSettleDetailReportPo::getProcessSettleOrderId, dto.getProcessSettleOrderId());
        wrapper.in(CollectionUtils.isNotEmpty(dto.getCompleteUsers()), ProcessSettleDetailReportPo::getCompleteUser, dto.getCompleteUsers());
        return baseMapper.selectPage(page, wrapper);
    }

    public Long getProcessSettleDetailExportTotals(GetProcessSettleOrderDetailAndScanSettleDto dto) {
        LambdaQueryWrapper<ProcessSettleDetailReportPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(dto.getProcessSettleOrderId()), ProcessSettleDetailReportPo::getProcessSettleOrderId, dto.getProcessSettleOrderId());
        wrapper.in(CollectionUtils.isNotEmpty(dto.getCompleteUsers()), ProcessSettleDetailReportPo::getCompleteUser, dto.getCompleteUsers());
        return baseMapper.selectCount(wrapper);
    }

    public List<ProcessSettleDetailReportPo> listByProcessSettleOrderId(Long processSettleOrderId) {
        LambdaQueryWrapper<ProcessSettleDetailReportPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(processSettleOrderId), ProcessSettleDetailReportPo::getProcessSettleOrderId, processSettleOrderId);
        return baseMapper.selectList(wrapper);
    }

    public List<ProcessSettleDetailReportPo> listByProcessSettleOrderIdAndCompleteUserAndLabel(Long processSettleOrderId,
                                                                                               Collection<ProcessLabel> processLabels,
                                                                                               Collection<String> completeUsers) {
        LambdaQueryWrapper<ProcessSettleDetailReportPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(processSettleOrderId), ProcessSettleDetailReportPo::getProcessSettleOrderId, processSettleOrderId);
        wrapper.in(CollectionUtils.isNotEmpty(processLabels), ProcessSettleDetailReportPo::getProcessLabel, processLabels);
        wrapper.in(CollectionUtils.isNotEmpty(completeUsers), ProcessSettleDetailReportPo::getCompleteUser, completeUsers);
        return baseMapper.selectList(wrapper);
    }
}
