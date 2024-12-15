package com.hete.supply.scm.server.scm.develop.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DevelopChildSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.DevelopChildOrderExportVo;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.adjust.dao.ChannelDao;
import com.hete.supply.scm.server.scm.develop.converter.*;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.bo.*;
import com.hete.supply.scm.server.scm.develop.entity.dto.*;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.handler.DevelopStatusHandler;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCancelEventDto;
import com.hete.supply.scm.server.scm.process.handler.WmsProcessCancelHandler;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderChangeDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseParentOrderDao;
import com.hete.supply.scm.server.scm.purchase.entity.bo.WmsDeliverBo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderChangePo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseParentOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRawRefService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.support.api.entity.vo.ExportationListResultBo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.CommonResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ChenWenLong
 * @date 2023/8/10 16:46
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevelopChildBaseService {

    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final DevelopReviewOrderDao developReviewOrderDao;
    private final DevelopPricingOrderDao developPricingOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final LogBaseService logBaseService;
    private final PurchaseRawRefService purchaseRawRefService;
    private final DevelopPamphletOrderRawDao developPamphletOrderRawDao;
    private final IdGenerateService idGenerateService;
    private final DevelopChildOrderChangeDao developChildOrderChangeDao;
    private final DevelopChildOrderAttrDao developChildOrderAttrDao;
    private final ScmImageBaseService scmImageBaseService;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final DevelopSampleOrderRawDao developSampleOrderRawDao;
    private final PlmRemoteService plmRemoteService;
    private final DevelopSampleOrderProcessDao developSampleOrderProcessDao;
    private final DevelopSampleOrderProcessDescDao developSampleOrderProcessDescDao;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final DevelopExceptionOrderDao developExceptionOrderDao;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderChangeDao purchaseChildOrderChangeDao;
    private final PurchaseParentOrderDao purchaseParentOrderDao;
    private final DevelopOrderPriceDao developOrderPriceDao;
    private final ChannelDao channelDao;


    /**
     * 日志操作内容KEY
     *
     * @author ChenWenLong
     * @date 2024/4/7 10:26
     */
    private final static String LOG_KEY = "操作内容";

    /**
     * 开发子单公共列表查询方法
     *
     * @param dto:
     * @return PageInfo<DevelopChildSearchVo>
     * @author ChenWenLong
     * @date 2024/4/12 09:45
     */
    public CommonPageResult.PageInfo<DevelopChildSearchVo> searchDevelopChild(DevelopChildSearchDto dto) {
        if (null == this.getSearchDevelopChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<DevelopChildSearchVo> pageResult = developChildOrderDao.searchDevelopChild(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<DevelopChildSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        // 处理时间问题
        records.forEach(record -> {
            final DevelopPamphletMsgVo developPamphletMsgVo = record.getDevelopPamphletMsgVo();
            if (developPamphletMsgVo != null && developPamphletMsgVo.getExpectedOnShelvesDate().toEpochSecond(ZoneOffset.UTC) == 0) {
                developPamphletMsgVo.setExpectedOnShelvesDate(null);
            }

            final DevelopOnShelvesMsgVo developOnShelvesMsgVo = record.getDevelopOnShelvesMsgVo();
            if (developOnShelvesMsgVo != null && developOnShelvesMsgVo.getExpectedOnShelvesDate().toEpochSecond(ZoneOffset.UTC) == 0) {
                developOnShelvesMsgVo.setExpectedOnShelvesDate(null);
            }
            if (developOnShelvesMsgVo != null && developOnShelvesMsgVo.getOnShelvesCompletionDate().toEpochSecond(ZoneOffset.UTC) == 0) {
                developOnShelvesMsgVo.setOnShelvesCompletionDate(null);
            }
            if (developOnShelvesMsgVo != null && developOnShelvesMsgVo.getNewestCompletionDate().toEpochSecond(ZoneOffset.UTC) == 0) {
                developOnShelvesMsgVo.setNewestCompletionDate(null);
            }

        });

        List<String> developChildOrderNoList = records.stream()
                .map(DevelopChildSearchVo::getDevelopChildBaseMsgVo)
                .map(DevelopChildBaseMsgVo::getDevelopChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        //获取样品单
        List<DevelopSampleOrderPo> developSampleOrderPoChildList = developSampleOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoMap = developSampleOrderPoChildList
                .stream().collect(Collectors.groupingBy(DevelopSampleOrderPo::getDevelopChildOrderNo));


        //获取核价
        Map<String, List<DevelopPricingOrderInfoPo>> developPricingOrderPoMap = developPricingOrderInfoDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopPricingOrderInfoPo::getDevelopChildOrderNo));

        //获取版单
        Map<String, List<DevelopPamphletOrderPo>> developPamphletOrderPoMap = developPamphletOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopPamphletOrderPo::getDevelopChildOrderNo));

        //获取版单原料
        Map<String, List<DevelopPamphletOrderRawPo>> developPamphletOrderPoRawMap = developPamphletOrderRawDao.getListByDevelopChildOrderNoListAndType(developChildOrderNoList, SampleRawBizType.FORMULA)
                .stream().collect(Collectors.groupingBy(DevelopPamphletOrderRawPo::getDevelopPamphletOrderNo));

        //获取审版单
        final List<DevelopReviewOrderPo> developReviewOrderPoList = developReviewOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        Map<String, List<DevelopReviewOrderPo>> developReviewOrderPoMap = developReviewOrderPoList.stream()
                .collect(Collectors.groupingBy(DevelopReviewOrderPo::getDevelopPamphletOrderNo));

        // 获取产前样采购单
        final List<String> purchaseChildOrderNoList = records.stream()
                .filter(record -> record.getDevelopPrenatalFirstMsgVo() != null)
                .flatMap(record -> Stream.of(record.getDevelopPrenatalFirstMsgVo().getPrenatalSampleOrderNo(),
                        record.getDevelopPrenatalFirstMsgVo().getFirstSampleOrderNo()))
                .collect(Collectors.toList());

        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        //获取核价单
        List<DevelopPricingOrderPo> developPricingOrderPoList = developPricingOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        Map<String, List<DevelopPricingOrderPo>> developPricingOrderMap = developPricingOrderPoList.stream()
                .collect(Collectors.groupingBy(DevelopPricingOrderPo::getDevelopPamphletOrderNo));
        //核价单获取样品单
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoPricingMap = developSampleOrderDao.getListByDevelopPricingOrderNoList(developPricingOrderPoList.stream()
                        .map(DevelopPricingOrderPo::getDevelopPricingOrderNo).collect(Collectors.toList()))
                .stream().collect(Collectors.groupingBy(DevelopSampleOrderPo::getDevelopPricingOrderNo));
        // 获取图片
        final List<Long> developChildOrderIdList = records.stream()
                .map(DevelopChildSearchVo::getDevelopChildOrderId)
                .collect(Collectors.toList());
        final Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_CHILD_STYLE, developChildOrderIdList);

        //获取数据
        for (DevelopChildSearchVo record : records) {
            DevelopSupplierPriceVo developSupplierPriceVo = record.getDevelopSupplierPriceVo() == null ? new DevelopSupplierPriceVo() : record.getDevelopSupplierPriceVo();
            DevelopPamphletMsgVo developPamphletMsgVo = record.getDevelopPamphletMsgVo() == null ? new DevelopPamphletMsgVo() : record.getDevelopPamphletMsgVo();
            DevelopReviewMsgVo developReviewMsgVo = record.getDevelopReviewMsgVo() == null ? new DevelopReviewMsgVo() : record.getDevelopReviewMsgVo();
            DevelopPricingMsgVo developPricingMsgVo = record.getDevelopPricingMsgVo() == null ? new DevelopPricingMsgVo() : record.getDevelopPricingMsgVo();

            //开发子单对应的样品单
            List<DevelopSampleOrderPo> developSampleOrderPoList = Optional.ofNullable(developSampleOrderPoMap.get(record.getDevelopChildBaseMsgVo().getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(DevelopSampleOrderPo::getCreateTime).reversed())
                    .collect(Collectors.toList());
            //核价单
            List<DevelopPricingOrderInfoPo> developPricingOrderInfoPoList = Optional.ofNullable(developPricingOrderPoMap.get(record.getDevelopChildBaseMsgVo().getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList());

            //打版信息
            Optional.ofNullable(developPamphletOrderPoMap.get(record.getDevelopChildBaseMsgVo().getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .max(Comparator.comparing(DevelopPamphletOrderPo::getCreateTime))
                    .ifPresent(developPamphletOrderPo -> {
                        developPamphletMsgVo.setDevelopPamphletOrderId(developPamphletOrderPo.getDevelopPamphletOrderId());
                        developPamphletMsgVo.setVersion(developPamphletOrderPo.getVersion());
                        developPamphletMsgVo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        developPamphletMsgVo.setDevelopPamphletOrderStatus(developPamphletOrderPo.getDevelopPamphletOrderStatus());
                        developPamphletMsgVo.setSupplierCode(developPamphletOrderPo.getSupplierCode());
                        developPamphletMsgVo.setSupplierName(developPamphletOrderPo.getSupplierName());
                        developPamphletMsgVo.setDemandDesc(developPamphletOrderPo.getDemandDesc());
                        developPamphletMsgVo.setDevelopSampleNum(developPamphletOrderPo.getDevelopSampleNum());
                        developPamphletMsgVo.setPamphletTimes(record.getDevelopChildBaseMsgVo().getPamphletTimes());
                        developPamphletMsgVo.setPlatform(record.getDevelopChildBaseMsgVo().getPlatform());
                        developPamphletMsgVo.setRefuseReason(developPamphletOrderPo.getRefuseReason());
                        developPamphletMsgVo.setSupplierSamplePrice(developPamphletOrderPo.getSamplePrice());
                        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPos = developPamphletOrderPoRawMap.get(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        if (CollectionUtils.isNotEmpty(developPamphletOrderRawPos)) {
                            developPamphletMsgVo.setDevelopPamphletRawDetailVoList(DevelopPamphletOrderRawConverter.INSTANCE.convert(developPamphletOrderRawPos));
                        }
                    });
            record.setDevelopPamphletMsgVo(developPamphletMsgVo);

            List<BigDecimal> supplierSamplePriceList = new ArrayList<>();
            List<BigDecimal> pricingSamplePriceList = new ArrayList<>();


            //无需打样逻辑处理
            if (BooleanType.FALSE.equals(record.getDevelopChildBaseMsgVo().getIsSample())) {
                List<DevelopPricingOrderInfoPo> developPricingOrderInfoPos = developPricingOrderInfoPoList.stream()
                        .filter(w -> w.getDevelopChildOrderNo().equals(record.getDevelopChildBaseMsgVo().getDevelopChildOrderNo()))
                        .collect(Collectors.toList());
                for (DevelopPricingOrderInfoPo developPricingOrderInfoPo : developPricingOrderInfoPos) {
                    pricingSamplePriceList.add(developPricingOrderInfoPo.getSamplePrice());
                }
                developSupplierPriceVo.setPricingSamplePriceList(pricingSamplePriceList);
                //供应商报价格
                supplierSamplePriceList.add(developPamphletMsgVo.getSupplierSamplePrice());
            }
            record.setDevelopSupplierPriceVo(developSupplierPriceVo);

            //供应商报价格
            if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
                List<DevelopSampleOrderPo> developSampleOrderPamphletPoList = developSampleOrderPoList.stream()
                        .filter(w -> w.getDevelopPamphletOrderNo().equals(developPamphletMsgVo.getDevelopPamphletOrderNo()))
                        .collect(Collectors.toList());
                for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPamphletPoList) {
                    supplierSamplePriceList.add(developSampleOrderPo.getSamplePrice());
                    developPricingOrderInfoPoList.stream().filter(w -> w.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                            .findAny().ifPresent(developPricingOrderInfoPo -> {
                                pricingSamplePriceList.add(developPricingOrderInfoPo.getSamplePrice());
                            });
                }
            }

            developSupplierPriceVo.setSupplierSamplePriceList(supplierSamplePriceList);
            developSupplierPriceVo.setPricingSamplePriceList(pricingSamplePriceList);

            //审版单信息
            final DevelopReviewOrderPo developReviewOrderPo = Optional.ofNullable(developReviewOrderPoMap.get(record.getDevelopPamphletMsgVo().getDevelopPamphletOrderNo()))
                    .flatMap(l -> l.stream().max(Comparator.comparing(DevelopReviewOrderPo::getCreateTime)))
                    .orElse(new DevelopReviewOrderPo());

            developReviewMsgVo.setDevelopReviewOrderNo(developReviewOrderPo.getDevelopReviewOrderNo());
            developReviewMsgVo.setDevelopReviewOrderStatus(developReviewOrderPo.getDevelopReviewOrderStatus());
            developReviewMsgVo.setReviewResult(developReviewOrderPo.getReviewResult());

            record.setDevelopReviewMsgVo(developReviewMsgVo);

            // 首单/产前样采购信息
            if (record.getDevelopPrenatalFirstMsgVo() != null) {
                final DevelopPrenatalFirstMsgVo recordPurchaseVo = record.getDevelopPrenatalFirstMsgVo();
                final PurchaseChildOrderPo prenatalPurchaseChildOrderPo = purchaseChildOrderNoPoMap.get(recordPurchaseVo.getPrenatalSampleOrderNo());
                final PurchaseChildOrderPo firstPurchaseChildOrderPo = purchaseChildOrderNoPoMap.get(recordPurchaseVo.getFirstSampleOrderNo());
                final DevelopPrenatalFirstMsgVo developPrenatalFirstMsgVo = DevelopChildConverter.convertPurchasePoToPrenatalFirstVo(prenatalPurchaseChildOrderPo,
                        firstPurchaseChildOrderPo,
                        recordPurchaseVo.getPurchaseParentOrderNo(),
                        record.getDevelopChildBaseMsgVo());
                record.setDevelopPrenatalFirstMsgVo(developPrenatalFirstMsgVo);
            }
            // 产前样样品单信息
            if (record.getDevelopSupplierPriceVo() != null
                    && ScmConstant.PROCESS_SUPPLIER_CODE.equals(record.getDevelopSupplierPriceVo().getSupplierCode())
                    && CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
                final DevelopPrenatalFirstMsgVo developPrenatalFirstMsgSampleVo = new DevelopPrenatalFirstMsgVo();
                // 获取产前样样品单
                String prenatalSampleOrderNoJoining = developSampleOrderPoList.stream()
                        .filter(po -> DevelopSampleType.PRENATAL_SAMPLE.equals(po.getDevelopSampleType()))
                        .map(DevelopSampleOrderPo::getDevelopSampleOrderNo).collect(Collectors.joining(","));
                developPrenatalFirstMsgSampleVo.setPrenatalSampleOrderNo(prenatalSampleOrderNoJoining);
                record.setDevelopPrenatalFirstMsgVo(developPrenatalFirstMsgSampleVo);
            }

            //核价单信息
            Optional.ofNullable(developPricingOrderMap.get(record.getDevelopPamphletMsgVo().getDevelopPamphletOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .max(Comparator.comparing(DevelopPricingOrderPo::getCreateTime))
                    .ifPresent(developPricingOrderPo -> {
                        developPricingMsgVo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
                        developPricingMsgVo.setDevelopPricingOrderStatus(developPricingOrderPo.getDevelopPricingOrderStatus());
                        List<DevelopSampleOrderPo> developSampleOrderPoPricingList = developSampleOrderPoPricingMap.get(developPricingOrderPo.getDevelopPricingOrderNo());
                        developPricingMsgVo.setDevelopPricingMsgSampleList(DevelopSampleOrderConverter.INSTANCE.developVoList(developSampleOrderPoPricingList));
                    });
            record.setDevelopPricingMsgVo(developPricingMsgVo);

            record.setFileCodeList(fileCodeMap.get(record.getDevelopChildOrderId()));
        }

        return pageResult;
    }

    /**
     * 开发子单查询条件
     *
     * @param dto:
     * @return DevelopChildSearchDto
     * @author ChenWenLong
     * @date 2023/8/24 17:32
     */
    public DevelopChildSearchDto getSearchDevelopChildWhere(DevelopChildSearchDto dto) {
        final List<DevelopPamphletOrderStatus> developPamphletOrderStatusList = dto.getDevelopPamphletOrderStatusList();
        if (CollectionUtils.isNotEmpty(developPamphletOrderStatusList)) {
            final List<DevelopPamphletOrderPo> developPamphletOrderPoList = developPamphletOrderDao.getListByStatusList(developPamphletOrderStatusList);
            if (CollectionUtils.isEmpty(developPamphletOrderPoList)) {
                return null;
            }
            final List<String> developChildOrderNoList = developPamphletOrderPoList.stream()
                    .map(DevelopPamphletOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());
            List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByParentOrderNoAndStatus(developChildOrderNoList, List.of(DevelopChildOrderStatus.PAMPHLET));
            List<String> developChildOrderNoPamphletList = developChildOrderPoList.stream()
                    .map(DevelopChildOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());
            //获取当前开发子单使用的版单
            List<String> searchDevelopChildOrderNoList = new ArrayList<>();
            Map<String, List<DevelopPamphletOrderPo>> developPamphletOrderPoMap = developPamphletOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                    .stream().collect(Collectors.groupingBy(DevelopPamphletOrderPo::getDevelopChildOrderNo));

            for (DevelopPamphletOrderPo developPamphletOrderPo : developPamphletOrderPoList) {
                Optional.ofNullable(developPamphletOrderPoMap.get(developPamphletOrderPo.getDevelopChildOrderNo()))
                        .orElse(Collections.emptyList())
                        .stream()
                        .max(Comparator.comparing(DevelopPamphletOrderPo::getCreateTime))
                        .ifPresent(item -> {
                            if (developPamphletOrderPo.getDevelopPamphletOrderNo().equals(item.getDevelopPamphletOrderNo()) &&
                                    developChildOrderNoPamphletList.contains(item.getDevelopChildOrderNo())) {
                                searchDevelopChildOrderNoList.add(item.getDevelopChildOrderNo());
                            }
                        });
            }
            if (CollectionUtils.isEmpty(searchDevelopChildOrderNoList)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                dto.setDevelopChildOrderNoList(searchDevelopChildOrderNoList);
            } else {
                dto.getDevelopChildOrderNoList().retainAll(searchDevelopChildOrderNoList);
            }
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                return null;
            }
        }

        if (dto.getReviewResult() != null) {
            List<DevelopReviewOrderPo> developReviewOrderPoResultList = developReviewOrderDao.getListByReviewResult(dto.getReviewResult());
            if (CollectionUtils.isEmpty(developReviewOrderPoResultList)) {
                return null;
            }
            List<String> developPamphletOrderNoList = developReviewOrderPoResultList.stream()
                    .map(DevelopReviewOrderPo::getDevelopPamphletOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            List<DevelopPamphletOrderPo> developPamphletOrderPoList = developPamphletOrderDao.getListByNoList(developPamphletOrderNoList);

            final List<String> developChildOrderNoList = developPamphletOrderPoList.stream()
                    .map(DevelopPamphletOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());
            List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
            List<String> developChildOrderNoPamphletList = developChildOrderPoList.stream()
                    .map(DevelopChildOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());
            //获取当前开发子单使用的版单
            List<String> searchDevelopChildOrderNoList = new ArrayList<>();
            Map<String, List<DevelopPamphletOrderPo>> developPamphletOrderPoMap = developPamphletOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                    .stream().collect(Collectors.groupingBy(DevelopPamphletOrderPo::getDevelopChildOrderNo));

            for (DevelopPamphletOrderPo developPamphletOrderPo : developPamphletOrderPoList) {
                Optional.ofNullable(developPamphletOrderPoMap.get(developPamphletOrderPo.getDevelopChildOrderNo()))
                        .orElse(Collections.emptyList())
                        .stream()
                        .max(Comparator.comparing(DevelopPamphletOrderPo::getCreateTime))
                        .ifPresent(item -> {
                            if (developPamphletOrderPo.getDevelopPamphletOrderNo().equals(item.getDevelopPamphletOrderNo()) &&
                                    developChildOrderNoPamphletList.contains(item.getDevelopChildOrderNo())) {
                                searchDevelopChildOrderNoList.add(item.getDevelopChildOrderNo());
                            }
                        });
            }
            if (CollectionUtils.isEmpty(searchDevelopChildOrderNoList)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                dto.setDevelopChildOrderNoList(searchDevelopChildOrderNoList);
            } else {
                dto.getDevelopChildOrderNoList().retainAll(searchDevelopChildOrderNoList);
            }
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                return null;
            }
        }

        final List<DevelopReviewOrderStatus> developReviewOrderStatusList = dto.getDevelopReviewOrderStatusList();
        if (CollectionUtils.isNotEmpty(developReviewOrderStatusList)) {
            final List<DevelopReviewOrderPo> developReviewOrderPoList = developReviewOrderDao.getListByStatusList(developReviewOrderStatusList);
            if (CollectionUtils.isEmpty(developReviewOrderPoList)) {
                return null;
            }
            final List<String> developChildOrderNoList = developReviewOrderPoList.stream()
                    .map(DevelopReviewOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());

            List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByParentOrderNoAndStatus(developChildOrderNoList, List.of(DevelopChildOrderStatus.REVIEW));
            List<String> developChildOrderNoReviewList = developChildOrderPoList.stream()
                    .map(DevelopChildOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(developChildOrderNoReviewList)) {
                return null;
            }

            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                dto.setDevelopChildOrderNoList(developChildOrderNoReviewList);
            } else {
                dto.getDevelopChildOrderNoList().retainAll(developChildOrderNoReviewList);
            }
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                return null;
            }
            return dto;
        }

        final List<DevelopPricingOrderStatus> developPricingOrderStatusList = dto.getDevelopPricingOrderStatusList();
        if (CollectionUtils.isNotEmpty(developPricingOrderStatusList)) {
            final List<DevelopPricingOrderPo> developPricingOrderPoList = developPricingOrderDao.getListByStatusList(developPricingOrderStatusList);
            if (CollectionUtils.isEmpty(developPricingOrderPoList)) {
                return null;
            }
            final List<String> developChildOrderNoList = developPricingOrderPoList.stream()
                    .map(DevelopPricingOrderPo::getDevelopChildOrderNo)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                dto.setDevelopChildOrderNoList(developChildOrderNoList);
            } else {
                dto.getDevelopChildOrderNoList().retainAll(developChildOrderNoList);
            }
            if (CollectionUtils.isEmpty(dto.getDevelopChildOrderNoList())) {
                return null;
            }
            return dto;

        }

        //产品名称批量查询
        if (CollectionUtils.isNotEmpty(dto.getSkuEncodeList())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(dto.getSkuEncodeList());
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                return null;
            }
        }

        return dto;

    }

    /**
     * 开发子单状态更新
     *
     * @param developChildOrderPo:
     * @param status:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/16 14:54
     */
    public void updateDevelopChildOrderStatus(DevelopChildOrderPo developChildOrderPo,
                                              DevelopChildOrderStatus status, Boolean pushMq, Boolean pushLog) {
        developChildOrderPo.setDevelopChildOrderStatus(status);
        developChildOrderDao.updateById(developChildOrderPo);

        //日志
        if (pushLog) {
            logBaseService.simpleLog(LogBizModule.DEVELOP_CHILD_ORDER_STATUS, ScmConstant.DEVELOP_CHILD_LOG_VERSION,
                    developChildOrderPo.getDevelopChildOrderNo(), status.getRemark(), Collections.emptyList());
        }

        //状态是否推送PLM
        if (pushMq) {
            final DevelopStatusDto developStatusDto = new DevelopStatusDto();
            developStatusDto.setDevelopChildOrderStatus(status);
            developStatusDto.setKey(developChildOrderPo.getDevelopChildOrderNo());
            developStatusDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            developStatusDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            developStatusDto.setSupplierCode(developChildOrderPo.getSupplierCode());
            developStatusDto.setSupplierName(developChildOrderPo.getSupplierName());
            developStatusDto.setUserKey(GlobalContext.getUserKey());
            developStatusDto.setUsername(GlobalContext.getUsername());
            developStatusDto.setStatusUpdateTime(LocalDateTime.now());
            consistencySendMqService.execSendMq(DevelopStatusHandler.class, developStatusDto);
        }

    }

    /**
     * 原料下单
     *
     * @param dto:
     * @param developPamphletOrderPo:
     * @param developChildOrderPo:
     * @author ChenWenLong
     * @date 2024/11/11 15:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplyRaw(DevelopPamphletOrderRawDeliveryDto dto,
                                @NotNull DevelopPamphletOrderPo developPamphletOrderPo,
                                @NotNull DevelopChildOrderPo developChildOrderPo) {
        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(dto.getDevelopPamphletOrderNo(),
                SampleRawBizType.DEMAND);
        // 校验平台信息
        Assert.notBlank(developChildOrderPo.getPlatform(), () -> new BizException("开发子单号:查询不到对应平台信息，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo()));
        final Map<String, DevelopPamphletOrderRawPo> skuRawPoMap = developPamphletOrderRawPoList.stream()
                .collect(Collectors.toMap(DevelopPamphletOrderRawPo::getSku, Function.identity(), (item1, item2) -> item1));
        List<DevelopPamphletOrderRawPo> updatePoList = new ArrayList<>();
        dto.getRawList()
                .forEach(item -> {
                    DevelopPamphletOrderRawPo developPamphletOrderRawPo = skuRawPoMap.get(item.getSku());
                    if (null == developPamphletOrderRawPo) {
                        developPamphletOrderRawPo = new DevelopPamphletOrderRawPo();
                        developPamphletOrderRawPo.setSku(item.getSku());
                        developPamphletOrderRawPo.setSkuCnt(item.getSkuCnt());
                        developPamphletOrderRawPo.setWarehouseCode(dto.getRawWarehouseCode());
                        developPamphletOrderRawPo.setWarehouseName(dto.getRawWarehouseName());
                        developPamphletOrderRawPo.setSampleRawBizType(SampleRawBizType.DEMAND);
                        developPamphletOrderRawPo.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        developPamphletOrderRawPo.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                        developPamphletOrderRawPo.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                    } else {
                        developPamphletOrderRawPo.setSkuCnt(developPamphletOrderRawPo.getSkuCnt() + item.getSkuCnt());
                    }
                    updatePoList.add(developPamphletOrderRawPo);
                });
        developPamphletOrderRawDao.insertOrUpdateBatch(updatePoList);


        // wms 生成原料发货单
        final List<RawDeliverBo> pamphletRawDeliverBoList = dto.getRawList().stream()
                .map(item -> {
                    final RawDeliverBo rawDeliverBo = new RawDeliverBo();
                    rawDeliverBo.setDeliveryCnt(item.getSkuCnt());
                    rawDeliverBo.setSku(item.getSku());

                    if (CollectionUtils.isNotEmpty(item.getRawLocationItemList())) {
                        final List<RawDeliverBo.WareLocationDelivery> wareLocationDeliveryList = item.getRawLocationItemList().stream().map(rawLocationItemDto -> {
                            final RawDeliverBo.WareLocationDelivery wareLocationDelivery = new RawDeliverBo.WareLocationDelivery();
                            wareLocationDelivery.setWarehouseLocationCode(rawLocationItemDto.getWarehouseLocationCode());
                            wareLocationDelivery.setDeliveryAmount(rawLocationItemDto.getDeliveryAmount());
                            wareLocationDelivery.setBatchCode(rawLocationItemDto.getBatchCode());
                            return wareLocationDelivery;
                        }).collect(Collectors.toList());

                        rawDeliverBo.setWareLocationDeliveryList(wareLocationDeliveryList);
                        rawDeliverBo.setParticularLocation(BooleanType.TRUE);
                    }
                    return rawDeliverBo;
                }).collect(Collectors.toList());
        final WmsDeliverBo wmsDeliverBo = new WmsDeliverBo();
        wmsDeliverBo.setRelatedOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        wmsDeliverBo.setRawWarehouseCode(dto.getRawWarehouseCode());
        wmsDeliverBo.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
        wmsDeliverBo.setRawDeliverMode(null);
        wmsDeliverBo.setDispatchNow(BooleanType.TRUE);
        wmsDeliverBo.setPlatform(developChildOrderPo.getPlatform());
        wmsDeliverBo.setConsignee(developPamphletOrderPo.getSupplierCode());
        purchaseRawRefService.wmsRawDeliver(wmsDeliverBo, pamphletRawDeliverBoList);

    }

    /**
     * 异常处理针对子单进行取消
     *
     * @param developChildOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/18 11:41
     */
    public void cancelExceptionChild(List<DevelopChildOrderPo> developChildOrderPoList, String cancelReason) {
        if (CollectionUtils.isEmpty(developChildOrderPoList)) {
            throw new BizException("子单数据为空，请刷新页面后重试！");
        }

        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            developChildOrderPo.setCancelReason(cancelReason);
            this.updateDevelopChildOrderStatus(developChildOrderPo, DevelopChildOrderStatus.CANCEL, true, true);

            final List<DevelopPamphletOrderPo> developPamphletOrderPoList = developPamphletOrderDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            if (CollectionUtils.isNotEmpty(developPamphletOrderPoList)) {
                developPamphletOrderPoList.forEach(po -> {
                    // 取消对应的原料出库单
                    final ProcessOrderCancelEventDto processOrderCancelEventDto = new ProcessOrderCancelEventDto();
                    processOrderCancelEventDto.setProcessOrderNo(po.getDevelopPamphletOrderNo());
                    processOrderCancelEventDto.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
                    processOrderCancelEventDto.setKey(po.getDevelopPamphletOrderNo());
                    processOrderCancelEventDto.setOperator(GlobalContext.getUserKey());
                    processOrderCancelEventDto.setOperatorName(GlobalContext.getUsername());
                    consistencySendMqService.execSendMq(WmsProcessCancelHandler.class, processOrderCancelEventDto);
                });
            }
        }
    }

    /**
     * 针对子单进行取消相关版单、审版单、核价单(一键取消母单和取消子单共用)
     *
     * @param developChildOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/18 11:41
     */
    public void cancelChild(List<DevelopChildOrderPo> developChildOrderPoList) {
        if (CollectionUtils.isEmpty(developChildOrderPoList)) {
            return;
        }

        // 获取需打样的开发子单
        List<String> developChildOrderNoProofingList = developChildOrderPoList.stream()
                .filter(po -> BooleanType.TRUE.equals(po.getIsSample()))
                .filter(po -> po.getDevelopChildOrderStatus().equals(DevelopChildOrderStatus.PAMPHLET))
                .map(DevelopChildOrderPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());

        // 获取无需打样的开发子单
        List<String> developChildOrderNoNotList = developChildOrderPoList.stream()
                .filter(po -> BooleanType.FALSE.equals(po.getIsSample()))
                .filter(po -> po.getDevelopChildOrderStatus().equals(DevelopChildOrderStatus.PRICING))
                .map(DevelopChildOrderPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());

        // 取消版单
        List<DevelopPamphletOrderPo> developPamphletOrderPoList = developPamphletOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoProofingList);
        for (DevelopPamphletOrderPo developPamphletOrderPo : developPamphletOrderPoList) {
            developPamphletOrderPo.setDevelopPamphletOrderStatus(developPamphletOrderPo.getDevelopPamphletOrderStatus().cancel());
        }
        developPamphletOrderDao.updateBatchByIdVersion(developPamphletOrderPoList);

        // 取消审版单
        List<DevelopReviewOrderPo> developReviewOrderPoList = developReviewOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoProofingList);
        for (DevelopReviewOrderPo developReviewOrderPo : developReviewOrderPoList) {
            developReviewOrderPo.setDevelopReviewOrderStatus(DevelopReviewOrderStatus.CANCEL_REVIEW);
        }
        developReviewOrderDao.updateBatchByIdVersion(developReviewOrderPoList);

        // 取消需打样的核价单
        List<DevelopPricingOrderPo> developPricingOrderPoList = developPricingOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoProofingList);
        for (DevelopPricingOrderPo developPricingOrderPo : developPricingOrderPoList) {
            developPricingOrderPo.setDevelopPricingOrderStatus(DevelopPricingOrderStatus.CANCEL_PRICE);
        }
        developPricingOrderDao.updateBatchByIdVersion(developPricingOrderPoList);


        // 处理无需打样的核价单
        List<DevelopPricingOrderPo> developPricingOrderPoNotList = developPricingOrderDao.getListByChildOrderNoListAndStatus(developChildOrderNoNotList,
                List.of(DevelopPricingOrderStatus.WAIT_SUBMIT_PRICE, DevelopPricingOrderStatus.WAIT_PRICE));
        for (DevelopPricingOrderPo developPricingOrderPo : developPricingOrderPoNotList) {
            developPricingOrderPo.setDevelopPricingOrderStatus(DevelopPricingOrderStatus.CANCEL_PRICE);
        }
        if (CollectionUtils.isNotEmpty(developPricingOrderPoNotList)) {
            developPricingOrderDao.updateBatchByIdVersion(developPricingOrderPoNotList);
        }

    }


    /**
     * 重新创建版单信息
     *
     * @param developPamphletOrderPo:
     * @param supplierCode:
     * @param supplierName:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/18 15:46
     */
    public DevelopPamphletOrderPo rebuildPamphletOrder(DevelopPamphletOrderPo developPamphletOrderPo, String supplierCode, String supplierName) {

        String developPamphletOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_PAMPHLET_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        DevelopPamphletOrderPo insertDevelopPamphletOrder = DevelopPamphletOrderConverter.convertToPo(developPamphletOrderPo, supplierCode,
                supplierName, developPamphletOrderNo);
        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(developPamphletOrderPo.getDevelopPamphletOrderNo(), SampleRawBizType.FORMULA);
        List<DevelopPamphletOrderRawPo> insertRawPoList = DevelopPamphletOrderConverter.convertRawToPo(insertDevelopPamphletOrder, developPamphletOrderRawPoList);
        developPamphletOrderDao.insert(insertDevelopPamphletOrder);
        developPamphletOrderRawDao.insertBatch(insertRawPoList);
        //复制款式图片、颜色图片
        Map<Long, List<String>> pamphletImageStyleMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_PAMPHLET_STYLE, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));
        Map<Long, List<String>> pamphletImageColorMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_PAMPHLET_COLOR, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));

        if (pamphletImageStyleMap.containsKey(developPamphletOrderPo.getDevelopPamphletOrderId())) {
            scmImageBaseService.insertBatchImage(pamphletImageStyleMap.get(developPamphletOrderPo.getDevelopPamphletOrderId()),
                    ImageBizType.DEVELOP_PAMPHLET_STYLE, insertDevelopPamphletOrder.getDevelopPamphletOrderId());
        }

        if (pamphletImageColorMap.containsKey(developPamphletOrderPo.getDevelopPamphletOrderId())) {
            scmImageBaseService.insertBatchImage(pamphletImageColorMap.get(developPamphletOrderPo.getDevelopPamphletOrderId()),
                    ImageBizType.DEVELOP_PAMPHLET_COLOR, insertDevelopPamphletOrder.getDevelopPamphletOrderId());
        }

        return insertDevelopPamphletOrder;
    }

    public Integer getExportTotals(DevelopChildSearchDto dto) {
        Integer exportTotals = developChildOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        return exportTotals;
    }

    public CommonResult<ExportationListResultBo<DevelopChildOrderExportVo>> getExportList(DevelopChildSearchDto dto) {
        ExportationListResultBo<DevelopChildOrderExportVo> resultBo = new ExportationListResultBo<>();

        CommonPageResult.PageInfo<DevelopChildOrderExportVo> exportList = developChildOrderDao.getExportList(PageDTO.of(dto.getPageNo(), dto.getPageSize(), false), dto);
        List<DevelopChildOrderExportVo> records = exportList.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return CommonResult.success(resultBo);
        }

        List<String> developChildOrderNoList = records.stream()
                .map(DevelopChildOrderExportVo::getDevelopChildOrderNo)
                .distinct()
                .collect(Collectors.toList());

        //获取开发子单信息，为了获取PLM的开发类型、是否加急枚举，提供给api
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList).stream().collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));

        //获取样品单
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoMap = developSampleOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopSampleOrderPo::getDevelopChildOrderNo));

        //获取核价
        Map<String, List<DevelopPricingOrderInfoPo>> developPricingOrderPoMap = developPricingOrderInfoDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopPricingOrderInfoPo::getDevelopChildOrderNo));

        //获取版单
        Map<String, List<DevelopPamphletOrderPo>> developPamphletOrderPoMap = developPamphletOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopPamphletOrderPo::getDevelopChildOrderNo));

        //获取审版单
        Map<String, List<DevelopReviewOrderPo>> developReviewOrderPoMap = developReviewOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopReviewOrderPo::getDevelopPamphletOrderNo));

        //获取核价单
        Map<String, List<DevelopPricingOrderPo>> developPricingOrderMap = developPricingOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                .stream().collect(Collectors.groupingBy(DevelopPricingOrderPo::getDevelopPamphletOrderNo));

        final List<String> purchaseChildOrderNoList = records.stream()
                .flatMap(record -> Stream.of(record.getPrenatalSampleOrderNo(),
                        record.getFirstSampleOrderNo()))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderPo> purchaseChildOrderPoList = purchaseChildOrderDao.getListByNoList(purchaseChildOrderNoList);
        final Map<String, PurchaseChildOrderPo> purchaseChildOrderNoPoMap = purchaseChildOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderPo::getPurchaseChildOrderNo, Function.identity()));

        //获取采购需求单
        final List<String> purchaseParentOrderNoList = records.stream()
                .flatMap(record -> Stream.of(record.getPurchaseParentOrderNo(),
                        record.getFirstSampleOrderNo()))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        final List<PurchaseParentOrderPo> purchaseParentOrderPoList = purchaseParentOrderDao.getListByNoList(purchaseParentOrderNoList);
        final Map<String, PurchaseParentOrderPo> purchaseParentOrderPoMap = purchaseParentOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseParentOrderPo::getPurchaseParentOrderNo, Function.identity()));

        final List<String> firstSampleOrderNoList = records.stream()
                .map(DevelopChildOrderExportVo::getFirstSampleOrderNo)
                .collect(Collectors.toList());

        final List<Long> firstSampleOrderPoIdList = purchaseChildOrderPoList.stream()
                .filter(po -> firstSampleOrderNoList.contains(po.getPurchaseChildOrderNo()))
                .map(PurchaseChildOrderPo::getPurchaseChildOrderId)
                .collect(Collectors.toList());
        final List<PurchaseChildOrderChangePo> purchaseChildOrderChangePoList = purchaseChildOrderChangeDao.getByChildOrderId(firstSampleOrderPoIdList);
        final Map<Long, PurchaseChildOrderChangePo> purchaseChildOrderChangePoMap = purchaseChildOrderChangePoList.stream()
                .collect(Collectors.toMap(PurchaseChildOrderChangePo::getPurchaseChildOrderId, Function.identity()));


        //获取数据
        for (DevelopChildOrderExportVo record : records) {
            //样品单
            List<DevelopSampleOrderPo> developSampleOrderPoList = Optional.ofNullable(developSampleOrderPoMap.get(record.getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(DevelopSampleOrderPo::getCreateTime).reversed())
                    .collect(Collectors.toList());

            //产前样的样品单
            List<DevelopSampleOrderPo> developSampleOrderPoPrenatalList = developSampleOrderPoList.stream()
                    .filter(po -> DevelopSampleType.PRENATAL_SAMPLE.equals(po.getDevelopSampleType()))
                    .filter(po -> record.getDevelopChildOrderNo().equals(po.getDevelopChildOrderNo()))
                    .collect(Collectors.toList());

            DevelopChildOrderPo developChildOrderPo = developChildOrderPoMap.get(record.getDevelopChildOrderNo());
            record.setDevelopChildOrderStatusName(record.getDevelopChildOrderStatus().getRemark());
            record.setSkuDevTypeName(developChildOrderPo.getSkuDevType().getRemark());
            record.setIsUrgentName(record.getIsUrgent().getValue());
            record.setIsOnShelvesName(record.getIsOnShelves().getValue());
            // 转时间
            record.setCreateTimeStr(ScmTimeUtil.localDateTimeToStr(record.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setExpectedOnShelvesDateStr(ScmTimeUtil.localDateTimeToStr(record.getExpectedOnShelvesDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setOnShelvesCompletionDateStr(ScmTimeUtil.localDateTimeToStr(record.getOnShelvesCompletionDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setNewestCompletionDateStr(ScmTimeUtil.localDateTimeToStr(record.getNewestCompletionDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            record.setPrenatalSampleOrderNo(developChildOrderPo.getPrenatalSampleOrderNo());
            record.setFirstSampleOrderNo(developChildOrderPo.getFirstSampleOrderNo());
            record.setPamphletTimes(developChildOrderPo.getPamphletTimes());
            final PurchaseChildOrderPo prenatalSampleOrderPo = purchaseChildOrderNoPoMap.get(developChildOrderPo.getPrenatalSampleOrderNo());
            final PurchaseChildOrderPo firstSampleOrderPo = purchaseChildOrderNoPoMap.get(developChildOrderPo.getFirstSampleOrderNo());
            if (null != prenatalSampleOrderPo) {
                record.setPrenatalSampleOrderCreateTime(ScmTimeUtil.localDateTimeToStr(prenatalSampleOrderPo.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            }

            final PurchaseParentOrderPo purchaseParentOrderPo = purchaseParentOrderPoMap.get(record.getPurchaseParentOrderNo());
            if (null != purchaseParentOrderPo) {
                record.setPurchaseParentOrderTime(ScmTimeUtil.localDateTimeToStr(purchaseParentOrderPo.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            }

            //针对是产前样的样品单
            if (prenatalSampleOrderPo == null && ScmConstant.PROCESS_SUPPLIER_CODE.equals(record.getSupplierCode())
                    && CollectionUtils.isNotEmpty(developSampleOrderPoPrenatalList)) {
                record.setPrenatalSampleOrderCreateTime(ScmTimeUtil.localDateTimeToStr(developSampleOrderPoPrenatalList.get(0).getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                String developSampleOrderNoJoining = developSampleOrderPoPrenatalList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo).collect(Collectors.joining(","));
                record.setPrenatalSampleOrderNo(developSampleOrderNoJoining);
            }

            if (null != firstSampleOrderPo) {
                record.setFirstSampleOrderCreateTime(ScmTimeUtil.localDateTimeToStr(firstSampleOrderPo.getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                record.setFirstSampleOrderWarehousingTime(ScmTimeUtil.localDateTimeToStr(purchaseChildOrderChangePoMap.get(firstSampleOrderPo.getPurchaseChildOrderId()).getCreateTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
            }

            //核价单
            List<DevelopPricingOrderInfoPo> developPricingOrderInfoPoList = Optional.ofNullable(developPricingOrderPoMap.get(record.getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList());

            if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
                List<BigDecimal> supplierSamplePriceList = new ArrayList<>();
                List<BigDecimal> pricingSamplePriceList = new ArrayList<>();
                for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                    supplierSamplePriceList.add(developSampleOrderPo.getSamplePrice());
                    developPricingOrderInfoPoList.stream().filter(w -> w.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                            .findAny().ifPresent(developPricingOrderInfoPo -> {
                                pricingSamplePriceList.add(developPricingOrderInfoPo.getSamplePrice());
                            });
                }
                record.setSupplierSamplePriceList(supplierSamplePriceList.stream().map(BigDecimal::toString).collect(Collectors.joining(",")));
                record.setPricingSamplePriceList(pricingSamplePriceList.stream().map(BigDecimal::toString).collect(Collectors.joining(",")));
            }

            //打版信息
            Optional.ofNullable(developPamphletOrderPoMap.get(record.getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .max(Comparator.comparing(DevelopPamphletOrderPo::getCreateTime))
                    .ifPresent(developPamphletOrderPo -> {
                        record.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        record.setDevelopPamphletOrderStatusName(developPamphletOrderPo.getDevelopPamphletOrderStatus().getRemark());
                        record.setDevelopSampleNum(developPamphletOrderPo.getDevelopSampleNum());
                        record.setPamphletDate(developPamphletOrderPo.getPamphletDate());
                        record.setPamphletDateStr(ScmTimeUtil.localDateTimeToStr(developPamphletOrderPo.getPamphletDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                        record.setPamphletCompletionDateStr(ScmTimeUtil.localDateTimeToStr(developPamphletOrderPo.getFinishPamphletDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    });

            //审版单信息
            Optional.ofNullable(developReviewOrderPoMap.get(record.getDevelopPamphletOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .max(Comparator.comparing(DevelopReviewOrderPo::getCreateTime))
                    .ifPresent(developReviewOrderPo -> {
                        record.setDevelopReviewOrderNo(developReviewOrderPo.getDevelopReviewOrderNo());
                        record.setDevelopReviewOrderStatusName(developReviewOrderPo.getDevelopReviewOrderStatus().getRemark());
                        record.setSubmitReviewDateStr(ScmTimeUtil.localDateTimeToStr(developReviewOrderPo.getSubmitReviewDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                        record.setReviewCompletionDateStr(ScmTimeUtil.localDateTimeToStr(developReviewOrderPo.getReviewDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                        if (null != developReviewOrderPo.getReviewResult()) {
                            record.setReviewResultName(developReviewOrderPo.getReviewResult().getRemark());
                        }
                    });

            //核价单信息
            Optional.ofNullable(developPricingOrderMap.get(record.getDevelopPamphletOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .max(Comparator.comparing(DevelopPricingOrderPo::getCreateTime))
                    .ifPresent(developPricingOrderPo -> {
                        record.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
                        record.setDevelopPricingOrderStatusName(developPricingOrderPo.getDevelopPricingOrderStatus().getRemark());
                        record.setSubmitTimeStr(ScmTimeUtil.localDateTimeToStr(developPricingOrderPo.getSubmitTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                        record.setPricingCompletionDateStr(ScmTimeUtil.localDateTimeToStr(developPricingOrderPo.getNuclearPriceTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));
                    });

            //获取首次样品单签收时间
            record.setFirstSignTimeStr(ScmTimeUtil.localDateTimeToStr(record.getFollowDate(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN));

            Optional.ofNullable(developSampleOrderPoMap.get(record.getDevelopChildOrderNo()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(developSampleOrderPo -> null != developSampleOrderPo.getSignTime())
                    .max(Comparator.comparing(DevelopSampleOrderPo::getSignTime))
                    .ifPresent(developSampleOrderPoFirstSignTime ->
                            record.setFollowDateStr(ScmTimeUtil.localDateTimeToStr(developSampleOrderPoFirstSignTime.getSignTime(), TimeZoneId.CN, DatePattern.NORM_DATETIME_PATTERN)));


        }

        resultBo.setRowDataList(records);

        return CommonResult.success(resultBo);
    }

    public DevelopChildDetailVo developChildDetail(DevelopChildNoDto dto) {
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(dto.getDevelopChildOrderNo());
        if (null == developChildOrderPo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        final DevelopChildOrderChangePo developChildOrderChangePo = developChildOrderChangeDao.getOneByDevelopChildOrderNo(dto.getDevelopChildOrderNo());
        if (null == developChildOrderChangePo) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }
        final List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(dto.getDevelopChildOrderNo());

        List<DevelopPamphletOrderPo> developPamphletOrderPoList = developPamphletOrderDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopChildOrderNoAndType(developChildOrderPo.getDevelopChildOrderNo(), SampleRawBizType.FORMULA);

        Map<Long, List<String>> pamphletImageStyleMap = new HashMap<>();
        Map<Long, List<String>> pamphletImageColorMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(developPamphletOrderPoList)) {
            pamphletImageStyleMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_PAMPHLET_STYLE,
                    developPamphletOrderPoList.stream().map(DevelopPamphletOrderPo::getDevelopPamphletOrderId).collect(Collectors.toList()));
            pamphletImageColorMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_PAMPHLET_COLOR,
                    developPamphletOrderPoList.stream().map(DevelopPamphletOrderPo::getDevelopPamphletOrderId).collect(Collectors.toList()));
        }
        // 获取子单图片
        final Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_CHILD_STYLE, List.of(developChildOrderPo.getDevelopChildOrderId()));

        //取最新的一条版单
        DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderPoList.stream().max(Comparator.comparing(DevelopPamphletOrderPo::getCreateTime)).orElse(null);
        if (developPamphletOrderPo == null) {
            throw new BizException("获取不到最新的版单数据，请刷新页面后重试！");
        }
        //获取版单原料关联单据
        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        List<String> purchaseRawReceiptOrderNoList = purchaseRawReceiptOrderPoList.stream()
                .map(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo)
                .distinct()
                .collect(Collectors.toList());
        Map<String, PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoMap = purchaseRawReceiptOrderPoList.stream()
                .collect(Collectors.toMap(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo, Function.identity()));
        List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByNoList(purchaseRawReceiptOrderNoList);

        //获取审版单
        final List<DevelopReviewOrderPo> developReviewOrderPoList = developReviewOrderDao.getListByDevelopPamphletOrderNoList(List.of(developPamphletOrderPo.getDevelopPamphletOrderNo()));
        Map<String, List<DevelopReviewOrderPo>> developReviewOrderPoMap = developReviewOrderPoList.stream()
                .collect(Collectors.groupingBy(DevelopReviewOrderPo::getDevelopPamphletOrderNo));

        //获取样品单信息
        Map<Long, List<String>> sampleImageSpecificationsMap = new HashMap<>();
        Map<Long, List<String>> sampleImageQuotationMap = new HashMap<>();
        Map<Long, List<String>> sampleImageMap = new HashMap<>();
        Map<String, List<DevelopSampleOrderRawPo>> developSampleOrderRawPoMap = new HashMap<>();
        Map<String, String> skuEncodeMap = new HashMap<>();
        Map<String, List<DevelopSampleOrderProcessPo>> developSampleOrderProcessPoMap = new HashMap<>();
        Map<String, List<DevelopSampleOrderProcessDescPo>> developSampleOrderProcessDescPoMap = new HashMap<>();
        Map<String, DevelopPricingOrderInfoPo> developPricingOrderInfoPoMap = new HashMap<>();
        Map<String, List<DevelopReviewSampleOrderPo>> developReviewSampleOrderPoMap = new HashMap<>();
        List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = new ArrayList<>();
        Map<Long, List<String>> developReviewSampleEffectMap = new HashMap<>();
        Map<Long, List<String>> developReviewSampleDetailMap = new HashMap<>();
        List<String> skuList = new ArrayList<>();

        if (StringUtils.isNotBlank(developChildOrderPo.getSku())) {
            skuList.add(developChildOrderPo.getSku());
        }

        //无需打样获取信息
        if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
            //样品单关联核价单信息
            developPricingOrderInfoPoMap = developPricingOrderInfoDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo())
                    .stream()
                    .collect(Collectors.toMap(itemPo -> itemPo.getDevelopPricingOrderNo() + "-" + itemPo.getDevelopPamphletOrderNo(), Function.identity()));

            List<DevelopSampleOrderProcessPo> developSampleOrderProcessPoList = developSampleOrderProcessDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderProcessPoMap = developSampleOrderProcessPoList.stream().collect(Collectors.groupingBy(DevelopSampleOrderProcessPo::getDevelopPamphletOrderNo));
            List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescPoList = developSampleOrderProcessDescDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderProcessDescPoMap = developSampleOrderProcessDescPoList.stream().collect(Collectors.groupingBy(DevelopSampleOrderProcessDescPo::getDevelopPamphletOrderNo));

            List<DevelopSampleOrderRawPo> developSampleOrderRawPoList = developSampleOrderRawDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderRawPoMap = developSampleOrderRawPoList.stream()
                    .collect(Collectors.groupingBy(DevelopSampleOrderRawPo::getDevelopPamphletOrderNo));
            List<String> skuNoSampleList = developSampleOrderRawPoList.stream().map(DevelopSampleOrderRawPo::getSku).distinct().collect(Collectors.toList());
            skuList.addAll(skuNoSampleList);
            sampleImageMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_SAMPLE, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));
            sampleImageQuotationMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_SAMPLE_QUOTATION, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));
            sampleImageSpecificationsMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_SAMPLE_SPECIFICATIONS, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));

        }

        //需要打样获取版单对应样品单
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        List<Long> developSampleOrderIdList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderId).distinct().collect(Collectors.toList());
        List<String> developSampleOrderNoList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
        List<String> developSampleSkuList = developSampleOrderPoList.stream()
                .filter(w -> w.getSku() != null && !w.getSku().isEmpty())
                .map(DevelopSampleOrderPo::getSku)
                .distinct().collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(developSampleOrderPoList)) {

            sampleImageMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_SAMPLE, developSampleOrderIdList);
            sampleImageSpecificationsMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_SAMPLE_SPECIFICATIONS, developSampleOrderIdList);
            sampleImageQuotationMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_SAMPLE_QUOTATION, developSampleOrderIdList);
            List<DevelopSampleOrderRawPo> developSampleOrderRawPoList = developSampleOrderRawDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
            developSampleOrderRawPoMap = developSampleOrderRawPoList.stream()
                    .collect(Collectors.groupingBy(DevelopSampleOrderRawPo::getDevelopSampleOrderNo));
            List<String> skuSampleList = developSampleOrderRawPoList.stream().map(DevelopSampleOrderRawPo::getSku).distinct().collect(Collectors.toList());
            skuList.addAll(developSampleSkuList);
            skuList.addAll(skuSampleList);
            List<DevelopSampleOrderProcessPo> developSampleOrderProcessPoList = developSampleOrderProcessDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
            developSampleOrderProcessPoMap = developSampleOrderProcessPoList.stream().collect(Collectors.groupingBy(DevelopSampleOrderProcessPo::getDevelopSampleOrderNo));
            List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescPoList = developSampleOrderProcessDescDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
            developSampleOrderProcessDescPoMap = developSampleOrderProcessDescPoList.stream().collect(Collectors.groupingBy(DevelopSampleOrderProcessDescPo::getDevelopSampleOrderNo));

            //样品单关联审版单信息
            List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
            developReviewSampleOrderPoMap = developReviewSampleOrderPoList.stream()
                    .collect(Collectors.groupingBy(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo));
            List<Long> developReviewSampleOrderIdList = developReviewSampleOrderPoList.stream().map(DevelopReviewSampleOrderPo::getDevelopReviewSampleOrderId).collect(Collectors.toList());
            developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);

            //样品单关联核价单信息
            developPricingOrderInfoPoMap = developPricingOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList)
                    .stream()
                    .collect(Collectors.toMap(itemPo -> itemPo.getDevelopPricingOrderNo() + "-" + itemPo.getDevelopSampleOrderNo(), Function.identity()));

            developReviewSampleEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT, developReviewSampleOrderIdList);
            developReviewSampleDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL, developReviewSampleOrderIdList);

        }
        if (CollectionUtils.isNotEmpty(skuList)) {
            skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        }

        //核价单信息
        DevelopPricingOrderPo developPricingOrderPo = developPricingOrderDao.getListByChildOrderNoOrderByTime(developChildOrderPo.getDevelopChildOrderNo());
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoPricingMap = new HashMap<>();
        if (developPricingOrderPo != null) {
            //核价单获取样品单
            developSampleOrderPoPricingMap = developSampleOrderDao.getListByDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo())
                    .stream().collect(Collectors.groupingBy(DevelopSampleOrderPo::getDevelopPricingOrderNo));
        }

        // 查询单据供应商报价大货价格/开发子单渠道价格
        List<String> newDevelopSampleOrderNoList = new ArrayList<>(developSampleOrderNoList);
        newDevelopSampleOrderNoList.add(developChildOrderPo.getDevelopChildOrderNo());
        if (developPricingOrderPo != null) {
            newDevelopSampleOrderNoList.add(developPricingOrderPo.getDevelopPricingOrderNo());
        }
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(newDevelopSampleOrderNoList,
                List.of(DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE,
                        DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE,
                        DevelopOrderPriceType.PRICING_PURCHASE_PRICE,
                        DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE));

        //开发子单数据处理
        DevelopChildDetailVo developChildDetailVo = DevelopChildConverter.convertDevelopChildPoToVo(developChildOrderPo, developChildOrderChangePo,
                developChildOrderAttrPoList, developPamphletOrderPo, developPamphletOrderRawPoList, pamphletImageStyleMap,
                pamphletImageColorMap, purchaseRawReceiptOrderItemPoList, purchaseRawReceiptOrderPoMap, fileCodeMap,
                developOrderPricePoList, skuEncodeMap);


        final PurchaseChildOrderPo firstPurchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(developChildOrderPo.getFirstSampleOrderNo());
        final PurchaseChildOrderPo prenatalPurchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(developChildOrderPo.getPrenatalSampleOrderNo());
        final DevelopPrenatalFirstMsgVo developPrenatalFirstMsgVo = DevelopChildConverter.convertPurchasePoToPrenatalFirstVo(prenatalPurchaseChildOrderPo,
                firstPurchaseChildOrderPo, developChildOrderPo.getPurchaseParentOrderNo(), developChildDetailVo.getDevelopChildBaseMsgVo());

        //审版单信息
        DevelopReviewMsgVo developReviewMsgVo = new DevelopReviewMsgVo();
        Optional.ofNullable(developReviewOrderPoMap.get(developChildDetailVo.getDevelopPamphletMsgVo().getDevelopPamphletOrderNo()))
                .flatMap(l -> l.stream().max(Comparator.comparing(DevelopReviewOrderPo::getCreateTime)))
                .ifPresent(item -> {
                    developReviewMsgVo.setDevelopReviewOrderNo(item.getDevelopReviewOrderNo());
                    developReviewMsgVo.setDevelopReviewOrderStatus(item.getDevelopReviewOrderStatus());
                    developReviewMsgVo.setReviewResult(item.getReviewResult());
                });
        developChildDetailVo.setDevelopReviewMsgVo(developReviewMsgVo);

        //开发子单数据处理
        return DevelopChildConverter.convertDevelopChildSamplePoToVo(developChildDetailVo, developSampleOrderPoList,
                sampleImageSpecificationsMap, sampleImageQuotationMap, sampleImageMap,
                developSampleOrderRawPoMap, skuEncodeMap, developSampleOrderProcessPoMap, developSampleOrderProcessDescPoMap,
                developPricingOrderInfoPoMap, developReviewSampleOrderPoMap, developReviewSampleOrderInfoPoList, developReviewSampleEffectMap,
                developReviewSampleDetailMap, developPricingOrderPo, developSampleOrderPoPricingMap, developPamphletOrderPo,
                developPrenatalFirstMsgVo, developOrderPricePoList);
    }

    /**
     * 编辑样品信息
     *
     * @param developSampleOrderDetailList:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/17 13:44
     */
    public void updateDevelopChildSample(List<DevelopSampleOrderDetailDto> developSampleOrderDetailList,
                                         DevelopChildOrderPo developChildOrderPo,
                                         DevelopPamphletOrderPo developPamphletOrderPo) {
        List<String> developSampleOrderNoList = developSampleOrderDetailList.stream().map(DevelopSampleOrderDetailDto::getDevelopSampleOrderNo)
                .distinct().collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);
        List<DevelopSampleOrderRawPo> developSampleOrderRawPoList = new ArrayList<>();
        List<DevelopSampleOrderProcessPo> developSampleOrderProcessPoList = new ArrayList<>();
        List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescList = new ArrayList<>();
        List<DevelopOrderPriceCreateBo> developOrderPriceCreateBoList = new ArrayList<>();
        for (DevelopSampleOrderDetailDto detailDto : developSampleOrderDetailList) {
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream()
                    .filter(w -> w.getDevelopSampleOrderNo().equals(detailDto.getDevelopSampleOrderNo())
                            && w.getVersion().equals(detailDto.getVersion()))
                    .findFirst().orElse(null);

            //样品或版单ID
            Long itemId;
            if (developSampleOrderPo == null) {
                //无需打样处理
                if (!BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
                    throw new ParamIllegalException("样品单:{}信息已被修改，请刷新页面后重试！", detailDto.getDevelopSampleOrderNo());
                }
                itemId = developPamphletOrderPo.getDevelopPamphletOrderId();
            } else {
                //需要打样处理
                itemId = developSampleOrderPo.getDevelopSampleOrderId();
                developSampleOrderPo.setProcessRemarks(detailDto.getProcessRemarks());
                developSampleOrderPo.setSamplePrice(detailDto.getSupplierSamplePrice());
                // 更新供应商渠道报价大货价格
                DevelopOrderPriceCreateBo developOrderPriceCreateBo = new DevelopOrderPriceCreateBo();
                developOrderPriceCreateBo.setDevelopOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developOrderPriceCreateBo.setDevelopOrderPriceType(DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE);
                List<DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo> developOrderPriceCreateItemBoList = Optional.ofNullable(detailDto.getDevelopOrderPriceList())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(item -> {
                            DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo developOrderPriceCreateItemBo = new DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo();
                            developOrderPriceCreateItemBo.setChannelId(item.getChannelId());
                            developOrderPriceCreateItemBo.setPrice(item.getPrice());
                            return developOrderPriceCreateItemBo;
                        }).collect(Collectors.toList());
                developOrderPriceCreateBo.setDevelopOrderPriceCreateItemBoList(developOrderPriceCreateItemBoList);
                developOrderPriceCreateBoList.add(developOrderPriceCreateBo);
            }


            //规格书
            List<Long> longs = List.of(itemId);
            List<String> scmImagePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEVELOP_SAMPLE_SPECIFICATIONS, longs);
            if (CollectionUtils.isNotEmpty(detailDto.getSpecificationsFileCodeList())) {
                scmImageBaseService.editImage(detailDto.getSpecificationsFileCodeList(), scmImagePos, ImageBizType.DEVELOP_SAMPLE_SPECIFICATIONS, itemId);
            } else {
                scmImageBaseService.removeAllImage(ImageBizType.DEVELOP_SAMPLE_SPECIFICATIONS, itemId);
            }

            //报价单
            List<String> scmImageQuotationPos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEVELOP_SAMPLE_QUOTATION, longs);
            if (CollectionUtils.isNotEmpty(detailDto.getQuotationFileCodeList())) {
                scmImageBaseService.editImage(detailDto.getQuotationFileCodeList(), scmImageQuotationPos, ImageBizType.DEVELOP_SAMPLE_QUOTATION, itemId);
            } else {
                scmImageBaseService.removeAllImage(ImageBizType.DEVELOP_SAMPLE_QUOTATION, itemId);
            }

            //样品图片
            List<String> scmImageFilePos = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEVELOP_SAMPLE, longs);
            if (CollectionUtils.isNotEmpty(detailDto.getFileCodeList())) {
                scmImageBaseService.editImage(detailDto.getFileCodeList(), scmImageFilePos, ImageBizType.DEVELOP_SAMPLE, itemId);
            } else {
                scmImageBaseService.removeAllImage(ImageBizType.DEVELOP_SAMPLE, itemId);
            }

            //原料
            if (CollectionUtils.isNotEmpty(detailDto.getDevelopSampleOrderRawList())) {
                Set<String> skuRawSet = new HashSet<>();
                detailDto.getDevelopSampleOrderRawList().forEach(developSampleOrderRaw -> {
                    if (skuRawSet.contains(developSampleOrderRaw.getSku())) {
                        throw new BizException("禁止添加的重复的原料sku:{}", developSampleOrderRaw.getSku());
                    } else {
                        skuRawSet.add(developSampleOrderRaw.getSku());
                    }
                });
                List<DevelopSampleOrderRawPo> developSampleOrderRawPos = DevelopSampleOrderRawConverter.INSTANCE.convert(detailDto.getDevelopSampleOrderRawList());
                developSampleOrderRawPoList.addAll(developSampleOrderRawPos.stream()
                        .peek(item -> {
                            item.setDevelopSampleOrderNo(detailDto.getDevelopSampleOrderNo());
                            item.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                            item.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                            item.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        }).collect(Collectors.toList()));
            }

            //工序
            if (CollectionUtils.isNotEmpty(detailDto.getDevelopSampleOrderProcessList())) {
                List<DevelopSampleOrderProcessPo> developSampleOrderProcessPos = DevelopSampleOrderProcessConverter.INSTANCE.convert(detailDto.getDevelopSampleOrderProcessList());
                developSampleOrderProcessPoList.addAll(developSampleOrderProcessPos.stream()
                        .peek(item -> {
                            item.setDevelopSampleOrderNo(detailDto.getDevelopSampleOrderNo());
                            item.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                            item.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                            item.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        }).collect(Collectors.toList()));
            }

            //工序描述
            if (CollectionUtils.isNotEmpty(detailDto.getDevelopSampleOrderProcessDescList())) {
                List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescPos = DevelopSampleOrderProcessDescConverter.INSTANCE.convert(detailDto.getDevelopSampleOrderProcessDescList());
                developSampleOrderProcessDescList.addAll(developSampleOrderProcessDescPos.stream()
                        .peek(item -> {
                            item.setDevelopSampleOrderNo(detailDto.getDevelopSampleOrderNo());
                            item.setDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
                            item.setDevelopParentOrderNo(developPamphletOrderPo.getDevelopParentOrderNo());
                            item.setDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
                        }).collect(Collectors.toList()));
            }

        }

        //更新样品单信息 工序补充、供应商样品报价、供应商大货报价
        if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
            developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);
        }
        //无需打样处理
        List<DevelopSampleOrderRawPo> developSampleOrderRawPoOldList = developSampleOrderRawDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
        List<DevelopSampleOrderProcessPo> developSampleOrderProcessPoOldList = developSampleOrderProcessDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
        List<DevelopSampleOrderProcessDescPo> developSampleOrderProcessDescPoOldList = developSampleOrderProcessDescDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
        if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
            //无需打样 需要版本号查询
            developSampleOrderRawPoOldList = developSampleOrderRawDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderProcessPoOldList = developSampleOrderProcessDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
            developSampleOrderProcessDescPoOldList = developSampleOrderProcessDescDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        }

        //删除原来原料旧数据增加新的
        if (CollectionUtils.isNotEmpty(developSampleOrderRawPoOldList)) {
            List<Long> developSampleOrderRawIdList = developSampleOrderRawPoOldList.stream()
                    .map(DevelopSampleOrderRawPo::getDevelopSampleOrderRawId)
                    .distinct()
                    .collect(Collectors.toList());
            developSampleOrderRawDao.removeBatchByIds(developSampleOrderRawIdList);
        }
        developSampleOrderRawDao.insertBatch(developSampleOrderRawPoList);

        //删除原来工序旧数据增加新的
        if (CollectionUtils.isNotEmpty(developSampleOrderProcessPoOldList)) {
            List<Long> developSampleOrderProcessIdList = developSampleOrderProcessPoOldList.stream()
                    .map(DevelopSampleOrderProcessPo::getDevelopSampleOrderProcessId)
                    .distinct()
                    .collect(Collectors.toList());
            developSampleOrderProcessDao.removeBatchByIds(developSampleOrderProcessIdList);
        }
        developSampleOrderProcessDao.insertBatch(developSampleOrderProcessPoList);

        //删除原来工序描述旧数据增加新的
        if (CollectionUtils.isNotEmpty(developSampleOrderProcessDescPoOldList)) {
            List<Long> developSampleOrderProcessDescIdList = developSampleOrderProcessDescPoOldList.stream()
                    .map(DevelopSampleOrderProcessDescPo::getDevelopSampleOrderProcessDescId)
                    .distinct()
                    .collect(Collectors.toList());
            developSampleOrderProcessDescDao.removeBatchByIds(developSampleOrderProcessDescIdList);
        }
        developSampleOrderProcessDescDao.insertBatch(developSampleOrderProcessDescList);

        // 更新渠道供应商报价大货价格
        this.developOrderPriceBatchSave(developOrderPriceCreateBoList);

    }

    /**
     * 创建处理子单异常处理记录
     *
     * @param developParentOrderNo:
     * @param developChildOrderNo:
     * @param developPamphletOrderNo:
     * @param developReviewOrderNo:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/26 17:11
     */
    public void createDevelopExceptionOrder(String developParentOrderNo,
                                            String developChildOrderNo,
                                            String developPamphletOrderNo,
                                            String developReviewOrderNo,
                                            DevelopChildOrderPo developChildOrderPo) {
        //更新开发子单异常
        developChildOrderPo.setHasException(BooleanType.TRUE);
        developChildOrderDao.updateByIdVersion(developChildOrderPo);
        DevelopExceptionOrderPo developExceptionOrderPo = new DevelopExceptionOrderPo();
        developExceptionOrderPo.setDevelopParentOrderNo(developParentOrderNo);
        developExceptionOrderPo.setDevelopChildOrderNo(developChildOrderNo);
        developExceptionOrderPo.setDevelopPamphletOrderNo(developPamphletOrderNo);
        developExceptionOrderPo.setDevelopReviewOrderNo(developReviewOrderNo);
        developExceptionOrderDao.insert(developExceptionOrderPo);
    }


    /**
     * 更新异常处理信息
     *
     * @param developPamphletOrderPo:
     * @param newDevelopPamphletOrderPo:
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/26 17:39
     */
    public void updateDevelopExceptionOrder(DevelopPamphletOrderPo developPamphletOrderPo,
                                            DevelopPamphletOrderPo newDevelopPamphletOrderPo,
                                            DevelopChildExceptionalDto dto) {
        List<DevelopExceptionOrderPo> developExceptionOrderPoList = developExceptionOrderDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());
        for (DevelopExceptionOrderPo developExceptionOrderPo : developExceptionOrderPoList) {
            developExceptionOrderPo.setDevelopChildExceptionalType(dto.getDevelopChildExceptionalType());
            developExceptionOrderPo.setCancelReason(dto.getCancelReason());
            developExceptionOrderPo.setSupplierCode(dto.getSupplierCode());
            developExceptionOrderPo.setSupplierName(dto.getSupplierName());
            developExceptionOrderPo.setDemandDesc(dto.getDemandDesc());
            developExceptionOrderPo.setNewDevelopPamphletOrderNo(newDevelopPamphletOrderPo.getDevelopPamphletOrderNo());
        }
        developExceptionOrderDao.updateBatchByIdVersion(developExceptionOrderPoList);
    }

    /**
     * 获取开发子单款式图片
     *
     * @param developChildOrderPoList:
     * @return Map<String, List < String>>
     * @author ChenWenLong
     * @date 2023/9/9 14:12
     */
    public Map<String, List<String>> getDevelopChildOrderStyleImg(List<DevelopChildOrderPo> developChildOrderPoList) {
        if (CollectionUtils.isEmpty(developChildOrderPoList)) {
            return Collections.emptyMap();
        }
        // 获取图片
        final List<Long> developChildOrderIdList = developChildOrderPoList.stream()
                .map(DevelopChildOrderPo::getDevelopChildOrderId)
                .collect(Collectors.toList());
        Map<Long, String> developChildOrderPoMap = developChildOrderPoList.stream()
                .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderId, DevelopChildOrderPo::getDevelopChildOrderNo));
        Map<Long, List<String>> developChildOrderIdMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_CHILD_STYLE, developChildOrderIdList);
        Map<String, List<String>> developChildOrderMap = new HashMap<>();
        developChildOrderIdMap.forEach((Long key, List<String> val) -> {
            if (developChildOrderPoMap.containsKey(key)) {
                developChildOrderMap.put(developChildOrderPoMap.get(key), val);
            }
        });
        return developChildOrderMap;
    }

    /**
     * 获取开发子单状态栏信息
     *
     * @param supplierCodeList:
     * @return DevelopChildOrderStatusListVo
     * @author ChenWenLong
     * @date 2023/9/19 14:57
     */
    public DevelopChildOrderStatusListVo developChildOrderStatus(List<String> supplierCodeList) {
        //开发子单分组统计
        Map<DevelopChildOrderStatus, Integer> developChildStatusMap = Optional.ofNullable(developChildOrderDao.getListByGroupByStatus(supplierCodeList))
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(DevelopChildGroupByStatusBo::getDevelopChildOrderStatus, DevelopChildGroupByStatusBo::getNum));

        //版单分组统计
        Map<DevelopPamphletOrderStatus, Integer> developPamphletStatusMap = Optional.ofNullable(developPamphletOrderDao.getListByGroupByStatus(supplierCodeList))
                .orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(DevelopPamphletGroupByStatusBo::getDevelopPamphletOrderStatus, DevelopPamphletGroupByStatusBo::getNum));

        //审版单分组统计
        Map<DevelopReviewOrderStatus, Integer> developReviewStatusMap = Optional.ofNullable(developReviewOrderDao.getListByGroupByStatus(List.of(DevelopReviewOrderType.SAMPLE_REVIEW), supplierCodeList))
                .orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(DevelopReviewGroupByStatusBo::getDevelopReviewOrderStatus, DevelopReviewGroupByStatusBo::getNum));

        //核价单分组统计
        Map<DevelopPricingOrderStatus, Integer> developPricingStatusMap = Optional.ofNullable(developPricingOrderDao.getListByGroupByStatus(supplierCodeList))
                .orElse(new ArrayList<>()).stream()
                .collect(Collectors.toMap(DevelopPricingGroupByStatusBo::getDevelopPricingOrderStatus, DevelopPricingGroupByStatusBo::getNum));


        List<DevelopChildOrderStatusVo> developChildOrderStatusList = new ArrayList<>();
        List<DevelopPamphletOrderStatusVo> developPamphletOrderStatusList = new ArrayList<>();
        List<DevelopReviewOrderStatusVo> developReviewOrderStatusList = new ArrayList<>();
        List<DevelopPricingOrderStatusVo> developPricingOrderStatusList = new ArrayList<>();
        DevelopChildOrderStatus[] developChildOrderStatusArr = DevelopChildOrderStatus.values();
        List<DevelopPamphletOrderStatus> developPamphletOrderStatusArr = DevelopPamphletOrderStatus.developPamphletMenuList();
        List<DevelopReviewOrderStatus> developReviewOrderStatusArr = DevelopReviewOrderStatus.developReviewMenuList();
        List<DevelopPricingOrderStatus> developPricingOrderStatusArr = DevelopPricingOrderStatus.developPricingMenuList();
        for (DevelopChildOrderStatus developChildOrderStatus : developChildOrderStatusArr) {
            final DevelopChildOrderStatusVo developChildOrderStatusVo = new DevelopChildOrderStatusVo();
            developChildOrderStatusVo.setDevelopChildOrderStatus(developChildOrderStatus);
            developChildOrderStatusVo.setDataCnt(developChildStatusMap.getOrDefault(developChildOrderStatus, 0));
            developChildOrderStatusList.add(developChildOrderStatusVo);
        }
        for (DevelopPamphletOrderStatus developPamphletOrderStatus : developPamphletOrderStatusArr) {
            final DevelopPamphletOrderStatusVo developPamphletOrderStatusVo = new DevelopPamphletOrderStatusVo();
            developPamphletOrderStatusVo.setDevelopPamphletOrderStatus(developPamphletOrderStatus);
            if (!DevelopPamphletOrderStatus.CANCEL_PAMPHLET.equals(developPamphletOrderStatus)) {
                developPamphletOrderStatusVo.setDataCnt(developPamphletStatusMap.getOrDefault(developPamphletOrderStatus, 0));
            }
            developPamphletOrderStatusList.add(developPamphletOrderStatusVo);
        }
        for (DevelopReviewOrderStatus developReviewOrderStatus : developReviewOrderStatusArr) {
            final DevelopReviewOrderStatusVo developReviewOrderStatusVo = new DevelopReviewOrderStatusVo();
            developReviewOrderStatusVo.setDevelopReviewOrderStatus(developReviewOrderStatus);
            if (!DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderStatus)) {
                developReviewOrderStatusVo.setDataCnt(developReviewStatusMap.getOrDefault(developReviewOrderStatus, 0));
            }
            developReviewOrderStatusList.add(developReviewOrderStatusVo);
        }
        for (DevelopPricingOrderStatus developPricingOrderStatus : developPricingOrderStatusArr) {
            final DevelopPricingOrderStatusVo developPricingOrderStatusVo = new DevelopPricingOrderStatusVo();
            developPricingOrderStatusVo.setDevelopPricingOrderStatus(developPricingOrderStatus);
            developPricingOrderStatusVo.setDataCnt(developPricingStatusMap.getOrDefault(developPricingOrderStatus, 0));
            developPricingOrderStatusList.add(developPricingOrderStatusVo);
        }

        return DevelopChildOrderStatusListVo.builder().developChildOrderStatusList(developChildOrderStatusList)
                .developPamphletOrderStatusList(developPamphletOrderStatusList)
                .developReviewOrderStatusList(developReviewOrderStatusList)
                .developPricingOrderStatusList(developPricingOrderStatusList).build();

    }

    /**
     * 开发子单日志
     *
     * @param developChildOrderPo:
     * @param scenes:
     * @return void
     * @author ChenWenLong
     * @date 2024/4/6 13:06
     */
    public void createStatusChangeLog(DevelopChildOrderPo developChildOrderPo, String scenes) {

        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.DEVELOP_CHILD_ORDER_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.DEVELOP_CHILD_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.DEVELOP_CHILD_ORDER_STATUS.name());
        bizLogCreateMqDto.setBizCode(developChildOrderPo.getDevelopChildOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());
        bizLogCreateMqDto.setOperateUser(StringUtils.isBlank(GlobalContext.getUserKey()) ?
                ScmConstant.SYSTEM_USER : GlobalContext.getUserKey());
        bizLogCreateMqDto.setOperateUsername(StringUtils.isBlank(GlobalContext.getUsername()) ?
                ScmConstant.SYSTEM_USER : GlobalContext.getUsername());
        bizLogCreateMqDto.setContent(developChildOrderPo.getDevelopChildOrderStatus().getRemark());

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();
        if (StringUtils.isNotBlank(scenes)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(LOG_KEY);
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(scenes);
            logVersionBos.add(logVersionBo);
        }
        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);


    }

    /**
     * 单个相关单据大货价格更新
     *
     * @param dtoList:
     * @param developOrderNo:
     * @param developOrderPriceType:
     * @author ChenWenLong
     * @date 2024/8/15 14:26
     */
    public void developOrderPriceSave(List<DevelopOrderPriceSaveDto> dtoList,
                                      @NotBlank String developOrderNo,
                                      @NotNull DevelopOrderPriceType developOrderPriceType) {

        // 查询旧数据
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoAndType(developOrderNo, developOrderPriceType);

        if (CollectionUtils.isNotEmpty(developOrderPricePoList)) {
            // 删除旧数据
            List<Long> developOrderPriceIds = developOrderPricePoList.stream().map(DevelopOrderPricePo::getDevelopOrderPriceId).collect(Collectors.toList());
            developOrderPriceDao.removeBatchByIds(developOrderPriceIds);
        }

        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }

        List<Long> channelIdList = dtoList.stream()
                .map(DevelopOrderPriceSaveDto::getChannelId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> channelNameMap = channelDao.getNameMapByIdList(channelIdList);

        // 写入新数据
        List<DevelopOrderPricePo> developOrderPricePoNewList = dtoList.stream().map(dto -> {
            DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
            developOrderPricePo.setDevelopOrderNo(developOrderNo);
            developOrderPricePo.setDevelopOrderPriceType(developOrderPriceType);
            developOrderPricePo.setChannelId(dto.getChannelId());
            developOrderPricePo.setChannelName(channelNameMap.get(dto.getChannelId()));
            developOrderPricePo.setPrice(dto.getPrice());
            if (null == dto.getIsDefaultPrice()) {
                developOrderPricePo.setIsDefaultPrice(BooleanType.FALSE);
            } else {
                developOrderPricePo.setIsDefaultPrice(dto.getIsDefaultPrice());
            }
            return developOrderPricePo;
        }).collect(Collectors.toList());

        developOrderPriceDao.insertBatch(developOrderPricePoNewList);

    }

    /**
     * 批量相关单据大货价格更新
     *
     * @param boList:
     * @author ChenWenLong
     * @date 2024/8/15 15:18
     */
    public void developOrderPriceBatchSave(List<DevelopOrderPriceCreateBo> boList) {

        if (CollectionUtils.isEmpty(boList)) {
            return;
        }

        // 获取关联单据号列表
        List<DevelopOrderPriceBatchQueryBo> developOrderPriceBatchQueryBoList = boList.stream()
                .map(bo -> {
                    DevelopOrderPriceBatchQueryBo developOrderPriceBatchQueryBo = new DevelopOrderPriceBatchQueryBo();
                    developOrderPriceBatchQueryBo.setDevelopOrderNo(bo.getDevelopOrderNo());
                    developOrderPriceBatchQueryBo.setDevelopOrderPriceType(bo.getDevelopOrderPriceType());
                    return developOrderPriceBatchQueryBo;
                }).collect(Collectors.toList());

        // 查询旧数据
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoAndTypeBatch(developOrderPriceBatchQueryBoList);

        if (CollectionUtils.isNotEmpty(developOrderPricePoList)) {
            // 删除旧数据
            List<Long> developOrderPriceIds = developOrderPricePoList.stream().map(DevelopOrderPricePo::getDevelopOrderPriceId).collect(Collectors.toList());
            developOrderPriceDao.removeBatchByIds(developOrderPriceIds);
        }

        // 查询渠道名称
        List<Long> channelIdList = boList.stream()
                .flatMap(bo -> Optional.ofNullable(bo.getDevelopOrderPriceCreateItemBoList()).orElse(Collections.emptyList()).stream())
                .map(DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo::getChannelId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> channelNameMap = channelDao.getNameMapByIdList(channelIdList);

        // 写入新数据
        List<DevelopOrderPricePo> developOrderPricePoNewList = new ArrayList<>();
        boList.forEach(bo -> {
            if (CollectionUtils.isNotEmpty(bo.getDevelopOrderPriceCreateItemBoList())) {
                bo.getDevelopOrderPriceCreateItemBoList().forEach(item -> {
                    DevelopOrderPricePo developOrderPricePo = new DevelopOrderPricePo();
                    developOrderPricePo.setDevelopOrderNo(bo.getDevelopOrderNo());
                    developOrderPricePo.setDevelopOrderPriceType(bo.getDevelopOrderPriceType());
                    developOrderPricePo.setChannelId(item.getChannelId());
                    developOrderPricePo.setChannelName(channelNameMap.get(item.getChannelId()));
                    developOrderPricePo.setPrice(item.getPrice());
                    if (null == item.getIsDefaultPrice()) {
                        developOrderPricePo.setIsDefaultPrice(BooleanType.FALSE);
                    } else {
                        developOrderPricePo.setIsDefaultPrice(item.getIsDefaultPrice());
                    }
                    developOrderPricePoNewList.add(developOrderPricePo);
                });
            }
        });

        if (CollectionUtils.isNotEmpty(developOrderPricePoNewList)) {
            developOrderPriceDao.insertBatch(developOrderPricePoNewList);
        }

    }

    /**
     * 获取样品单的默认渠道大货价格
     *
     * @param developOrderNoList:
     * @param developOrderPriceType:
     * @return Map<String, BigDecimal>
     * @author ChenWenLong
     * @date 2024/8/23 17:40
     */
    public Map<String, BigDecimal> getDevelopOrderPriceDefaultPrice(List<String> developOrderNoList,
                                                                    DevelopOrderPriceType developOrderPriceType) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(developOrderNoList)) {
            return resultMap;
        }
        List<String> developOrderNos = developOrderNoList.stream().distinct().collect(Collectors.toList());
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developOrderNos, List.of(developOrderPriceType));
        for (String developOrderNo : developOrderNos) {
            developOrderPricePoList.stream()
                    .filter(po -> BooleanType.TRUE.equals(po.getIsDefaultPrice()))
                    .findFirst().ifPresent(developOrderPricePo -> resultMap.putIfAbsent(developOrderNo, developOrderPricePo.getPrice()));
        }
        return resultMap;

    }

    /**
     * 获取样品单的默认渠道PO
     *
     * @param developOrderNoList:
     * @param developOrderPriceType:
     * @return Map<String, DevelopOrderPricePo>
     * @author ChenWenLong
     * @date 2024/8/26 18:45
     */
    public Map<String, DevelopOrderPricePo> getDevelopOrderPriceDefaultPo(List<String> developOrderNoList,
                                                                          DevelopOrderPriceType developOrderPriceType) {
        Map<String, DevelopOrderPricePo> resultMap = new HashMap<>();
        if (CollectionUtils.isEmpty(developOrderNoList)) {
            return resultMap;
        }
        List<String> developOrderNos = developOrderNoList.stream().distinct().collect(Collectors.toList());
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developOrderNos, List.of(developOrderPriceType));
        for (String developOrderNo : developOrderNos) {
            developOrderPricePoList.stream()
                    .filter(po -> BooleanType.TRUE.equals(po.getIsDefaultPrice()))
                    .findFirst().ifPresent(developOrderPricePo -> resultMap.putIfAbsent(developOrderNo, developOrderPricePo));
        }
        return resultMap;

    }

    /**
     * 统一获取样品单大货价格和样品价格
     *
     * @param bo:
     * @return List<SampleOrderPriceVo>
     * @author ChenWenLong
     * @date 2024/8/27 16:03
     */
    public List<SampleOrderPriceResultBo> getSampleOrderPriceBo(SampleOrderPriceBo bo) {
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByChildNoOrSampleNoList(bo.getDevelopPamphletOrderNo(),
                bo.getDevelopSampleOrderNoList());
        if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
            return Collections.emptyList();
        }

        // 查询核价单
        List<String> developSampleOrderNoList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());
        List<DevelopPricingOrderInfoPo> developPricingOrderInfoPoList = developPricingOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
        Map<String, DevelopPricingOrderInfoPo> developPricingOrderInfoPoMap = developPricingOrderInfoPoList.stream()
                .collect(Collectors.toMap(DevelopPricingOrderInfoPo::getDevelopSampleOrderNo, Function.identity(), (existing, replacement) -> existing));

        // 查询渠道大货价格
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(developSampleOrderNoList,
                List.of(DevelopOrderPriceType.PRICING_PURCHASE_PRICE, DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE, DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE, DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE));

        return developSampleOrderPoList.stream().map(developSampleOrderPo -> {
            SampleOrderPriceResultBo sampleOrderPriceResultBo = new SampleOrderPriceResultBo();
            sampleOrderPriceResultBo.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
            sampleOrderPriceResultBo.setVersion(developSampleOrderPo.getVersion());
            sampleOrderPriceResultBo.setDevelopSampleMethod(developSampleOrderPo.getDevelopSampleMethod());
            // 价格默认值
            BigDecimal samplePrice = BigDecimal.ZERO;

            // 取核价单
            if (developPricingOrderInfoPoMap.containsKey(developSampleOrderPo.getDevelopSampleOrderNo())) {
                samplePrice = developPricingOrderInfoPoMap.get(developSampleOrderPo.getDevelopSampleOrderNo()).getSamplePrice();
            }

            // 取供应商报价
            if (samplePrice.compareTo(BigDecimal.ZERO) == 0) {
                samplePrice = developSampleOrderPo.getSamplePrice();
            }

            // 设置核价渠道大货价格
            List<DevelopOrderPricePo> developOrderPricePoFilterList = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> List.of(DevelopOrderPriceType.PRICING_PURCHASE_PRICE, DevelopOrderPriceType.PRICING_NOT_PURCHASE_PRICE).contains(developOrderPricePo.getDevelopOrderPriceType()))
                    .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .collect(Collectors.toList());
            sampleOrderPriceResultBo.setDevelopOrderPriceList(DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterList));

            // 如果是产前样以审版填价格为准(即样品单的批次价格)
            if (DevelopSampleType.PRENATAL_SAMPLE.equals(developSampleOrderPo.getDevelopSampleType())) {
                samplePrice = developSampleOrderPo.getSkuBatchSamplePrice();
                // 设置产前样渠道大货价格
                List<DevelopOrderPricePo> developOrderPricePoPrenatalList = developOrderPricePoList.stream()
                        .filter(developOrderPricePo -> DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                        .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                        .collect(Collectors.toList());
                sampleOrderPriceResultBo.setDevelopOrderPriceList(DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoPrenatalList));
            }

            // 设置供应商报价渠道大货价格
            List<DevelopOrderPricePo> developOrderPricePoFilterSupplierList = developOrderPricePoList.stream()
                    .filter(developOrderPricePo -> DevelopOrderPriceType.SUPPLIER_SAMPLE_PURCHASE_PRICE.equals(developOrderPricePo.getDevelopOrderPriceType()))
                    .filter(developOrderPricePo -> developOrderPricePo.getDevelopOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sampleOrderPriceResultBo.getDevelopOrderPriceList())) {
                sampleOrderPriceResultBo.setDevelopOrderPriceList(DevelopChildConverter.developOrderPricePoToVoList(developOrderPricePoFilterSupplierList));
            }

            sampleOrderPriceResultBo.setSamplePrice(samplePrice);
            return sampleOrderPriceResultBo;
        }).collect(Collectors.toList());
    }

    /**
     * 手动检验大货价格
     *
     * @param developOrderPriceList:
     * @return void
     * @author ChenWenLong
     * @date 2024/8/29 10:23
     */
    public void verifyDevelopOrderPrice(List<DevelopOrderPriceSaveDto> developOrderPriceList) {
        if (CollectionUtils.isEmpty(developOrderPriceList)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(developOrderPriceList)) {
            for (DevelopOrderPriceSaveDto developOrderPriceSaveDto : developOrderPriceList) {
                // Validate channelId
                if (developOrderPriceSaveDto.getChannelId() == null) {
                    throw new ParamIllegalException("渠道ID不能为空");
                }

                // Validate price
                BigDecimal price = developOrderPriceSaveDto.getPrice();
                if (price == null) {
                    throw new ParamIllegalException("大货价格不能为空");
                }

                if (price.compareTo(new BigDecimal("99999999.99")) > 0) {
                    throw new ParamIllegalException("大货价格不能超过1亿");
                }

                if (price.scale() > 2) {
                    throw new ParamIllegalException("大货价格小数位数不能超过两位");
                }

                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ParamIllegalException("大货价格必须大于0");
                }
            }
        }
    }
}
