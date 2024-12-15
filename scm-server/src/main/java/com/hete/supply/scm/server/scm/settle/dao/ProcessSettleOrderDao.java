package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.api.scm.entity.dto.ProcessSettleOrderDto;
import com.hete.supply.scm.api.scm.entity.enums.ProcessSettleStatus;
import com.hete.supply.scm.api.scm.entity.vo.ProcessSettleOrderExportVo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.vo.ProcessSettleOrderVo;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.mybatis.plus.dao.BaseDao;
import com.hete.support.mybatis.plus.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * <p>
 * 加工结算单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-10
 */
@Component
@Validated
public class ProcessSettleOrderDao extends BaseDao<ProcessSettleOrderMapper, ProcessSettleOrderPo> {

    /**
     * 列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 10:34
     */
    public CommonPageResult.PageInfo<ProcessSettleOrderVo> searchProcessSettleOrder(Page<Void> page, ProcessSettleOrderDto processSettleOrderDto) {
        IPage<ProcessSettleOrderVo> pageResult = baseMapper.selectProcessSettleOrderPage(page, processSettleOrderDto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 统计导出的总数
     *
     * @param dto
     * @return
     */
    public Integer getExportTotals(ProcessSettleOrderDto dto) {
        return baseMapper.getExportTotals(dto);
    }

    /**
     * 查询导出的列表
     *
     * @param dto
     * @return
     */
    public CommonPageResult.PageInfo<ProcessSettleOrderExportVo> getExportList(Page<Void> page, ProcessSettleOrderDto dto) {
        IPage<ProcessSettleOrderExportVo> pageResult = baseMapper.getExportList(page, dto);
        return PageInfoUtil.getPageInfo(pageResult);
    }

    /**
     * 通过编号查询单记录
     *
     * @author ChenWenLong
     * @date 2023/1/31 14:02
     */
    public ProcessSettleOrderPo getByProcessSettleOrderNo(String processSettleOrderNo) {
        return baseMapper.selectOne(Wrappers.<ProcessSettleOrderPo>lambdaQuery()
                .eq(ProcessSettleOrderPo::getProcessSettleOrderNo, processSettleOrderNo));
    }

    /**
     * 通过批量编号查询记录
     *
     * @author ChenWenLong
     * @date 2023/1/31 14:02
     */
    public List<ProcessSettleOrderPo> getByProcessSettleOrderNos(List<String> processSettleOrderNos, ProcessSettleStatus processSettleStatus) {
        return list(Wrappers.<ProcessSettleOrderPo>lambdaQuery().in(ProcessSettleOrderPo::getProcessSettleOrderNo, processSettleOrderNos)
                .eq(processSettleStatus != null, ProcessSettleOrderPo::getProcessSettleStatus, processSettleStatus));
    }

    /**
     * 根据月份查询
     *
     * @author ChenWenLong
     * @date 2023/2/4 18:12
     */
    public List<ProcessSettleOrderPo> getListByMonth(String month) {
        return list(Wrappers.<ProcessSettleOrderPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(month), ProcessSettleOrderPo::getMonth, month));
    }

    /**
     * 根据月份查询单记录
     *
     * @param month:
     * @return ProcessSettleOrderPo
     * @author ChenWenLong
     * @date 2023/6/7 10:11
     */
    public ProcessSettleOrderPo getByMonth(String month) {
        return baseMapper.selectOne(Wrappers.<ProcessSettleOrderPo>lambdaQuery()
                .eq(ProcessSettleOrderPo::getMonth, month));
    }
}
