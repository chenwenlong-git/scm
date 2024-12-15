package com.hete.supply.scm.server.scm.settle.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.settle.entity.po.ProcessSettleOrderItemPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 加工结算单明细 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-10
 */
@Component
@Validated
public class ProcessSettleOrderItemDao extends BaseDao<ProcessSettleOrderItemMapper, ProcessSettleOrderItemPo> {

    /**
     * 根据加工结算单ID查询
     *
     * @author ChenWenLong
     * @date 2022/11/10 17:31
     */
    public List<ProcessSettleOrderItemPo> getByProcessSettleOrderId(Long processSettleOrderId) {
        return list(Wrappers.<ProcessSettleOrderItemPo>lambdaQuery().eq(ProcessSettleOrderItemPo::getProcessSettleOrderId, processSettleOrderId));
    }

    /**
     * 根据ID批量查询
     *
     * @author ChenWenLong
     * @date 2022/11/14 10:43
     */
    public List<ProcessSettleOrderItemPo> getByProcessSettleOrderIdList(List<Long> idList) {
        return Optional.ofNullable(baseMapper.selectList(Wrappers.<ProcessSettleOrderItemPo>lambdaQuery()
                        .in(ProcessSettleOrderItemPo::getProcessSettleOrderId, idList)))
                .orElse(new ArrayList<>());
    }

    /**
     * 根据编号查询列表
     *
     * @author ChenWenLong
     * @date 2022/11/8 14:49
     */
    public List<ProcessSettleOrderItemPo> getByProcessSettleOrderNo(String processSettleOrderNo) {
        return list(Wrappers.<ProcessSettleOrderItemPo>lambdaQuery()
                .like(ProcessSettleOrderItemPo::getProcessSettleOrderNo, processSettleOrderNo));
    }

    /**
     * ID和版本号查询详情
     *
     * @author ChenWenLong
     * @date 2022/11/8 16:12
     */
    public ProcessSettleOrderItemPo getByIdAndVersion(Long processSettleOrderItemId, Integer version) {
        return baseMapper.selectByIdVersion(processSettleOrderItemId, version);
    }

    /**
     * 根据ID查询
     *
     * @author ChenWenLong
     * @date 2022/11/16 17:31
     */
    public ProcessSettleOrderItemPo getByProcessSettleOrderItemId(Long processSettleOrderItemId) {
        return baseMapper.selectById(processSettleOrderItemId);
    }

    /**
     * 通过多个主键查询
     *
     * @param idList
     * @return
     */
    public List<ProcessSettleOrderItemPo> getByProcessSettleOrderItemIdList(List<Long> idList) {
        return Optional.ofNullable(baseMapper.selectList(Wrappers.<ProcessSettleOrderItemPo>lambdaQuery()
                        .in(ProcessSettleOrderItemPo::getProcessSettleOrderItemId, idList)))
                .orElse(new ArrayList<>());
    }

    /**
     * 通过完成人和结算单ID查询
     *
     * @author ChenWenLong
     * @date 2022/11/23 09:59
     */
    public ProcessSettleOrderItemPo getBySupplementUserAndId(String completeUser, Long processSettleOrderId) {
        return baseMapper.selectOne(Wrappers.<ProcessSettleOrderItemPo>lambdaQuery()
                .eq(ProcessSettleOrderItemPo::getCompleteUser, completeUser)
                .eq(ProcessSettleOrderItemPo::getProcessSettleOrderId, processSettleOrderId));
    }

    /**
     * 根据员工编码查询
     *
     * @author ChenWenLong
     * @date 2023/1/31 10:47
     */
    public List<ProcessSettleOrderItemPo> getListByCompleteUser(String completeUser, String processSettleOrderNo) {
        return list(Wrappers.<ProcessSettleOrderItemPo>lambdaQuery()
                .eq(StringUtils.isNotBlank(completeUser), ProcessSettleOrderItemPo::getCompleteUser, completeUser)
                .like(StringUtils.isNotBlank(processSettleOrderNo), ProcessSettleOrderItemPo::getProcessSettleOrderNo, processSettleOrderNo));
    }

    public IPage<ProcessSettleOrderItemPo> getByPage(int currentPage,
                                                     int pageSize) {
        // 创建查询条件的 LambdaQueryWrapper
        LambdaQueryWrapper<ProcessSettleOrderItemPo> queryWrapper = Wrappers.lambdaQuery();
        // 执行分页查询
        return baseMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }
}
