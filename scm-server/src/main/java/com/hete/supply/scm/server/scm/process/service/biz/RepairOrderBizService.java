package com.hete.supply.scm.server.scm.process.service.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.google.common.collect.Lists;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.GetRepairOrderProcessResultInfoDto;
import com.hete.supply.scm.api.scm.entity.dto.GetRepairOrderStatusBatchDto;
import com.hete.supply.scm.api.scm.entity.dto.RepairOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.IsReceiveMaterial;
import com.hete.supply.scm.api.scm.entity.enums.QcOrigin;
import com.hete.supply.scm.api.scm.entity.enums.QcOriginProperty;
import com.hete.supply.scm.api.scm.entity.enums.RepairOrderStatus;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderProcessResultVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderResultExportVo;
import com.hete.supply.scm.api.scm.entity.vo.RepairOrderStatusVo;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.SdaRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.dao.ScmImageDao;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderCreateMqDto;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.process.builder.RepairOrderBuilder;
import com.hete.supply.scm.server.scm.process.config.ScmProcessProp;
import com.hete.supply.scm.server.scm.process.converter.RepairOrderConverter;
import com.hete.supply.scm.server.scm.process.dao.*;
import com.hete.supply.scm.server.scm.process.entity.bo.*;
import com.hete.supply.scm.server.scm.process.entity.dto.*;
import com.hete.supply.scm.server.scm.process.entity.po.*;
import com.hete.supply.scm.server.scm.process.entity.vo.*;
import com.hete.supply.scm.server.scm.process.handler.*;
import com.hete.supply.scm.server.scm.process.service.base.CostCalculatorStrategyFactory;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.QcOriginBo;
import com.hete.supply.scm.server.scm.qc.entity.bo.RepairOrderCreateQcBo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.supply.scm.server.scm.qc.service.base.AbstractQcOrderCreator;
import com.hete.supply.scm.server.scm.qc.service.base.RepairOrderQcOrderCreator;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreateDto;
import com.hete.supply.wms.api.basic.entity.vo.WarehouseVo;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.ExceptionUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import com.hete.trace.util.TraceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanjiawei
 * Created on 2023/12/28.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RepairOrderBizService {
    private final ConsistencySendMqService sendMqService;
    private final ConsistencyService consistencyService;
    private final RepairOrderDao repairOrderDao;
    private final RepairOrderItemDao repairOrderItemDao;
    private final ProcessOrderMaterialDao processOrderMaterialDao;
    private final IdGenerateService idGenerateService;
    private final RepairOrderBaseService repairOrderBaseService;
    private final QcOrderDao qcOrderDao;
    private final QcDetailDao qcDetailDao;
    private final RepairOrderResultDao repairOrderResultDao;
    private final ProcessMaterialReceiptDao processMaterialReceiptDao;
    private final ProcessMaterialReceiptItemDao processMaterialReceiptItemDao;
    private final ScmImageDao scmImageDao;
    private final WmsRemoteService wmsRemoteService;
    private final PlmRemoteService plmRemoteService;
    private final ScmImageBaseService scmImageBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final ProcessMaterialBackDao processMaterialBackDao;
    private final ProcessMaterialBackItemDao processMaterialBackItemDao;
    private final Environment environment;
    private final SdaRemoteService sdaRemoteService;
    private final CostCalculatorStrategyFactory costCalculatorStrategyFactory;
    private final ScmProcessProp scmProcessProp;
    private final SupplierDao supplierDao;


    public static final int REPAIR_NOT_PASS = 0;
    public static final int MAX_REPAIR_NUM = 10;

    /**
     * 创建返修单主入口方法。执行返修单的拆分、保存、生成批次码和推送PLM等操作。
     *
     * @param planOrderDto 计划单DTO，包含了创建返修单所需的信息
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_PLAN_REPAIR_CREATE, key = "#planOrderDto.planNo", waitTime = 1,
            leaseTime = -1)
    public void createRepairOrder(CreateRepairOrderMqDto planOrderDto) {
        // 前置校验
        String planNo = planOrderDto.getPlanNo();
        List<RepairOrderPo> repairOrderPos = repairOrderDao.listByPlanNo(planNo);
        if (CollectionUtils.isNotEmpty(repairOrderPos)) {
            log.error("创建返修单失败！原因：{}计划单已创建对应返修单，请PLM/PDC相关同事注意！", planNo);
            return;
        }

        // 第一步：返修单拆分
        List<RepairAllocationResultBo> repairAllocationResults = splitPlanOrder(planOrderDto);

        // 第二步：构建拆分计划订单结果列表
        List<SplitPlanOrderResultBo> splitPlanOrderResultBos = buildSplitPlanOrderResults(planOrderDto,
                repairAllocationResults);

        // 第三步：保存拆分计划订单结果列表
        saveRepairResult(splitPlanOrderResultBos);

        // 第四步：为拆分计划订单结果列表生成批次码
        createRepairItemBatchCode(splitPlanOrderResultBos);

        // 第五步：推送拆分计划订单结果列表到PLM
        pushResultToPlm(splitPlanOrderResultBos);

        // 第六步：执行库存匹配操作，更新返修单原料的库存信息
        executeInventoryMatchingAsync(splitPlanOrderResultBos);
    }

    /**
     * 将创建返修单消息DTO拆分成加工商品和加工材料，并执行分配返修单的操作。
     *
     * @param createRepairOrderMqDto 创建返修单消息DTO。
     * @return 分配后的返修单结果BO列表。
     */
    private List<RepairAllocationResultBo> splitPlanOrder(CreateRepairOrderMqDto createRepairOrderMqDto) {
        // 获取加工商品列表 & 将加工商品列表拆分并分组
        List<CreateRepairOrderMqDto.RepairChildItem> repairSkuList = createRepairOrderMqDto.getRepairChildList();
        Map<Integer, List<RepairOrderItemBo>> splitSkus = splitRepairSkuList(repairSkuList);

        // 获取加工材料列表 & 将加工材料列表拆分并分组
        List<CreateRepairOrderMqDto.RepairMaterialItem> repairMaterialList
                = createRepairOrderMqDto.getRepairMaterialList();
        Map<Integer, List<RepairMaterialBo>> splitMaterials = splitMaterials(repairMaterialList);

        // 执行返修单分配操作
        return allocateRepairOrders(splitSkus, splitMaterials);
    }


    /**
     * 构建 SplitPlanOrderResultBo 列表。
     *
     * @param repairAllocationResults 包含返修单分配结果的列表
     * @return 构建的 SplitPlanOrderResultBo 列表
     */
    public List<SplitPlanOrderResultBo> buildSplitPlanOrderResults(CreateRepairOrderMqDto planOrderDto,
                                                                   List<RepairAllocationResultBo> repairAllocationResults) {
        List<SplitPlanOrderResultBo> splitResults = Lists.newArrayList();

        // 遍历返修单分配结果列表，构建 SplitPlanOrderResultBo 对象
        for (RepairAllocationResultBo repairAllocationResult : repairAllocationResults) {
            String repairOrderNo = idGenerateService.getConfuseCode(ScmConstant.REPAIR_ORDER_NO_PREFIX, TimeType.CN_DAY,
                    ConfuseLength.L_4);
            SplitPlanOrderResultBo splitResultBo = new SplitPlanOrderResultBo();

            // 返修单明细
            List<RepairOrderItemBo> repairOrderItems = repairAllocationResult.getRepairOrderItems();
            List<RepairOrderItemPo> repairOrderItemPos = RepairOrderBuilder.buildRepairOrderItemPos(repairOrderNo,
                    repairOrderItems);
            splitResultBo.setRepairOrderItemPos(repairOrderItemPos);

            // 返修单
            int expectProcessNum = repairOrderItemPos.stream()
                    .mapToInt(RepairOrderItemPo::getExpectProcessNum)
                    .sum();
            RepairOrderPo repairOrderPo = RepairOrderBuilder.buildRepairOrderPo(repairOrderNo, expectProcessNum,
                    planOrderDto);
            splitResultBo.setRepairOrderPo(repairOrderPo);

            // 返修单原料
            List<RepairMaterialBo> repairMaterials = repairAllocationResult.getRepairMaterials();
            List<ProcessOrderMaterialPo> repairMaterialPos = RepairOrderBuilder.buildRepairMaterialPos(repairOrderNo,
                    repairMaterials);
            splitResultBo.setProcessOrderMaterialPos(repairMaterialPos);

            splitResults.add(splitResultBo);
        }

        return splitResults;
    }

    /**
     * 异步执行库存匹配操作，更新拆分后的返修单原料的库存信息。
     *
     * @param splitPlanOrderResultBos 拆分后的返修单结果BO列表。
     */
    public void executeInventoryMatchingAsync(List<SplitPlanOrderResultBo> splitPlanOrderResultBos) {
        for (SplitPlanOrderResultBo splitPlanOrderResultBo : splitPlanOrderResultBos) {
            // 获取返修单信息
            RepairOrderPo repairOrderPo = splitPlanOrderResultBo.getRepairOrderPo();
            String planNo = repairOrderPo.getPlanNo();
            String repairOrderNo = repairOrderPo.getRepairOrderNo();

            // 构建同步返修单信息的BO对象
            CreateRepairOrderSyncBo createRepairOrderSyncBo = RepairOrderBuilder.buildCreateRepairOrderSyncBo(planNo,
                    repairOrderNo);

            // 异步执行库存匹配操作
            consistencyService.execAsyncTask(ReOrderInvMatchHandler.class, createRepairOrderSyncBo);
        }
    }


    /**
     * 为拆分计划订单结果列表生成批次码。
     *
     * @param splitResults 拆分计划订单结果列表
     */
    public void createRepairItemBatchCode(List<SplitPlanOrderResultBo> splitResults) {
        String createBatchCodeSupplierCode = scmProcessProp.getCreateBatchCodeSupplierCode();

        for (SplitPlanOrderResultBo splitResult : splitResults) {
            validateCreateBatchCodeParam(splitResults);

            RepairOrderPo repairOrderPo = splitResult.getRepairOrderPo();
            String repairOrderNo = repairOrderPo.getRepairOrderNo();

            List<RepairOrderItemPo> repairOrderItemPos = splitResult.getRepairOrderItemPos();
            List<String> skuList = repairOrderItemPos.stream()
                    .filter(item -> StringUtils.isBlank(item.getBatchCode()))
                    .map(RepairOrderItemPo::getSku)
                    .collect(Collectors.toList());

            // 调用远程服务批量创建批次码
            String supplierName = "";
            if (StrUtil.isNotBlank(createBatchCodeSupplierCode)) {
                SupplierPo supplierPo = supplierDao.getBySupplierCode(createBatchCodeSupplierCode);
                if (Objects.nonNull(supplierPo)) {
                    supplierName = supplierPo.getSupplierName();
                }
            }
            SkuBatchCreateDto createBatchCodeParam = RepairOrderBuilder.buildSkuBatchCreateDto(repairOrderNo, createBatchCodeSupplierCode, supplierName, skuList);
            Map<String, String> groupedSkuVos = wmsRemoteService.batchCreateBatchCode(createBatchCodeParam);

            // 更新商品信息的批次码
            repairOrderItemPos.forEach(repairOrderItemPo -> {
                repairOrderItemPo.setBatchCode(groupedSkuVos.getOrDefault(repairOrderItemPo.getSku(), ""));
            });

            repairOrderItemDao.updateBatchByIdVersion(repairOrderItemPos);
        }
    }


    private Map<Integer, List<RepairOrderItemBo>> splitRepairSkuList(List<CreateRepairOrderMqDto.RepairChildItem> repairChildList) {
        List<Map<String, Integer>> items = repairChildList.stream()
                .map(repairChildItem -> Map.of(repairChildItem.getSkuCode(), repairChildItem.getAmount()))
                .collect(Collectors.toList());
        Map<Integer, List<Map<String, Integer>>> splitResultMap = repairOrderBaseService.splitAndGroupByBatchSize(items,
                MAX_REPAIR_NUM);

        Map<Integer, List<RepairOrderItemBo>> resultMap = new HashMap<>(splitResultMap.size());
        for (Map.Entry<Integer, List<Map<String, Integer>>> entry : splitResultMap.entrySet()) {
            Integer key = entry.getKey();
            List<Map<String, Integer>> value = entry.getValue();
            List<RepairOrderItemBo> repairOrderItemBos = value.stream()
                    .map(RepairOrderConverter::convertRepairOrderItem)
                    .collect(Collectors.toList());
            for (RepairOrderItemBo repairOrderItemBo : repairOrderItemBos) {
                String sku = repairOrderItemBo.getSku();
                CreateRepairOrderMqDto.RepairChildItem matchSpu = repairChildList.stream()
                        .filter(repairChildItem -> Objects.equals(sku, repairChildItem.getSkuCode()))
                        .findFirst()
                        .orElse(null);
                repairOrderItemBo.setSpu(Objects.nonNull(matchSpu) ? matchSpu.getSpuCode() : "");
            }
            resultMap.put(key, repairOrderItemBos);
        }
        return resultMap;
    }

    private Map<Integer, List<RepairMaterialBo>> splitMaterials(List<CreateRepairOrderMqDto.RepairMaterialItem> repairMaterialList) {
        generateUniqueCode(repairMaterialList);

        List<Map<String, Integer>> items = repairMaterialList.stream()
                .map(repairChildItem -> Map.of(repairChildItem.getUniqueCode(), repairChildItem.getAmount()))
                .collect(Collectors.toList());
        Map<Integer, List<Map<String, Integer>>> splitResultMap = repairOrderBaseService.splitAndGroupByBatchSize(items,
                MAX_REPAIR_NUM);

        Map<Integer, List<RepairMaterialBo>> resultMap = new HashMap<>(splitResultMap.size());

        // 遍历外部 Map
        for (Map.Entry<Integer, List<Map<String, Integer>>> entry : splitResultMap.entrySet()) {
            Integer no = entry.getKey();
            List<Map<String, Integer>> value = entry.getValue();

            // 转换内部 List<Map<String, Integer>> 到 List<ProcessOrderMaterialPo>
            List<RepairMaterialBo> processOrderMaterialPos = RepairOrderConverter.toCreateRepairMaterialBo(
                    repairMaterialList, value);

            // 将结果放入新的 Map
            resultMap.put(no, processOrderMaterialPos);
        }

        return resultMap;
    }

    private static void generateUniqueCode(List<CreateRepairOrderMqDto.RepairMaterialItem> repairMaterialList) {
        for (CreateRepairOrderMqDto.RepairMaterialItem item : repairMaterialList) {
            item.setUniqueCode(IdUtil.randomUUID());
        }
    }

    /**
     * 保存拆分后的返修单结果信息。
     *
     * @param splitResults 拆分后的返修单结果BO列表。
     */
    public void saveRepairResult(List<SplitPlanOrderResultBo> splitResults) {
        for (SplitPlanOrderResultBo splitResult : splitResults) {
            // 保存返修子单
            RepairOrderPo repairOrderPo = ParamValidUtils.requireNotNull(splitResult.getRepairOrderPo(),
                    "保存返修单信息失败，返修单信息为空！");

            // 保存返修明细信息
            List<RepairOrderItemPo> repairOrderItemPos = ParamValidUtils.requireNotNull(
                    splitResult.getRepairOrderItemPos(), "保存返修单商品信息失败，返修单商品信息为空！");

            // 保存返修原料信息
            List<ProcessOrderMaterialPo> processOrderMaterialPos = ParamValidUtils.requireNotNull(
                    splitResult.getProcessOrderMaterialPos(), "保存返修单商品信息失败，返修单原料信息为空！");

            repairOrderDao.insert(repairOrderPo);
            repairOrderItemDao.insertBatch(repairOrderItemPos);
            processOrderMaterialDao.insertBatch(processOrderMaterialPos);
        }
    }

    /**
     * 推送拆分计划订单结果列表的创建返修单结果消息DTO到PLM。
     *
     * @param splitResults 拆分计划订单结果列表
     * @throws BizException 如果拆分计划订单结果列表无效，将抛出业务异常
     */
    public void pushResultToPlm(List<SplitPlanOrderResultBo> splitResults) {
        // 第一步：校验拆分计划订单结果列表的有效性
        validatePushResults(splitResults);

        // 第二步：构建拆分计划订单结果列表的创建返修单结果消息DTO列表
        List<CreateRepairOrderResultMqDto> createResultDtoList = RepairOrderBuilder.buildCreateResultDtoList(
                splitResults);

        // 第三步：推送创建返修单结果消息DTO到PLM
        createResultDtoList.forEach(createResultDto -> {
            String msgKey = createResultDto.getRepairNo();
            createResultDto.setKey(StrUtil.format("{}:{}", ScmConstant.REPAIR_ORDER_CREATE_RESULT, msgKey));
            sendMqService.execSendMq(ReOrderCreResHandler.class, createResultDto);
        });
    }


    /**
     * 从队列中取出所有值。
     *
     * @param queue 包含Map条目的队列
     * @return 合并后的Map.Entry，具有相同键的值已被合并
     */
    private Map.Entry<Integer, List<RepairMaterialBo>> pollAll(Queue<Map.Entry<Integer, List<RepairMaterialBo>>> queue) {
        // 创建临时列表用于存储合并后的返修单原料
        List<RepairMaterialBo> allMaterials = new LinkedList<>();

        // 逐个取出队列中的元素并添加到临时列表中
        while (!queue.isEmpty()) {
            Map.Entry<Integer, List<RepairMaterialBo>> materialPoll = queue.poll();
            allMaterials.addAll(materialPoll.getValue());
        }

        // 返回合并后的Map.Entry，键为0（可以根据实际情况修改）
        return Map.entry(0, allMaterials);
    }

    /**
     * 执行返修单库存匹配和创建出库单任务的定时任务。
     */
    public void repairOrderStockMatchingJob() {
        // 查询待处理状态的返修单列表
        List<RepairOrderPo> repairOrderPos = repairOrderDao.listByStatus(List.of(RepairOrderStatus.WAITING_FOR_READY));

        // 通过加入Thread.sleep(0)，尝试主动释放CPU资源
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
        }

        // 记录日志，输出待处理的返修单数量
        log.info("执行校验库存和创建出库单任务，待处理返修单数量：{} 条", repairOrderPos.size());

        // 遍历待处理返修单列表，逐个执行库存匹配和创建出库单任务
        for (RepairOrderPo repairOrderPo : repairOrderPos) {
            String planNo = repairOrderPo.getPlanNo();
            String repairOrderNo = repairOrderPo.getRepairOrderNo();

            try {
                // 调用库存匹配和创建出库单任务的方法
                repairOrderBaseService.doRepairOrderStockMatching(planNo, repairOrderNo);
            } catch (Exception e) {
                log.error("repairOrderStockMatchingJob error stack:{} spanId:{}", ExceptionUtil.getBizStackTrace(e),
                        TraceUtil.getSpanId());
            }
        }
    }

    /**
     * 提交返修单的加工结果。
     *
     * @param request 包含返修单加工结果信息的请求对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_UPDATE_REPAIR_RESULT, key = "#request.batchCode", waitTime = 1,
            leaseTime = -1)
    public void submitRepairDetailResult(RepairDetailResultRequestDto request) {
        // 前置校验：获取请求中的批次码和成功返修数量
        String batchCode = request.getBatchCode();
        String curMaterialBatchCode = request.getMaterialBatchCode();
        Integer successfulRepairQuantity = request.getSuccessfulRepairQuantity();

        // 根据批次码从数据库中获取返修明细 & 验证本次返修完成数量<预计返修数量
        RepairOrderItemPo repairOrderItemPo = ParamValidUtils.requireNotNull(
                repairOrderItemDao.getByBatchCode(batchCode),
                StrUtil.format("未找到与该批次码相关的数据，请检查批次码是否正确。"));
        Integer totalExpectProcessQuantity = repairOrderItemPo.getExpectProcessNum();
        ParamValidUtils.requireLessThanOrEqual(successfulRepairQuantity, totalExpectProcessQuantity,
                "您提交的返修完成数量超过下单数，请确认后重试。");

        // 获取返修订单号 & 根据返修单号从数据库中获取返修单信息 & 验证当前返修订单的状态 & 是否回料
        String repairOrderNo = repairOrderItemPo.getRepairOrderNo();
        RepairOrderPo repairOrderPo = ParamValidUtils.requireNotNull(repairOrderDao.getByRepairOrderNo(repairOrderNo),
                "无法提交加工结果，因为所选返修单已不存在。请重新选择有效的返修单进行提交。");
        RepairOrderStatus curRepairOrderStatus = repairOrderPo.getRepairOrderStatus();
        ParamValidUtils.requireContains(curRepairOrderStatus, Arrays.asList(RepairOrderStatus.WAITING_FOR_PRODUCTION,
                        RepairOrderStatus.IN_PROCESS),
                "很抱歉，当前返修单状态不允许提交加工结果。请检查返修单的状态并重新尝试。");
        ParamValidUtils.requireEquals(repairOrderPo.getIsReceiveMaterial(), IsReceiveMaterial.TRUE,
                "无法提交加工结果，因为加工部尚未确认收到所需原料，请等待原料出库或操作原料确认收货后再进行操作。");

        // 获取返修订单号 & 根据返修单号从数据库中获取原料收货单信息 & 验证原料收货单批次码是否存在 & 当前返修完成数量<原料可用数量
        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = ParamValidUtils.requireNotEmpty(
                processMaterialReceiptDao.listByRepairOrderNo(repairOrderNo),
                "无法提交加工结果，因为仓库尚未迁出原料，请等待仓库签出原料后确认收货再进行操作。");
        List<Long> processMaterialReceiptIds = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos = ParamValidUtils.requireNotEmpty(
                processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds),
                "无法提交加工结果，因为仓库尚未迁出原料明细，请等待仓库签出原料后确认收货再进行操作。");
        ParamValidUtils.requireNotNull(processMaterialReceiptItemPos.stream()
                        .filter(materialReceiptItem -> Objects.equals(curMaterialBatchCode,
                                materialReceiptItem.getSkuBatchCode()))
                        .findFirst()
                        .orElse(null),
                "无法提交加工结果，您绑定的原料批次码有误，请校验后再进行操作。");
        ParamValidUtils.requireGreaterThan(processMaterialReceiptItemPos.stream()
                        .filter(materialReceiptItem -> Objects.equals(curMaterialBatchCode,
                                materialReceiptItem.getSkuBatchCode()))
                        .findFirst()
                        .map(ProcessMaterialReceiptItemPo::getReceiptNum)
                        .orElse(0), 0,
                "无法提交加工结果，您绑定的原料批次码收货数为0，请校验后再进行操作。");

        List<RepairOrderBindingInfoBo> repairOrderBindingInfos = repairOrderBaseService.getRepairOrderBindingInfo(
                repairOrderNo);
        Integer availableMaterialQuantity = repairOrderBindingInfos.stream()
                .filter(repairOrderBindingInfo -> Objects.equals(curMaterialBatchCode,
                        repairOrderBindingInfo.getBatchCode()))
                .findFirst()
                .map(RepairOrderBindingInfoBo::getAvailableForReturn)
                .orElse(0);
        ParamValidUtils.requireLessThanOrEqual(successfulRepairQuantity, availableMaterialQuantity, StrUtil.format(
                "无法提交加工结果，您绑定的原料批次码 {} 提交数量需小于等于原料可绑定数量，请校验后再进行操作。",
                curMaterialBatchCode));

        // 获取返修明细的ID & 根据返修订单项ID从数据库中获取已完成的加工结果列表 & 计算已完成的加工数量总和 & 验证成功返修数量不能超过返修单剩余的可加工数
        Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();
        List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultDao.listByRepairOrderItemId(
                repairOrderItemId);
        int totalCompletedQuantity = repairOrderResultPos.stream()
                .mapToInt(RepairOrderResultPo::getCompletedQuantity)
                .sum();
        ParamValidUtils.requireLessThanOrEqual(successfulRepairQuantity,
                totalExpectProcessQuantity - totalCompletedQuantity,
                "您提交的加工数量已经超过了返修单剩余的可加工数，请仔细核对输入。");

        // 更新返修单状态
        repairOrderPo.setRepairOrderStatus(RepairOrderStatus.IN_PROCESS);
        repairOrderDao.updateByIdVersion(repairOrderPo);

        // 保存返修结果
        RepairOrderResultPo repairOrderResultPo = RepairOrderBuilder.buildRepairOrderResultPo(request,
                repairOrderItemPo);
        repairOrderResultDao.insert(repairOrderResultPo);

        // 保存图片信息
        List<ScmImagePo> scmImagePos = RepairOrderBuilder.buildScmImagePoList(request, repairOrderResultPo);
        if (CollectionUtils.isNotEmpty(scmImagePos)) {
            scmImageDao.insertBatch(scmImagePos);
        }

        // 更新返修明细已加工数量
        int actProcessedCompleteCnt = totalCompletedQuantity + successfulRepairQuantity;
        repairOrderItemPo.setActProcessedCompleteCnt(actProcessedCompleteCnt);
        repairOrderItemDao.updateByIdVersion(repairOrderItemPo);
    }

    /**
     * 完成返修单加工流程，更新加工结果和相关信息。
     *
     * @param request 包含返修单加工完成信息的请求对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_REPAIR_COMPLETE, key = "#request.repairOrderNo", waitTime = 1,
            leaseTime = -1)
    public void completeProcessing(CompletionRequestDto request) {
        // 获取请求中的返修单号和返修单完成明细
        final String repairOrderNo = request.getRepairOrderNo();
        final Integer curVersion = request.getVersion();
        final List<CompletionRequestDto.CompletionDetailRequestDto> completionDetails = request.getCompletionDetails();

        // 前置校验：
        RepairOrderPo verifyRepairOrderPo = ParamValidUtils.requireNotNull(
                repairOrderDao.getByRepairOrderNo(repairOrderNo, curVersion),
                "无法进行完成返修，因为所选返修单已被删除或更新，请刷新页面并重新选择有效的返修单进行操作。");

        // 验证当前返修单的状态 & 是否回料
        RepairOrderStatus curRepairOrderStatus = verifyRepairOrderPo.getRepairOrderStatus();
        ParamValidUtils.requireContains(curRepairOrderStatus, Arrays.asList(RepairOrderStatus.WAITING_FOR_PRODUCTION,
                        RepairOrderStatus.IN_PROCESS),
                "当前状态不允许进行操作，请确保当前处于待投产或加工中的状态，请刷新后重试!");
        IsReceiveMaterial isReceiveMaterial = verifyRepairOrderPo.getIsReceiveMaterial();
        ParamValidUtils.requireEquals(isReceiveMaterial, IsReceiveMaterial.TRUE,
                "无法提交完成返修，因为加工部尚未确认收到所需原料，请等待原料出库或操作原料确认收货后再进行操作。");

        // 更新原料消耗数
        updateMaterialUsageQuantity(repairOrderNo, completionDetails);

        // 补充未提交加工结果的返修单明细
        handleUnSubmittedResults(repairOrderNo, completionDetails);

        // 验证返修单的结果列表和返修明细列表
        List<RepairOrderResultPo> repairOrderResultPos = ParamValidUtils.requireNotEmpty(
                repairOrderResultDao.getListByRepairOrderNo(repairOrderNo),
                "无法完成返修，当前返修单暂无返修结果。请确认是否已提交结果并刷新页面后再试。");
        List<RepairOrderItemPo> repairOrderItemPos = ParamValidUtils.requireNotNull(repairOrderItemDao.listByIds(
                        repairOrderResultPos.stream()
                                .map(RepairOrderResultPo::getRepairOrderItemId)
                                .collect(Collectors.toSet())),
                "无法完成返修，因为所选返修单商品信息已被删除，请刷新页面并重新选择有效的返修单进行操作。");
        // 批量赋值返修单结果 & 校验返修结果原料消耗数
        batchUpdateRepairOrderResults(completionDetails, repairOrderResultPos);
        validateMaterialBatchCodeReceivedQuantity(repairOrderNo, repairOrderResultPos);

        // 更新返修单明细信息
        batchUpdateRepairOrderItems(repairOrderItemPos, repairOrderResultPos);

        // 更新返修单信息
        RepairOrderPo updateRepairOrderPo = RepairOrderBuilder.buildCompletedQcRepairOrderPo(
                repairOrderDao.getByRepairOrderNo(repairOrderNo), repairOrderResultPos);

        // 数据更新
        repairOrderDao.updateByIdVersion(updateRepairOrderPo);
        repairOrderResultDao.updateBatchByIdVersion(repairOrderResultPos);
        repairOrderItemDao.updateBatchByIdVersion(repairOrderItemPos);

        // 发送MQ消息通知
        String msgKey = StrUtil.format("{}:{}", ScmConstant.REPAIR_ORDER_PROCESS_COMPLETED, repairOrderNo);
        RepairOrderChangeDto repairOrderChangeDto = RepairOrderBuilder.buildRepairOrderChangeDto(repairOrderNo, msgKey);
        sendMqService.execSendMq(ReOrderComProHandler.class, repairOrderChangeDto);
    }

    /**
     * 处理未提交加工结果的返修单明细，补充加工结果并提交。
     *
     * @param repairOrderNo     返修单号。
     * @param completionDetails 包含返修单加工完成信息的请求列表。
     */
    public void handleUnSubmittedResults(String repairOrderNo,
                                         List<CompletionRequestDto.CompletionDetailRequestDto> completionDetails) {
        List<CompletionRequestDto.CompletionDetailRequestDto> notExistResultIdList = completionDetails.stream()
                .filter(completionDetail -> Objects.isNull(completionDetail.getRepairOrderResultId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(notExistResultIdList)) {
            return;
        }

        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = processMaterialReceiptDao.listByRepairOrderNo(
                repairOrderNo);
        List<Long> processMaterialReceiptIds = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> materialReceiptItems = processMaterialReceiptItemDao.getByMaterialReceiptIds(
                processMaterialReceiptIds);
        List<RepairDetailResultRequestDto> newRepairResults = RepairOrderBuilder.buildRepairDetailResultRequestDtoList(
                notExistResultIdList, materialReceiptItems);

        for (RepairDetailResultRequestDto newRepairResult : newRepairResults) {
            submitRepairDetailResult(newRepairResult);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_SUB_REPAIR_QC_RESULT, key = "#request.repairOrderNo", waitTime = 1,
            leaseTime = -1)
    public void submitInspectionResult(InspectionResultRequestDto request) {
        // 前置校验：获取请求中的返修单号 & 版本号
        String repairOrderNo = request.getRepairOrderNo();
        Integer reVersion = request.getVersion();

        // 通过返修单号和版本号从数据库中获取返修单信息 & 验证当前返修单的状态、期望的仓库编码
        RepairOrderPo repairOrderPo = ParamValidUtils.requireNotNull(
                repairOrderDao.getByRepairOrderNo(repairOrderNo, reVersion),
                "无法进行质检，因为所选返修单已被删除，请刷新页面并重新选择有效的返修单进行质检。");
        RepairOrderStatus currentStatus = repairOrderPo.getRepairOrderStatus();
        ParamValidUtils.requireEquals(RepairOrderStatus.WAITING_FOR_QUALITY_INSPECTION, currentStatus,
                "当前返修单状态不为待质检，请刷新后重试!");

        // 获取返修结果ID列表 & 验证返修结果是否存在
        List<InspectionResultRequestDto.InspectionDetailRequestDto> inspectionDetails = request.getInspectionDetails();
        List<Long> repairOrderResultIds = inspectionDetails.stream()
                .map(InspectionResultRequestDto.InspectionDetailRequestDto::getRepairOrderResultId)
                .collect(Collectors.toList());
        List<RepairOrderResultPo> repairOrderResultPos = ParamValidUtils.requireNotEmpty(
                repairOrderResultDao.listByIds(repairOrderResultIds),
                "无法进行质检，返修结果已被删除，请确认加工师傅已重新提交结果并刷新页面后再试。");

        // 获取返修明细ID列表 & 验证返修明细信息是否存在
        List<Long> repairOrderItemIds = ParamValidUtils.requireNotEmpty(repairOrderResultPos.stream()
                        .map(RepairOrderResultPo::getRepairOrderItemId)
                        .distinct()
                        .collect(Collectors.toList()),
                "返修结果商品信息缺失，请联系相关业务人员！");
        List<RepairOrderItemPo> repairOrderItemPos = ParamValidUtils.requireNotEmpty(
                repairOrderItemDao.listByIds(repairOrderItemIds), "无法进行质检，返修商品信息缺失，请刷新后重试");

        // 赋值返修结果
        updateRepairItemQcResults(inspectionDetails, repairOrderResultPos);

        // 筛选返修结果正品数大于 0 的数据
        List<RepairOrderResultPo> qcResultWithGoodList = repairOrderResultPos.stream()
                .filter(detail -> detail.getQcPassQuantity() > 0)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(qcResultWithGoodList)) {
            repairOrderPo.setRepairOrderStatus(RepairOrderStatus.COMPLETED);
        } else {
            String expectWarehouseCode = repairOrderPo.getExpectWarehouseCode();

            // 创建返修质检单
            RepairOrderCreateQcBo repairOrderCreateQcBo = RepairOrderBuilder.buildRepairOrderCreateQcBo(repairOrderNo,
                    expectWarehouseCode, repairOrderResultPos, repairOrderPo.getPlatform());
            AbstractQcOrderCreator<RepairOrderCreateQcBo, String> qcOrderCreator = new RepairOrderQcOrderCreator(
                    qcOrderDao, qcDetailDao, idGenerateService, plmRemoteService);
            qcOrderCreator.createQcOrder(repairOrderCreateQcBo);

            // 创建返修收货单
            List<RepairQcDetailCreateBo> receiveOrderDetails
                    = RepairOrderBuilder.buildRepairDetailCreateBo(qcResultWithGoodList);
            String platform = ParamValidUtils.requireNotBlank(repairOrderPo.getPlatform(), "创建收货单失败！平台编码为空");
            ReceiveOrderCreateMqDto receiveOrderCreateMqDto
                    = RepairOrderBuilder.buildReceiveOrderCreateMqDto(repairOrderPo, platform, receiveOrderDetails, repairOrderItemPos);
            sendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);

            // 更新返修单发货数量 & 状态
            int deliveryNum = receiveOrderDetails.stream()
                    .mapToInt(RepairQcDetailCreateBo::getQcPassQuantity)
                    .sum();
            repairOrderPo.setDeliveryNum(deliveryNum);
            repairOrderPo.setRepairOrderStatus(RepairOrderStatus.WAITING_FOR_RECEIVING);
        }

        repairOrderDao.updateByIdVersion(repairOrderPo);
        repairOrderResultDao.updateBatchByIdVersion(repairOrderResultPos);

        updateDeliveryNum(repairOrderResultPos, repairOrderItemPos);
        repairOrderItemDao.updateBatchByIdVersion(repairOrderItemPos);
    }

    private void updateDeliveryNum(List<RepairOrderResultPo> repairOrderResultPos,
                                   List<RepairOrderItemPo> repairOrderItemPos) {
        // 使用流和收集器按 repairOrderItemId 分组并求和 qcPassQuantity
        Map<Long, Integer> qcPassQuantityByItemId = repairOrderResultPos.stream()
                .collect(Collectors.groupingBy(RepairOrderResultPo::getRepairOrderItemId,
                        Collectors.summingInt(RepairOrderResultPo::getQcPassQuantity)));
        for (RepairOrderItemPo repairOrderItemPo : repairOrderItemPos) {
            Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();
            repairOrderItemPo.setDeliveryNum(qcPassQuantityByItemId.getOrDefault(repairOrderItemId, 0));
        }
    }

    /**
     * 处理返修收货单状态变更的方法。
     *
     * @param message 收货单状态变更消息
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PROCESS_RECEIVE_STATUS_LOCK_PREFIX, key = "#message.scmBizNo", waitTime = 1,
            leaseTime = -1)
    public void handleRepairReceiptStatusChange(ReceiveOrderChangeMqDto message) {
        WmsEnum.ReceiveOrderState receiveOrderState = message.getReceiveOrderState();
        String repairOrderNo = message.getScmBizNo();
        String receiveOrderNo = message.getReceiveOrderNo();
        List<ReceiveOrderChangeMqDto.BatchCodeDetail> skuOnShelfInfoList = message.getBatchCodeDetailList();

        if (WmsEnum.ReceiveOrderState.WAIT_RECEIVE.equals(receiveOrderState)) {
            RepairOrderPo createReceiveOrderPo = repairOrderDao.getByRepairOrderNo(repairOrderNo);
            if (Objects.isNull(createReceiveOrderPo)) {
                throw new BizException("返修收货单创建异常，原因：{}，返修单号：{}", "返修单信息不存在！", repairOrderNo);
            }
            createReceiveOrderPo.setReceiveOrderNo(receiveOrderNo);
            repairOrderDao.updateByIdVersion(createReceiveOrderPo);
        }

        if (WmsEnum.ReceiveOrderState.RECEIVED.equals(receiveOrderState)) {
            RepairOrderPo createReceiveOrderPo = repairOrderDao.getByRepairOrderNo(repairOrderNo);
            if (Objects.isNull(createReceiveOrderPo)) {
                throw new BizException("返修收货单创建异常，原因：{}，返修单号：{}", "返修单信息不存在！", repairOrderNo);
            }
            createReceiveOrderPo.setRepairOrderStatus(RepairOrderStatus.COMPLETED);
            repairOrderDao.updateByIdVersion(createReceiveOrderPo);

            // 异步计算返修单成本
            CalculateRepairOrderCostBo calculateCostBo = new CalculateRepairOrderCostBo();
            calculateCostBo.setRepairOrderNo(repairOrderNo);
            consistencyService.execAsyncTask(RepairCostCalHandler.class, calculateCostBo);
        }

        if (WmsEnum.ReceiveOrderState.ONSHELVESED.equals(receiveOrderState)) {
            RepairOrderPo inStorageRepairOrderPo = repairOrderDao.getByReceiveOrderNo(receiveOrderNo);
            if (Objects.isNull(inStorageRepairOrderPo)) {
                throw new BizException("返修收货单状态变更，原因：{}，收货单号：{}", "收货单无关联返修单信息！",
                        receiveOrderNo);
            }

            inStorageRepairOrderPo.setRepairOrderStatus(RepairOrderStatus.COMPLETED);
            repairOrderDao.updateByIdVersion(inStorageRepairOrderPo);

            String inStorageRepairOrderNo = inStorageRepairOrderPo.getRepairOrderNo();
            RepairOrderInStorageDto repairOrderInStorageDto = RepairOrderBuilder.buildRepairOrderInStorageDto(
                    inStorageRepairOrderNo, skuOnShelfInfoList);
            sendMqService.execSendMq(ReOrderInsHandler.class, repairOrderInStorageDto);

            // 异步计算返修单成本
            CalculateRepairOrderCostBo calculateCostBo = new CalculateRepairOrderCostBo();
            calculateCostBo.setRepairOrderNo(repairOrderNo);
            consistencyService.execAsyncTask(RepairCostCalHandler.class, calculateCostBo);
        }
    }

    /**
     * 处理返修原料出库单的取消操作
     *
     * @param deliveryOrderNos 出库单号列表
     * @throws BizException 处理异常时抛出业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleRepairDeliveryCancellation(List<String> deliveryOrderNos) {
        Set<String> uniqueDeliveryOrderNos = new HashSet<>(deliveryOrderNos);

        for (String deliveryNo : uniqueDeliveryOrderNos) {
            // 查询关联的原料出库单信息
            List<ProcessOrderMaterialPo> processOrderMaterialPos = ParamValidUtils.requireNotEmpty(
                    processOrderMaterialDao.listByDeliveryNo(deliveryNo),
                    "返修原料出库单取消异常，无原料出库单信息" + deliveryNo);
            // 获取关联的返修单号
            List<String> repairOrderNos = ParamValidUtils.requireNotEmpty(processOrderMaterialPos.stream()
                            .distinct()
                            .map(ProcessOrderMaterialPo::getRepairOrderNo)
                            .collect(Collectors.toList()),
                    "返修原料出库单取消异常，返修单号信息丢失！出库单号" + deliveryNo);
            for (String repairOrderNo : repairOrderNos) {
                RepairOrderPo repairOrderPo = ParamValidUtils.requireNotNull(
                        repairOrderDao.getByRepairOrderNo(repairOrderNo),
                        "返修原料出库单取消推送异常,无返修单信息!" + repairOrderNo);
                repairOrderPo.setRepairOrderStatus(RepairOrderStatus.CANCELED);
                repairOrderDao.updateById(repairOrderPo);

                // 构建返修单状态变更DTO & 执行取消处理的消息推送
                String msgKey = StrUtil.format("{}:{}", ScmConstant.REPAIR_ORDER_CANCEL, repairOrderNo);
                RepairOrderChangeDto repairOrderChangeDto = RepairOrderBuilder.buildRepairOrderChangeDto(repairOrderNo,
                        msgKey);
                sendMqService.execSendMq(ReOrderCancelHandler.class, repairOrderChangeDto);
            }
        }
    }


    /**
     * 返修单列表查询
     *
     * @param dto:
     * @return PageInfo<RepairOrderSearchVo>
     * @author ChenWenLong
     * @date 2024/1/8 11:36
     */
    public CommonPageResult.PageInfo<RepairOrderSearchVo> searchRepairOrder(RepairOrderSearchDto dto) {
        CommonPageResult.PageInfo<RepairOrderSearchVo> pageResult = repairOrderDao.searchRepairOrder(
                PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<RepairOrderSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }
        // 查询详情列表
        List<String> repairOrderNoList = records.stream()
                .map(RepairOrderSearchVo::getRepairOrderNo)
                .collect(Collectors.toList());
        final Map<String, List<RepairOrderItemPo>> repairOrderItemPoMap = repairOrderItemDao.getMapByRepairOrderNoList(
                repairOrderNoList);

        // 质检单
        List<QcOrderPo> qcOrderPoList = qcOrderDao.getListByRepairOrderNoList(repairOrderNoList);
        final Map<String, QcOrderPo> qcOrderPoMap = qcOrderPoList.stream()
                .collect(Collectors.toMap(QcOrderPo::getRepairOrderNo, Function.identity(), (item1, item2) -> item1));

        // 收货单号
        List<String> receiveOrderNoList = records.stream()
                .map(RepairOrderSearchVo::getReceiveOrderNo)
                .collect(Collectors.toList());

        // 原料签收
        List<ProcessMaterialReceiptPo> processMaterialReceiptPoList
                = processMaterialReceiptDao.getListByRepairOrderNoList(repairOrderNoList);
        Map<String, List<ProcessMaterialReceiptPo>> processMaterialReceiptPoMap = processMaterialReceiptPoList.stream()
                .collect(Collectors.groupingBy(ProcessMaterialReceiptPo::getRepairOrderNo));
        List<Long> processMaterialReceiptIdList = processMaterialReceiptPoList.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIdList);

        // 入库信息
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(receiveOrderNoList);
        List<ReceiveOrderForScmVo> receiveOrderForScmVoList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);

        // 返修结果
        List<RepairOrderResultPo> repairOrderResultPoList = repairOrderResultDao.getListByRepairOrderNoList(
                repairOrderNoList);

        // 获取仓库名称
        List<String> warehouseCodeList = records.stream()
                .map(RepairOrderSearchVo::getExpectWarehouseCode)
                .collect(Collectors.toList());
        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(warehouseCodeList);
        final Map<String, WarehouseVo> warehouseVoMap = warehouseVoList.stream()
                .collect(Collectors.toMap(WarehouseVo::getWarehouseCode, Function.identity()));

        pageResult.setRecords(RepairOrderConverter.poListConvertVoList(records, repairOrderItemPoMap, qcOrderPoMap,
                processMaterialReceiptPoMap,
                processMaterialReceiptItemPoList,
                receiveOrderForScmVoList,
                repairOrderResultPoList, warehouseVoMap));
        return pageResult;
    }


    /**
     * 返修单详情
     *
     * @param dto:
     * @return RepairOrderDetailVo
     * @author ChenWenLong
     * @date 2024/1/8 14:19
     */
    public RepairOrderDetailVo repairOrderDetail(RepairOrderNoDto dto) {
        RepairOrderPo repairOrderPo = repairOrderDao.getByRepairOrderNo(dto.getRepairOrderNo());
        if (repairOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        //详情
        final List<RepairOrderItemPo> repairOrderItemPoList = repairOrderItemDao.getListByRepairOrderNo(
                repairOrderPo.getRepairOrderNo());

        //结果
        final List<RepairOrderResultPo> repairOrderResultPoList = repairOrderResultDao.getListByRepairOrderNo(
                repairOrderPo.getRepairOrderNo());

        // 质检单
        QcOrderPo qcOrderPo = qcOrderDao.getOneByRepairOrderNo(repairOrderPo.getRepairOrderNo());

        // 入库信息
        List<ReceiveOrderForScmVo> receiveOrderForScmVoList = new ArrayList<>();
        if (StringUtils.isNotBlank(repairOrderPo.getReceiveOrderNo())) {
            ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
            receiveOrderGetDto.setReceiveOrderNoList(List.of(repairOrderPo.getReceiveOrderNo()));
            receiveOrderForScmVoList = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto);
        }


        // 原料收货
        List<ProcessMaterialReceiptPo> processMaterialReceiptPoList = processMaterialReceiptDao.getListByRepairOrderNo(
                repairOrderPo.getRepairOrderNo());
        List<Long> processMaterialReceiptIdList = processMaterialReceiptPoList.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPoList
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIdList);

        List<ProcessMaterialBackPo> processMaterialBackPos = processMaterialBackDao.listByRepairOrderNo(
                repairOrderPo.getRepairOrderNo());
        List<Long> processMaterialBackIds = processMaterialBackPos.stream()
                .map(ProcessMaterialBackPo::getProcessMaterialBackId)
                .collect(Collectors.toList());
        List<ProcessMaterialBackItemPo> materialBackItemPoList
                = processMaterialBackItemDao.listByProcessMaterialBackIds(processMaterialBackIds);

        // 原料
        List<ProcessOrderMaterialPo> processOrderMaterialPoList = processOrderMaterialDao.getListByRepairOrderNo(
                repairOrderPo.getRepairOrderNo());

        List<String> skuList = processMaterialReceiptItemPoList.stream()
                .map(ProcessMaterialReceiptItemPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        skuList.addAll(processOrderMaterialPoList.stream()
                .map(ProcessOrderMaterialPo::getSku)
                .distinct()
                .collect(Collectors.toList()));

        // 获取产品名称
        Map<String, String> skuEncodeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(skuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        }

        // 获取返修完成图片
        List<Long> repairOrderResultIdList = repairOrderResultPoList.stream()
                .map(RepairOrderResultPo::getRepairOrderResultId)
                .collect(Collectors.toList());
        Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.REPAIR_RESULT,
                repairOrderResultIdList);

        // 获取仓库名称
        final List<WarehouseVo> warehouseVoList = wmsRemoteService.getWarehouseByCode(
                List.of(repairOrderPo.getExpectWarehouseCode()));
        final Map<String, WarehouseVo> warehouseVoMap = warehouseVoList.stream()
                .collect(Collectors.toMap(WarehouseVo::getWarehouseCode, Function.identity()));

        return RepairOrderConverter.poConvertVo(repairOrderPo, repairOrderItemPoList, receiveOrderForScmVoList,
                processMaterialReceiptPoList, processMaterialReceiptItemPoList,
                materialBackItemPoList,
                skuEncodeMap, qcOrderPo, repairOrderResultPoList, fileCodeMap,
                warehouseVoMap, processOrderMaterialPoList);
    }

    /**
     * 导出返修单
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/8 14:38
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportRepairOrder(RepairOrderSearchDto dto) {
        Integer exportTotals = repairOrderDao.getExportRepairOrderTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_REPAIR_ORDER_EXPORT.getCode(),
                        dto));
    }

    /**
     * 导出返修记录
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/8 14:38
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportRepairOrderResult(RepairOrderSearchDto dto) {
        Integer exportTotals = repairOrderDao.getExportRepairOrderResultTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class,
                new FileOperateMessageDto<>(GlobalContext.getUserKey(),
                        GlobalContext.getUsername(),
                        FileOperateBizType.SCM_REPAIR_ORDER_RESULT_EXPORT.getCode(),
                        dto));
    }

    /**
     * 分配返修单，根据提供的返修商品信息和返修单原料信息。
     *
     * @param skuMap      包含返修商品信息的Map，键为返修单编号，值为返修商品信息列表
     * @param materialMap 包含返修单原料的Map，键为返修单编号，值为返修单原料列表
     * @return 分配结果的列表，包含多个 RepairAllocationResultBo 对象
     */
    public List<RepairAllocationResultBo> allocateRepairOrders(Map<Integer, List<RepairOrderItemBo>> skuMap,
                                                               Map<Integer, List<RepairMaterialBo>> materialMap) {
        // 存储分配结果的列表
        List<RepairAllocationResultBo> allocationResults = new ArrayList<>();
        //分配编号
        AtomicInteger no = new AtomicInteger(1);

        // 迭代器遍历返修商品信息的Map
        Iterator<Map.Entry<Integer, List<RepairOrderItemBo>>> repairSkuIterator = skuMap.entrySet()
                .iterator();

        // 使用队列保存返修单原料的Map的条目，以便后续处理
        Queue<Map.Entry<Integer, List<RepairMaterialBo>>> materialQueue = new LinkedList<>(materialMap.entrySet());

        // 遍历返修商品信息的Map
        while (repairSkuIterator.hasNext()) {
            // 创建分配结果对象
            RepairAllocationResultBo allocationResult = new RepairAllocationResultBo();
            allocationResult.setNo(no.getAndIncrement());

            // 获取返修商品信息的Map的条目
            Map.Entry<Integer, List<RepairOrderItemBo>> entry = repairSkuIterator.next();
            // 创建新的对象避免修改原对象
            allocationResult.setRepairOrderItems(new ArrayList<>(entry.getValue()));

            // 如果没有下一个返修商品信息，处理剩余的返修单原料
            if (!repairSkuIterator.hasNext()) {
                if (!materialQueue.isEmpty()) {
                    // 从队列中取出并合并相同键的条目的值，设置到分配结果中
                    Map.Entry<Integer, List<RepairMaterialBo>> materialPoll = pollAll(materialQueue);
                    // 创建新的对象避免修改原对象
                    allocationResult.setRepairMaterials(new ArrayList<>(materialPoll.getValue()));
                }
            } else {
                // 处理返修单原料，直到返修单原料队列为空
                if (!materialQueue.isEmpty()) {
                    // 直接取出返修单原料的条目的值，设置到分配结果中
                    Map.Entry<Integer, List<RepairMaterialBo>> materialPoll = materialQueue.poll();
                    // 创建新的对象避免修改原对象
                    allocationResult.setRepairMaterials(new ArrayList<>(materialPoll.getValue()));
                }
            }

            // 将分配结果对象添加到列表中
            allocationResults.add(allocationResult);
        }

        // 返回分配结果列表
        return allocationResults;
    }


    private void validatePushResults(List<SplitPlanOrderResultBo> splitResults) {
        if (CollectionUtils.isEmpty(splitResults)) {
            throw new BizException("计划单创建返修单回推PLM失败！原因：{}", "返修单创建结果为空");
        }

        splitResults.forEach(splitPlanOrderResultBo -> {
            // 获取返修单信息，确保不为空
            if (Objects.isNull(splitPlanOrderResultBo.getRepairOrderPo())) {
                throw new BizException("计划单创建返修单回推PLM失败！原因：返修单信息列表为空");
            }

            if (CollectionUtils.isEmpty(splitPlanOrderResultBo.getRepairOrderItemPos())) {
                throw new BizException("计划单创建返修单回推PLM失败！原因：返修单明细信息列表为空");
            }

            if (CollectionUtils.isEmpty(splitPlanOrderResultBo.getProcessOrderMaterialPos())) {
                throw new BizException("计划单创建返修单回推PLM失败！原因：返修单原料信息列表为空");
            }
        });
    }


    private void validateCreateBatchCodeParam(List<SplitPlanOrderResultBo> splitResults) {
        if (CollectionUtils.isEmpty(splitResults)) {
            throw new BizException("创建批次码失败！原因：{}", "返修单创建结果为空");
        }

        splitResults.forEach(splitPlanOrderResultBo -> {
            // 获取返修单信息，确保不为空
            if (Objects.isNull(splitPlanOrderResultBo.getRepairOrderPo())) {
                throw new BizException("创建批次码失败！原因：返修单信息列表为空");
            }

            if (CollectionUtils.isEmpty(splitPlanOrderResultBo.getRepairOrderItemPos())) {
                throw new BizException("创建批次码失败！原因：返修单明细信息列表为空");
            }
        });
    }

    /**
     * 通过返修单号查询质检信息
     *
     * @param dto:
     * @return RepairOrderProcessResultVo
     * @author ChenWenLong
     * @date 2024/1/9 17:12
     */
    public RepairOrderProcessResultVo getRepairOrderProcessResultByRepairNo(GetRepairOrderProcessResultInfoDto dto) {
        RepairOrderProcessResultVo repairOrderProcessResultVo = new RepairOrderProcessResultVo();
        RepairOrderPo repairOrderPo = repairOrderDao.getByRepairOrderNo(dto.getRepairOrderNo());
        if (repairOrderPo == null) {
            return repairOrderProcessResultVo;
        }
        RepairOrderProcessResultVo.RepairOrderProcessResultInfo repairOrderProcessResultInfo
                = new RepairOrderProcessResultVo.RepairOrderProcessResultInfo();
        repairOrderProcessResultInfo.setRepairOrderNo(repairOrderPo.getRepairOrderNo());
        repairOrderProcessResultVo.setRepairOrderProcessResult(repairOrderProcessResultInfo);
        List<RepairOrderResultPo> repairOrderResultPoList = repairOrderResultDao.getListByRepairOrderNo(
                dto.getRepairOrderNo());

        if (CollectionUtils.isEmpty(repairOrderResultPoList)) {
            return repairOrderProcessResultVo;
        }

        // 获取返修完成图片
        List<Long> repairOrderResultIdList = repairOrderResultPoList.stream()
                .map(RepairOrderResultPo::getRepairOrderResultId)
                .collect(Collectors.toList());
        Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.REPAIR_RESULT,
                repairOrderResultIdList);

        List<RepairOrderProcessResultVo.FinishedProductInfo> finishedProductList = new ArrayList<>();
        Map<String, List<RepairOrderResultPo>> repairOrderResultPoGroupingSku = repairOrderResultPoList.stream()
                .collect(Collectors.groupingBy(RepairOrderResultPo::getSku));

        repairOrderResultPoGroupingSku.forEach((String sku, List<RepairOrderResultPo> poList) -> {
            RepairOrderProcessResultVo.FinishedProductInfo finishedProductInfo
                    = new RepairOrderProcessResultVo.FinishedProductInfo();
            finishedProductInfo.setSku(sku);
            int completedQuantity = poList.stream()
                    .mapToInt(RepairOrderResultPo::getCompletedQuantity)
                    .sum();
            finishedProductInfo.setQuantity(completedQuantity);

            // 获取图片
            List<String> fileCodeAllList = new ArrayList<>();
            for (RepairOrderResultPo repairOrderResultPo : poList) {
                List<String> fileCodeList = fileCodeMap.get(repairOrderResultPo.getRepairOrderResultId());
                if (CollectionUtils.isNotEmpty(fileCodeList)) {
                    fileCodeAllList.addAll(fileCodeList);
                }
            }
            finishedProductInfo.setFileCodeList(fileCodeAllList);

            finishedProductList.add(finishedProductInfo);
        });
        repairOrderProcessResultInfo.setFinishedProductList(finishedProductList);

        return repairOrderProcessResultVo;
    }

    /**
     * 通过返修单号查询状态
     *
     * @param dto:
     * @return RepairOrderStatusVo
     * @author ChenWenLong
     * @date 2024/1/9 17:12
     */
    public RepairOrderStatusVo getRepairOrderStatusByPlanNos(GetRepairOrderStatusBatchDto dto) {
        RepairOrderStatusVo repairOrderStatusVo = new RepairOrderStatusVo();
        List<RepairOrderPo> repairOrderPoList = repairOrderDao.getListByPlanNoList(
                new ArrayList<>(dto.getPlanOrderNos()));
        if (CollectionUtils.isEmpty(repairOrderPoList)) {
            return repairOrderStatusVo;
        }

        List<RepairOrderStatusVo.RepairOrderStatusInfo> repairOrderStatusInfos = repairOrderPoList.stream()
                .map(po -> {
                    RepairOrderStatusVo.RepairOrderStatusInfo repairOrderStatusInfo
                            = new RepairOrderStatusVo.RepairOrderStatusInfo();
                    repairOrderStatusInfo.setRepairOrderNo(po.getRepairOrderNo());
                    repairOrderStatusInfo.setRepairOrderStatus(po.getRepairOrderStatus());
                    repairOrderStatusInfo.setPlanOrderNo(po.getPlanNo());
                    return repairOrderStatusInfo;
                })
                .collect(Collectors.toList());

        repairOrderStatusVo.setRepairOrderStatusInfos(repairOrderStatusInfos);
        return repairOrderStatusVo;
    }


    /**
     * 返修单导出数量
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/1/10 11:50
     */
    public Integer getExportTotals(RepairOrderSearchDto dto) {
        Integer exportTotals = repairOrderDao.getExportRepairOrderTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }


    /**
     * 返修单结果导出数量
     *
     * @param dto:
     * @return Integer
     * @author ChenWenLong
     * @date 2024/1/10 11:50
     */
    public Integer getResultExportTotals(RepairOrderSearchDto dto) {
        Integer exportTotals = repairOrderDao.getExportRepairOrderResultTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    /**
     * 返修单导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RepairOrderExportVo>>
     * @author ChenWenLong
     * @date 2024/1/10 11:50
     */
    public CommonResult<ExportationListResultBo<RepairOrderExportVo>> getExportList(RepairOrderSearchDto dto) {
        ExportationListResultBo<RepairOrderExportVo> resultBo = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<RepairOrderSearchVo> exportList = this.searchRepairOrder(dto);
        List<RepairOrderSearchVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        // 获取平台名称
        final List<String> platCodeList = records.stream()
                .map(RepairOrderSearchVo::getPlatform)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> platCodeNameMap = sdaRemoteService.getNameMapByCodeList(platCodeList);

        List<RepairOrderExportVo> voList = records.stream()
                .map(searchVo -> {
                    RepairOrderExportVo repairOrderExportVo = new RepairOrderExportVo();
                    repairOrderExportVo.setRepairOrderNo(searchVo.getRepairOrderNo());
                    repairOrderExportVo.setPlanNo(searchVo.getPlanNo());
                    repairOrderExportVo.setSalePrice(searchVo.getSalePrice());
                    repairOrderExportVo.setExpectProcessNum(searchVo.getExpectProcessNum());
                    repairOrderExportVo.setPlatform(searchVo.getPlatform());
                    repairOrderExportVo.setExpectWarehouseName(searchVo.getExpectWarehouseName());
                    repairOrderExportVo.setPlanCreateUsername(searchVo.getPlanCreateUsername());
                    repairOrderExportVo.setActProcessedCompleteCntTotal(searchVo.getActProcessedCompleteCntTotal());
                    repairOrderExportVo.setCompleteRepairUserName(searchVo.getCompleteRepairUserName());
                    repairOrderExportVo.setQcOrderNo(searchVo.getQcOrderNo());
                    repairOrderExportVo.setPassAmount(searchVo.getPassAmount());
                    repairOrderExportVo.setNotPassAmount(searchVo.getNotPassAmount());
                    repairOrderExportVo.setDeliveryNum(searchVo.getDeliveryNum());
                    repairOrderExportVo.setReceiptNum(searchVo.getReceiptNum());
                    repairOrderExportVo.setReceiveOrderNo(searchVo.getReceiveOrderNo());
                    repairOrderExportVo.setExpectCompleteProcessTime(
                            ScmTimeUtil.localDateTimeToStr(searchVo.getExpectCompleteProcessTime(), TimeZoneId.CN,
                                    DatePattern.NORM_DATETIME_PATTERN));
                    repairOrderExportVo.setPlanCreateTime(
                            ScmTimeUtil.localDateTimeToStr(searchVo.getPlanCreateTime(), TimeZoneId.CN,
                                    DatePattern.NORM_DATETIME_PATTERN));
                    repairOrderExportVo.setConfirmCompleteTime(
                            ScmTimeUtil.localDateTimeToStr(searchVo.getConfirmCompleteTime(), TimeZoneId.CN,
                                    DatePattern.NORM_DATETIME_PATTERN));
                    repairOrderExportVo.setPlatform(platCodeNameMap.get(searchVo.getPlatform()));
                    List<RepairOrderItemDetailVo> repairOrderItemDetailList = searchVo.getRepairOrderItemDetailList();
                    if (CollectionUtils.isNotEmpty(repairOrderItemDetailList)) {
                        String spu = repairOrderItemDetailList.stream()
                                .map(RepairOrderItemDetailVo::getSpu)
                                .distinct()
                                .collect(Collectors.joining(","));
                        repairOrderExportVo.setSpu(spu);
                    }
                    return repairOrderExportVo;
                })
                .collect(Collectors.toList());

        resultBo.setRowDataList(voList);

        return CommonResult.success(resultBo);
    }

    /**
     * 返修单结果导出列表
     *
     * @param dto:
     * @return CommonResult<ExportationListResultBo < RepairOrderResultExportVo>>
     * @author ChenWenLong
     * @date 2024/1/10 14:18
     */
    public CommonResult<ExportationListResultBo<RepairOrderResultExportVo>> getResultExportList(RepairOrderSearchDto dto) {
        ExportationListResultBo<RepairOrderResultExportVo> resultBo = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<RepairOrderResultExportVo> exportList = repairOrderDao.getExportRepairOrderResultList(
                PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<RepairOrderResultExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }
        for (RepairOrderResultExportVo record : records) {
            record.setRepairTimeStr(ScmTimeUtil.localDateTimeToStr(record.getRepairTime(), TimeZoneId.CN,
                    DatePattern.NORM_DATETIME_PATTERN));
        }
        resultBo.setRowDataList(records);
        return CommonResult.success(resultBo);
    }

    /**
     * 批量更新返修单加工结果的详细信息，包括完成数量、删除标志和图片信息。
     *
     * @param completionDetails    返修单加工完成的详细信息列表。
     * @param repairOrderResultPos 返修单的加工结果信息列表。
     */
    public void batchUpdateRepairOrderResults(List<CompletionRequestDto.CompletionDetailRequestDto> completionDetails,
                                              List<RepairOrderResultPo> repairOrderResultPos) {

        for (RepairOrderResultPo repairOrderResultPo : repairOrderResultPos) {
            Long repairOrderResultId = repairOrderResultPo.getRepairOrderResultId();
            CompletionRequestDto.CompletionDetailRequestDto matchCompletionResult = completionDetails.stream()
                    .filter(completionDetail -> Objects.equals(repairOrderResultId,
                            completionDetail.getRepairOrderResultId()))
                    .findFirst()
                    .orElse(null);

            if (Objects.nonNull(matchCompletionResult)) {
                Integer completedQuantity = matchCompletionResult.getCompletedQuantity();
                repairOrderResultPo.setMaterialUsageQuantity(completedQuantity);
                repairOrderResultPo.setCompletedQuantity(completedQuantity);

                String materialBatchCode = matchCompletionResult.getMaterialBatchCode();
                repairOrderResultPo.setMaterialBatchCode(materialBatchCode);

                // 删除图片
                scmImageDao.removeByTypeAndId(ImageBizType.REPAIR_RESULT, repairOrderResultId);
                List<ScmImagePo> scmImagePos = RepairOrderBuilder.buildScmImagePoList(matchCompletionResult,
                        repairOrderResultPo);

                if (CollectionUtils.isNotEmpty(scmImagePos)) {
                    scmImageDao.insertBatch(scmImagePos);
                }
            }

            if (REPAIR_NOT_PASS == repairOrderResultPo.getCompletedQuantity()) {
                repairOrderResultPo.setDelTimestamp(DateUtil.current());
            }
        }
    }

    /**
     * 批量更新返修单明细的已加工数量和实际加工废品数量。
     *
     * @param repairOrderItemPos   返修单明细信息列表。
     * @param repairOrderResultPos 返修单的加工结果信息列表。
     */
    private void batchUpdateRepairOrderItems(List<RepairOrderItemPo> repairOrderItemPos,
                                             List<RepairOrderResultPo> repairOrderResultPos) {
        for (RepairOrderItemPo repairOrderItemPo : repairOrderItemPos) {
            Integer expectProcessNum = repairOrderItemPo.getExpectProcessNum();
            Long repairOrderItemId = repairOrderItemPo.getRepairOrderItemId();

            List<RepairOrderResultPo> mathRepairResults = repairOrderResultPos.stream()
                    .filter(repairOrderResultPo -> Objects.equals(repairOrderItemId,
                            repairOrderResultPo.getRepairOrderItemId()))
                    .collect(Collectors.toList());

            int sumCompletedQuantity = mathRepairResults.stream()
                    .mapToInt(RepairOrderResultPo::getCompletedQuantity)
                    .sum();

            ParamValidUtils.requireLessThanOrEqual(sumCompletedQuantity, expectProcessNum,
                    StrUtil.format("{}完成数量大于预计加工数！请更正后重试",
                            repairOrderItemPo.getBatchCode()));

            repairOrderItemPo.setActProcessedCompleteCnt(sumCompletedQuantity);
            repairOrderItemPo.setActProcessScrapCnt(expectProcessNum - sumCompletedQuantity);
        }
    }

    public void updateRepairItemQcResults(List<InspectionResultRequestDto.InspectionDetailRequestDto> inspectionDetails,
                                          List<RepairOrderResultPo> repairOrderResultPos) {
        for (RepairOrderResultPo repairOrderResultPo : repairOrderResultPos) {
            String sku = repairOrderResultPo.getSku();
            Integer completedQuantity = repairOrderResultPo.getCompletedQuantity();
            Long repairOrderResultId = repairOrderResultPo.getRepairOrderResultId();
            Integer curVersion = repairOrderResultPo.getVersion();

            InspectionResultRequestDto.InspectionDetailRequestDto matchQcResult = ParamValidUtils.requireNotNull(
                    inspectionDetails.stream()
                            .filter(inspectionDetail -> Objects.equals(repairOrderResultId,
                                    inspectionDetail.getRepairOrderResultId()))
                            .findFirst()
                            .orElse(null), "返修单结果不存在，请刷新后重试!");
            Integer goodQuantity = matchQcResult.getGoodQuantity();
            Integer defectiveQuantity = matchQcResult.getDefectiveQuantity();
            Integer tarVersion = matchQcResult.getVersion();

            ParamValidUtils.requireLessThanOrEqual(goodQuantity + defectiveQuantity, completedQuantity, StrUtil.format(
                    "请注意：您填写{}的正品数量和次品数量总和超过了返修完成数量。请仔细核对并确保输入的数值正确!", sku));
            ParamValidUtils.requireEqual(goodQuantity + defectiveQuantity, completedQuantity,
                    sku + "未质检完成，请质检!");
            ParamValidUtils.requireEqual(tarVersion, curVersion, sku + "返修结果信息已变更，请刷新后重试！");

            // 更新返修结果质检正品数与次品数
            repairOrderResultPo.setQcPassQuantity(goodQuantity);
            repairOrderResultPo.setQcFailQuantity(defectiveQuantity);
        }
    }

    /**
     * 获取返修单打印信息的方法。
     *
     * @param requestDto 返修单打印信息的请求DTO，包含订单信息列表。
     * @return 返修单打印信息结果列表，包含返修单号字段。
     */
    public List<RepairOrderPrintResultVo> getRepairOrderPrintInfo(RepairOrderPrintRequestDto requestDto) {
        // 根据返修单号查询加工原料表，根据返修单号去重得到返修单号列表
        List<String> deliveryNos = requestDto.getDeliveryNos();
        List<ProcessOrderMaterialPo> repairMaterialPos = processOrderMaterialDao.listByDeliveryNos(deliveryNos);

        List<String> repairOrderNos = repairMaterialPos.stream()
                .distinct()
                .map(ProcessOrderMaterialPo::getRepairOrderNo)
                .collect(Collectors.toList());
        List<RepairOrderPo> repairOrderPos = repairOrderDao.listByRepairOrderNos(repairOrderNos);
        List<RepairOrderItemPo> repairOrderItemPos = repairOrderItemDao.listByRepairOrderNos(repairOrderNos);

        // 返修单出库信息
        List<ProcessDeliveryOrderVo> repairDeliveryVos = wmsRemoteService.getProcessDeliveryOrderBatch(repairOrderNos,
                WmsEnum.DeliveryType.PROCESS);

        // 仓库信息
        List<String> queryWarehouseCode = Stream.of(repairOrderPos.stream()
                        .map(RepairOrderPo::getExpectWarehouseCode)
                        .collect(Collectors.toList()), repairMaterialPos.stream()
                        .map(ProcessOrderMaterialPo::getWarehouseCode)
                        .collect(Collectors.toList()))
                .distinct()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<WarehouseVo> relateWarehouses = wmsRemoteService.getWarehouseByCode(queryWarehouseCode);

        // 产品名称列表
        List<String> queryEncodeSkus = Stream.of(repairMaterialPos.stream()
                        .map(ProcessOrderMaterialPo::getSku)
                        .collect(Collectors.toList()), repairOrderItemPos.stream()
                        .map(RepairOrderItemPo::getSku)
                        .collect(Collectors.toList()))
                .distinct()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        List<PlmSkuVo> plmSkuVos = plmRemoteService.getSkuEncodeBySku(queryEncodeSkus);

        return deliveryNos.stream()
                .map(deliveryNo -> {
                            ProcessOrderMaterialPo matchOneOfRepairMaterial = repairMaterialPos.stream()
                                    .filter(repairMaterialPo -> Objects.equals(deliveryNo,
                                            repairMaterialPo.getDeliveryNo()))
                                    .findFirst()
                                    .orElse(null);
                            String repairOrderNo = Objects.nonNull(
                                    matchOneOfRepairMaterial) ? matchOneOfRepairMaterial.getRepairOrderNo() : "";

                            RepairOrderPo matchRepairOrder = repairOrderPos.stream()
                                    .filter(repairOrderPo -> Objects.equals(repairOrderNo,
                                            repairOrderPo.getRepairOrderNo()))
                                    .findFirst()
                                    .orElse(null);

                            ProcessDeliveryOrderVo matchRepairDelivery = repairDeliveryVos.stream()
                                    .filter(repairDeliveryVo -> Objects.equals(deliveryNo,
                                            repairDeliveryVo.getDeliveryOrderNo()))
                                    .findFirst()
                                    .orElse(null);

                            // 构建返修单信息
                            RepairOrderPrintResultVo repairOrderPrintResultVo
                                    = RepairOrderBuilder.buildRepairOrderPrintResultVo(deliveryNo, matchRepairOrder,
                                    matchOneOfRepairMaterial,
                                    matchRepairDelivery,
                                    relateWarehouses);

                            // 构建返修产品列表信息
                            List<RepairOrderItemPo> matchRepairOrderItemPos = repairOrderItemPos.stream()
                                    .filter(repairOrderItemPo -> Objects.equals(repairOrderNo,
                                            repairOrderItemPo.getRepairOrderNo()))
                                    .collect(Collectors.toList());
                            List<RepairOrderPrintResultVo.RepairProductInfoVo> repairProductInfoVos
                                    = RepairOrderBuilder.buildRepairProductInfoVo(matchRepairOrderItemPos, plmSkuVos);
                            repairOrderPrintResultVo.setRepairProductList(repairProductInfoVos);

                            // 构建原料配比信息
                            List<ProcessOrderMaterialPo> matchRepairMaterial = repairMaterialPos.stream()
                                    .filter(repairMaterialPo -> Objects.equals(repairOrderNo,
                                            repairMaterialPo.getRepairOrderNo()))
                                    .collect(Collectors.toList());
                            List<RepairOrderPrintResultVo.UnitMaterialRatioVo> unitMaterialRatioVos
                                    = RepairOrderBuilder.buildUnitMaterialRatioVos(matchRepairMaterial, plmSkuVos);
                            repairOrderPrintResultVo.setUnitMaterialRatioList(unitMaterialRatioVos);

                            // 构建出库明细列表信息
                            List<RepairOrderPrintResultVo.OutboundDetailInfoVo> outboundDetailInfoVos
                                    = RepairOrderBuilder.buildOutboundDetailInfoVos(matchRepairMaterial, plmSkuVos);
                            repairOrderPrintResultVo.setOutboundDetailList(outboundDetailInfoVos);
                            return repairOrderPrintResultVo;
                        }

                )
                .collect(Collectors.toList());

    }

    /**
     * 打印批次码
     *
     * @param dto:
     * @return PageInfo<RepairOrderPrintBatchCodeVo>
     * @author ChenWenLong
     * @date 2024/1/19 11:11
     */
    public CommonPageResult.PageInfo<RepairOrderPrintBatchCodeVo> getRepairOrderPrintBatchCode(RepairOrderNoPageDto dto) {
        CommonPageResult.PageInfo<RepairOrderPrintBatchCodeVo> pageResult = repairOrderDao.getRepairOrderPrintBatchCode(
                PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<RepairOrderPrintBatchCodeVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        // 查询详情列表
        List<String> repairOrderNoList = records.stream()
                .map(RepairOrderPrintBatchCodeVo::getRepairOrderNo)
                .collect(Collectors.toList());
        final Map<String, List<RepairOrderItemPo>> repairOrderItemPoMap = repairOrderItemDao.getMapByRepairOrderNoList(
                repairOrderNoList);
        for (RepairOrderPrintBatchCodeVo record : records) {
            List<RepairOrderItemPo> repairOrderItemPoList = repairOrderItemPoMap.get(record.getRepairOrderNo());
            List<RepairOrderPrintBatchCodeVo.RepairOrderPrintBatchCodeItemVo> repairOrderPrintBatchCodeItemList
                    = Optional.ofNullable(repairOrderItemPoList)
                    .orElse(new ArrayList<>())
                    .stream()
                    .flatMap(itemPo -> {
                        List<RepairOrderPrintBatchCodeVo.RepairOrderPrintBatchCodeItemVo> items = Lists.newArrayList();
                        RepairOrderPrintBatchCodeVo.RepairOrderPrintBatchCodeItemVo repairOrderPrintBatchCodeItemVo
                                = new RepairOrderPrintBatchCodeVo.RepairOrderPrintBatchCodeItemVo();
                        repairOrderPrintBatchCodeItemVo.setSku(itemPo.getSku());
                        repairOrderPrintBatchCodeItemVo.setBatchCode(itemPo.getBatchCode());

                        for (int i = 0; i < itemPo.getExpectProcessNum(); i++) {
                            items.add(repairOrderPrintBatchCodeItemVo);
                        }
                        return items.stream();
                    })
                    .collect(Collectors.toList());
            record.setRepairOrderPrintBatchCodeItemList(repairOrderPrintBatchCodeItemList);
        }

        return pageResult;
    }

    /**
     * 根据返修单号获取返修单原料绑定信息列表
     *
     * @param request 包含返修单号列表的请求DTO
     * @return 返修单原料绑定信息列表
     */
    public List<RepairOrderPrintMaterialBatchCodeVo> getRepairMaterialBindingInfo(RepairMaterialBatchCodeRequestDto request) {
        List<String> repairOrderNoList = request.getRepairOrderNoList();

        // 查询返修单的原料收货记录
        List<ProcessMaterialReceiptPo> processMaterialReceiptPos = processMaterialReceiptDao.listByRepairOrderNos(
                repairOrderNoList);

        // 提取工序物料收货记录的ID列表 & 根据原料收货记录ID列表查询原料收货明细
        List<Long> processMaterialReceiptIds = processMaterialReceiptPos.stream()
                .map(ProcessMaterialReceiptPo::getProcessMaterialReceiptId)
                .collect(Collectors.toList());
        List<ProcessMaterialReceiptItemPo> processMaterialReceiptItemPos
                = processMaterialReceiptItemDao.getByMaterialReceiptIds(processMaterialReceiptIds);

        // 构建返修单原料绑定信息列表
        return processMaterialReceiptPos.stream()
                .map(processMaterialReceiptPo -> {
                    RepairOrderPrintMaterialBatchCodeVo repairOrderPrintMaterialBatchCodeVo
                            = new RepairOrderPrintMaterialBatchCodeVo();
                    repairOrderPrintMaterialBatchCodeVo.setRepairOrderNo(processMaterialReceiptPo.getRepairOrderNo());

                    // 过滤匹配的收货明细
                    List<ProcessMaterialReceiptItemPo> matchReceiptItems = processMaterialReceiptItemPos.stream()
                            .filter(processMaterialReceiptItemPo -> Objects.equals(
                                    processMaterialReceiptPo.getProcessMaterialReceiptId(),
                                    processMaterialReceiptItemPo.getProcessMaterialReceiptId()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(matchReceiptItems)) {
                        // 构建返修单原料绑定信息列表
                        List<RepairOrderPrintMaterialBatchCodeVo.MaterialBindingInfoVo> materialBindingInfoVos
                                = matchReceiptItems.stream()
                                .map(matchReceiptItem -> {
                                    RepairOrderPrintMaterialBatchCodeVo.MaterialBindingInfoVo materialBindingInfoVo
                                            = new RepairOrderPrintMaterialBatchCodeVo.MaterialBindingInfoVo();
                                    materialBindingInfoVo.setSku(matchReceiptItem.getSku());
                                    materialBindingInfoVo.setBatchCode(matchReceiptItem.getSkuBatchCode());
                                    return materialBindingInfoVo;
                                })
                                .collect(Collectors.toList());
                        repairOrderPrintMaterialBatchCodeVo.setMaterialBindingInfoVos(materialBindingInfoVos);
                    }
                    return repairOrderPrintMaterialBatchCodeVo;
                })
                .collect(Collectors.toList());
    }

    public RepairOrderReturnMaterialInfoVo getReturnMaterialInfo(RepairOrderReturnMaterialInfoRequestDto request) {
        String repairOrderNo = request.getRepairOrderNo();
        String returnWarehouseCode = environment.getProperty("repairOrder.returnWarehouseCode", String.class);
        String returnWarehouseName = StrUtil.isBlank(returnWarehouseCode) ? "" : wmsRemoteService.getWarehouseByCode(
                        Collections.singletonList(returnWarehouseCode))
                .stream()
                .filter(warehouseVo -> Objects.equals(returnWarehouseCode, warehouseVo.getWarehouseCode()))
                .findFirst()
                .map(WarehouseVo::getWarehouseName)
                .orElse("");

        List<RepairOrderBindingInfoBo> repairOrderBindingInfo = repairOrderBaseService.getRepairOrderBindingInfo(
                repairOrderNo);
        return RepairOrderConverter.toRepairOrderReturnMaterialInfoVo(repairOrderNo, returnWarehouseCode,
                returnWarehouseName, repairOrderBindingInfo);
    }

    /**
     * 提交原料归还操作。
     *
     * @param request 提交原料归还请求DTO，包含返修单号、归还仓库代码以及原料归还信息列表。
     * @throws Exception 如果发生异常，会触发事务回滚。
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_REPAIR_RETURN_MATERIAL, key = "#request.repairOrderNo", waitTime = 1,
            leaseTime = -1)
    public void submitReturnMaterial(SubmitReturnMaterialRequestDto request) {
        // 获取参数：返修单号 & 归还仓库代码 & 原料归还信息列表
        String repairOrderNo = request.getRepairOrderNo();
        String returnWarehouseCode = request.getReturnWarehouseCode();
        List<SubmitReturnMaterialRequestDto.MaterialReturnInfoVo> materialReturnInfoList
                = request.getMaterialReturnInfoList();

        // 前置校验：检查返修单是否存在，并且是否已收到原料
        RepairOrderPo repairOrderPo = ParamValidUtils.requireNotNull(repairOrderDao.getByRepairOrderNo(repairOrderNo),
                "无法执行原料归还操作。找不到对应的返修单，请确保提供的返修单是否存在。");
        ParamValidUtils.requireEquals(IsReceiveMaterial.TRUE, repairOrderPo.getIsReceiveMaterial(),
                "无法执行原料归还操作。当前返修单尚未收到任何原料。请确保在尝试归还操作之前已经完成原料的收货。");

        // 获取返修单绑定信息列表 & 校验原料归还数量是否合法
        List<RepairOrderBindingInfoBo> repairOrderBindingInfos = repairOrderBaseService.getRepairOrderBindingInfo(
                repairOrderNo);
        for (SubmitReturnMaterialRequestDto.MaterialReturnInfoVo materialReturnInfoVo : materialReturnInfoList) {
            String returnMaterialBatchCode = materialReturnInfoVo.getBatchCode();
            Integer curReturnQuantity = materialReturnInfoVo.getReturnQuantity();

            // 查找当前原料批次码对应的可归还数量 & 检查当前归还数量是否超过可归还数量
            Integer availableForReturn = repairOrderBindingInfos.stream()
                    .filter(repairOrderBindingInfo -> Objects.equals(returnMaterialBatchCode,
                            repairOrderBindingInfo.getBatchCode()))
                    .findFirst()
                    .map(RepairOrderBindingInfoBo::getAvailableForReturn)
                    .orElse(0);
            ParamValidUtils.requireLessThanOrEqual(curReturnQuantity, availableForReturn,
                    "无法完成原料归还操作。归还数量超过了当前可归还的原料数量。请检查并确保归还数量不超过可归还的最大数量。");
        }

        String msgKey = idGenerateService.getSnowflakeCode(repairOrderNo + "-");

        // 创建归还原料收货单
        String platform = ParamValidUtils.requireNotBlank(repairOrderPo.getPlatform(), "归还原料失败！平台编码为空");
        doCreateMaterialReturnReceiveOrder(msgKey, repairOrderNo, returnWarehouseCode, platform, materialReturnInfoList);

        // 记录原料信息和原料归还明细列表
        doRecordMaterialBackInfo(msgKey, repairOrderNo, materialReturnInfoList);
    }

    /**
     * 创建归还原料收货单
     *
     * @param repairOrderNo          返修单号
     * @param returnWarehouseCode    归还仓库代码
     * @param materialReturnInfoList 原料归还信息列表
     */
    public void doCreateMaterialReturnReceiveOrder(String msgKey,
                                                   String repairOrderNo,
                                                   String returnWarehouseCode,
                                                   String platform,
                                                   List<SubmitReturnMaterialRequestDto.MaterialReturnInfoVo> materialReturnInfoList) {
        // 检查归还仓库代码是否匹配加工原料收货类型
        ParamValidUtils.requireEquals(BooleanType.TRUE, wmsRemoteService.mateWarehouseCodeAndReceiveType(
                WmsEnum.ReceiveType.PROCESS_MATERIAL, returnWarehouseCode), ScmConstant.VERIFY_WAREHOUSE_PROMPTS);

        // 创建原料收货单
        ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem
                = RepairOrderBuilder.buildReceiveOrderCreateItem(repairOrderNo, msgKey, returnWarehouseCode);

        // 构建收货单明细
        List<String> querySku = materialReturnInfoList.stream()
                .map(SubmitReturnMaterialRequestDto.MaterialReturnInfoVo::getSku)
                .distinct()
                .collect(Collectors.toList());
        Map<String, String> skuSpuMap = plmRemoteService.getSpuMapBySkuList(querySku);
        List<ReceiveOrderCreateMqDto.ReceiveOrderDetail> detailList
                = RepairOrderBuilder.buildReceiveOrderDetail(skuSpuMap, platform, materialReturnInfoList);
        receiveOrderCreateItem.setDetailList(detailList);

        // 发送收货单创建消息
        ReceiveOrderCreateMqDto receiveOrderCreateMqDto
                = RepairOrderBuilder.buildReceiveOrderCreateMqDto(msgKey, receiveOrderCreateItem);
        consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
    }


    /**
     * 记录原料批次码归还信息和原料归还明细实体对象列表。
     *
     * @param repairOrderNo          返修单号
     * @param msgKey                 消息键
     * @param materialReturnInfoList 原料归还信息列表
     */
    public void doRecordMaterialBackInfo(String msgKey,
                                         String repairOrderNo,
                                         List<SubmitReturnMaterialRequestDto.MaterialReturnInfoVo> materialReturnInfoList) {
        // 记录原料归还信息
        ProcessMaterialBackPo processMaterialBackPo = RepairOrderBuilder.buildProcessMaterialBackPo(repairOrderNo,
                msgKey);
        processMaterialBackDao.insert(processMaterialBackPo);

        // 原料归还明细
        Long processMaterialBackId = processMaterialBackPo.getProcessMaterialBackId();
        List<ProcessMaterialBackItemPo> materialBackItemPoList = RepairOrderBuilder.buildProcessMaterialBackItemPos(
                processMaterialBackId, materialReturnInfoList);
        processMaterialBackItemDao.insertBatch(materialBackItemPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.PROCESS_RECEIVE_STATUS_LOCK_PREFIX, key = "#message.receiveOrderNo",
            waitTime = 1, leaseTime = -1)
    public void syncMaterialReceiptMsg(ReceiveOrderChangeMqDto message) {
        String unionKey = message.getUnionKey();
        String bizKey = StringUtils.isNotBlank(unionKey) ? (unionKey.split(
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK).length > 0 ? unionKey.split(
                ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK)[0] : null) : null;
        ParamValidUtils.requireNotNull(bizKey,
                StrUtil.format("无法根据收货单业务编码解析对应的类型。unionKey:{}", unionKey));

        ProcessMaterialBackPo updateProcessMaterialBackPo = ParamValidUtils.requireNotNull(
                processMaterialBackDao.getByMessageKey(bizKey),
                StrUtil.format("无法找到与业务编码 {} 相关联的返修单原料归还信息，请检查业务编码是否正确。", bizKey));

        String receiveOrderNo = message.getReceiveOrderNo();
        updateProcessMaterialBackPo.setReceiptNo(receiveOrderNo);
        processMaterialBackDao.updateByIdVersion(updateProcessMaterialBackPo);
    }

    /**
     * 验证指定返修单号对应的原料批次码是否有足够的收货数量满足要求。
     *
     * @param repairOrderNo        返修单号
     * @param repairOrderResultPos 原料批次码及对应的使用数量列表
     */
    public void validateMaterialBatchCodeReceivedQuantity(String repairOrderNo,
                                                          List<RepairOrderResultPo> repairOrderResultPos) {
        // 获取返修单绑定信息列表
        List<RepairOrderBindingInfoBo> repairOrderBindingInfos = repairOrderBaseService.getRepairOrderBindingInfo(
                repairOrderNo);

        // 计算每个原料批次码的使用数量总和
        Map<String, Integer> materialUsageQuantityMap = repairOrderResultPos.stream()
                .collect(Collectors.groupingBy(RepairOrderResultPo::getMaterialBatchCode,
                        Collectors.summingInt(RepairOrderResultPo::getMaterialUsageQuantity)));

        // 遍历每个原料批次码及对应的使用数量总和
        for (Map.Entry<String, Integer> entry : materialUsageQuantityMap.entrySet()) {
            String materialBatchCode = entry.getKey();
            Integer totalMaterialUsageQuantity = entry.getValue();

            // 查找当前原料批次码对应的可用数量
            Integer receivedQuantity = repairOrderBindingInfos.stream()
                    .filter(repairOrderBindingInfo -> Objects.equals(materialBatchCode,
                            repairOrderBindingInfo.getBatchCode()))
                    .findFirst()
                    .map(RepairOrderBindingInfoBo::getReceivedQuantity)
                    .orElse(0);

            // 校验当前原料批次码的使用数量是否超过可用数量
            ParamValidUtils.requireLessThanOrEqual(totalMaterialUsageQuantity, receivedQuantity, StrUtil.format(
                    "无法操作加工完成，您绑定的原料批次码 {} 提交数量需小于等于原料可绑定数量，请校验后再进行操作",
                    materialBatchCode));
        }
    }


    /**
     * 更新指定返修单号下原料使用数量。
     *
     * @param repairOrderNo     返修单号
     * @param completionDetails 原料完成详情列表
     */
    public void updateMaterialUsageQuantity(String repairOrderNo,
                                            List<CompletionRequestDto.CompletionDetailRequestDto> completionDetails) {
        // 获取指定返修单号对应的原料使用结果列表
        List<RepairOrderResultPo> repairOrderResultPos = repairOrderResultDao.getListByRepairOrderNo(repairOrderNo);

        // 遍历原料使用结果列表
        for (RepairOrderResultPo repairOrderResultPo : repairOrderResultPos) {
            // 获取原料使用结果的ID
            Long repairOrderResultId = repairOrderResultPo.getRepairOrderResultId();

            // 在原料完成详情列表中查找与当前原料使用结果ID相匹配的记录，并获取完成数量
            Integer materialUsageQuantity = completionDetails.stream()
                    .filter(completionDetail -> Objects.equals(repairOrderResultId,
                            completionDetail.getRepairOrderResultId()))
                    .findFirst()
                    .map(CompletionRequestDto.CompletionDetailRequestDto::getCompletedQuantity)
                    .orElse(null);

            // 如果找到了匹配的记录，更新原料使用数量
            if (Objects.nonNull(materialUsageQuantity)) {
                repairOrderResultPo.setMaterialUsageQuantity(materialUsageQuantity);
            }
        }

        // 批量更新原料使用结果列表
        repairOrderResultDao.updateBatchByIdVersion(repairOrderResultPos);
    }

    public QcOriginBo getQcOriginBo(String repairOrderNo) {
        RepairOrderPo repairOrderPo = repairOrderDao.getByRepairOrderNo(repairOrderNo);
        if (Objects.isNull(repairOrderPo)) {
            return null;
        }

        QcOriginBo qcOriginBo = new QcOriginBo();
        qcOriginBo.setQcOrigin(QcOrigin.REPAIR_ORDER);
        qcOriginBo.setQcOriginProperty(QcOriginProperty.REPAIR);

        return qcOriginBo;
    }

    public List<RepairOrderPrintProductReceiptVo> printProductReceiptInfo(PrintRepairOrderProductRequestDto request) {
        List<String> printRepos = request.getRepairOrderNos();
        List<RepairOrderPo> printRepairOrderPos
                = ParamValidUtils.requireNotEmpty(repairOrderDao.listByRepairOrderNos(printRepos),
                "返修单信息不存在，请刷新页面后重试");
        List<RepairOrderStatus> canPrintStatus = List.of(RepairOrderStatus.WAITING_FOR_RECEIVING);
        List<String> canPrintRemarks = canPrintStatus.stream()
                .map(RepairOrderStatus::getRemark)
                .collect(Collectors.toList());

        boolean canPrint = printRepairOrderPos.stream()
                .allMatch(printRepairOrderPo -> canPrintStatus.contains(printRepairOrderPo.getRepairOrderStatus()));
        ParamValidUtils.requireEquals(canPrint, Boolean.TRUE, StrUtil.format("请仅选择状态为 {} 的返修单进行打印。",
                canPrintRemarks.stream()
                        .map(String::valueOf)
                        .collect(
                                Collectors.joining("/"))));
        printRepairOrderPos.removeIf(printRepairOrderPo -> StrUtil.isBlank(printRepairOrderPo.getReceiveOrderNo()));
        ParamValidUtils.requireNotEmpty(printRepairOrderPos,
                "所选返修单中，全部返修单均无收货单信息，请选择至少一个存在收货信息的返修单进行打印。");
        return RepairOrderBuilder.buildRepairOrderPrintProductReceiptVos(printRepairOrderPos);
    }

    public List<RepairOrderPrintMaterialReturnVo> printMaterialReturnInfo(PrintRepairOrderMaterialBackRequestDto request) {
        List<String> printRepos = request.getRepairOrderNos();
        List<RepairOrderPo> printRepairOrderPos
                = ParamValidUtils.requireNotEmpty(repairOrderDao.listByRepairOrderNos(printRepos),
                "返修单信息不存在，请刷新页面后重试");
        List<RepairOrderStatus> canPrintStatus
                = List.of(RepairOrderStatus.WAITING_FOR_RECEIVING, RepairOrderStatus.COMPLETED);
        List<String> canPrintRemarks = canPrintStatus.stream()
                .map(RepairOrderStatus::getRemark)
                .collect(Collectors.toList());
        boolean canPrint = printRepairOrderPos.stream()
                .allMatch(printRepairOrderPo -> canPrintStatus.contains(printRepairOrderPo.getRepairOrderStatus()));
        ParamValidUtils.requireEquals(canPrint, Boolean.TRUE, StrUtil.format("请仅选择状态为 {} 的返修单进行打印。",
                canPrintRemarks.stream()
                        .map(String::valueOf)
                        .collect(
                                Collectors.joining("/"))));

        List<ProcessMaterialBackPo> processMaterialBackPos = ParamValidUtils.requireNotEmpty(
                processMaterialBackDao.listByRepairOrderNos(printRepos),
                "所选返修单中，全部返修单均无原料归还，请选择至少一个存在原料归还的返修单进行打印。");

        processMaterialBackPos.removeIf(processMaterialBackPo -> StrUtil.isBlank(processMaterialBackPo.getReceiptNo()));
        ParamValidUtils.requireNotEmpty(processMaterialBackPos,
                "所选返修单中，全部返修单均无原料归还收货单，请选择至少一个存在原料归还收货单的返修单进行打印。");

        Map<String, List<String>> repairOrderBackReceiptNosMap = processMaterialBackPos.stream()
                .collect(Collectors.groupingBy(ProcessMaterialBackPo::getRepairOrderNo,
                        Collectors.mapping(ProcessMaterialBackPo::getReceiptNo,
                                Collectors.toList())));

        List<String> receiptNos = processMaterialBackPos.stream()
                .map(ProcessMaterialBackPo::getReceiptNo)
                .collect(Collectors.toList());
        ReceiveOrderGetDto queryReceiveOrderDto = RepairOrderBuilder.buildReceiveOrderGetDto(receiptNos);
        List<ReceiveOrderForScmVo> receiveOrderList = wmsRemoteService.getReceiveOrderList(queryReceiveOrderDto);
        Map<String, String> receiveOrderWarehouseCodeMap = receiveOrderList.stream()
                .collect(Collectors.toMap(
                        ReceiveOrderForScmVo::getReceiveOrderNo,
                        ReceiveOrderForScmVo::getWarehouseCode,
                        (existing, replacement) -> existing)
                );

        return RepairOrderBuilder.buildRepairOrderPrintMaterialReturnVos(repairOrderBackReceiptNosMap,
                receiveOrderWarehouseCodeMap);
    }
}










