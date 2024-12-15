package com.hete.supply.scm.server.scm.develop.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsDetailVo;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleType;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.ChannelDao;
import com.hete.supply.scm.server.scm.develop.converter.DevelopChildConverter;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopReviewAndSampleBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.SampleOrderPriceBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.SampleOrderPriceResultBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.*;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopAndSampleNoVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleOrderSearchVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSimpleVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.SampleOrderPriceVo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleDirection;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleOrderBaseService;
import com.hete.supply.scm.server.scm.develop.service.strategy.DevelopSampleOrderHandleStrategy;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.entity.dto.ReceiveOrderChangeMqDto;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.consistency.core.service.ConsistencyService;
import com.hete.support.core.handler.HandlerContext;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/3 14:17
 */
@Service
@RequiredArgsConstructor
@Validated
public class DevelopSampleOrderBizService {

    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopSampleOrderBaseService developSampleOrderBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopChildOrderChangeDao developChildOrderChangeDao;
    private final PlmRemoteService plmRemoteService;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final WmsRemoteService wmsRemoteService;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;
    private final ProduceDataBaseService produceDataBaseService;
    private final DevelopReviewOrderDao developReviewOrderDao;
    private final ConsistencyService consistencyService;
    private final DevelopChildBaseService developChildBaseService;
    private final IdGenerateService idGenerateService;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final ScmImageBaseService scmImageBaseService;
    private final ChannelDao channelDao;

    /**
     * 查询开发需求样品单列表
     *
     * @param dto:
     * @return PageInfo<DevelopSampleOrderSearchVo>
     * @author ChenWenLong
     * @date 2023/8/3 17:00
     */
    public CommonPageResult.PageInfo<DevelopSampleOrderSearchVo> search(DevelopSampleOrderSearchDto dto) {
        return developSampleOrderBaseService.search(dto);
    }

    /**
     * 导出
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2023/8/3 17:01
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(DevelopSampleOrderSearchDto dto) {
        if (null == developSampleOrderBaseService.getSearchDevelopSampleWhere(dto)) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = developSampleOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(),
                FileOperateBizType.SCM_DEVELOP_SAMPLE_ORDER_EXPORT.getCode(), dto));
    }

    /**
     * 确认处理
     *
     * @param dto:
     * @author ChenWenLong
     * @date 2023/8/3 17:01
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitHandle(DevelopSampleOrderSubmitHandleDto dto) {
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : dto.getDevelopSampleOrderSubmitHandleItemList()) {
            // 非退样时进行渠道大货价格检验
            if (!DevelopSampleDirection.RETURN_SAMPLES.equals(itemDto.getDevelopSampleDirection())) {
                List<DevelopOrderPriceSaveDto> developOrderPriceList = itemDto.getDevelopOrderPriceList();
                developChildBaseService.verifyDevelopOrderPrice(developOrderPriceList);
            }
        }

        List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> developSampleOrderItemDtoList = dto.getDevelopSampleOrderSubmitHandleItemList();
        if (CollectionUtils.isEmpty(developSampleOrderItemDtoList)) {
            throw new BizException("样品单信息不能为空！");
        }
        List<String> developSampleOrderNoList = developSampleOrderItemDtoList.stream()
                .map(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getDevelopSampleOrderNo)
                .distinct()
                .collect(Collectors.toList());

        // 验证样品单是否存在重复
        if (developSampleOrderItemDtoList.size() != developSampleOrderNoList.size()) {
            throw new BizException("样品单号存在重复数据，请联系管理员后重试！");
        }

        Map<String, DevelopSampleOrderPo> developSampleOrderPoMap = developSampleOrderDao.getMapByNoList(developSampleOrderNoList);
        if (CollectionUtils.isEmpty(developSampleOrderPoMap)) {
            throw new BizException("样品单数据不能为空，请刷新页面后重试！");
        }

        // 用于验证存储一个样品单只能添加一个默认渠道大货价格
        Map<String, BigDecimal> developSampleOrderNoPriceMap = new HashMap<>();
        // 用于验证sku+供应商+渠道作为唯一
        Set<String> costOfGoodsSet = new HashSet<>();
        // 统一检验入参样品单版本和处理方式
        for (DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto itemDto : developSampleOrderItemDtoList) {
            String developSampleOrderNo = itemDto.getDevelopSampleOrderNo();
            Assert.notBlank(developSampleOrderNo, () -> new BizException("请求样品单号为空，数据存在问题请联系管理员后重试！"));
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoMap.get(developSampleOrderNo);
            if (null == developSampleOrderPo) {
                throw new BizException("数据已被修改或删除，请刷新页面后重试！");
            }
            Assert.isTrue(developSampleOrderPo.getVersion().equals(itemDto.getVersion()), () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
            Assert.isTrue(developSampleOrderPo.getDevelopSampleMethod().equals(itemDto.getDevelopSampleMethod()), () -> new ParamIllegalException("样品处理方式的数据已被修改或删除，请刷新页面后重试！"));

            // 验证一个样品单只能设置一个渠道大货价格
            if (!DevelopSampleDirection.RETURN_SAMPLES.equals(itemDto.getDevelopSampleDirection())
                    && CollectionUtils.isNotEmpty(itemDto.getDevelopOrderPriceList())) {
                for (DevelopOrderPriceSaveDto developOrderPriceSaveDto : itemDto.getDevelopOrderPriceList()) {
                    if (BooleanType.TRUE.equals(developOrderPriceSaveDto.getIsDefaultPrice())
                            && developSampleOrderNoPriceMap.containsKey(itemDto.getDevelopSampleOrderNo())) {
                        throw new ParamIllegalException("样品单:{}只能设置一个渠道批次价格，请刷新页面后重试！", itemDto.getDevelopSampleOrderNo());
                    }
                    if (BooleanType.TRUE.equals(developOrderPriceSaveDto.getIsDefaultPrice())) {
                        developSampleOrderNoPriceMap.put(itemDto.getDevelopSampleOrderNo(), developOrderPriceSaveDto.getPrice());
                    }

                    String combinationKey = developSampleOrderPo.getSupplierCode() + itemDto.getSku() + developOrderPriceSaveDto.getChannelId();
                    if (costOfGoodsSet.contains(combinationKey)) {
                        throw new ParamIllegalException("不允许同时处理多条同供应商同渠道样品价格数据，请分批处理！");
                    } else {
                        costOfGoodsSet.add(combinationKey);
                    }
                }
                // 验证必须存在一个默认渠道大货价格
                if (!developSampleOrderNoPriceMap.containsKey(itemDto.getDevelopSampleOrderNo())) {
                    throw new ParamIllegalException("样品单:{}必须设置一个渠道批次价格，请刷新页面后重试！", itemDto.getDevelopSampleOrderNo());
                }
            }
        }


        // 封样入库、闪售、退样场景进行策略处理
        Map<DevelopSampleMethod, List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto>> developSampleMethodDto = developSampleOrderItemDtoList.stream()
                .collect(Collectors.groupingBy(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getDevelopSampleMethod));
        developSampleMethodDto.forEach((DevelopSampleMethod developSampleMethod, List<DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto> itemDto) -> {
            // 审批单回调之后相关业务单据逻辑处理
            DevelopSampleOrderHandleStrategy handlerBean = HandlerContext.getHandlerBean(DevelopSampleOrderHandleStrategy.class, developSampleMethod);

            // 获取当前场景的样品单PO集合
            List<String> developSampleOrderNoFilterList = itemDto.stream()
                    .map(DevelopSampleOrderSubmitHandleDto.DevelopSampleOrderSubmitHandleItemDto::getDevelopSampleOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            List<DevelopSampleOrderPo> poList = developSampleOrderNoFilterList.stream()
                    .map(developSampleOrderPoMap::get) // 获取Map中对应样品单号的值
                    .collect(Collectors.toList()); // 收集结果到列表中

            // 前置操作相关业务检验入参
            handlerBean.submitHandleVerify(itemDto, poList);
            // 业务逻辑处理
            handlerBean.developSampleOrderSubmitHandle(itemDto, poList);

        });

    }

    /**
     * 处理WMS生成入库MQ
     *
     * @param message:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/10 11:42
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncReceiptMsg(ReceiveOrderChangeMqDto message) {
        if (WmsEnum.ReceiveOrderState.WAIT_RECEIVE.equals(message.getReceiveOrderState())) {
            final String scmBizNo = message.getScmBizNo();
            final DevelopSampleOrderPo developSampleOrderPo = developSampleOrderDao.getOneByNo(scmBizNo);
            Assert.notNull(developSampleOrderPo, () -> new BizException("查找不到对应的开发样品单，WMS推送MQ数据错误！"));
            developSampleOrderPo.setReceiptOrderNo(message.getReceiveOrderNo());
            developSampleOrderDao.updateByIdVersion(developSampleOrderPo);
        }
        if (WmsEnum.ReceiveOrderState.ONSHELVESED.equals(message.getReceiveOrderState())) {
            final String scmBizNo = message.getScmBizNo();
            final DevelopSampleOrderPo developSampleOrderPo = developSampleOrderDao.getOneByNo(scmBizNo);
            Assert.notNull(developSampleOrderPo, () -> new BizException("查找不到对应的开发样品单，WMS上架时推送MQ数据错误！"));
            developSampleOrderPo.setShelvesTime(message.getBizTime());
            developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.ON_SHELVES);
            developSampleOrderDao.updateByIdVersion(developSampleOrderPo);
        }
    }

    /**
     * 签收样品
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/25 16:46
     */
    @Transactional(rollbackFor = Exception.class)
    public void signSample(DevelopSampleOrderNoListDto dto) {
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(dto.getDevelopSampleOrderNoList());
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new BizException("数据不能为空，请刷新页面后重试！");
        }

        List<String> developChildOrderNoNormalList = developSampleOrderPoList.stream()
                .filter(po -> DevelopSampleType.NORMAL.equals(po.getDevelopSampleType()))
                .map(DevelopSampleOrderPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());

        List<DevelopChildOrderChangePo> developChildOrderChangePoList = developChildOrderChangeDao.getListByNoList(developChildOrderNoNormalList);

        // 获取开发子单更新日志
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoNormalList);

        //验证
        final LocalDateTime now = LocalDateTime.now();
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            developSampleOrderPo.setDevelopSampleStatus(developSampleOrderPo.getDevelopSampleStatus().toDocumentaryReceipt());
            developSampleOrderPo.setSignTime(now);
            developSampleOrderPo.setSignUser(GlobalContext.getUserKey());
            developSampleOrderPo.setSignUsername(GlobalContext.getUsername());
        }

        //更新开发子单
        for (DevelopChildOrderChangePo developChildOrderChangePo : developChildOrderChangePoList) {

            if (StringUtils.isBlank(developChildOrderChangePo.getFollowUser())) {
                developChildOrderChangePo.setFollowUser(GlobalContext.getUserKey());
                developChildOrderChangePo.setFollowDate(now);
            }
            if (StringUtils.isBlank(developChildOrderChangePo.getFollowUsername())) {
                developChildOrderChangePo.setFollowUsername(GlobalContext.getUsername());
            }
        }

        developChildOrderChangeDao.updateBatchByIdVersion(developChildOrderChangePoList);
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            String developSampleOrderNoJoining = developSampleOrderPoList.stream()
                    .filter(developSampleOrderPo -> developSampleOrderPo.getDevelopChildOrderNo().equals(developChildOrderPo.getDevelopChildOrderNo()))
                    .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                    .collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(developSampleOrderNoJoining)) {
                developChildBaseService.createStatusChangeLog(developChildOrderPo, "签收样品:" + developSampleOrderNoJoining);
            }
        }

        // 产前样样品单自动生成产前样的审版单
        List<DevelopSampleOrderPo> developChildOrderPoPrenatalList = developSampleOrderPoList.stream()
                .filter(po -> DevelopSampleType.PRENATAL_SAMPLE.equals(po.getDevelopSampleType()))
                .filter(po -> ScmConstant.PROCESS_SUPPLIER_CODE.equals(po.getSupplierCode()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(developChildOrderPoPrenatalList)) {
            this.autoCreateReviewOrders(developChildOrderPoPrenatalList);
        }
    }

    /**
     * 产前样样品单自动生成产前样的审版单
     *
     * @param developChildOrderPoPrenatalList:
     * @return void
     * @author ChenWenLong
     * @date 2024/7/29 15:35
     */
    private void autoCreateReviewOrders(List<DevelopSampleOrderPo> developChildOrderPoPrenatalList) {

        // 入参是空返回
        if (CollectionUtils.isEmpty(developChildOrderPoPrenatalList)) {
            return;
        }

        // 开发子单号
        List<String> developChildOrderNoList = developChildOrderPoPrenatalList.stream()
                .map(DevelopSampleOrderPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());

        // 获取开发子单关联审版单时克重最高的封样入库的审版样品单
        Map<String, DevelopReviewSampleOrderPo> developChildOrderSealSampleMap = developSampleOrderBaseService.getSealSampleByChildOrderNoList(developChildOrderNoList);

        // 获取开发子单信息
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderPoList.stream()
                .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));

        // 查询开发审版关联样品单
        List<Long> developReviewSampleOrderIdList = developChildOrderSealSampleMap.values().stream()
                .map(DevelopReviewSampleOrderPo::getDevelopReviewSampleOrderId)
                .collect(Collectors.toList());
        List<String> developSampleOrderNoList = developChildOrderSealSampleMap.values().stream()
                .map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());

        // 获取图片信息
        Map<Long, List<String>> fileCodeListByEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT, developReviewSampleOrderIdList);
        Map<Long, List<String>> fileCodeListByDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL, developReviewSampleOrderIdList);


        // 查询开发审版关联样品单属性信息
        List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
        Map<String, List<DevelopReviewSampleOrderInfoPo>> developReviewSampleOrderInfoPoMap = developReviewSampleOrderInfoPoList.stream()
                .collect(Collectors.groupingBy(DevelopReviewSampleOrderInfoPo::getDevelopSampleOrderNo));


        //创建样品单信息
        List<DevelopReviewOrderPo> insertDevelopReviewOrderPoList = new ArrayList<>();
        List<DevelopReviewSampleOrderPo> insertDevelopReviewSampleOrderPoList = new ArrayList<>();
        List<DevelopReviewSampleOrderInfoPo> insertDevelopReviewSampleOrderInfoPoList = new ArrayList<>();
        // 效果图
        List<ScmImageBo> insertEffectScmImageBoList = new ArrayList<>();
        // 细节图
        List<ScmImageBo> insertDetailScmImageBoList = new ArrayList<>();

        for (DevelopSampleOrderPo developSampleOrderPo : developChildOrderPoPrenatalList) {
            DevelopChildOrderPo developChildOrderByNoPo = developChildOrderPoMap.get(developSampleOrderPo.getDevelopChildOrderNo());
            if (developChildOrderByNoPo == null) {
                throw new BizException("开发子单号：" + developSampleOrderPo.getDevelopChildOrderNo() + "不存在信息，请联系系统管理员！");
            }

            String developReviewOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_REVIEW_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);

            // 获取开发子单关联审版单时克重最高的封样入库的样品单
            List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = new ArrayList<>();
            List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPos = new ArrayList<>();
            DevelopReviewSampleOrderPo developReviewSampleOrderPo = developChildOrderSealSampleMap.get(developSampleOrderPo.getDevelopChildOrderNo());
            if (null != developReviewSampleOrderPo) {
                developReviewSampleOrderPoList.add(developReviewSampleOrderPo);
                developReviewSampleOrderInfoPos.addAll(developReviewSampleOrderInfoPoMap.get(developReviewSampleOrderPo.getDevelopSampleOrderNo()));
            } else {
                // 针对无需打样场景
                developReviewSampleOrderPo = new DevelopReviewSampleOrderPo();
                developReviewSampleOrderPo.setDevelopChildOrderNo(developSampleOrderPo.getDevelopChildOrderNo());
                developReviewSampleOrderPo.setDevelopParentOrderNo(developSampleOrderPo.getDevelopParentOrderNo());
                developReviewSampleOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
                developReviewSampleOrderPo.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developReviewSampleOrderPoList.add(developReviewSampleOrderPo);
            }

            DevelopReviewAndSampleBo developReviewAndSampleBo = DevelopChildConverter.sampleCreateReviewToPo(developSampleOrderPo,
                    developChildOrderByNoPo,
                    developReviewOrderNo,
                    developReviewSampleOrderPoList,
                    developReviewSampleOrderInfoPos,
                    fileCodeListByEffectMap,
                    fileCodeListByDetailMap,
                    idGenerateService
            );

            insertDevelopReviewOrderPoList.add(developReviewAndSampleBo.getDevelopReviewOrderPo());

            if (CollectionUtils.isNotEmpty(developReviewAndSampleBo.getDevelopReviewSampleOrderPoList())) {
                insertDevelopReviewSampleOrderPoList.addAll(developReviewAndSampleBo.getDevelopReviewSampleOrderPoList());
            }
            if (CollectionUtils.isNotEmpty(developReviewAndSampleBo.getDevelopReviewSampleOrderInfoPoList())) {
                insertDevelopReviewSampleOrderInfoPoList.addAll(developReviewAndSampleBo.getDevelopReviewSampleOrderInfoPoList());
            }

            if (CollectionUtils.isNotEmpty(developReviewAndSampleBo.getEffectScmImageBoList())) {
                insertEffectScmImageBoList.addAll(developReviewAndSampleBo.getEffectScmImageBoList());
            }
            if (CollectionUtils.isNotEmpty(developReviewAndSampleBo.getDetailScmImageBoList())) {
                insertDetailScmImageBoList.addAll(developReviewAndSampleBo.getDetailScmImageBoList());
            }

        }

        // 图片处理
        if (CollectionUtils.isNotEmpty(insertEffectScmImageBoList)) {
            scmImageBaseService.insertBatchImageBo(insertEffectScmImageBoList, ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT);
        }
        if (CollectionUtils.isNotEmpty(insertDetailScmImageBoList)) {
            scmImageBaseService.insertBatchImageBo(insertDetailScmImageBoList, ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL);
        }

        developReviewOrderDao.insertBatch(insertDevelopReviewOrderPoList);
        developReviewSampleOrderDao.insertBatch(insertDevelopReviewSampleOrderPoList);
        developReviewSampleOrderInfoDao.insertBatch(insertDevelopReviewSampleOrderInfoPoList);

    }

    /**
     * 寄送样品
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/10/25 10:35
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendSample(DevelopSampleOrderNoListDto dto) {
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(dto.getDevelopSampleOrderNoList());
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            throw new BizException("数据不能为空，请刷新页面后重试！");
        }

        //验证
        final LocalDateTime now = LocalDateTime.now();
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            if (!DevelopSampleType.PRENATAL_SAMPLE.equals(developSampleOrderPo.getDevelopSampleType())) {
                throw new BizException("样品单类型是:{}时才进行操作", DevelopSampleType.PRENATAL_SAMPLE.getRemark());
            }
            developSampleOrderPo.setSendTime(now);
            developSampleOrderPo.setSendUser(GlobalContext.getUserKey());
            developSampleOrderPo.setSendUsername(GlobalContext.getUsername());
            developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.ALREADY_SEND_SAMPLES);
        }

        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);
    }

    public List<DevelopAndSampleNoVo> getSampleOrderByNo(DevelopSampleNoDto dto) {
        final List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getPreSampleOrderByNo(dto.getDevelopSampleOrderNo());

        //过滤掉已创建的样品单
        List<String> developSampleOrderNoList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());
        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoExist = developReviewSampleOrderDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);

        //返回数据处理
        List<DevelopAndSampleNoVo> voList = new ArrayList<>();
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderPoExist.stream()
                    .filter(poExist -> developSampleOrderPo.getDevelopSampleOrderNo().equals(poExist.getDevelopSampleOrderNo()))
                    .findFirst()
                    .orElse(null);
            if (developReviewSampleOrderPo == null) {
                final DevelopAndSampleNoVo developAndSampleNoVo = new DevelopAndSampleNoVo();
                developAndSampleNoVo.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developAndSampleNoVo.setDevelopChildOrderNo(developSampleOrderPo.getDevelopChildOrderNo());
                voList.add(developAndSampleNoVo);
            }
        }
        return voList;

    }

    public DevelopSampleSimpleVo getSampleSimpleVoByNo(DevelopSampleOrderNoListDto dto) {
        final List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(dto.getDevelopSampleOrderNoList());
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            return new DevelopSampleSimpleVo();
        }
        final DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.get(0);
        developSampleOrderPoList.forEach(developSampleOrderPo1 -> {
            if (!developSampleOrderPo.getDevelopChildOrderNo().equals(developSampleOrderPo1.getDevelopChildOrderNo())) {
                throw new ParamIllegalException("选择的开发样品单号不属于同一开发子单，请重新选择！");
            }
        });

        final List<PlmGoodsDetailVo> plmGoodsDetailVoList = plmRemoteService.getGoodsDetail(Collections.singletonList(developSampleOrderPo.getSku()));
        return DevelopSampleSimpleVo.builder()
                .developSampleOrderNoList(dto.getDevelopSampleOrderNoList())
                .platform(developSampleOrderPo.getPlatform())
                .supplierCode(developSampleOrderPo.getSupplierCode())
                .sku(developSampleOrderPo.getSku())
                .plmGoodsDetailVoList(plmGoodsDetailVoList)
                .build();
    }

    /**
     * 获取样品单大货价格和样品价格
     *
     * @param dto:
     * @return List<SampleOrderPriceVo>
     * @author ChenWenLong
     * @date 2024/1/25 16:54
     */
    public List<SampleOrderPriceVo> getSampleOrderPrice(SampleOrderPriceDto dto) {
        List<SampleOrderPriceResultBo> sampleOrderPriceResultBoList = developChildBaseService.getSampleOrderPriceBo(SampleOrderPriceBo.builder()
                .developPamphletOrderNo(dto.getDevelopPamphletOrderNo())
                .developSampleOrderNoList(dto.getDevelopSampleOrderNoList()).build());
        if (CollectionUtils.isEmpty(sampleOrderPriceResultBoList)) {
            return Collections.emptyList();
        }

        return sampleOrderPriceResultBoList.stream().map(sampleOrderPriceResultBo -> {
            SampleOrderPriceVo sampleOrderPriceVo = new SampleOrderPriceVo();
            sampleOrderPriceVo.setDevelopSampleOrderNo(sampleOrderPriceResultBo.getDevelopSampleOrderNo());
            sampleOrderPriceVo.setVersion(sampleOrderPriceResultBo.getVersion());
            sampleOrderPriceVo.setDevelopSampleMethod(sampleOrderPriceResultBo.getDevelopSampleMethod());
            sampleOrderPriceVo.setDevelopOrderPriceList(sampleOrderPriceResultBo.getDevelopOrderPriceList());
            sampleOrderPriceVo.setSkuBatchSamplePrice(sampleOrderPriceResultBo.getSamplePrice());
            return sampleOrderPriceVo;
        }).collect(Collectors.toList());
    }

}
