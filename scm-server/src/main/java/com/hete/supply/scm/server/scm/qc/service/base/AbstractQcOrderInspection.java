package com.hete.supply.scm.server.scm.qc.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.QcState;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yanjiawei
 * @Description 抽象的质检订单巡检类，提供了质检巡检操作的基本结构，具体的质检巡检操作需要在子类中实现。
 * @Date 2023/10/25 09:31
 */
@Slf4j
public abstract class AbstractQcOrderInspection {
    protected final QcOrderDao qcOrderDao;
    private final static int PAGE_SIZE = 500;
    private final static int MAX_ITERATIONS = 200;

    /**
     * 构造一个 AbstractQcOrderInspection 实例。
     *
     * @param qcOrderDao 质检订单数据访问对象，用于查询质检订单信息。
     */
    public AbstractQcOrderInspection(QcOrderDao qcOrderDao) {
        this.qcOrderDao = qcOrderDao;
    }

    /**
     * 执行质检巡检操作，检查指定天数内指定状态的质检订单。
     *
     * @param beforeDay 检查的天数范围，表示查询更新时间多少天内的质检订单。
     * @param qcState   指定的质检单状态，用于筛选需要检查的订单。
     */
    public final void doInspection(int beforeDay,
                                   QcState qcState) {
        doInspection(beforeDay, qcState, null);
    }

    /**
     * 执行质检巡检操作，检查指定天数内指定状态的质检订单。
     *
     * @param beforeDay 检查的天数范围，表示查询更新时间多少天内的质检订单。
     * @param qcState   指定的质检单状态，用于筛选需要检查的订单。
     * @param qcFilter  质检单筛选器
     */
    public final void doInspection(int beforeDay,
                                   QcState qcState,
                                   Boolean qcFilter) {
        int currentPage = 1;
        while (currentPage <= MAX_ITERATIONS) {
            // 调用分页查询方法
            IPage<QcOrderPo> pageResult;
            if (null == qcFilter) {
                // 无判断条件，查询质检单
                pageResult
                        = qcOrderDao.getQcOrdersWithInLastDaysAndState(beforeDay, qcState, currentPage, PAGE_SIZE);
            } else {
                // 当blankProcessOrderNo=true 查询加工单号为空的质检单数据，当blankProcessOrderNo=false 查询加工单号不为空的数据
                pageResult = qcFilter ?
                        qcOrderDao.getQcOrdersWithInLastDaysAndStateAndFilter(beforeDay, qcState, currentPage, PAGE_SIZE) :
                        qcOrderDao.getQcOrdersWithInLastDaysAndStateAndProcessOrderNoNotBlank(beforeDay, qcState, currentPage, PAGE_SIZE);
            }

            final List<QcOrderPo> qcOrders = pageResult.getRecords();
            if (CollectionUtils.isNotEmpty(qcOrders)) {
                log.info("开始巡检！扫描到近{}天发生过变更，状态是{}的质检单数据，条数：{}", beforeDay, qcState.getRemark(), qcOrders.size());
            } else {
                log.info("结束巡检! 尚未扫描到近{}天发生过变更，状态是{}的质检单数据", beforeDay, qcState.getRemark());
                break;
            }

            // 回调业务方法
            inspectQcOrders(qcOrders);
            currentPage++;
        }
    }

    /**
     * 对一组质检订单进行巡检的抽象方法。
     *
     * @param qcOrders 质检订单列表，包含需要进行质检检查的订单信息。
     */
    protected abstract void inspectQcOrders(List<QcOrderPo> qcOrders);

}
