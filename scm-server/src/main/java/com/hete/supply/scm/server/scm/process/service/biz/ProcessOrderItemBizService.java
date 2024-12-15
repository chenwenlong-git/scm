package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.ProcessOrderType;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.enums.CostCalculationStrategyType;
import com.hete.supply.scm.server.scm.process.dao.BatchCodeCostDao;
import com.hete.supply.scm.server.scm.process.dao.ProcessOrderDao;
import com.hete.supply.scm.server.scm.process.entity.bo.CalculateCostBo;
import com.hete.supply.scm.server.scm.process.entity.po.BatchCodeCostPo;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessOrderPo;
import com.hete.supply.scm.server.scm.process.handler.ProcessCostHandler;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculationStrategy;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculatorStrategyFactory;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/24.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessOrderItemBizService {

    private final ProcessOrderDao processOrderDao;
    private final BatchCodeCostDao batchCodeCostDao;
    private final ConsistencyService consistencyService;
    private final CostCalculatorStrategyFactory costCalculatorStrategyFactory;

    private final static int PAGE_SIZE = 500;
    private final static int MAX_ITERATIONS = 100;

    /**
     * 初始化批次码价格信息的方法
     * 该方法通过分页查询的方式遍历处理加工单，获取符合状态条件的加工单数据，
     * 并调用 initializeBatchCodePrices(Set<String> processOrderNos) 方法
     * 对每页订单数据进行批次码价格的初始化。
     */
    @Transactional(rollbackFor = Exception.class)
    public void initializeBatchCodePricesByTypes(Set<ProcessOrderType> processOrderTypes) {
        // 当前页数
        int currentPage = 1;

        // 循环迭代直到达到最大迭代次数
        while (currentPage <= MAX_ITERATIONS) {
            // 调用分页查询方法
            IPage<ProcessOrderPo> pageResult = processOrderDao.getProcessOrderNoByPage(currentPage, PAGE_SIZE,
                    Set.of(ProcessOrderStatus.CHECKING,
                            ProcessOrderStatus.WAIT_DELIVERY,
                            ProcessOrderStatus.WAIT_RECEIPT,
                            ProcessOrderStatus.WAIT_STORE,
                            ProcessOrderStatus.STORED),
                    processOrderTypes);

            // 获取当前页的处理订单记录 & 如果当前页订单记录为空，结束方法执行
            final List<ProcessOrderPo> processOrderPos = pageResult.getRecords();
            if (CollectionUtils.isEmpty(processOrderPos)) {
                return;
            }

            // 提取当前页订单号集合 & 调用初始化批次码价格的方法
            Set<String> processOrderNos = processOrderPos.stream()
                    .map(ProcessOrderPo::getProcessOrderNo)
                    .collect(Collectors.toSet());
            initializeBatchCodePrices(processOrderNos);

            // 增加当前页数
            currentPage++;
        }
    }

    /**
     * 根据逗号分隔的处理加工单字符串，将处理订单号初始化批次码价格信息的方法。
     * 该方法首先将逗号分隔的字符串转换为处理加工单号的数组，然后将数组转换为 Set 集合。
     * 如果集合不为空，则调用 initializeBatchCodePrices(Set<String> processOrderNos) 方法，
     * 对每个处理订单号进行批次码价格的初始化。
     *
     * @param ponStr 逗号分隔的处理订单号字符串
     */
    @Transactional(rollbackFor = Exception.class)
    public void initializeBatchCodePrices(String ponStr) {
        // 将逗号分隔的字符串转换为处理订单号的数组 & 将数组转换为 Set 集合
        String[] ponArray = ponStr.split(",");
        Set<String> processOrderNos = new HashSet<>(Arrays.asList(ponArray));

        // 如果集合不为空，则调用 initializeBatchCodePrices 方法
        if (CollectionUtils.isNotEmpty(processOrderNos)) {
            initializeBatchCodePrices(processOrderNos);
        }
    }

    /**
     * 根据给定的处理加工单号集合，使用异步任务执行批次码价格初始化的方法。
     * 该方法创建 CalculateCostBo 对象，将处理订单号集合设置到对象中，
     * 然后调用 ConsistencyService 的 execAsyncTask 方法异步执行 ProcessCostHandler 类的任务。
     *
     * @param processOrderNos 处理订单号集合
     */
    public void initializeBatchCodePrices(Set<String> processOrderNos) {
        // 使用异步任务执行批次码价格初始化
        CalculateCostBo calculateCostBo = new CalculateCostBo();
        calculateCostBo.setProcessOrderNos(processOrderNos);
        consistencyService.execAsyncTask(ProcessCostHandler.class, calculateCostBo);
    }


    /**
     * 计算加工批次码总成本价。
     *
     * @param processOrderNo 加工单号
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmConstant.PROCESSING_ORDER_COST_CALCULATION_PREFIX, key = "#processOrderNo", waitTime = 1,
            leaseTime = -1)
    public boolean doCalculateBatchCodeCost(String processOrderNo) {
        List<BatchCodeCostPo> batchCodeCostPos = batchCodeCostDao.listByRelateOrderNo(processOrderNo);
        if (CollectionUtils.isNotEmpty(batchCodeCostPos)) {
            batchCodeCostPos.forEach(batchCodeCostPo -> batchCodeCostPo.setDelTimestamp(DateUtil.current()));
            batchCodeCostDao.updateBatchByIdVersion(batchCodeCostPos);
        }

        ProcessOrderPo processOrderPo = processOrderDao.getByProcessOrderNo(processOrderNo);
        ProcessOrderType processOrderType = processOrderPo.getProcessOrderType();
        boolean isRework = Arrays.asList(ProcessOrderType.REWORKING, ProcessOrderType.REWORKING)
                .contains(processOrderType);

        CostCalculationStrategyType strategyType
                = isRework ? CostCalculationStrategyType.REWORK_PROCESS_ORDER :
                CostCalculationStrategyType.NON_REWORK_PROCESS_ORDER;
        CostCalculationStrategy costCalculationStrategy
                = costCalculatorStrategyFactory.getCostCalculationStrategy(strategyType);
        return costCalculationStrategy.calculateBatchCodeCost(processOrderNo);
    }

}



