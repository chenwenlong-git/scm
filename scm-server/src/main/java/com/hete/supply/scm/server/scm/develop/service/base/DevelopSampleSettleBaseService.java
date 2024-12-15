package com.hete.supply.scm.server.scm.develop.service.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.DevelopSampleSettleSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleMethod;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleSettleStatus;
import com.hete.supply.scm.api.scm.entity.enums.DevelopSampleStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.server.scm.develop.dao.DevelopChildOrderDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopPricingOrderInfoDao;
import com.hete.supply.scm.server.scm.develop.dao.DevelopSampleOrderDao;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderDetailDto;
import com.hete.supply.scm.server.scm.develop.entity.dto.DevelopSampleSettleOrderExamineDto;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopChildOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopPricingOrderInfoPo;
import com.hete.supply.scm.server.scm.develop.entity.po.DevelopSampleOrderPo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleOrderDetailVo;
import com.hete.supply.scm.server.scm.develop.entity.vo.DevelopSampleSettleSearchVo;
import com.hete.supply.scm.server.scm.develop.enums.DevelopSampleSettleExamine;
import com.hete.supply.scm.server.scm.entity.bo.LogVersionBo;
import com.hete.supply.scm.server.scm.entity.dto.BizLogCreateMqDto;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleItemPo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettleOrderPo;
import com.hete.supply.scm.server.scm.entity.po.DevelopSampleSettlePayPo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.LogVersionValueType;
import com.hete.supply.scm.server.scm.handler.LogVersionHandler;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.converter.DevelopSampleSettleItemConverter;
import com.hete.supply.scm.server.scm.settle.converter.DevelopSampleSettleOrderConverter;
import com.hete.supply.scm.server.scm.settle.converter.DevelopSampleSettlePayConverter;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleItemDao;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettleOrderDao;
import com.hete.supply.scm.server.scm.settle.dao.DevelopSampleSettlePayDao;
import com.hete.supply.scm.server.scm.settle.enums.PurchaseSettleExamine;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.TimeUtil;
import com.hete.support.core.util.TimeZoneId;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2023/8/1 14:35
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DevelopSampleSettleBaseService {


    private final DevelopSampleSettleOrderDao developSampleSettleOrderDao;
    private final DevelopSampleSettleItemDao developSampleSettleItemDao;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final PlmRemoteService plmRemoteService;
    private final SupplierBaseService supplierBaseService;
    private final DevelopSampleSettlePayDao developSampleSettlePayDao;
    private final ScmImageBaseService scmImageBaseService;
    private final IdGenerateService idGenerateService;
    private final ConsistencySendMqService consistencySendMqService;
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopPricingOrderInfoDao developPricingOrderInfoDao;

    public CommonPageResult.PageInfo<DevelopSampleSettleSearchVo> search(DevelopSampleSettleSearchDto dto) {
        //条件过滤
        if (null == this.getSearchWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        CommonPageResult.PageInfo<DevelopSampleSettleSearchVo> pageResult = developSampleSettleOrderDao.search(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        List<DevelopSampleSettleSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }
        List<String> developSampleSettleOrderNoList = records.stream().map(DevelopSampleSettleSearchVo::getDevelopSampleSettleOrderNo).collect(Collectors.toList());
        List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByNoList(developSampleSettleOrderNoList);

        for (DevelopSampleSettleSearchVo record : records) {
            Long itemTotal = developSampleSettleItemPoList.stream()
                    .filter(w -> w.getDevelopSampleSettleOrderNo().equals(record.getDevelopSampleSettleOrderNo())).count();
            record.setItemTotal(itemTotal);
        }

        return pageResult;

    }

    /**
     * 搜索条件
     *
     * @param dto:
     * @return DevelopSampleSettleSearchDto
     * @author ChenWenLong
     * @date 2023/8/2 17:23
     */
    public DevelopSampleSettleSearchDto getSearchWhere(DevelopSampleSettleSearchDto dto) {
        if (CollectionUtils.isEmpty(dto.getDevelopSampleSettleOrderNoList())) {
            dto.setDevelopSampleSettleOrderNoList(new ArrayList<>());
        }
        if (CollectionUtils.isEmpty(dto.getSkuList())) {
            dto.setSkuList(new ArrayList<>());
        }
        if (StringUtils.isNotBlank(dto.getDevelopChildOrderNo())) {
            List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByLikeDevelopChildOrderNo(dto.getDevelopChildOrderNo());
            if (CollectionUtils.isEmpty(developSampleSettleItemPoList)) {
                return null;
            }
            this.mergeDevelopSampleSettleItemNo(developSampleSettleItemPoList, dto);
        }

        if (StringUtils.isNotBlank(dto.getDevelopSampleOrderNo())) {
            List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByLikeDevelopSampleOrderNo(dto.getDevelopSampleOrderNo());
            if (CollectionUtils.isEmpty(developSampleSettleItemPoList)) {
                return null;
            }
            this.mergeDevelopSampleSettleItemNo(developSampleSettleItemPoList, dto);
        }

        //产品名称
        if (StringUtils.isNotBlank(dto.getSkuEncode())) {
            List<String> skuListByEncode = plmRemoteService.getSkuStrListBySkuEncode(List.of(dto.getSkuEncode()));
            if (CollectionUtils.isEmpty(skuListByEncode)) {
                return null;
            }
            if (CollectionUtils.isEmpty(dto.getSkuList())) {
                dto.setSkuList(skuListByEncode);
            } else {
                dto.getSkuList().retainAll(skuListByEncode);
            }
        }

        //sku批量和上面产品名称关联
        if (CollectionUtils.isNotEmpty(dto.getSkuList())) {
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListBySku(dto.getSkuList());
            if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
                return null;
            }
            List<String> itemNoList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                    .collect(Collectors.toList());
            List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByDevelopSampleOrderNoList(itemNoList);
            if (CollectionUtils.isEmpty(developSampleSettleItemPoList)) {
                return null;
            }
            this.mergeDevelopSampleSettleItemNo(developSampleSettleItemPoList, dto);
        }

        //sku
        if (StringUtils.isNotBlank(dto.getSku())) {
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByLikeSku(dto.getSku());
            if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
                return null;
            }
            List<String> itemNoList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                    .collect(Collectors.toList());
            List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByDevelopSampleOrderNoList(itemNoList);
            if (CollectionUtils.isEmpty(developSampleSettleItemPoList)) {
                return null;
            }
            this.mergeDevelopSampleSettleItemNo(developSampleSettleItemPoList, dto);
        }

        //批次码
        if (StringUtils.isNotBlank(dto.getSkuBatchCode())) {
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByLikeSkuBatchCode(dto.getSkuBatchCode());
            if (CollectionUtils.isEmpty(developSampleOrderPoList)) {
                return null;
            }
            List<String> itemNoList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getDevelopSampleOrderNo)
                    .collect(Collectors.toList());
            List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByDevelopSampleOrderNoList(itemNoList);
            if (CollectionUtils.isEmpty(developSampleSettleItemPoList)) {
                return null;
            }
            this.mergeDevelopSampleSettleItemNo(developSampleSettleItemPoList, dto);
        }

        return dto;
    }


    /**
     * 处理详情样品单号条件
     *
     * @param list:
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/4 13:57
     */
    public void mergeDevelopSampleSettleItemNo(List<DevelopSampleSettleItemPo> list, DevelopSampleSettleSearchDto dto) {
        List<String> developSampleSettleNoList = list.stream()
                .map(DevelopSampleSettleItemPo::getDevelopSampleSettleOrderNo)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dto.getDevelopSampleSettleOrderNoList())) {
            dto.getDevelopSampleSettleOrderNoList().addAll(developSampleSettleNoList);
        } else {
            dto.getDevelopSampleSettleOrderNoList().retainAll(developSampleSettleNoList);
        }

    }

    /**
     * 详情
     *
     * @param dto:
     * @return DevelopSampleSettleOrderDetailVo
     * @author ChenWenLong
     * @date 2023/8/4 14:48
     */
    public DevelopSampleSettleOrderDetailVo detail(DevelopSampleSettleOrderDetailDto dto) {
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByDevelopSampleSettleOrderNo(dto.getDevelopSampleSettleOrderNo());
        if (developSampleSettleOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        DevelopSampleSettleOrderDetailVo detailVo = DevelopSampleSettleOrderConverter.INSTANCE.convert(developSampleSettleOrderPo);
        SupplierPo supplier = supplierBaseService.getSupplierByCode(developSampleSettleOrderPo.getSupplierCode());
        if (supplier != null) {
            detailVo.setSupplierGrade(supplier.getSupplierGrade());
        }

        //获取支付列表
        List<DevelopSampleSettlePayPo> developSampleSettlePayPoList = developSampleSettlePayDao.getListByDevelopSampleSettleOrderNo(dto.getDevelopSampleSettleOrderNo());
        List<DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderPayVo> payVoList = DevelopSampleSettlePayConverter.INSTANCE.convert(developSampleSettlePayPoList);
        List<Long> payIdList = payVoList.stream().map(DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderPayVo::getDevelopSampleSettlePayId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(payIdList)) {
            Map<Long, List<String>> scmImageList = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SAMPLE_SETTLE_PAY, payIdList);
            List<DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderPayVo> payVos = payVoList.stream().peek(
                    item -> {
                        //获取凭证
                        if (scmImageList.containsKey(item.getDevelopSampleSettlePayId())) {
                            item.setFileCodeList(scmImageList.get(item.getDevelopSampleSettlePayId()));
                        }
                    }
            ).collect(Collectors.toList());
            detailVo.setDevelopSampleSettleOrderPayVoList(payVos);
        }

        BigDecimal paidPrice = developSampleSettlePayPoList.stream().map(DevelopSampleSettlePayPo::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        detailVo.setPaidPrice(paidPrice);
        detailVo.setWaitPayPrice(developSampleSettleOrderPo.getPayPrice().subtract(paidPrice));

        //获取详情列表
        List<DevelopSampleSettleItemPo> developSampleSettleItemPoList = developSampleSettleItemDao.getListByDevelopSampleSettleOrderNo(dto.getDevelopSampleSettleOrderNo());
        List<DevelopSampleSettleOrderDetailVo.DevelopSampleSettleOrderItemVo> developSampleSettleOrderItemVoList = DevelopSampleSettleItemConverter.INSTANCE.convert(developSampleSettleItemPoList);

        detailVo.setDevelopSampleSettleOrderItemVoList(developSampleSettleOrderItemVoList);

        return detailVo;
    }

    /**
     * 审核
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/4 16:29
     */
    @Transactional(rollbackFor = Exception.class)
    public void examine(DevelopSampleSettleOrderExamineDto dto) {
        DevelopSampleSettleOrderPo developSampleSettleOrderPo = developSampleSettleOrderDao.getByIdVersion(dto.getDevelopSampleSettleOrderId(), dto.getVersion());
        if (developSampleSettleOrderPo == null) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        String user = GlobalContext.getUserKey();
        String username = GlobalContext.getUsername();
        LocalDateTime nowTime = LocalDateTime.now();

        DevelopSampleSettleStatus developSampleSettleStatus = developSampleSettleOrderPo.getDevelopSampleSettleStatus();
        switch (dto.getDevelopSampleSettleExamine()) {
            case ALREADY_CONFIRM:
                DevelopSampleSettleStatus toSettle = developSampleSettleStatus.toSettle();
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(toSettle);
                developSampleSettleOrderPo.setConfirmUser(user);
                developSampleSettleOrderPo.setConfirmUsername(username);
                developSampleSettleOrderPo.setConfirmTime(nowTime);
                break;
            case SETTLE_AGREE:
                DevelopSampleSettleStatus examine = developSampleSettleStatus.toExamine();
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(examine);
                developSampleSettleOrderPo.setExamineUser(user);
                developSampleSettleOrderPo.setSettleRefuseRemarks(null);
                developSampleSettleOrderPo.setExamineUsername(username);
                developSampleSettleOrderPo.setExamineTime(nowTime);
                break;
            case SETTLE_REFUSE:
                DevelopSampleSettleStatus notExamine = developSampleSettleStatus.toNotExamine();
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(notExamine);
                if (com.alibaba.cloud.commons.lang.StringUtils.isBlank(dto.getSettleRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}备注", DevelopSampleSettleExamine.SETTLE_REFUSE.getRemark());
                }
                developSampleSettleOrderPo.setSettleRefuseRemarks(dto.getSettleRefuseRemarks());
                developSampleSettleOrderPo.setExamineUser(user);
                developSampleSettleOrderPo.setExamineUsername(username);
                developSampleSettleOrderPo.setExamineTime(nowTime);
                break;
            case EXAMINE_AGREE:
                DevelopSampleSettleStatus audited = developSampleSettleStatus.toAudited();
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(audited);
                developSampleSettleOrderPo.setSettleUser(user);
                developSampleSettleOrderPo.setExamineRefuseRemarks(null);
                developSampleSettleOrderPo.setSettleUsername(username);
                developSampleSettleOrderPo.setSettleTime(nowTime);
                break;
            case EXAMINE_REFUSE:
                DevelopSampleSettleStatus notAudited = developSampleSettleStatus.toNotAudited();
                developSampleSettleOrderPo.setDevelopSampleSettleStatus(notAudited);
                if (StringUtils.isBlank(dto.getExamineRefuseRemarks())) {
                    throw new ParamIllegalException("填写{}备注", DevelopSampleSettleExamine.EXAMINE_REFUSE.getRemark());
                }
                developSampleSettleOrderPo.setExamineRefuseRemarks(dto.getExamineRefuseRemarks());
                developSampleSettleOrderPo.setSettleUser(user);
                developSampleSettleOrderPo.setSettleUsername(username);
                developSampleSettleOrderPo.setSettleTime(nowTime);
                break;
            default:
                throw new ParamIllegalException("请求类型错误！");
        }
        developSampleSettleOrderDao.updateByIdVersion(developSampleSettleOrderPo);
        this.createStatusChangeLog(developSampleSettleOrderPo, dto.getDevelopSampleSettleExamine());
    }

    /**
     * 样品结算状态日志
     *
     * @param developSampleSettleOrderPo:
     * @param developSampleSettleExamine:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/4 16:49
     */
    public void createStatusChangeLog(DevelopSampleSettleOrderPo developSampleSettleOrderPo, DevelopSampleSettleExamine developSampleSettleExamine) {
        BizLogCreateMqDto bizLogCreateMqDto = new BizLogCreateMqDto();
        bizLogCreateMqDto.setBizLogCode(idGenerateService.getSnowflakeCode(LogBizModule.SAMPLE_SETTLE_STATUS.name()));
        bizLogCreateMqDto.setBizSystemCode(ScmConstant.SYSTEM_CODE);
        bizLogCreateMqDto.setLogVersion(ScmConstant.SAMPLE_SETTLE_ORDER_LOG_VERSION);
        bizLogCreateMqDto.setBizModule(LogBizModule.SAMPLE_SETTLE_STATUS.name());
        bizLogCreateMqDto.setBizCode(developSampleSettleOrderPo.getDevelopSampleSettleOrderNo());
        bizLogCreateMqDto.setOperateTime(DateUtil.current());

        String userKey = com.alibaba.cloud.commons.lang.StringUtils.isNotBlank(GlobalContext.getUserKey()) ? GlobalContext.getUserKey() : ScmConstant.SYSTEM_USER;
        String username = com.alibaba.cloud.commons.lang.StringUtils.isNotBlank(GlobalContext.getUsername()) ? GlobalContext.getUsername() : ScmConstant.MQ_DEFAULT_USER;
        bizLogCreateMqDto.setOperateUser(userKey);
        bizLogCreateMqDto.setOperateUsername(username);

        ArrayList<LogVersionBo> logVersionBos = new ArrayList<>();

        //记录备注信息
        if (DevelopSampleSettleExamine.SETTLE_REFUSE.equals(developSampleSettleExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(DevelopSampleSettleExamine.SETTLE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(developSampleSettleOrderPo.getSettleRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }
        if (DevelopSampleSettleExamine.EXAMINE_REFUSE.equals(developSampleSettleExamine)) {
            LogVersionBo logVersionBo = new LogVersionBo();
            logVersionBo.setKey(PurchaseSettleExamine.EXAMINE_REFUSE.getName());
            logVersionBo.setValueType(LogVersionValueType.STRING);
            logVersionBo.setValue(developSampleSettleOrderPo.getExamineRefuseRemarks());
            logVersionBos.add(logVersionBo);
        }

        bizLogCreateMqDto.setContent(developSampleSettleOrderPo.getDevelopSampleSettleStatus().getName());
        bizLogCreateMqDto.setDetail(logVersionBos);

        consistencySendMqService.execSendMq(LogVersionHandler.class, bizLogCreateMqDto);
    }


    /**
     * 统计样品结算单
     *
     * @author ChenWenLong
     * @date 2022/11/21 15:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void countDevelopSampleSettleOrder(String supplierCode, String settleTime) {
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
        List<DevelopSampleSettleOrderPo> developSampleSettleOrderPoList = developSampleSettleOrderDao.getListBySupplierCodeAndMonth(supplierCode, month);
        if (CollectionUtil.isNotEmpty(developSampleSettleOrderPoList)) {
            throw new BizException("样品结算单统计{}月份错误，数据库已经存在", month);
        }


        log.info("统计样品结算的月初时间：{}", warehousingTimeStart);
        log.info("统计样品结算的月末时间：{}", warehousingTimeEnd);

        //查询样品单数据
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByStatusList(List.of(DevelopSampleStatus.ON_SHELVES),
                supplierCodeList,
                warehousingTimeStart,
                warehousingTimeEnd,
                List.of(DevelopSampleMethod.SEAL_SAMPLE, DevelopSampleMethod.SALE),
                List.of(ScmConstant.PROCESS_SUPPLIER_CODE)
        );

        //获取结算价格
        Map<String, BigDecimal> developSampleOrderPriceMap = new HashMap<>();
        Map<String, BigDecimal> developPricingOrderMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(developSampleOrderPoList)) {
            List<String> developSampleOrderNoList = developSampleOrderPoList.stream().map(DevelopSampleOrderPo::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
            developPricingOrderMap = developPricingOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList)
                    .stream()
                    .collect(Collectors.toMap(DevelopPricingOrderInfoPo::getDevelopSampleOrderNo, DevelopPricingOrderInfoPo::getSamplePrice));

            List<String> developChildOrderNoList = developSampleOrderPoList.stream()
                    .map(DevelopSampleOrderPo::getDevelopChildOrderNo)
                    .distinct().collect(Collectors.toList());
            developSampleOrderPriceMap = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList)
                    .stream()
                    .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, DevelopChildOrderPo::getSamplePrice));
        }
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoMap = developSampleOrderPoList.stream()
                .collect(Collectors.groupingBy(DevelopSampleOrderPo::getSupplierCode));

        //要插入的数据结算单
        List<DevelopSampleSettleOrderPo> developSampleSettleOrderPos = new ArrayList<>();

        //要插入的数据结算单详情
        List<DevelopSampleSettleItemPo> developSampleSettleItemPos = new ArrayList<>();


        //创建
        final Map<String, BigDecimal> finalDevelopSampleOrderPriceMap = developSampleOrderPriceMap;
        final Map<String, BigDecimal> finalDevelopPricingOrderMap = developPricingOrderMap;
        developSampleOrderPoMap.forEach((String key, List<DevelopSampleOrderPo> itemPos) -> {
            //生成结算编号
            String developSampleSettleOrderNo = idGenerateService.getConfuseCode(ScmConstant.SAMPLE_SETTLE_ORDER_CODE, TimeType.CN_DAY, ConfuseLength.L_4);

            String supplierName = itemPos.get(0).getSupplierName();

            // 统计结算金额
            BigDecimal totalPrice = BigDecimal.ZERO;

            //创建详情
            for (DevelopSampleOrderPo itemPo : itemPos) {
                BigDecimal settlePrice = itemPo.getSkuBatchSamplePrice();

                DevelopSampleSettleItemPo developSampleSettleItemPo = new DevelopSampleSettleItemPo();
                developSampleSettleItemPo.setDevelopSampleSettleOrderNo(developSampleSettleOrderNo);
                developSampleSettleItemPo.setDevelopSampleOrderNo(itemPo.getDevelopSampleOrderNo());
                developSampleSettleItemPo.setBusinessNo(itemPo.getReceiptOrderNo());
                developSampleSettleItemPo.setDevelopChildOrderNo(itemPo.getDevelopChildOrderNo());
                developSampleSettleItemPo.setSettleTime(itemPo.getShelvesTime());
                developSampleSettleItemPo.setDevelopSampleMethod(itemPo.getDevelopSampleMethod());
                developSampleSettleItemPo.setSamplePrice(settlePrice);

                developSampleSettleItemPos.add(developSampleSettleItemPo);
                totalPrice = totalPrice.add(settlePrice);
            }

            //增加结算单
            DevelopSampleSettleOrderPo developSampleSettleOrderPo = new DevelopSampleSettleOrderPo();
            developSampleSettleOrderPo.setDevelopSampleSettleOrderNo(developSampleSettleOrderNo);
            developSampleSettleOrderPo.setDevelopSampleSettleStatus(DevelopSampleSettleStatus.WAIT_CONFIRM);
            developSampleSettleOrderPo.setMonth(month);
            developSampleSettleOrderPo.setSupplierCode(key);
            developSampleSettleOrderPo.setSupplierName(supplierName);
            developSampleSettleOrderPo.setTotalPrice(totalPrice);
            developSampleSettleOrderPo.setPayPrice(totalPrice);
            developSampleSettleOrderPos.add(developSampleSettleOrderPo);

        });
        //执行数据插入动作
        this.developSampleSettleOrderInsertBatch(developSampleSettleOrderPos, developSampleSettleItemPos);

    }

    /**
     * 统计执行数据插入动作
     *
     * @param developSampleSettleOrderPos:
     * @param developSampleSettleItemPos:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/5 14:56
     */
    public void developSampleSettleOrderInsertBatch(List<DevelopSampleSettleOrderPo> developSampleSettleOrderPos,
                                                    List<DevelopSampleSettleItemPo> developSampleSettleItemPos) {

        developSampleSettleOrderDao.insertBatch(developSampleSettleOrderPos);
        developSampleSettleItemDao.insertBatch(developSampleSettleItemPos);

        //创建日志记录
        Map<String, DevelopSampleSettleOrderPo> mapList = developSampleSettleOrderDao.getMapByNoList(developSampleSettleOrderPos.stream()
                .map(DevelopSampleSettleOrderPo::getDevelopSampleSettleOrderNo)
                .collect(Collectors.toList()));

        mapList.forEach((String key, DevelopSampleSettleOrderPo val) -> this.createStatusChangeLog(val, null));
    }
}
