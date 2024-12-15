package com.hete.supply.scm.server.scm.qc.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.process.service.biz.ProcessOrderBizService;
import com.hete.supply.scm.server.scm.qc.dao.QcDetailDao;
import com.hete.supply.scm.server.scm.qc.dao.QcOrderDao;
import com.hete.supply.scm.server.scm.qc.entity.bo.*;
import com.hete.supply.scm.server.scm.qc.entity.po.QcDetailPo;
import com.hete.supply.scm.server.scm.qc.entity.po.QcOrderPo;
import com.hete.support.api.exception.BizException;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ProcessOrderQcOrderCreator 是质检单创建的具体实现类，用于创建质检单相关的信息，包括质检单主体和质检明细。
 * 这个类继承自 AbstractQcOrderCreator，并实现了其中的抽象方法来执行质检单的创建逻辑。
 * 它将校验输入数据，创建质检单和质检明细，然后绑定加工单与质检单关系。
 *
 * @author yanjiawei
 * @date 2023/10/25 09:33
 */
public class ProcessOrderQcOrderCreator extends AbstractQcOrderCreator<ProcessOrderCreateQcBo,
        ProcessOrderCreateQcResultBo> {

    private final ProcessOrderBizService processOrderBizService;
    private final ConsistencySendMqService consistencySendMqService;
    private final PlmRemoteService plmRemoteService;

    public ProcessOrderQcOrderCreator(QcOrderDao qcOrderDao,
                                      QcDetailDao qcDetailDao,
                                      IdGenerateService idGenerateService,
                                      ProcessOrderBizService processOrderBizService,
                                      ConsistencySendMqService consistencySendMqService,
                                      PlmRemoteService plmRemoteService) {
        super(qcOrderDao, qcDetailDao, idGenerateService);
        this.processOrderBizService = processOrderBizService;
        this.consistencySendMqService = consistencySendMqService;
        this.plmRemoteService = plmRemoteService;
    }

    /**
     * 执行质检单创建前的前置操作，校验输入数据的有效性。
     *
     * @param input 质检单创建的输入参数，包括仓库、加工单号、质检类型、操作人等信息。
     * @throws BizException 如果输入数据校验失败，将抛出业务异常。
     */
    @Override
    protected void createPreOperations(ProcessOrderCreateQcBo input) {
        final List<ProcessOrderCreateQcOrderBo> createQcOrderBos = input.getCreateQcOrderBos();

        if (CollectionUtil.isEmpty(createQcOrderBos)) {
            throw new BizException("加工单创建质检单失败！原因：加工质检信息为空！");
        }
        if (createQcOrderBos.stream()
                .anyMatch(order -> StrUtil.isBlank(order.getWarehouseCode()))) {
            throw new BizException("创建质检单失败！原因：仓库编码不能为空。");
        }
        if (createQcOrderBos.stream()
                .anyMatch(order -> StrUtil.isBlank(order.getProcessOrderNo()))) {
            throw new BizException("创建质检单失败！原因：加工单号不能为空。");
        }
        if (createQcOrderBos.stream()
                .anyMatch(order -> Objects.isNull(order.getQcType()))) {
            throw new BizException("创建质检单失败！原因：质检类型为空。");
        }
        if (createQcOrderBos.stream()
                .anyMatch(order -> StrUtil.isBlank(order.getOperator()))) {
            throw new BizException("创建质检单失败！原因：操作人编码为空。");
        }
        if (createQcOrderBos.stream()
                .anyMatch(order -> StrUtil.isBlank(order.getOperatorName()))) {
            throw new BizException("创建质检单失败！原因：操作人名称为空。");
        }
        if (createQcOrderBos.stream()
                .anyMatch(order -> CollectionUtil.isEmpty(order.getQcOrderDetails()))) {
            throw new BizException("创建质检单失败！原因：加工质检明细列表为空");
        }

        // 校验加工质检明细
        for (ProcessOrderCreateQcOrderBo createQcOrderBo : createQcOrderBos) {
            final List<ProcessOrderCreateQcOrderDetailBo> qcOrderDetails = createQcOrderBo.getQcOrderDetails();
            if (qcOrderDetails.stream()
                    .anyMatch(detail -> StrUtil.isBlank(detail.getContainerCode()))) {
                throw new BizException("创建质检单失败！原因：容器编码为空。");
            }
            if (qcOrderDetails.stream()
                    .anyMatch(detail -> StrUtil.isBlank(detail.getBatchCode()))) {
                throw new BizException("创建质检单失败！原因：批次码为空。");
            }
            if (qcOrderDetails.stream()
                    .anyMatch(detail -> StrUtil.isBlank(detail.getSkuCode()))) {
                throw new BizException("创建质检单失败！原因：sku为空。");
            }
            if (qcOrderDetails.stream()
                    .anyMatch(detail -> Objects.isNull(detail.getAmount()))) {
                throw new BizException("创建质检单失败！原因：应质检数量为空。");
            }
        }
    }

    /**
     * 执行质检单的创建操作，包括创建质检单主体和质检明细。
     *
     * @param processOrderCreateQcBo 质检单创建的输入参数，包括质检单明细等信息。
     * @return ProcessOrderCreateQcResultBo 质检单创建的结果，包括质检单号等信息。
     */
    @Override
    protected ProcessOrderCreateQcResultBo performCreateQcOrder(ProcessOrderCreateQcBo processOrderCreateQcBo) {
        final List<ProcessOrderCreateQcOrderBo> createQcOrderBos = processOrderCreateQcBo.getCreateQcOrderBos();
        final String operator = createQcOrderBos.stream()
                .map(ProcessOrderCreateQcOrderBo::getOperator)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse("");
        final String operatorName = createQcOrderBos.stream()
                .map(ProcessOrderCreateQcOrderBo::getOperatorName)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse("");

        List<String> skuList = createQcOrderBos.stream()
                .flatMap(orderBo -> orderBo.getQcOrderDetails()
                        .stream())
                .map(ProcessOrderCreateQcOrderDetailBo::getSkuCode)
                .distinct()
                .collect(Collectors.toList());
        //查询sku的分类信息
        Map<String, String> skuSecondCategoriesMap = plmRemoteService.getSkuSecondCategoriesCnMapBySkuList(skuList);

        List<ProcessOrderCreateQcOrderResultBo> qcOrderResultBos = createQcOrderBos.stream()
                .map(createQcOrderBo -> {
                    final String processOrderNo = createQcOrderBo.getProcessOrderNo();
                    List<QcOrderPo> qcOrderPoList = qcOrderDao.listByProcessOrderNo(processOrderNo);
                    if (qcOrderPoList.stream()
                            .anyMatch(qcOrder -> !Objects.equals(qcOrder.getQcState(), QcState.FINISHED))) {
                        return null;
                    }

                    final QcType qcType = createQcOrderBo.getQcType();
                    final String warehouseCode = createQcOrderBo.getWarehouseCode();
                    final List<ProcessOrderCreateQcOrderDetailBo> qcOrderDetails = createQcOrderBo.getQcOrderDetails();
                    final String qcOrderNo = idGenerateService.getIncrCode(ScmConstant.QC_CODE, TimeType.CN_DAY, 4);
                    final ProcessOrderType processOrderType = createQcOrderBo.getProcessOrderType();
                    int qcAmount = qcOrderDetails.stream()
                            .mapToInt(ProcessOrderCreateQcOrderDetailBo::getAmount)
                            .sum();

                    // 质检单信息
                    QcOrderPo qcOrderPo = new QcOrderPo();
                    qcOrderPo.setQcOrderNo(qcOrderNo);
                    qcOrderPo.setQcType(qcType);
                    qcOrderPo.setWarehouseCode(warehouseCode);
                    qcOrderPo.setProcessOrderNo(processOrderNo);
                    qcOrderPo.setQcSourceOrderNo(processOrderNo);
                    qcOrderPo.setQcSourceOrderType(QcSourceOrderType.PROCESS_ORDER_NO);
                    qcOrderPo.setQcState(QcState.WAIT_HAND_OVER);
                    qcOrderPo.setQcAmount(qcAmount);

                    QcOriginBo qcOriginBo = processOrderBizService.getQcOriginBo(processOrderNo);
                    if (Objects.nonNull(qcOriginBo)) {
                        qcOrderPo.setQcOrigin(qcOriginBo.getQcOrigin());
                        qcOrderPo.setQcOriginProperty(qcOriginBo.getQcOriginProperty());
                    }

                    qcOrderDao.insert(qcOrderPo);

                    // 质检单日志(由于Qc的BaseService调用BizService导致这里没办法调用Base的方法)
                    BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
                    bizLogCreateMqDto.setBizLogCode(
                            idGenerateService.getSnowflakeCode(LogBizModule.QC_ORDER_STATUS.name()));
                    bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
                    bizLogCreateMqDto.setLogVersion(ScmConstant.QC_ORDER_LOG_VERSION);
                    bizLogCreateMqDto.setBizModule(LogBizModule.QC_ORDER_STATUS.name());
                    bizLogCreateMqDto.setBizCode(qcOrderPo.getQcOrderNo());
                    bizLogCreateMqDto.setOperateTime(DateUtil.current());
                    String userKey = StringUtils.isNotBlank(
                            GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
                    String username = StringUtils.isNotBlank(
                            GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
                    bizLogCreateMqDto.setOperateUser(userKey);
                    bizLogCreateMqDto.setOperateUsername(username);
                    ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
                    bizLogCreateMqDto.setContent(qcOrderPo.getQcState()
                            .getRemark());
                    bizLogCreateMqDto.setDetail(logVersionBos);
                    consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);


                    // 质检明细信息
                    List<QcDetailPo> qcDetailPos = qcOrderDetails.stream()
                            .map(qcOrderDetail -> {
                                QcDetailPo qcDetailPo = new QcDetailPo();
                                qcDetailPo.setQcOrderNo(qcOrderNo);
                                qcDetailPo.setContainerCode(qcOrderDetail.getContainerCode());
                                qcDetailPo.setBatchCode(qcOrderDetail.getBatchCode());
                                qcDetailPo.setSkuCode(qcOrderDetail.getSkuCode());
                                qcDetailPo.setAmount(qcOrderDetail.getAmount());
                                qcDetailPo.setWaitAmount(qcOrderDetail.getAmount());
                                qcDetailPo.setPassAmount(0);
                                qcDetailPo.setNotPassAmount(0);
                                qcDetailPo.setQcResult(QcResult.PASSED);
                                qcDetailPo.setCategoryName(skuSecondCategoriesMap.get(qcOrderDetail.getSkuCode()));
                                qcDetailPo.setPlatform(createQcOrderBo.getPlatform());
                                return qcDetailPo;
                            })
                            .collect(Collectors.toList());
                    qcDetailDao.insertBatch(qcDetailPos);

                    // 创建结果
                    ProcessOrderCreateQcOrderResultBo resultBo = new ProcessOrderCreateQcOrderResultBo();
                    resultBo.setProcessOrderNo(processOrderNo);
                    resultBo.setQcOrderNo(qcOrderNo);
                    resultBo.setOperator(operator);
                    resultBo.setOperatorName(operatorName);
                    resultBo.setContainerCodeList(qcDetailPos.stream()
                            .map(QcDetailPo::getContainerCode)
                            .collect(Collectors.toSet()));
                    return resultBo;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        ProcessOrderCreateQcResultBo createResult = new ProcessOrderCreateQcResultBo();
        createResult.setQcOrderResultBos(qcOrderResultBos);
        return createResult;
    }

    /**
     * 在质检单创建后执行后续操作，例如绑定加工单与质检单的关系。
     *
     * @param processOrderCreateQcResultBo 质检单创建的结果，包括质检单号等信息。
     */
    @Override
    protected void doAfterCreation(ProcessOrderCreateQcResultBo processOrderCreateQcResultBo) {
        // 绑定加工单与质检单关系
        processOrderBizService.handleQcOrderCreation(processOrderCreateQcResultBo);
    }

}
