package com.hete.supply.scm.server.scm.settle.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseSettleOrderConverter;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseSettleOrderItemConverter;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseSettleOrderPayConverter;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderItemPo;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseChildOrderChangeSearchVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleChildOrderChangeSearchVo;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.dao.*;
import com.hete.supply.scm.server.scm.settle.entity.bo.PatrolPurchaseSettleOrderBo;
import com.hete.supply.scm.server.scm.settle.entity.bo.SettleSupplierBo;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderProductDto;
import com.hete.supply.scm.server.scm.settle.entity.dto.PurchaseSettleOrderUpdateDto;
import com.hete.supply.scm.server.scm.settle.entity.po.*;
import com.hete.supply.scm.server.scm.settle.entity.vo.*;
import com.hete.supply.scm.server.scm.settle.enums.PurchaseSettleExamine;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseDeliverOrderPo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购结算单基础类
 *
 * @author ChenWenLong
 * @date 2022/11/15 10:08
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PurchaseSettleOrderBaseService {
    private final PurchaseSettleOrderDao purchaseSettleOrderDao;
    private final PurchaseSettleOrderPayDao purchaseSettleOrderPayDao;
    private final SupplierBaseService supplierBaseService;
    private final ScmImageBaseService scmImageBaseService;
    private final PurchaseSettleOrderItemDao purchaseSettleOrderItemDao;
    private final PurchaseBaseService purchaseBaseService;
    private final SampleBaseService sampleBaseService;
    private final SupplementOrderBaseService supplementOrderBaseService;
    private final DeductOrderBaseService deductOrderBaseService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SupplementOrderDao supplementOrderDao;
    private final DeductOrderDao deductOrderDao;
    private final PurchaseDeliverOrderDao purchaseDeliverOrderDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final PurchaseDeliverOrderItemDao purchaseDeliverOrderItemDao;

    /**
     * 列表查询
     *
     * @author ChenWenLong
     * @date 2022/11/15 10:18
     */
    public CommonPageResult.PageInfo<PurchaseSettleOrderVo> searchPurchaseSettleOrder(PurchaseSettleOrderSearchDto purchaseSettleOrderSearchDto) {

        if (StringUtils.isNotBlank(purchaseSettleOrderSearchDto.getSupplementOrderNo()) || StringUtils.isNotBlank(purchaseSettleOrderSearchDto.getDeductOrderNo())) {
            String businessNo = StringUtils.isNotBlank(purchaseSettleOrderSearchDto.getSupplementOrderNo()) ? purchaseSettleOrderSearchDto.getSupplementOrderNo() : purchaseSettleOrderSearchDto.getDeductOrderNo();
            List<PurchaseSettleOrderItemPo> itemPoList = purchaseSettleOrderItemDao.getByBusinessNo(businessNo);
            if (CollectionUtil.isEmpty(itemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            purchaseSettleOrderSearchDto.setPurchaseSettleOrderIds(itemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId).collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(purchaseSettleOrderSearchDto.getBusinessNo())) {
            List<PurchaseSettleOrderItemPo> itemPoList = purchaseSettleOrderItemDao.getByBusinessNo(purchaseSettleOrderSearchDto.getBusinessNo());
            if (CollectionUtil.isEmpty(itemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            purchaseSettleOrderSearchDto.setPurchaseSettleOrderIds(itemPoList.stream().map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderId).collect(Collectors.toList()));
        }

        CommonPageResult.PageInfo<PurchaseSettleOrderVo> pageResult = purchaseSettleOrderDao.searchPurchaseSettleOrder(PageDTO.of(purchaseSettleOrderSearchDto.getPageNo(), purchaseSettleOrderSearchDto.getPageSize()), purchaseSettleOrderSearchDto);
        List<PurchaseSettleOrderVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }
        List<Long> idList = records.stream().map(PurchaseSettleOrderVo::getPurchaseSettleOrderId).collect(Collectors.toList());
        Map<Long, List<PurchaseSettleOrderPayPo>> purchaseSettleOrderPayPoMap = purchaseSettleOrderPayDao.getBatchPurchaseSettleOrderId(idList);
        List<PurchaseSettleOrderVo> newRecords = records.stream().peek(item -> {
            List<PurchaseSettleOrderPayPo> purchaseSettleOrderPayPoList = purchaseSettleOrderPayPoMap.get(item.getPurchaseSettleOrderId());
            if (CollectionUtil.isNotEmpty(purchaseSettleOrderPayPoList)) {
                BigDecimal alreadyPayPrice = purchaseSettleOrderPayPoList.stream().map(PurchaseSettleOrderPayPo::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                item.setAlreadyPayPrice(alreadyPayPrice);
                item.setWaitPayPrice(item.getPayPrice().subtract(alreadyPayPrice));
            } else {
                item.setAlreadyPayPrice(new BigDecimal(0));
                item.setWaitPayPrice(item.getPayPrice());
            }
        }).collect(Collectors.toList());
        pageResult.setRecords(newRecords);
        return pageResult;
    }

    /**
     * 详情
     *
     * @author ChenWenLong
     * @date 2022/11/15 10:19
     */
    public PurchaseSettleOrderDetailVo getPurchaseSettleOrderDetail(PurchaseSettleOrderDetailDto dto) {
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getById(dto.getPurchaseSettleOrderId());
        if (purchaseSettleOrderPo == null) {
            throw new ParamIllegalException("查询不到信息");
        }
        PurchaseSettleOrderDetailVo detailVo = PurchaseSettleOrderConverter.INSTANCE.convert(purchaseSettleOrderPo);
        SupplierPo supplier = supplierBaseService.getSupplierByCode(purchaseSettleOrderPo.getSupplierCode());
        if (supplier != null) {
            detailVo.setSupplierGrade(supplier.getSupplierGrade());
        }
        //获取支付列表
        List<PurchaseSettleOrderPayPo> purchaseSettleOrderPays = purchaseSettleOrderPayDao.getByPurchaseSettleOrderId(dto.getPurchaseSettleOrderId());
        List<PurchaseSettleOrderDetailVo.PurchaseSettleOrderPayVo> purchaseSettleOrderPayVos = PurchaseSettleOrderPayConverter.INSTANCE.purchaseSettleOrderPayList(purchaseSettleOrderPays);

        List<Long> payIdList = purchaseSettleOrderPays.stream().map(PurchaseSettleOrderPayPo::getPurchaseSettleOrderPayId).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(payIdList)) {
            Map<Long, List<String>> scmImageList = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.PURCHASE_SETTLE_PAY, payIdList);
            List<PurchaseSettleOrderDetailVo.PurchaseSettleOrderPayVo> purchaseSettleOrderPayVoList = purchaseSettleOrderPayVos.stream().peek(
                    item -> {
                        //获取凭证
                        if (scmImageList.containsKey(item.getPurchaseSettleOrderPayId())) {
                            item.setFileCodeList(scmImageList.get(item.getPurchaseSettleOrderPayId()));
                        }

                    }
            ).collect(Collectors.toList());
            detailVo.setPurchaseSettleOrderPayVoList(purchaseSettleOrderPayVoList);
        }

        return detailVo;
    }

    /**
     * 查询结算产品明细
     *
     * @author ChenWenLong
     * @date 2022/11/15 10:21
     */
    public PurchaseSettleOrderProductVo searchPurchaseSettleOrderProduct(PurchaseSettleOrderProductDto dto) {
        List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos = purchaseSettleOrderItemDao.searchPurchaseSettleOrderItem(dto);

        PurchaseSettleOrderProductVo purchaseSettleOrderProductVo = new PurchaseSettleOrderProductVo();

        BigDecimal totalSettlePrice = purchaseSettleOrderItemPos.stream().map(PurchaseSettleOrderItemPo::getSettlePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        purchaseSettleOrderProductVo.setTotalSettlePrice(totalSettlePrice);
        purchaseSettleOrderProductVo.setTotalNum(purchaseSettleOrderItemPos.size());
        List<PurchaseSettleOrderProductVo.PurchaseSettleOrderProductDetail> purchaseSettleOrderProductDetailList = PurchaseSettleOrderItemConverter.INSTANCE.purchaseSettleOrderItemList(purchaseSettleOrderItemPos);

        List<String> businessNoList = purchaseSettleOrderProductDetailList.stream().map(PurchaseSettleOrderProductVo.PurchaseSettleOrderProductDetail::getBusinessNo).distinct().collect(Collectors.toList());
        Map<String, String> purchaseDeliverOrderNoMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(businessNoList)) {
            purchaseDeliverOrderNoMap = purchaseDeliverOrderDao.getListByNoList(businessNoList)
                    .stream()
                    .collect(Collectors.toMap(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo, PurchaseDeliverOrderPo::getPurchaseChildOrderNo));
        }
        for (PurchaseSettleOrderProductVo.PurchaseSettleOrderProductDetail detail : purchaseSettleOrderProductDetailList) {
            detail.setPurchaseChildOrderNo(purchaseDeliverOrderNoMap.get(detail.getBusinessNo()));
        }
        purchaseSettleOrderProductVo.setPurchaseSettleOrderProductDetailList(purchaseSettleOrderProductDetailList);

        return purchaseSettleOrderProductVo;
    }

    /**
     * 审核
     *
     * @author ChenWenLong
     * @date 2022/11/15 10:22
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean examine(PurchaseSettleOrderExamineDto dto) {
        PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderDao.getByIdVersion(dto.getPurchaseSettleOrderId(), dto.getVersion());
        if (purchaseSettleOrderPo == null) {
            throw new ParamIllegalException("查询不到信息");
        }

        String user = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        LocalDateTime nowTime = new DateTime().toLocalDateTime();

        purchaseSettleOrderPo.setPurchaseSettleOrderId(purchaseSettleOrderPo.getPurchaseSettleOrderId());
        purchaseSettleOrderPo.setVersion(purchaseSettleOrderPo.getVersion());

        PurchaseSettleStatus purchaseSettleStatus = purchaseSettleOrderPo.getPurchaseSettleStatus();
        switch (dto.getPurchaseSettleExamine()) {
            case ALREADY_CONFIRM:
                PurchaseSettleStatus toSettle = purchaseSettleStatus.toSettle();
                purchaseSettleOrderPo.setPurchaseSettleStatus(toSettle);
                purchaseSettleOrderPo.setConfirmUser(user);
                purchaseSettleOrderPo.setConfirmUsername(username);
                purchaseSettleOrderPo.setConfirmTime(nowTime);
                break;
            case SETTLE_AGREE:
                PurchaseSettleStatus examine = purchaseSettleStatus.toExamine();
                purchaseSettleOrderPo.setPurchaseSettleStatus(examine);
                purchaseSettleOrderPo.setExamineUser(user);
                purchaseSettleOrderPo.setSettleRefuseRemarks(null);
                purchaseSettleOrderPo.setExamineUsername(username);
                purchaseSettleOrderPo.setExamineTime(nowTime);
                break;
            case SETTLE_REFUSE:
                PurchaseSettleStatus notExamine = purchaseSettleStatus.toNotExamine();
                purchaseSettleOrderPo.setPurchaseSettleStatus(notExamine);
                if (StringUtils.isBlank(dto.getSettleRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}备注", PurchaseSettleExamine.SETTLE_REFUSE.getRemark());
                }
                purchaseSettleOrderPo.setSettleRefuseRemarks(dto.getSettleRefuseRemarks());
                purchaseSettleOrderPo.setExamineUser(user);
                purchaseSettleOrderPo.setExamineUsername(username);
                purchaseSettleOrderPo.setExamineTime(nowTime);
                break;
            case EXAMINE_AGREE:
                PurchaseSettleStatus audited = purchaseSettleStatus.toAudited();
                purchaseSettleOrderPo.setPurchaseSettleStatus(audited);
                purchaseSettleOrderPo.setSettleUser(user);
                purchaseSettleOrderPo.setExamineRefuseRemarks(null);
                purchaseSettleOrderPo.setSettleUsername(username);
                purchaseSettleOrderPo.setSettleTime(nowTime);
                break;
            case EXAMINE_REFUSE:
                PurchaseSettleStatus notAudited = purchaseSettleStatus.toNotAudited();
                purchaseSettleOrderPo.setPurchaseSettleStatus(notAudited);
                if (StringUtils.isBlank(dto.getExamineRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}备注", PurchaseSettleExamine.EXAMINE_REFUSE.getRemark());
                }
                purchaseSettleOrderPo.setExamineRefuseRemarks(dto.getExamineRefuseRemarks());
                purchaseSettleOrderPo.setSettleUser(user);
                purchaseSettleOrderPo.setSettleUsername(username);
                purchaseSettleOrderPo.setSettleTime(nowTime);
                break;
            default:
                throw new ParamIllegalException("请求类型错误！");
        }
        purchaseSettleOrderDao.updateByIdVersion(purchaseSettleOrderPo);
        createStatusChangeLog(purchaseSettleOrderPo, dto.getPurchaseSettleExamine());
        return true;
    }

    /**
     * 更新
     *
     * @author ChenWenLong
     * @date 2022/11/15 10:24
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(PurchaseSettleOrderUpdateDto dto) {
        return true;
    }


    /**
     * 统计采购结算单
     *
     * @author ChenWenLong
     * @date 2022/11/21 15:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void countPurchaseSettleOrder(String supplierCode,
                                         LocalDateTime warehousingTime,
                                         String settleTime) {
        //月份
        String month;
        List<String> supplierCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(supplierCode)) {
            supplierCodeList.add(supplierCode);
        }

        //自定义统计月份
        LocalDateTime warehousingTimeStart;
        LocalDateTime warehousingTimeEnd;
        if (StringUtils.isBlank(settleTime)) {
            // 获取上个月的第一天
            warehousingTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
            // 获取上个月的最后一天
            warehousingTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
            //月份
            month = LocalDateTimeUtil.format(TimeUtil.utcConvertZone(TimeUtil.now(), TimeZoneId.CN), ScmConstant.SETTLE_ORDER_MONTH);
        } else {
            warehousingTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
            warehousingTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
            //月份
            month = warehousingTimeEnd.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        //查询是否已经生成过结算单
        List<PurchaseSettleOrderPo> purchaseSettleOrderPoList = purchaseSettleOrderDao.getListBySupplierCodeAndMonth(supplierCode, month);
        if (CollectionUtil.isNotEmpty(purchaseSettleOrderPoList)) {
            throw new BizException("采购结算单统计{}月份错误，数据库已经存在", month);
        }


        log.info("统计采购结算的月初时间：{}", warehousingTimeStart);
        log.info("统计采购结算的月末时间：{}", warehousingTimeEnd);

        //查询采购发货单数据
        List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByStatus(List.of(DeliverOrderStatus.WAREHOUSED), supplierCodeList, warehousingTimeStart, warehousingTimeEnd, null);
        Map<String, List<PurchaseDeliverOrderItemPo>> purchaseDeliverOrderItemPoMap = purchaseDeliverOrderItemDao.getListByDeliverOrderNoList(purchaseDeliverOrderPoList.stream()
                        .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo).collect(Collectors.toList()))
                .stream().collect(Collectors.groupingBy(PurchaseDeliverOrderItemPo::getPurchaseDeliverOrderNo));

        Map<String, BigDecimal> purchaseChildOrderPriceMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(purchaseDeliverOrderPoList)) {
            List<String> purchaseChildOrderNoList = purchaseDeliverOrderPoList.stream().map(PurchaseDeliverOrderPo::getPurchaseChildOrderNo).distinct().collect(Collectors.toList());
            purchaseChildOrderPriceMap = purchaseChildOrderItemDao.getListByChildNoList(purchaseChildOrderNoList)
                    .stream()
                    .collect(Collectors.toMap(PurchaseChildOrderItemPo::getPurchaseChildOrderNo,
                            PurchaseChildOrderItemPo::getSettlePrice,
                            (existingValue, newValue) -> existingValue));
        }
        Map<String, List<PurchaseDeliverOrderPo>> purchaseDeliverOrderPoMap = purchaseDeliverOrderPoList.stream().collect(Collectors.groupingBy(PurchaseDeliverOrderPo::getSupplierCode));


        //要插入的数据采购结算单
        List<PurchaseSettleOrderPo> purchaseSettleOrderPos = new ArrayList<>();

        //要插入的数据采购结算单详情
        List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos = new ArrayList<>();

        //已经增加的结算供应商
        Map<String, SettleSupplierBo> settleSupplier = new HashMap<>();

        //创建
        final Map<String, BigDecimal> finalPurchaseChildOrderPriceMap = purchaseChildOrderPriceMap;
        purchaseDeliverOrderPoMap.forEach((String key, List<PurchaseDeliverOrderPo> itemPos) -> {
            //生成结算编号
            String purchaseSettleOrderNo = idGenerateService.getConfuseCodeNoPrefix(ScmConstant.PURCHASE_SETTLE_ORDER_CODE, TimeType.CN_DAY_YYMM, ConfuseLength.L_4);
            // 采购结算单雪花id
            long purchaseSettleOrderId = idGenerateService.getSnowflakeId();

            String supplierName = itemPos.get(0).getSupplierName();

            // 统计结算金额
            BigDecimal totalPrice = BigDecimal.ZERO;

            //增加的结算供应商
            SettleSupplierBo settleSupplierBo = new SettleSupplierBo();
            settleSupplierBo.setPurchaseSettleOrderId(purchaseSettleOrderId);
            settleSupplierBo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
            settleSupplierBo.setSupplierName(supplierName);
            settleSupplier.put(key, settleSupplierBo);

            //创建发货单详情
            for (PurchaseDeliverOrderPo itemPo : itemPos) {
                BigDecimal settlePrice = Optional
                        .ofNullable(finalPurchaseChildOrderPriceMap.get(itemPo.getPurchaseChildOrderNo()))
                        .orElse(BigDecimal.ZERO);
                List<PurchaseDeliverOrderItemPo> purchaseDeliverOrderItemPos = Optional
                        .ofNullable(purchaseDeliverOrderItemPoMap.get(itemPo.getPurchaseDeliverOrderNo()))
                        .orElse(Collections.emptyList());
                int skuNum = purchaseDeliverOrderItemPos.stream().map(PurchaseDeliverOrderItemPo::getQualityGoodsCnt).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();
                settlePrice = settlePrice.multiply(BigDecimal.valueOf(skuNum));

                PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
                purchaseSettleOrderItemPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                purchaseSettleOrderItemPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                purchaseSettleOrderItemPo.setBusinessNo(itemPo.getPurchaseDeliverOrderNo());
                purchaseSettleOrderItemPo.setPurchaseSettleItemType(PurchaseSettleItemType.DELIVER);
                purchaseSettleOrderItemPo.setSettleTime(itemPo.getWarehousingTime());
                purchaseSettleOrderItemPo.setSkuNum(skuNum);
                purchaseSettleOrderItemPo.setSettlePrice(settlePrice);
                purchaseSettleOrderItemPo.setStatusName(itemPo.getDeliverOrderStatus().getRemark());
                purchaseSettleOrderItemPos.add(purchaseSettleOrderItemPo);
                totalPrice = totalPrice.add(settlePrice);
            }

            //增加采购结算单
            PurchaseSettleOrderPo purchaseSettleOrderPo = new PurchaseSettleOrderPo();
            purchaseSettleOrderPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
            purchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.WAIT_CONFIRM);
            purchaseSettleOrderPo.setMonth(month);
            purchaseSettleOrderPo.setSupplierCode(key);
            purchaseSettleOrderPo.setSupplierName(supplierName);
            purchaseSettleOrderPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
            purchaseSettleOrderPo.setTotalPrice(totalPrice);
            purchaseSettleOrderPo.setPayPrice(totalPrice);
            purchaseSettleOrderPos.add(purchaseSettleOrderPo);

        });


        //统计：样品采购单
        List<SampleChildOrderChangeSearchVo> sampleChildOrderChangeSearchVos = sampleBaseService.sampleChildOrderChangeSearch(supplierCode, warehousingTime, warehousingTimeStart,
                warehousingTimeEnd, SampleOrderStatus.SELECTED);
        Map<String, List<SampleChildOrderChangeSearchVo>> sampleItemPoGroup = sampleChildOrderChangeSearchVos.stream().collect(Collectors.groupingBy(SampleChildOrderChangeSearchVo::getSupplierCode));
        //创建
        sampleItemPoGroup.forEach((String key, List<SampleChildOrderChangeSearchVo> itemPos) -> {
            Long purchaseSettleOrderId;
            String purchaseSettleOrderNo;
            // 统计结算金额
            BigDecimal totalPrice = itemPos.stream().map(SampleChildOrderChangeSearchVo::getSettlePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleSupplier.get(key) == null) {
                //生成结算编号
                purchaseSettleOrderNo = idGenerateService.getConfuseCodeNoPrefix(ScmConstant.PURCHASE_SETTLE_ORDER_CODE, TimeType.CN_DAY_YYMM, ConfuseLength.L_4);
                // 采购结算单雪花id
                purchaseSettleOrderId = idGenerateService.getSnowflakeId();
                //创建采购结算单
                PurchaseSettleOrderPo newPurchaseSettleOrderPo = new PurchaseSettleOrderPo();
                newPurchaseSettleOrderPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                newPurchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.WAIT_CONFIRM);
                newPurchaseSettleOrderPo.setMonth(month);
                newPurchaseSettleOrderPo.setSupplierCode(key);
                String supplierName = itemPos.get(0).getSupplierName();
                newPurchaseSettleOrderPo.setSupplierName(supplierName);
                newPurchaseSettleOrderPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                newPurchaseSettleOrderPo.setTotalPrice(totalPrice);
                newPurchaseSettleOrderPo.setPayPrice(totalPrice);
                purchaseSettleOrderPos.add(newPurchaseSettleOrderPo);

                //增加的结算供应商
                SettleSupplierBo settleSupplierBo = new SettleSupplierBo();
                settleSupplierBo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                settleSupplierBo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                settleSupplierBo.setSupplierName(supplierName);
                settleSupplier.put(key, settleSupplierBo);

            } else {
                purchaseSettleOrderId = settleSupplier.get(key).getPurchaseSettleOrderId();
                purchaseSettleOrderNo = settleSupplier.get(key).getPurchaseSettleOrderNo();

                //追加结算金额
                for (int i = 0; i < purchaseSettleOrderPos.size(); i++) {
                    if (purchaseSettleOrderPos.get(i).getPurchaseSettleOrderNo().equals(settleSupplier.get(key).getPurchaseSettleOrderNo())) {
                        PurchaseSettleOrderPo newPurchaseSettleOrderPo = purchaseSettleOrderPos.get(i);
                        newPurchaseSettleOrderPo.setTotalPrice((purchaseSettleOrderPos.get(i).getTotalPrice()).add(totalPrice));
                        newPurchaseSettleOrderPo.setPayPrice((purchaseSettleOrderPos.get(i).getPayPrice()).add(totalPrice));
                        purchaseSettleOrderPos.set(i, newPurchaseSettleOrderPo);
                    }
                }

            }

            //创建详情
            for (SampleChildOrderChangeSearchVo itemPo : itemPos) {
                PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
                purchaseSettleOrderItemPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                purchaseSettleOrderItemPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                purchaseSettleOrderItemPo.setBusinessNo(itemPo.getSampleChildOrderNo());
                purchaseSettleOrderItemPo.setPurchaseSettleItemType(PurchaseSettleItemType.SAMPLE);
                purchaseSettleOrderItemPo.setSettleTime(itemPo.getSampleTime());
                purchaseSettleOrderItemPo.setSkuNum(1);
                purchaseSettleOrderItemPo.setSettlePrice(itemPo.getSettlePrice());
                purchaseSettleOrderItemPo.setStatusName(itemPo.getSampleOrderStatus().getRemark());
                purchaseSettleOrderItemPos.add(purchaseSettleOrderItemPo);
            }
        });


        //统计：补款单
        List<SupplementOrderPo> supplementOrderPos = supplementOrderBaseService.getBySupplierCodeAndExamineTime(supplierCodeList, warehousingTime, warehousingTimeStart,
                warehousingTimeEnd);
        Map<String, List<SupplementOrderPo>> supplementPoGroup = supplementOrderPos.stream().collect(Collectors.groupingBy(SupplementOrderPo::getSupplierCode));
        //创建
        supplementPoGroup.forEach((String key, List<SupplementOrderPo> itemPos) -> {
            Long purchaseSettleOrderId;
            String purchaseSettleOrderNo;
            // 统计结算金额
            BigDecimal totalPrice = itemPos.stream().map(SupplementOrderPo::getSupplementPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleSupplier.get(key) == null) {
                //生成结算编号
                purchaseSettleOrderNo = idGenerateService.getConfuseCodeNoPrefix(ScmConstant.PURCHASE_SETTLE_ORDER_CODE, TimeType.CN_DAY_YYMM, ConfuseLength.L_4);
                // 采购结算单雪花id
                purchaseSettleOrderId = idGenerateService.getSnowflakeId();
                //创建采购结算单
                PurchaseSettleOrderPo newPurchaseSettleOrderPo = new PurchaseSettleOrderPo();
                newPurchaseSettleOrderPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                newPurchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.WAIT_CONFIRM);
                newPurchaseSettleOrderPo.setMonth(month);
                newPurchaseSettleOrderPo.setSupplierCode(key);
                String supplierName = itemPos.get(0).getSupplierName();
                newPurchaseSettleOrderPo.setSupplierName(supplierName);
                newPurchaseSettleOrderPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                newPurchaseSettleOrderPo.setTotalPrice(totalPrice);
                newPurchaseSettleOrderPo.setPayPrice(totalPrice);
                purchaseSettleOrderPos.add(newPurchaseSettleOrderPo);

                //增加的结算供应商
                SettleSupplierBo settleSupplierBo = new SettleSupplierBo();
                settleSupplierBo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                settleSupplierBo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                settleSupplierBo.setSupplierName(supplierName);
                settleSupplier.put(key, settleSupplierBo);

            } else {
                purchaseSettleOrderId = settleSupplier.get(key).getPurchaseSettleOrderId();
                purchaseSettleOrderNo = settleSupplier.get(key).getPurchaseSettleOrderNo();

                //追加结算金额
                for (int i = 0; i < purchaseSettleOrderPos.size(); i++) {
                    if (purchaseSettleOrderPos.get(i).getPurchaseSettleOrderNo().equals(settleSupplier.get(key).getPurchaseSettleOrderNo())) {
                        PurchaseSettleOrderPo newPurchaseSettleOrderPo = purchaseSettleOrderPos.get(i);
                        newPurchaseSettleOrderPo.setTotalPrice((purchaseSettleOrderPos.get(i).getTotalPrice()).add(totalPrice));
                        newPurchaseSettleOrderPo.setPayPrice((purchaseSettleOrderPos.get(i).getPayPrice()).add(totalPrice));
                        purchaseSettleOrderPos.set(i, newPurchaseSettleOrderPo);
                    }
                }

            }

            //创建详情
            for (SupplementOrderPo itemPo : itemPos) {
                PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
                purchaseSettleOrderItemPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                purchaseSettleOrderItemPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                purchaseSettleOrderItemPo.setBusinessNo(itemPo.getSupplementOrderNo());
                purchaseSettleOrderItemPo.setPurchaseSettleItemType(PurchaseSettleItemType.REPLENISH);
                purchaseSettleOrderItemPo.setSettleTime(itemPo.getExamineTime());
                purchaseSettleOrderItemPo.setSkuNum(0);
                purchaseSettleOrderItemPo.setSettlePrice(itemPo.getSupplementPrice());
                purchaseSettleOrderItemPo.setStatusName(itemPo.getSupplementStatus().getRemark());
                purchaseSettleOrderItemPos.add(purchaseSettleOrderItemPo);
            }
        });


        //统计：扣款单
        List<DeductOrderDetailVo> deductOrderDetailVos = deductOrderBaseService.getBySupplierCodeAndExamineTime(supplierCodeList, warehousingTime, warehousingTimeStart,
                warehousingTimeEnd);
        Map<String, List<DeductOrderDetailVo>> deductPoGroup = deductOrderDetailVos.stream().collect(Collectors.groupingBy(DeductOrderDetailVo::getSupplierCode));
        //创建
        deductPoGroup.forEach((String key, List<DeductOrderDetailVo> itemPos) -> {
            Long purchaseSettleOrderId;
            String purchaseSettleOrderNo;
            // 统计扣款金额
            BigDecimal deductPrice = itemPos.stream().map(DeductOrderDetailVo::getDeductPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleSupplier.get(key) == null) {
                //生成结算编号
                purchaseSettleOrderNo = idGenerateService.getConfuseCodeNoPrefix(ScmConstant.PURCHASE_SETTLE_ORDER_CODE, TimeType.CN_DAY_YYMM, ConfuseLength.L_4);
                // 采购结算单雪花id
                purchaseSettleOrderId = idGenerateService.getSnowflakeId();
                //创建采购结算单
                PurchaseSettleOrderPo newPurchaseSettleOrderPo = new PurchaseSettleOrderPo();
                newPurchaseSettleOrderPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                newPurchaseSettleOrderPo.setPurchaseSettleStatus(PurchaseSettleStatus.WAIT_CONFIRM);
                newPurchaseSettleOrderPo.setMonth(month);
                newPurchaseSettleOrderPo.setSupplierCode(key);
                String supplierName = itemPos.get(0).getSupplierName();
                newPurchaseSettleOrderPo.setSupplierName(supplierName);
                newPurchaseSettleOrderPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                newPurchaseSettleOrderPo.setDeductPrice(deductPrice);
                purchaseSettleOrderPos.add(newPurchaseSettleOrderPo);

                //增加的结算供应商
                SettleSupplierBo settleSupplierBo = new SettleSupplierBo();
                settleSupplierBo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                settleSupplierBo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                settleSupplierBo.setSupplierName(supplierName);
                settleSupplier.put(key, settleSupplierBo);

            } else {
                purchaseSettleOrderId = settleSupplier.get(key).getPurchaseSettleOrderId();
                purchaseSettleOrderNo = settleSupplier.get(key).getPurchaseSettleOrderNo();

                //追加结算金额
                for (int i = 0; i < purchaseSettleOrderPos.size(); i++) {
                    if (purchaseSettleOrderPos.get(i).getPurchaseSettleOrderNo().equals(settleSupplier.get(key).getPurchaseSettleOrderNo())) {
                        PurchaseSettleOrderPo newPurchaseSettleOrderPo = purchaseSettleOrderPos.get(i);
                        if (purchaseSettleOrderPos.get(i).getDeductPrice() == null) {
                            newPurchaseSettleOrderPo.setDeductPrice(deductPrice);
                        } else {
                            newPurchaseSettleOrderPo.setDeductPrice((purchaseSettleOrderPos.get(i).getDeductPrice()).add(deductPrice));
                        }
                        newPurchaseSettleOrderPo.setPayPrice((purchaseSettleOrderPos.get(i).getPayPrice()).subtract(deductPrice));
                        purchaseSettleOrderPos.set(i, newPurchaseSettleOrderPo);
                    }
                }

            }

            //创建详情
            for (DeductOrderDetailVo itemPo : itemPos) {
                PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
                purchaseSettleOrderItemPo.setPurchaseSettleOrderId(purchaseSettleOrderId);
                purchaseSettleOrderItemPo.setPurchaseSettleOrderNo(purchaseSettleOrderNo);
                purchaseSettleOrderItemPo.setBusinessNo(itemPo.getDeductOrderNo());
                purchaseSettleOrderItemPo.setPurchaseSettleItemType(PurchaseSettleItemType.DEDUCT);
                purchaseSettleOrderItemPo.setSettleTime(itemPo.getExamineTime());
                purchaseSettleOrderItemPo.setSkuNum(0);
                purchaseSettleOrderItemPo.setSettlePrice(itemPo.getDeductPrice());
                purchaseSettleOrderItemPo.setStatusName(itemPo.getDeductStatus().getRemark());
                purchaseSettleOrderItemPos.add(purchaseSettleOrderItemPo);
            }

        });
        //执行数据插入动作
        this.purchaseSettleOrderInsertBatch(purchaseSettleOrderPos, purchaseSettleOrderItemPos);

    }


    public List<PurchaseSettleSimpleVo> getPurchaseSettleByBusinessNo(List<String> childOrderNoList) {
        List<PurchaseSettleOrderItemPo> settleOrderItemPoList = purchaseSettleOrderItemDao.getByBusinessNoList(childOrderNoList);
        final List<String> settleOrderNoList = settleOrderItemPoList.stream()
                .map(PurchaseSettleOrderItemPo::getPurchaseSettleOrderNo)
                .distinct()
                .collect(Collectors.toList());

        Map<String, PurchaseSettleOrderPo> purchaseSettleOrderMap = purchaseSettleOrderDao.getMapByNoList(settleOrderNoList);

        return settleOrderItemPoList.stream()
                .map(po -> {
                    final PurchaseSettleOrderPo purchaseSettleOrderPo = purchaseSettleOrderMap.get(po.getPurchaseSettleOrderNo());
                    if (null == purchaseSettleOrderPo) {
                        return null;
                    }
                    final PurchaseSettleSimpleVo purchaseSettleSimpleVo = new PurchaseSettleSimpleVo();
                    purchaseSettleSimpleVo.setPurchaseSettleOrderNo(po.getPurchaseSettleOrderNo());
                    purchaseSettleSimpleVo.setPurchaseSettleStatus(purchaseSettleOrderPo.getPurchaseSettleStatus());
                    purchaseSettleSimpleVo.setPayPrice(purchaseSettleOrderPo.getPayPrice());
                    purchaseSettleSimpleVo.setPayTime(purchaseSettleOrderPo.getPayTime());
                    return purchaseSettleSimpleVo;
                }).collect(Collectors.toList());
    }

    /**
     * 日志
     *
     * @param purchaseSettleOrderPo
     */
    public void createStatusChangeLog(PurchaseSettleOrderPo purchaseSettleOrderPo, PurchaseSettleExamine purchaseSettleExamine) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.PURCHASE_SETTLE_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.PURCHASE_SETTLE_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.PURCHASE_SETTLE_STATUS.name());
        bizLogCreateMqDto.setBizCode(purchaseSettleOrderPo.getPurchaseSettleOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());

        String userKey = StringUtils.isNotBlank(GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
        String username = StringUtils.isNotBlank(GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
        bizLogCreateMqDto.setOperateUser(userKey);
        bizLogCreateMqDto.setOperateUsername(username);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();


        if (PurchaseSettleExamine.SETTLE_REFUSE.equals(purchaseSettleExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(PurchaseSettleExamine.SETTLE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(purchaseSettleOrderPo.getSettleRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        if (PurchaseSettleExamine.EXAMINE_REFUSE.equals(purchaseSettleExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(PurchaseSettleExamine.EXAMINE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(purchaseSettleOrderPo.getExamineRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        bizLogCreateMqDto.setContent(purchaseSettleOrderPo.getPurchaseSettleStatus().getName());

        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }

    /**
     * 结算统计执行批量插入
     *
     * @author ChenWenLong
     * @date 2023/2/15 16:21
     */
    public void purchaseSettleOrderInsertBatch(List<PurchaseSettleOrderPo> purchaseSettleOrderPos, List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPos) {

        purchaseSettleOrderDao.insertBatch(purchaseSettleOrderPos);
        purchaseSettleOrderItemDao.insertBatch(purchaseSettleOrderItemPos);

        //创建日志记录
        Map<String, PurchaseSettleOrderPo> mapList = purchaseSettleOrderDao.getMapByNoList(purchaseSettleOrderPos.stream().map(PurchaseSettleOrderPo::getPurchaseSettleOrderNo).collect(Collectors.toList()));
        mapList.forEach((String key, PurchaseSettleOrderPo val) -> {
            this.createStatusChangeLog(val, null);
        });
    }


    /**
     * 巡检结算单数据
     *
     * @param settleTime:格式yyyy-MM
     * @return void
     * @author ChenWenLong
     * @date 2023/6/5 15:12
     */
    public void patrolSettleOrder(String settleTime) {
        LocalDateTime settleTimeStart;
        LocalDateTime settleTimeEnd;
        if (StringUtils.isBlank(settleTime)) {
            // 获取上个月的第一天
            settleTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
            // 获取上个月的最后一天
            settleTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonth(), TimeZoneId.CN, TimeZoneId.UTC);
        } else {
            settleTimeStart = TimeUtil.convertZone(ScmTimeUtil.getStartOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
            settleTimeEnd = TimeUtil.convertZone(ScmTimeUtil.getEndOfLastMonthByString(settleTime), TimeZoneId.CN, TimeZoneId.UTC);
        }

        //月份
        String month = settleTimeEnd.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String settleMonth = settleTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        //获取月份结算单
        List<PurchaseSettleOrderPo> purchaseSettleOrderPoList = purchaseSettleOrderDao.getListBySupplierCodeAndMonth(null, month);
        if (CollectionUtils.isEmpty(purchaseSettleOrderPoList)) {
            log.info("巡检采购结算单数据{}月份时未发现生成结算单", settleMonth);
            return;
        }
        Map<String, PurchaseSettleOrderPo> purchaseSettleOrderPoMap = purchaseSettleOrderPoList.stream().collect(Collectors.toMap(PurchaseSettleOrderPo::getSupplierCode, purchaseSettleOrderPo -> purchaseSettleOrderPo));
        //获取月全部供应商代码
        List<String> supplierCodeList = purchaseSettleOrderPoList.stream()
                .map(PurchaseSettleOrderPo::getSupplierCode).collect(Collectors.toList());
        //获取结算单号
        List<String> purchaseSettleOrderNoList = purchaseSettleOrderPoList.stream().map(PurchaseSettleOrderPo::getPurchaseSettleOrderNo).collect(Collectors.toList());

        //获取结算详情列表
        Map<String, List<PurchaseSettleOrderItemPo>> purchaseSettleOrderItemPoMap = purchaseSettleOrderItemDao.getMapByBatchPurchaseSettleOrderNo(purchaseSettleOrderPoList.stream().map(PurchaseSettleOrderPo::getPurchaseSettleOrderNo).collect(Collectors.toList()));

        //查询大货采购单、加工采购单
        List<PurchaseBizType> purchaseBizTypeList = new ArrayList<>();
        purchaseBizTypeList.add(PurchaseBizType.PRODUCT);
        purchaseBizTypeList.add(PurchaseBizType.PROCESS);
        final List<PurchaseChildOrderChangeSearchVo> purchaseChildOrderChangeSearchVos = purchaseBaseService.purchaseChildOrderChangeSearch(supplierCodeList,
                purchaseBizTypeList,
                null,
                settleTimeStart,
                settleTimeEnd,
                PurchaseOrderStatus.WAREHOUSED);
        final Map<String, List<PurchaseChildOrderChangeSearchVo>> purchaseItemPoGroup = purchaseChildOrderChangeSearchVos.stream()
                .collect(Collectors.groupingBy(PurchaseChildOrderChangeSearchVo::getSupplierCode));

        //查询补款单
        final List<SupplementOrderPo> supplementOrderPoList = supplementOrderDao.getByExamineTimeAndSettleOrderNo(
                settleTimeStart,
                settleTimeEnd,
                purchaseSettleOrderNoList,
                List.of(SupplementType.PRICE, SupplementType.OTHER),
                supplierCodeList,
                Collections.emptyList());
        final Map<String, List<SupplementOrderPo>> supplementOrderPoMap = supplementOrderPoList.stream().collect(Collectors.groupingBy(SupplementOrderPo::getSupplierCode));

        //查询扣款单
        final List<DeductOrderPo> deductOrderPoList = deductOrderDao.getByExamineTimeAndSettleOrderNo(
                settleTimeStart,
                settleTimeEnd,
                purchaseSettleOrderNoList,
                List.of(DeductType.PRICE, DeductType.QUALITY, DeductType.OTHER, DeductType.PAY, DeductType.DEFECTIVE_RETURN),
                supplierCodeList,
                Collections.emptyList());
        final Map<String, List<DeductOrderPo>> deductOrderPoMap = deductOrderPoList.stream().collect(Collectors.groupingBy(DeductOrderPo::getSupplierCode));

        //查询采购发货单数据
        List<PurchaseDeliverOrderPo> purchaseDeliverOrderPoList = purchaseDeliverOrderDao.getListByStatus(List.of(DeliverOrderStatus.WAREHOUSED), supplierCodeList, settleTimeStart, settleTimeEnd, null);
        Map<String, List<PurchaseDeliverOrderPo>> purchaseDeliverOrderPoMap = purchaseDeliverOrderPoList.stream().collect(Collectors.groupingBy(PurchaseDeliverOrderPo::getSupplierCode));

        //结算单数据检验
        List<PatrolPurchaseSettleOrderBo> patrolPurchaseSettleOrderBoList = this.verifySettleOrder(
                purchaseSettleOrderPoMap,
                purchaseSettleOrderItemPoMap,
                purchaseItemPoGroup,
                supplementOrderPoMap,
                deductOrderPoMap,
                purchaseDeliverOrderPoMap
        );

        //推送日志
        if (patrolPurchaseSettleOrderBoList.stream().anyMatch(w -> CollectionUtils.isNotEmpty(w.getPurchaseSettleItemTypeList()))) {
            log.error("巡检采购结算单数据{}月份时发现数据异常。bo={}", settleMonth, JacksonUtil.parse2Str(patrolPurchaseSettleOrderBoList));
        } else {
            log.info("巡检采购结算单数据{}月份时未发现问题。bo={}", settleMonth, JacksonUtil.parse2Str(patrolPurchaseSettleOrderBoList));
        }

    }


    /**
     * 结算单数据检验
     *
     * @param purchaseSettleOrderPoMap:
     * @param purchaseSettleOrderItemPoMap:
     * @param purchaseItemPoGroup:
     * @param supplementOrderPoMap:
     * @param deductOrderPoMap:
     * @param purchaseDeliverOrderPoMap:
     * @return List<PatrolSettleOrderBo>
     * @author ChenWenLong
     * @date 2023/6/6 15:39
     */
    public List<PatrolPurchaseSettleOrderBo> verifySettleOrder(Map<String, PurchaseSettleOrderPo> purchaseSettleOrderPoMap,
                                                               Map<String, List<PurchaseSettleOrderItemPo>> purchaseSettleOrderItemPoMap,
                                                               Map<String, List<PurchaseChildOrderChangeSearchVo>> purchaseItemPoGroup,
                                                               Map<String, List<SupplementOrderPo>> supplementOrderPoMap,
                                                               Map<String, List<DeductOrderPo>> deductOrderPoMap,
                                                               Map<String, List<PurchaseDeliverOrderPo>> purchaseDeliverOrderPoMap) {
        //需要记录日志的BO
        List<PatrolPurchaseSettleOrderBo> patrolPurchaseSettleOrderBoList = new ArrayList<>();

        //循环每供应商数据对比
        purchaseSettleOrderPoMap.forEach((String supplierCode, PurchaseSettleOrderPo purchaseSettleOrderPo) -> {
            PatrolPurchaseSettleOrderBo patrolPurchaseSettleOrderBo = new PatrolPurchaseSettleOrderBo();
            List<PurchaseSettleItemType> purchaseSettleItemTypeList = new ArrayList<>();
            patrolPurchaseSettleOrderBo.setPurchaseSettleItemTypeList(new ArrayList<>());
            final List<PurchaseSettleOrderItemPo> purchaseSettleOrderItemPoList = Optional.ofNullable(purchaseSettleOrderItemPoMap.get(purchaseSettleOrderPo.getPurchaseSettleOrderNo()))
                    .orElse(Collections.emptyList());
            final List<PurchaseChildOrderChangeSearchVo> purchaseChildOrderChangeSearchVoList = Optional.ofNullable(purchaseItemPoGroup.get(supplierCode))
                    .orElse(Collections.emptyList());
            // 判断大货采购单、加工采购单数据检验
            List<String> purchaseOrderNoList = purchaseSettleOrderItemPoList.stream()
                    .filter(w -> PurchaseSettleItemType.PRODUCT_PURCHASE.equals(w.getPurchaseSettleItemType())
                            || PurchaseSettleItemType.PROCESS_PURCHASE.equals(w.getPurchaseSettleItemType()))
                    .map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList());

            boolean purchaseExist = purchaseOrderNoList.size() == purchaseChildOrderChangeSearchVoList.size() &&
                    purchaseChildOrderChangeSearchVoList.stream()
                            .map(PurchaseChildOrderChangeSearchVo::getPurchaseChildOrderNo)
                            .allMatch(purchaseOrderNoList::contains);

            if (!purchaseExist) {
                purchaseSettleItemTypeList.add(PurchaseSettleItemType.PRODUCT_PURCHASE);
                //记录采购原始数据
                patrolPurchaseSettleOrderBo.setPurchaseChildOrderChangeSearchVoList(purchaseChildOrderChangeSearchVoList.stream().map(
                        vo -> {
                            PurchaseChildOrderChangeSearchVo purchaseChildOrderChangeSearchVo = new PurchaseChildOrderChangeSearchVo();
                            purchaseChildOrderChangeSearchVo.setPurchaseChildOrderNo(vo.getPurchaseChildOrderNo());
                            return purchaseChildOrderChangeSearchVo;
                        }
                ).collect(Collectors.toList()));
            }


            // 判断补款单数据检验
            final List<String> supplementNoList = purchaseSettleOrderItemPoList.stream()
                    .filter(w -> PurchaseSettleItemType.REPLENISH.equals(w.getPurchaseSettleItemType()))
                    .map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList());

            List<SupplementOrderPo> supplementOrderPoList = Optional.ofNullable(supplementOrderPoMap.get(supplierCode))
                    .orElse(Collections.emptyList());

            boolean supplementExist = supplementNoList.size() == supplementOrderPoList.size() &&
                    supplementOrderPoList.stream()
                            .map(SupplementOrderPo::getSupplementOrderNo)
                            .allMatch(supplementNoList::contains);

            if (!supplementExist) {
                purchaseSettleItemTypeList.add(PurchaseSettleItemType.REPLENISH);
                //记录补款原始数据
                patrolPurchaseSettleOrderBo.setSupplementOrderPoList(supplementOrderPoMap.get(supplierCode));
            }


            // 判断扣款单数据检验
            List<String> deductNoList = purchaseSettleOrderItemPoList.stream()
                    .filter(w -> PurchaseSettleItemType.DEDUCT.equals(w.getPurchaseSettleItemType()))
                    .map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList());

            List<DeductOrderPo> deductOrderPoList = Optional.ofNullable(deductOrderPoMap.get(supplierCode))
                    .orElse(Collections.emptyList());

            boolean deductExist = deductNoList.size() == deductOrderPoList.size() &&
                    deductOrderPoList.stream()
                            .map(DeductOrderPo::getDeductOrderNo)
                            .allMatch(deductNoList::contains);

            if (!deductExist) {
                purchaseSettleItemTypeList.add(PurchaseSettleItemType.DEDUCT);
                //记录扣款原始数据
                patrolPurchaseSettleOrderBo.setDeductOrderPoList(deductOrderPoMap.get(supplierCode));
            }

            // 判断发货单数据检验
            List<String> deliverNoList = purchaseSettleOrderItemPoList.stream()
                    .filter(w -> PurchaseSettleItemType.DELIVER.equals(w.getPurchaseSettleItemType()))
                    .map(PurchaseSettleOrderItemPo::getBusinessNo).collect(Collectors.toList());

            List<PurchaseDeliverOrderPo> deliverOrderPoList = Optional.ofNullable(purchaseDeliverOrderPoMap.get(supplierCode))
                    .orElse(Collections.emptyList());

            boolean deliverExist = deliverNoList.size() == deliverOrderPoList.size() &&
                    deliverOrderPoList.stream()
                            .map(PurchaseDeliverOrderPo::getPurchaseDeliverOrderNo)
                            .allMatch(deductNoList::contains);

            if (!deliverExist) {
                purchaseSettleItemTypeList.add(PurchaseSettleItemType.DELIVER);
                //记录发货单原始数据
                patrolPurchaseSettleOrderBo.setPurchaseDeliverOrderPoList(purchaseDeliverOrderPoMap.get(supplierCode));
            }

            patrolPurchaseSettleOrderBo.setPurchaseSettleItemTypeList(purchaseSettleItemTypeList);
            patrolPurchaseSettleOrderBo.setSupplierCode(supplierCode);
            patrolPurchaseSettleOrderBo.setPurchaseSettleOrderItemPoList(purchaseSettleOrderItemPoList.stream()
                    .map(po -> {
                        PurchaseSettleOrderItemPo purchaseSettleOrderItemPo = new PurchaseSettleOrderItemPo();
                        purchaseSettleOrderItemPo.setBusinessNo(po.getBusinessNo());
                        purchaseSettleOrderItemPo.setPurchaseSettleItemType(po.getPurchaseSettleItemType());
                        return purchaseSettleOrderItemPo;
                    }).collect(Collectors.toList()));
            patrolPurchaseSettleOrderBoList.add(patrolPurchaseSettleOrderBo);
        });

        return patrolPurchaseSettleOrderBoList;
    }

}
