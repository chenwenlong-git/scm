package com.hete.supply.scm.server.scm.develop.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.plm.api.developorder.entity.dto.PlmDevelopOrderSearchDto;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopChildOrderVo;
import com.hete.supply.plm.api.developorder.entity.vo.PlmDevelopOrderDetailVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmGoodsSkuVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.scm.api.scm.entity.dto.*;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.adjust.entity.bo.GoodsPriceAddBo;
import com.hete.supply.scm.server.scm.adjust.service.base.GoodsPriceBaseService;
import com.hete.supply.scm.server.scm.develop.converter.DevelopChildConverter;
import com.hete.supply.scm.server.scm.develop.converter.DevelopPamphletOrderRawConverter;
import com.hete.supply.scm.server.scm.develop.converter.DevelopPricingOrderConverter;
import com.hete.supply.scm.server.scm.develop.converter.DevelopSampleOrderConverter;
import com.hete.supply.scm.server.scm.develop.dao.*;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevChildPurchaseBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopChildBatchCodeCostPriceBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopOrderPriceCreateBo;
import com.hete.supply.scm.server.scm.develop.entity.bo.DevelopReviewAndSampleBo;
import com.hete.supply.scm.server.scm.develop.entity.dto.*;
import com.hete.supply.scm.server.scm.develop.entity.po.*;
import com.hete.supply.scm.server.scm.develop.entity.vo.*;
import com.hete.supply.scm.server.scm.develop.enums.DevelopOrderPriceType;
import com.hete.supply.scm.server.scm.develop.enums.DevelopReviewRelated;
import com.hete.supply.scm.server.scm.develop.handler.DevelopCancelResultHandler;
import com.hete.supply.scm.server.scm.develop.handler.DevelopCompleteInfoHandler;
import com.hete.supply.scm.server.scm.develop.handler.DevelopCompleteNoticeHandler;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopChildBaseService;
import com.hete.supply.scm.server.scm.develop.service.base.DevelopSampleOrderBaseService;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataSkuSealImageBo;
import com.hete.supply.scm.server.scm.entity.bo.ProduceDataUpdatePurchasePriceBo;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.process.entity.dto.ProcessOrderCancelEventDto;
import com.hete.supply.scm.server.scm.process.entity.dto.UpdateBatchCodePriceDto;
import com.hete.supply.scm.server.scm.process.handler.WmsProcessCancelHandler;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderDao;
import com.hete.supply.scm.server.scm.purchase.dao.PurchaseChildOrderItemDao;
import com.hete.supply.scm.server.scm.purchase.entity.po.PurchaseChildOrderPo;
import com.hete.supply.scm.server.scm.purchase.service.ref.PurchaseRefService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ProduceDataBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.service.base.WmsMqBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.develop.converter.SupplierDevelopConverter;
import com.hete.supply.scm.server.supplier.enums.FileOperateBizType;
import com.hete.supply.scm.server.supplier.handler.ScmExportHandler;
import com.hete.supply.sfds.api.entity.dto.FileOperateMessageDto;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreate4defectNoListDto;
import com.hete.supply.wms.api.basic.entity.vo.SkuVo;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.JacksonUtil;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/8/8 16:54
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DevelopChildBizService {
    private final DevelopChildOrderDao developChildOrderDao;
    private final DevelopChildOrderChangeDao developChildOrderChangeDao;
    private final DevelopChildOrderAttrDao developChildOrderAttrDao;
    private final DevelopReviewOrderDao developReviewOrderDao;
    private final DevelopChildBaseService developChildBaseService;
    private final PlmRemoteService plmRemoteService;
    private final DevelopChildOrderRawDao developChildOrderRawDao;
    private final IdGenerateService idGenerateService;
    private final DevelopPamphletOrderDao developPamphletOrderDao;
    private final DevelopPamphletOrderRawDao developPamphletOrderRawDao;
    private final DevelopPricingOrderDao developPricingOrderDao;
    private final ScmImageBaseService scmImageBaseService;
    private final DevelopSampleOrderDao developSampleOrderDao;
    private final ConsistencySendMqService consistencySendMqService;
    private final WmsRemoteService wmsRemoteService;
    private final ProduceDataBaseService produceDataBaseService;
    private final DevelopReviewSampleOrderDao developReviewSampleOrderDao;
    private final DevelopReviewSampleOrderInfoDao developReviewSampleOrderInfoDao;
    private final PurchaseRefService purchaseRefService;
    private final DevelopReviewOrderUnusualDao developReviewOrderUnusualDao;
    private final SupplierDao supplierDao;
    private final LogBaseService logBaseService;
    private final PurchaseChildOrderDao purchaseChildOrderDao;
    private final PurchaseChildOrderItemDao purchaseChildOrderItemDao;
    private final SupplierBaseService supplierBaseService;
    private final WmsMqBaseService wmsMqBaseService;
    private final DevelopSampleOrderBaseService developSampleOrderBaseService;
    private final DevelopChildFeiShuService developChildFeiShuService;
    private final DevelopOrderPriceDao developOrderPriceDao;
    private final GoodsPriceBaseService goodsPriceBaseService;


    public CommonPageResult.PageInfo<DevelopChildSearchVo> searchDevelopChild(DevelopChildSearchDto dto) {
        return developChildBaseService.searchDevelopChild(dto);
    }

    public DevelopChildDetailVo developChildDetail(DevelopChildNoDto dto) {
        return developChildBaseService.developChildDetail(dto);
    }

    /**
     * 编辑
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/17 13:45
     */
    @Transactional(rollbackFor = Exception.class)
    public void editDevelopChild(DevelopChildEditDto dto) {
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getByIdVersion(dto.getDevelopChildOrderId(), dto.getVersion());
        if (null == developChildOrderPo) {
            throw new ParamIllegalException("开发单数据已被修改或删除，请刷新页面后重试！");
        }

        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByIdVersion(dto.getDevelopPamphletMsgVo().getDevelopPamphletOrderId(), dto.getDevelopPamphletMsgVo().getVersion());
        if (null == developPamphletOrderPo) {
            throw new ParamIllegalException("版单数据已被修改或删除，请刷新页面后重试！");
        }

        developChildOrderPo.setSkuEncode(dto.getDevelopChildBaseMsgVo().getSkuEncode());
        developChildOrderPo.setSamplePrice(dto.getDevelopChildBaseMsgVo().getSamplePrice());
        // 保存渠道大货价格
        developChildBaseService.developOrderPriceSave(List.of(dto.getDevelopChildBaseMsgVo().getDevelopOrderPrice()),
                developChildOrderPo.getDevelopChildOrderNo(),
                DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE);

        //无需打样供应商报价维护
        if (!developChildOrderPo.getIsSample().isBooleanVal() && CollectionUtils.isNotEmpty(dto.getDevelopSampleOrderDetailList())) {
            developPamphletOrderPo.setSamplePrice(dto.getDevelopSampleOrderDetailList().get(0).getSupplierSamplePrice());
        }
        developPamphletOrderPo.setExpectedOnShelvesDate(dto.getDevelopPamphletMsgVo().getExpectedOnShelvesDate());
        final List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = DevelopChildConverter.convertDevelopAttrDtoToPo(developChildOrderPo, dto.getDevelopChildBaseMsgVo().getDevelopChildOrderAttrList());

        List<DevelopChildOrderAttrPo> developChildOrderAttrPos = developChildOrderAttrDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        if (CollectionUtils.isNotEmpty(developChildOrderAttrPos)) {
            developChildOrderAttrDao.deleteByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        }
        if (CollectionUtils.isNotEmpty(developChildOrderAttrPoList)) {
            developChildOrderAttrDao.insertBatch(developChildOrderAttrPoList);
        }
        developChildOrderDao.updateByIdVersion(developChildOrderPo);
        developPamphletOrderDao.updateByIdVersion(developPamphletOrderPo);

        //更新关联样品单信息
        if (CollectionUtils.isNotEmpty(dto.getDevelopSampleOrderDetailList())) {
            developChildBaseService.updateDevelopChildSample(dto.getDevelopSampleOrderDetailList(), developChildOrderPo, developPamphletOrderPo);
        }

    }

    public CommonPageResult.PageInfo<DevelopReviewSearchVo> searchDevelopReview(DevelopReviewSearchDto dto) {
        if (StringUtils.isNotBlank(dto.getDevelopSampleOrderNo())) {
            final DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderDao.getOneBySampleOrderNo(dto.getDevelopSampleOrderNo());
            if (null == developReviewSampleOrderPo) {
                return new CommonPageResult.PageInfo<>();
            }
            dto.setDevelopReviewOrderNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());
        }

        final CommonPageResult.PageInfo<DevelopReviewSearchVo> pageInfo = developReviewOrderDao.searchDevelopReview(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);
        final List<DevelopReviewSearchVo> records = pageInfo.getRecords();
        final List<String> developReviewOrderNoList = records.stream().map(DevelopReviewSearchVo::getDevelopReviewOrderNo).collect(Collectors.toList());

        List<String> developChildOrderNoList = records.stream().map(DevelopReviewSearchVo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        // 获取图片
        Map<String, List<String>> developChildOrderStyleImg = developChildBaseService.getDevelopChildOrderStyleImg(developChildOrderPoList);

        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByReviewNoList(developReviewOrderNoList);

        final Map<String, List<DevelopReviewSampleOrderPo>> reviewNoSamplePoListMap = developReviewSampleOrderPoList.stream().collect(Collectors.groupingBy(DevelopReviewSampleOrderPo::getDevelopReviewOrderNo));

        records.forEach(record -> {
            final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList1 = reviewNoSamplePoListMap.get(record.getDevelopReviewOrderNo());
            final List<DevelopSampleReviewVo> developSampleReviewVoList = Optional.ofNullable(developReviewSampleOrderPoList1).orElse(Collections.emptyList()).stream().map(samplePo -> {
                final DevelopSampleReviewVo developSampleReviewVo = new DevelopSampleReviewVo();
                developSampleReviewVo.setDevelopSampleOrderNo(samplePo.getDevelopSampleOrderNo());
                developSampleReviewVo.setDevelopSampleMethod(samplePo.getDevelopSampleMethod());

                return developSampleReviewVo;
            }).collect(Collectors.toList());
            record.setDevelopSampleReviewList(developSampleReviewVoList);
            record.setFileCodeList(developChildOrderStyleImg.get(record.getDevelopChildOrderNo()));
        });

        return pageInfo;
    }

    /**
     * plm推送MQ创建开发需求子单
     *
     * @param developParentOrderMqDto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/12 10:12
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_DEVELOP_CREATE, key = "#developParentOrderMqDto.developParentOrderNo", waitTime = 1, leaseTime = -1)
    public void createDevelopChildOrder(DevelopParentOrderMqDto developParentOrderMqDto) {
        //检验是否已经生成
        String developParentOrderNo = developParentOrderMqDto.getDevelopParentOrderNo();
        List<String> developChildOrderNoList = developParentOrderMqDto.getDevelopChildOrderNoList();
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        if (CollectionUtils.isNotEmpty(developChildOrderPoList)) {
            log.error("开发需求子单:{}已经生成信息,plm推送mq内容重复!", JacksonUtil.parse2Str(developChildOrderPoList));
            return;
        }

        PlmDevelopOrderDetailVo developOrderDetail = plmRemoteService.getDevelopOrderDetail(new PlmDevelopOrderSearchDto(developParentOrderNo, developChildOrderNoList));
        List<PlmDevelopChildOrderVo> developChildOrderVoList = developOrderDetail.getPlmDevelopChildOrderVoList();

        if (CollectionUtils.isEmpty(developChildOrderVoList) || developChildOrderVoList.size() != developChildOrderNoList.size()) {
            log.error("mq推送开发需求子单数量和请求Dubbo接口:{}返回数量不一致!", JacksonUtil.parse2Str(developChildOrderVoList));
            return;
        }

        //获取供应商信息
        List<String> supplierCodeList = developChildOrderVoList.stream().map(PlmDevelopChildOrderVo::getSupplierCode).collect(Collectors.toList());
        Map<String, String> supplierCodeMap = supplierDao.getSupplierNameBySupplierCodeList(supplierCodeList);

        //创建子单数据
        List<DevelopChildOrderPo> insertDevelopChildPoList = DevelopChildConverter.convertPlmDevelopChildVoToPo(developOrderDetail, supplierCodeMap);
        List<DevelopChildOrderChangePo> insertChangePoList = DevelopChildConverter.convertPoToChangePo(insertDevelopChildPoList);
        List<DevelopChildOrderAttrPo> insertAttrPoList = DevelopChildConverter.convertPoToAttrPo(developOrderDetail);
        List<DevelopChildOrderRawPo> insertRawPoList = DevelopChildConverter.convertPoToRawPo(developOrderDetail);

        //生成版单/核价单
        List<DevelopPamphletOrderPo> developPamphletOrderPoList = new ArrayList<>();
        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = new ArrayList<>();
        List<DevelopPricingOrderPo> developPricingOrderPoList = new ArrayList<>();

        for (DevelopChildOrderPo developChildOrderPo : insertDevelopChildPoList) {
            //版单
            DevelopPamphletOrderPo pamphletPo = new DevelopPamphletOrderPo();
            String developPamphletOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_PAMPHLET_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
            pamphletPo.setDevelopPamphletOrderNo(developPamphletOrderNo);
            pamphletPo.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            pamphletPo.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            if (BooleanType.TRUE.equals(developChildOrderPo.getIsSample())) {
                pamphletPo.setDevelopPamphletOrderStatus(DevelopPamphletOrderStatus.TO_BE_CONFIRMED_PAMPHLET);
            } else {
                pamphletPo.setDevelopPamphletOrderStatus(DevelopPamphletOrderStatus.NOT_SAMPLE_PAMPHLET);
            }
            pamphletPo.setSupplierCode(developChildOrderPo.getSupplierCode());
            pamphletPo.setSupplierName(developChildOrderPo.getSupplierName());
            pamphletPo.setDevelopSampleNum(developChildOrderPo.getProofCnt());
            pamphletPo.setProposedPrice(developChildOrderPo.getExpectedPurchaseCost());
            pamphletPo.setExpectedOnShelvesDate(developChildOrderPo.getExpectedArrivalDate());
            pamphletPo.setDemandDesc(developChildOrderPo.getDemandDesc());
            pamphletPo.setParentCreateTime(developChildOrderPo.getParentCreateTime());
            pamphletPo.setParentCreateUser(developChildOrderPo.getParentCreateUser());
            pamphletPo.setParentCreateUsername(developChildOrderPo.getParentCreateUsername());
            developPamphletOrderPoList.add(pamphletPo);

            //版单原料
            List<DevelopChildOrderRawPo> developChildOrderRawPoList = insertRawPoList.stream().filter(w -> w.getDevelopChildOrderNo().equals(developChildOrderPo.getDevelopChildOrderNo())).collect(Collectors.toList());
            for (DevelopChildOrderRawPo developChildOrderRawPo : developChildOrderRawPoList) {
                DevelopPamphletOrderRawPo pamphletRawPo = new DevelopPamphletOrderRawPo();
                pamphletRawPo.setDevelopPamphletOrderNo(developPamphletOrderNo);
                pamphletRawPo.setDevelopChildOrderNo(developChildOrderRawPo.getDevelopChildOrderNo());
                pamphletRawPo.setDevelopParentOrderNo(developChildOrderRawPo.getDevelopParentOrderNo());
                pamphletRawPo.setMaterialType(developChildOrderRawPo.getMaterialType());
                pamphletRawPo.setSku(developChildOrderRawPo.getSku());
                pamphletRawPo.setSkuCnt(developChildOrderRawPo.getSkuCnt());
                pamphletRawPo.setSampleRawBizType(SampleRawBizType.FORMULA);
                developPamphletOrderRawPoList.add(pamphletRawPo);
            }

            if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
                //核价单
                DevelopPricingOrderPo pricingPo = new DevelopPricingOrderPo();
                String developPricingOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_PRICING_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
                pricingPo.setDevelopPricingOrderNo(developPricingOrderNo);
                pricingPo.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
                pricingPo.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
                pricingPo.setDevelopPamphletOrderNo(developPamphletOrderNo);
                pricingPo.setDevelopPricingOrderStatus(DevelopPricingOrderStatus.WAIT_SUBMIT_PRICE);
                pricingPo.setPlatform(developChildOrderPo.getPlatform());
                pricingPo.setSupplierCode(developChildOrderPo.getSupplierCode());
                pricingPo.setSupplierName(developChildOrderPo.getSupplierName());
                pricingPo.setParentCreateTime(developChildOrderPo.getParentCreateTime());
                pricingPo.setParentCreateUser(developChildOrderPo.getParentCreateUser());
                pricingPo.setParentCreateUsername(developChildOrderPo.getParentCreateUsername());
                developPricingOrderPoList.add(pricingPo);
            }
        }

        developChildOrderDao.insertBatch(insertDevelopChildPoList);
        developChildOrderChangeDao.insertBatch(insertChangePoList);
        developChildOrderRawDao.insertBatch(insertRawPoList);
        developChildOrderAttrDao.insertBatch(insertAttrPoList);
        developPamphletOrderDao.insertBatch(developPamphletOrderPoList);
        developPamphletOrderRawDao.insertBatch(developPamphletOrderRawPoList);
        developPricingOrderDao.insertBatch(developPricingOrderPoList);

        //日志
        for (DevelopChildOrderPo developChildOrderPo : insertDevelopChildPoList) {

            logBaseService.simpleLog(LogBizModule.DEVELOP_CHILD_ORDER_STATUS, ScmConstant.DEVELOP_CHILD_LOG_VERSION, developChildOrderPo.getDevelopChildOrderNo(), developChildOrderPo.getDevelopChildOrderStatus().getRemark(), Collections.emptyList());

        }

        //新增图片
        List<String> styleReferenceFileCodeList = developOrderDetail.getStyleReferenceFileCodeList();
        List<String> colorReferenceFileCodeList = developOrderDetail.getColorReferenceFileCodeList();

        //子单
        List<ScmImageBo> scmImageBoStyleList = new ArrayList<>();
        List<ScmImageBo> scmImageBoColorList = new ArrayList<>();
        //版单
        List<ScmImageBo> scmImageBoPamphletStyleList = new ArrayList<>();
        List<ScmImageBo> scmImageBoPamphletColorList = new ArrayList<>();
        for (DevelopChildOrderPo developChildOrderPo : insertDevelopChildPoList) {
            //增加子单图片凭证
            if (CollectionUtils.isNotEmpty(styleReferenceFileCodeList)) {
                ScmImageBo scmImageStyleBo = new ScmImageBo();
                scmImageStyleBo.setImageBizId(developChildOrderPo.getDevelopChildOrderId());
                scmImageStyleBo.setFileCodeList(styleReferenceFileCodeList);
                scmImageBoStyleList.add(scmImageStyleBo);
            }
            if (CollectionUtils.isNotEmpty(colorReferenceFileCodeList)) {
                ScmImageBo scmImageColorBo = new ScmImageBo();
                scmImageColorBo.setImageBizId(developChildOrderPo.getDevelopChildOrderId());
                scmImageColorBo.setFileCodeList(colorReferenceFileCodeList);
                scmImageBoColorList.add(scmImageColorBo);
            }
        }
        for (DevelopPamphletOrderPo developPamphletOrderPo : developPamphletOrderPoList) {
            //增加版单图片凭证
            if (CollectionUtils.isNotEmpty(styleReferenceFileCodeList)) {
                ScmImageBo scmImageStyleBo = new ScmImageBo();
                scmImageStyleBo.setImageBizId(developPamphletOrderPo.getDevelopPamphletOrderId());
                scmImageStyleBo.setFileCodeList(styleReferenceFileCodeList);
                scmImageBoPamphletStyleList.add(scmImageStyleBo);
            }
            if (CollectionUtils.isNotEmpty(colorReferenceFileCodeList)) {
                ScmImageBo scmImageColorBo = new ScmImageBo();
                scmImageColorBo.setImageBizId(developPamphletOrderPo.getDevelopPamphletOrderId());
                scmImageColorBo.setFileCodeList(colorReferenceFileCodeList);
                scmImageBoPamphletColorList.add(scmImageColorBo);
            }
        }

        scmImageBaseService.insertBatchImageBo(scmImageBoStyleList, ImageBizType.DEVELOP_CHILD_STYLE);
        scmImageBaseService.insertBatchImageBo(scmImageBoColorList, ImageBizType.DEVELOP_CHILD_COLOR);
        scmImageBaseService.insertBatchImageBo(scmImageBoPamphletStyleList, ImageBizType.DEVELOP_PAMPHLET_STYLE);
        scmImageBaseService.insertBatchImageBo(scmImageBoPamphletColorList, ImageBizType.DEVELOP_PAMPHLET_COLOR);

    }

    /**
     * PLM一键取消
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/16 15:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(DevelopCancelDto dto) {
        String developParentOrderNo = dto.getDevelopParentOrderNo();
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByParentOrderNoAndNotStatus(developParentOrderNo, List.of(DevelopChildOrderStatus.CANCEL));
        if (CollectionUtils.isEmpty(developChildOrderPoList)) {
            throw new ParamIllegalException("子单数据为空，请刷新页面后重试！");
        }

        // 验证子单状态
        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            developChildOrderPo.getDevelopChildOrderStatus().cancel(developChildOrderPo.getIsSample());
        }

        // 取消子单相关单据状态
        developChildBaseService.cancelChild(developChildOrderPoList);

        // 更新子单信息
        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            developChildOrderPo.setCancelReason(ScmConstant.DEVELOP_PARENT_ORDER_CANCEL);
            developChildOrderPo.setHasException(BooleanType.FALSE);
            developChildOrderPo.setDevelopChildOrderStatus(DevelopChildOrderStatus.CANCEL);
            //日志
            logBaseService.simpleLog(LogBizModule.DEVELOP_CHILD_ORDER_STATUS, ScmConstant.DEVELOP_CHILD_LOG_VERSION,
                    developChildOrderPo.getDevelopChildOrderNo(), DevelopChildOrderStatus.CANCEL.getRemark(), Collections.emptyList());

            // 取消对应的出库单
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
        developChildOrderDao.updateBatchById(developChildOrderPoList);

        // 一键取消MQ推送PLM
        final DevelopCancelResultMqDto developCancelResultMqDto = new DevelopCancelResultMqDto();
        developCancelResultMqDto.setKey(developParentOrderNo);
        developCancelResultMqDto.setDevelopParentOrderNo(developParentOrderNo);
        developCancelResultMqDto.setUserKey(GlobalContext.getUserKey());
        developCancelResultMqDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(DevelopCancelResultHandler.class, developCancelResultMqDto);

    }


    /**
     * 查看版单原料
     *
     * @param dto:
     * @return List<DevelopPamphletRawDetailVo>
     * @author ChenWenLong
     * @date 2023/8/17 14:58
     */
    public List<DevelopPamphletRawDetailVo> getDevelopPamphletOrderRaw(DevelopPamphletOrderRawDto dto) {
        return DevelopPamphletOrderRawConverter.INSTANCE.convert(developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(dto.getDevelopPamphletOrderNo(), SampleRawBizType.FORMULA));
    }

    /**
     * 原料下单提交
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/17 15:01
     */
    public void supplyRaw(DevelopPamphletOrderRawDeliveryDto dto) {
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(dto.getDevelopPamphletOrderNo());
        Assert.notNull(developPamphletOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developPamphletOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderPo, () -> new BizException("查询不到开发子单，数据已被修改或删除，请刷新页面后重试！"));
        Assert.notBlank(developChildOrderPo.getPlatform(), () -> new BizException("开发子单:{}的平台编码为空，数据异常请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo()));

        //验证库存是否充足
        List<String> skuList = dto.getRawList().stream()
                .map(DevelopPamphletOrderRawListDto::getSku)
                .distinct()
                .collect(Collectors.toList());
        SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
        skuInstockInventoryQueryDto.setWarehouseCode(dto.getRawWarehouseCode());
        skuInstockInventoryQueryDto.setSkuCodes(skuList);
        skuInstockInventoryQueryDto.setPlatCode(developChildOrderPo.getPlatform());
        skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
        skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
        List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
        if (CollectionUtils.isEmpty(skuInventoryList)) {
            throw new ParamIllegalException("查询不到对应库存信息，禁止原料下单!");
        } else {
            // 判断是否有缺货的
            List<String> wmsSkuList = skuInventoryList.stream().map(SkuInventoryVo::getSkuCode).distinct().collect(Collectors.toList());
            dto.getRawList().forEach(item -> {
                if (!wmsSkuList.contains(item.getSku())) {
                    throw new ParamIllegalException("sku{}库存不足，禁止补充原料!", item.getSku());
                }
            });
            skuInventoryList.forEach(item -> {
                Integer inStockAmount = item.getInStockAmount();
                int sum = dto.getRawList().stream()
                        .filter(it -> it.getSku().equals(item.getSkuCode()))
                        .mapToInt(DevelopPamphletOrderRawListDto::getSkuCnt)
                        .sum();
                if (inStockAmount < sum) {
                    throw new ParamIllegalException("sku{}库存不足，禁止补充原料!", item.getSkuCode());
                }
            });
        }
        developChildBaseService.updateSupplyRaw(dto, developPamphletOrderPo, developChildOrderPo);
    }

    /**
     * 异常处理
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/18 11:30
     */
    @Transactional(rollbackFor = Exception.class)
    public void exceptional(DevelopChildExceptionalDto dto) {
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getByIdVersion(dto.getDevelopChildOrderId(), dto.getVersion());
        Assert.notNull(developChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        if (developChildOrderPo.getHasException() == null) {
            throw new BizException("开发子单处于异常处理才能进行操作！");
        }
        if (!developChildOrderPo.getHasException().isBooleanVal()) {
            throw new BizException("开发子单处于异常处理才能进行操作！");
        }
        DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developPamphletOrderPo, () -> new BizException("版单的数据已被修改或删除，请刷新页面后重试！"));

        //查询样品单
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByDevelopPamphletOrderNo(developPamphletOrderPo.getDevelopPamphletOrderNo());

        if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
            // 更新样品单信息
            List<DevelopSampleOrderPo> updateDevelopSampleOrderPoList = new ArrayList<>();
            for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                DevelopSampleOrderPo updateDevelopSampleOrderPo = new DevelopSampleOrderPo();
                updateDevelopSampleOrderPo.setDevelopSampleOrderId(developSampleOrderPo.getDevelopSampleOrderId());
                updateDevelopSampleOrderPo.setVersion(developSampleOrderPo.getVersion());
                updateDevelopSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
                updateDevelopSampleOrderPoList.add(updateDevelopSampleOrderPo);
            }
            if (CollectionUtils.isNotEmpty(updateDevelopSampleOrderPoList)) {
                developSampleOrderDao.updateBatchByIdVersion(updateDevelopSampleOrderPoList);
            }
        }
        developChildOrderPo.setHasException(BooleanType.FALSE);
        DevelopPamphletOrderPo newDevelopPamphletOrderPo;
        switch (dto.getDevelopChildExceptionalType()) {
            case CANCEL_DEVELOPMENT:
                if (StringUtils.isBlank(dto.getCancelReason())) {
                    throw new ParamIllegalException("请填写取消原因!");
                }
                developChildBaseService.cancelExceptionChild(List.of(developChildOrderPo), dto.getCancelReason());
                break;
            case CHANGE_SUPPLIER:
                if (StringUtils.isBlank(dto.getSupplierCode())) {
                    throw new ParamIllegalException("供应商代码不能为空!");
                }
                if (StringUtils.isBlank(dto.getSupplierName())) {
                    throw new ParamIllegalException("供应商名称不能为空!");
                }
                developChildOrderPo.setSupplierCode(dto.getSupplierCode());
                developChildOrderPo.setSupplierName(dto.getSupplierName());
                developChildOrderPo.setPamphletTimes(developChildOrderPo.getPamphletTimes() + ScmConstant.DEVELOP_PAMPHLET_TIMES);
                //更新开发子单状态
                developChildBaseService.updateDevelopChildOrderStatus(developChildOrderPo, DevelopChildOrderStatus.PAMPHLET, true, true);

                newDevelopPamphletOrderPo = developChildBaseService.rebuildPamphletOrder(developPamphletOrderPo, dto.getSupplierCode(), dto.getSupplierName());
                developChildBaseService.updateDevelopExceptionOrder(developPamphletOrderPo, newDevelopPamphletOrderPo, dto);
                break;
            case REPRINT:
                developChildOrderPo.setPamphletTimes(developChildOrderPo.getPamphletTimes() + ScmConstant.DEVELOP_PAMPHLET_TIMES);
                developPamphletOrderPo.setDemandDesc(dto.getDemandDesc());
                newDevelopPamphletOrderPo = developChildBaseService.rebuildPamphletOrder(developPamphletOrderPo, developPamphletOrderPo.getSupplierCode(), developPamphletOrderPo.getSupplierName());
                //更新开发子单状态
                developChildBaseService.updateDevelopChildOrderStatus(developChildOrderPo, DevelopChildOrderStatus.PAMPHLET, true, true);
                developChildBaseService.updateDevelopExceptionOrder(developPamphletOrderPo, newDevelopPamphletOrderPo, dto);
                break;
            default:
                throw new BizException("请求类型错误！");
        }
    }

    /**
     * 开发子单取消开发
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/18 16:16
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelChild(DevelopChildCancelDto dto) {
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getByIdVersion(dto.getDevelopChildOrderId(), dto.getVersion());
        Assert.notNull(developChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        // 验证子单的状态
        developChildOrderPo.getDevelopChildOrderStatus().cancel(developChildOrderPo.getIsSample());

        // 取消子单相关单据状态
        developChildBaseService.cancelChild(List.of(developChildOrderPo));

        // 更新子单信息、推送MQ给PLM
        developChildOrderPo.setCancelReason(dto.getCancelReason());
        developChildOrderPo.setHasException(BooleanType.FALSE);
        developChildBaseService.updateDevelopChildOrderStatus(developChildOrderPo, DevelopChildOrderStatus.CANCEL, true, true);

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

    /**
     * 导出
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/18 17:04
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(DevelopChildSearchDto dto) {
        DevelopChildSearchDto searchDevelopChildWhere = developChildBaseService.getSearchDevelopChildWhere(dto);
        if (null == searchDevelopChildWhere) {
            throw new ParamIllegalException("导出数据为空");
        }
        Integer exportTotals = developChildOrderDao.getExportTotals(dto);
        Assert.isTrue(null != exportTotals && 0 != exportTotals, () -> new ParamIllegalException("导出数据为空"));
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_DEVELOP_ORDER_EXPORT.getCode(), dto));
    }

    /**
     * 齐备信息
     *
     * @param dto:
     * @return Boolean
     * @author ChenWenLong
     * @date 2023/8/20 15:30
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeInfo(DevelopChildCompleteInfoDto dto) {

        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getByIdVersion(dto.getDevelopChildOrderId(), dto.getVersion());
        Assert.notNull(developChildOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final DevelopChildOrderChangePo developChildOrderChangePo = developChildOrderChangeDao.getOneByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderChangePo, () -> new BizException("开发子单:{}数据存在问题，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo()));

        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByIdVersion(dto.getDevelopPamphletMsgVo().getDevelopPamphletOrderId(), dto.getDevelopPamphletMsgVo().getVersion());
        Assert.notNull(developPamphletOrderPo, () -> new BizException("开发子单:{}的版单数据存在问题，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo()));

        DevelopChildOrderStatus status = developChildOrderPo.getDevelopChildOrderStatus().toComplete();
        Map<DevelopSampleMethod, List<DevelopSampleCompleteInfoDto>> infoDtoMap = dto.getDevelopSampleCompleteInfoList().stream()
                .filter(info -> DevelopSampleMethod.SALE.equals(info.getDevelopSampleMethod())
                        || DevelopSampleMethod.SAMPLE_RETURN.equals(info.getDevelopSampleMethod())
                        || DevelopSampleMethod.SEAL_SAMPLE.equals(info.getDevelopSampleMethod()))
                .collect(Collectors.groupingBy(DevelopSampleCompleteInfoDto::getDevelopSampleMethod));
        List<String> developSampleOrderNoList = dto.getDevelopSampleCompleteInfoList().stream()
                .map(DevelopSampleCompleteInfoDto::getDevelopSampleOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        //针对样品单检验
        List<DevelopSampleOrderPo> developSampleOrderPoVerifyList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoVerifyList) {
            DevelopSampleCompleteInfoDto developSampleCompleteInfoDto = dto.getDevelopSampleCompleteInfoList()
                    .stream()
                    .filter(info -> info.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .findFirst()
                    .orElse(null);

            if (!developSampleOrderPo.getDevelopSampleStatus().verifyCompleteInfo() && developSampleCompleteInfoDto != null) {
                Assert.isTrue(developSampleOrderPo.getDevelopSampleMethod().equals(developSampleCompleteInfoDto.getDevelopSampleMethod()),
                        () -> new BizException("样品单号:{}，禁止修改处理方式！", developSampleOrderPo.getDevelopSampleOrderNo()));
            }
        }

        //验证spu和sku是否正确
        List<DevelopSampleCompleteInfoDto> developSampleCompleteInfoDtoSkuList = dto.getDevelopSampleCompleteInfoList().stream()
                .filter(info -> StringUtils.isNotBlank(info.getSku()))
                .collect(Collectors.toList());
        this.verifySpuAndSku(developSampleCompleteInfoDtoSkuList);

        //更新关联样品单信息
        if (CollectionUtils.isNotEmpty(dto.getDevelopSampleOrderDetailList())) {
            developChildBaseService.updateDevelopChildSample(dto.getDevelopSampleOrderDetailList(), developChildOrderPo, developPamphletOrderPo);
        }

        //更新开发子单
        developChildOrderPo.setSkuEncode(dto.getDevelopChildBaseMsgVo().getSkuEncode());
        developChildOrderPo.setSamplePrice(dto.getDevelopChildBaseMsgVo().getSamplePrice());
        developChildOrderPo.setIsOnShelves(BooleanType.TRUE);
        developChildOrderChangePo.setOnShelvesCompletionDate(LocalDateTime.now());
        developChildOrderChangePo.setNewestCompletionDate(LocalDateTime.now());
        // 保存渠道大货价格
        developChildBaseService.developOrderPriceSave(List.of(dto.getDevelopChildBaseMsgVo().getDevelopOrderPrice()),
                developChildOrderPo.getDevelopChildOrderNo(),
                DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE);

        // 保存生产属性
        final List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = DevelopChildConverter.convertDevelopAttrDtoToPo(developChildOrderPo, dto.getDevelopChildBaseMsgVo().getDevelopChildOrderAttrList());
        List<DevelopChildOrderAttrPo> developChildOrderAttrPos = developChildOrderAttrDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        if (CollectionUtils.isNotEmpty(developChildOrderAttrPos)) {
            developChildOrderAttrDao.deleteByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        }
        if (CollectionUtils.isNotEmpty(developChildOrderAttrPoList)) {
            developChildOrderAttrDao.insertBatch(developChildOrderAttrPoList);
        }


        //无需打样 处理逻辑
        List<DevelopSampleCompleteInfoDto> noProofingSample = dto.getDevelopSampleCompleteInfoList();
        if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
            if (CollectionUtils.isEmpty(noProofingSample)) {
                throw new BizException("无需打样时请求数据错误，请联系管理员后重试！");
            }
            DevelopSampleCompleteInfoDto noProofingSampleInfoDto = noProofingSample.get(0);
            if (StringUtils.isNotBlank(noProofingSampleInfoDto.getSpu()) && StringUtils.isNotBlank(noProofingSampleInfoDto.getSku())) {
                developChildOrderPo.setSpu(noProofingSampleInfoDto.getSpu());
                developChildOrderPo.setSku(noProofingSampleInfoDto.getSku());
            }
            if (StringUtils.isNotBlank(developChildOrderPo.getSpu()) && StringUtils.isBlank(noProofingSampleInfoDto.getSpu())) {
                throw new BizException("请求入参错误，开发子单{}已经存在SPU，请填写对应SPU:{}", developChildOrderPo.getDevelopChildOrderNo(), developChildOrderPo.getSpu());
            }
            //无需打样逻辑处理
            this.noProofingSampleUpdate(noProofingSampleInfoDto,
                    developChildOrderPo,
                    developPamphletOrderPo);
        }

        //需打样-非封样入库 处理逻辑
        List<DevelopSampleCompleteInfoDto> developSampleCompleteInfoDtoSealSampleList = dto.getDevelopSampleCompleteInfoList().stream()
                .filter(info -> DevelopSampleMethod.SEAL_SAMPLE.equals(info.getDevelopSampleMethod()))
                .collect(Collectors.toList());
        //是否有spu和sku
        boolean isExistSpuAndSku = false;
        if (BooleanType.TRUE.equals(developChildOrderPo.getIsSample()) && CollectionUtils.isEmpty(developSampleCompleteInfoDtoSealSampleList)) {
            List<DevelopSampleCompleteInfoDto> developSampleCompleteInfoNotSealSampleList = dto.getDevelopSampleCompleteInfoList().stream()
                    .filter(info -> DevelopSampleCompleteType.NOT_SEAL_SAMPLE.equals(info.getDevelopSampleCompleteType()))
                    .collect(Collectors.toList());
            //有SPU和SKU情况
            DevelopSampleCompleteInfoDto notSealSampleInfoDto = new DevelopSampleCompleteInfoDto();
            if (CollectionUtils.isNotEmpty(developSampleCompleteInfoNotSealSampleList)) {
                //只取第一条数据进行处理
                notSealSampleInfoDto = developSampleCompleteInfoNotSealSampleList.get(0);
                if (StringUtils.isBlank(notSealSampleInfoDto.getSku())) {
                    throw new BizException("若全部样品单均为非入库，请填写SKU后再次提交！", notSealSampleInfoDto.getSpu());
                }
                if (StringUtils.isBlank(notSealSampleInfoDto.getSpu())) {
                    throw new BizException("若全部样品单均为非入库，请填写SPU后再次提交！", notSealSampleInfoDto.getSku());
                }
                //SPU有、SKU有更新当前子单
                if (StringUtils.isNotBlank(notSealSampleInfoDto.getSpu())
                        && StringUtils.isNotBlank(notSealSampleInfoDto.getSku())) {
                    developChildOrderPo.setSpu(notSealSampleInfoDto.getSpu());
                    developChildOrderPo.setSku(notSealSampleInfoDto.getSku());
                }
                isExistSpuAndSku = true;
            }
            //非封样入库处理逻辑
            this.notSealSampleUpdate(notSealSampleInfoDto,
                    developChildOrderPo,
                    developPamphletOrderPo,
                    isExistSpuAndSku);
        }

        //闪售 处理逻辑
        List<DevelopSampleCompleteInfoDto> sale = infoDtoMap.get(DevelopSampleMethod.SALE);
        if (CollectionUtils.isNotEmpty(sale)) {
            this.saleSampleUpdate(sale);
        }

        //退样 处理逻辑
        List<DevelopSampleCompleteInfoDto> sampleReturn = infoDtoMap.get(DevelopSampleMethod.SAMPLE_RETURN);
        if (CollectionUtils.isNotEmpty(sampleReturn)) {
            this.returnSampleUpdate(sampleReturn);
        }

        //封样入库 处理逻辑
        List<DevelopSampleCompleteInfoDto> sealSample = infoDtoMap.get(DevelopSampleMethod.SEAL_SAMPLE);
        if (CollectionUtils.isNotEmpty(sealSample)) {
            this.sealSampleUpdate(sealSample, developChildOrderPo, developPamphletOrderPo);
        }

        developChildOrderDao.updateByIdVersion(developChildOrderPo);
        developChildOrderChangeDao.updateByIdVersion(developChildOrderChangePo);

        //更新开发子单状态
        developChildBaseService.updateDevelopChildOrderStatus(developChildOrderPo, status, true, true);

        // 开发子单完成推送飞书消息
        developChildFeiShuService.sendDevelopChildStateFeiShuMsg(developChildOrderPo);
    }

    /**
     * 齐备信息:无需打样的逻辑
     *
     * @param sampleInfoDto:
     * @param developChildOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/9/8 15:20
     */
    private void noProofingSampleUpdate(DevelopSampleCompleteInfoDto sampleInfoDto,
                                        DevelopChildOrderPo developChildOrderPo,
                                        DevelopPamphletOrderPo developPamphletOrderPo) {

        //SPU有、SKU无
        if (StringUtils.isNotBlank(sampleInfoDto.getSpu()) && StringUtils.isBlank(sampleInfoDto.getSku())) {
            throw new BizException("请填写sku，sku不能为空！");
        }
        //SPU有、SKU有
        if (StringUtils.isNotBlank(sampleInfoDto.getSpu()) && StringUtils.isNotBlank(sampleInfoDto.getSku())) {
            List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(List.of(developChildOrderPo.getDevelopChildOrderNo()), List.of(DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE));
            if (CollectionUtils.isEmpty(developOrderPricePoList)) {
                throw new BizException("无需打样时创建商品调价获取不到对应的开发子单：{}渠道大货价格，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo());
            }
            //推送PLM
            final DevelopSampleCompleteNoticeMqDto noticeMqDto = new DevelopSampleCompleteNoticeMqDto();
            noticeMqDto.setUserKey(GlobalContext.getUserKey());
            noticeMqDto.setUsername(GlobalContext.getUsername());
            noticeMqDto.setKey(developChildOrderPo.getDevelopParentOrderNo());
            noticeMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            noticeMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            noticeMqDto.setSpu(sampleInfoDto.getSpu());
            noticeMqDto.setSku(sampleInfoDto.getSku());
            consistencySendMqService.execSendMq(DevelopCompleteNoticeHandler.class, noticeMqDto);
            // 新增生产信息
            produceDataBaseService.addDevelopProduceData(developChildOrderPo, new ArrayList<>(), developPamphletOrderPo);
            // 更新商品采购价格
            ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
            produceDataUpdatePurchasePriceBo.setSku(developChildOrderPo.getSku());
            produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developOrderPricePoList.get(0).getPrice());
            produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(List.of(produceDataUpdatePurchasePriceBo));

            // 更新商品调价的价格
            List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();
            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
            goodsPriceAddBo.setSku(developChildOrderPo.getSku());
            goodsPriceAddBo.setSupplierCode(developChildOrderPo.getSupplierCode());
            goodsPriceAddBo.setChannelId(developOrderPricePoList.get(0).getChannelId());
            goodsPriceAddBo.setChannelPrice(developOrderPricePoList.get(0).getPrice());
            goodsPriceAddBoList.add(goodsPriceAddBo);
            // 更新商品调价的价格
            goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);
        }
        //SPU无、SKU无
        if (StringUtils.isBlank(sampleInfoDto.getSpu()) && StringUtils.isBlank(sampleInfoDto.getSku())) {
            //推送PLM的MQ创建对应的SPU和SKU
            this.completeInfoSale(developChildOrderPo, Collections.emptyList(), null);
        }

    }

    /**
     * 齐备信息:闪售的逻辑
     *
     * @param sale:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/30 15:22
     */
    private void saleSampleUpdate(List<DevelopSampleCompleteInfoDto> sale) {
        List<String> developSampleOrderNos = sale.stream().map(DevelopSampleCompleteInfoDto::getDevelopSampleOrderNo).collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoSaleList = developSampleOrderDao.getListByNoList(developSampleOrderNos);
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoSaleList) {
            developSampleOrderPo.setDevelopSampleMethod(DevelopSampleMethod.SALE);
            developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
        }
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoSaleList);
    }

    /**
     * 齐备信息:退样的逻辑
     *
     * @param sampleReturn:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/30 15:26
     */
    private void returnSampleUpdate(List<DevelopSampleCompleteInfoDto> sampleReturn) {
        List<String> developSampleOrderNoReturnList = sampleReturn.stream().map(DevelopSampleCompleteInfoDto::getDevelopSampleOrderNo).collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoReturn = developSampleOrderDao.getListByNoList(developSampleOrderNoReturnList);
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoReturn) {
            sampleReturn.stream().filter(w -> w.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                    .findFirst().ifPresent(infoDto -> {
                        if (developSampleOrderPo.getDevelopSampleStatus().verifyCompleteInfo()) {
                            developSampleOrderPo.setDevelopSampleMethod(infoDto.getDevelopSampleMethod());
                            developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
                        }
                    });
        }
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoReturn);
    }

    /**
     * 齐备信息:封样入库的逻辑
     *
     * @param sealSample:
     * @param developChildOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/1/30 15:41
     */
    private void sealSampleUpdate(List<DevelopSampleCompleteInfoDto> sealSample,
                                  DevelopChildOrderPo developChildOrderPo,
                                  DevelopPamphletOrderPo developPamphletOrderPo) {

        List<String> developSampleOrderSealSamples = sealSample.stream().map(DevelopSampleCompleteInfoDto::getDevelopSampleOrderNo).collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderSealSamples);
        //获取第一条进行处理
        DevelopSampleCompleteInfoDto sampleInfoDto = sealSample.get(0);
        String developSampleOrderNo = sampleInfoDto.getDevelopSampleOrderNo();

        //更新样品单的信息
        for (DevelopSampleOrderPo sampleOrderPo : developSampleOrderPoList) {
            sealSample.stream().filter(w -> w.getDevelopSampleOrderNo().equals(sampleOrderPo.getDevelopSampleOrderNo())).findFirst().ifPresent(infoDto -> {
                sampleOrderPo.setDevelopSampleMethod(infoDto.getDevelopSampleMethod());
                sampleOrderPo.setSpu(infoDto.getSpu());
                sampleOrderPo.setSku(infoDto.getSku());
                if (StringUtils.isNotBlank(infoDto.getSpu()) && StringUtils.isNotBlank(infoDto.getSku())) {
                    sampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
                }
            });
        }

        DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoList.stream().filter(w -> w.getDevelopSampleOrderNo().equals(developSampleOrderNo)).findFirst().orElse(null);
        if (developSampleOrderPo == null) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        //SPU有、SKU无
        if (StringUtils.isNotBlank(sampleInfoDto.getSpu()) && StringUtils.isBlank(sampleInfoDto.getSku())) {
            throw new BizException("请填写sku，sku不能为空！");
        }

        //SPU有、SKU有
        if (StringUtils.isNotBlank(sampleInfoDto.getSpu()) && StringUtils.isNotBlank(sampleInfoDto.getSku())) {
            developChildOrderPo.setSpu(sampleInfoDto.getSpu());
            developChildOrderPo.setSku(sampleInfoDto.getSku());
            //推送PLM
            final DevelopSampleCompleteNoticeMqDto noticeMqDto = new DevelopSampleCompleteNoticeMqDto();
            noticeMqDto.setUserKey(GlobalContext.getUserKey());
            noticeMqDto.setUsername(GlobalContext.getUsername());
            noticeMqDto.setKey(developSampleOrderPo.getDevelopSampleOrderNo());
            noticeMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            noticeMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            noticeMqDto.setSpu(sampleInfoDto.getSpu());
            noticeMqDto.setSku(sampleInfoDto.getSku());
            consistencySendMqService.execSendMq(DevelopCompleteNoticeHandler.class, noticeMqDto);

            // 新增生产信息
            produceDataBaseService.addDevelopProduceData(developChildOrderPo, List.of(developSampleOrderPo), developPamphletOrderPo);

        }

        //SPU无、SKU无
        if (StringUtils.isBlank(sampleInfoDto.getSpu()) && StringUtils.isBlank(sampleInfoDto.getSku())) {
            if (StringUtils.isNotBlank(developChildOrderPo.getSpu())) {
                throw new BizException("请求入参错误，开发子单{}已经存在SPU，请填写对应SPU:{}", developChildOrderPo.getDevelopChildOrderNo(), developChildOrderPo.getSpu());
            }
            //推送PLM
            this.completeInfoSale(developChildOrderPo, List.of(developSampleOrderPo), DevelopSampleMethod.SEAL_SAMPLE);
        }

        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);
    }


    /**
     * 验证spu和sku是否正确
     *
     * @param developSampleCompleteInfoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/9/1 14:24
     */
    private void verifySpuAndSku(List<DevelopSampleCompleteInfoDto> developSampleCompleteInfoList) {
        List<String> skuList = developSampleCompleteInfoList.stream()
                .map(DevelopSampleCompleteInfoDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(skuList)) {
            final List<PlmGoodsSkuVo> plmSkuInfoVoList = plmRemoteService.getSkuDetailListBySkuList(skuList);
            if (CollectionUtils.isEmpty(plmSkuInfoVoList)) {
                throw new BizException("sku都不存在，请重新填写！");
            }
            for (DevelopSampleCompleteInfoDto developSampleCompleteInfoDto : developSampleCompleteInfoList) {
                plmSkuInfoVoList.stream()
                        .filter(w -> w.getSpuCode().equals(developSampleCompleteInfoDto.getSpu())
                                && w.getSkuCode().equals(developSampleCompleteInfoDto.getSku()))
                        .findAny()
                        .orElseThrow(() -> new BizException("sku:{}不存在，请重新填写！", developSampleCompleteInfoDto.getSku()));
            }

        }

    }

    /**
     * 闪售PLM创建sku
     *
     * @param developChildOrderPo:
     * @param developSampleOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/29 17:18
     */
    public void completeInfoSale(DevelopChildOrderPo developChildOrderPo, List<DevelopSampleOrderPo> developSampleOrderPoList, DevelopSampleMethod developSampleMethod) {

        log.info("齐备信息进行创建新SPU和SKU。PO={}", JacksonUtil.parse2Str(developChildOrderPo));
        //无需打样逻辑
        if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())) {
            //推送PLM
            final DevelopCompleteInfoMqDto developCompleteInfoMqDto = new DevelopCompleteInfoMqDto();
            developCompleteInfoMqDto.setKey(developChildOrderPo.getDevelopParentOrderNo());
            developCompleteInfoMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            developCompleteInfoMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            developCompleteInfoMqDto.setDevelopSampleDestination(DevelopSampleDestination.NOT_SAMPLE);
            developCompleteInfoMqDto.setUserKey(GlobalContext.getUserKey());
            developCompleteInfoMqDto.setUsername(GlobalContext.getUsername());
            developCompleteInfoMqDto.setPlatCode(developChildOrderPo.getPlatform());
            developCompleteInfoMqDto.setCategoryId(developChildOrderPo.getCategoryId());
            String title = developChildOrderPo.getDevelopParentOrderNo() + developChildOrderPo.getDevelopChildOrderNo();
            developCompleteInfoMqDto.setTitleCn(title);
            developCompleteInfoMqDto.setDescription(title);
            consistencySendMqService.execSendMq(DevelopCompleteInfoHandler.class, developCompleteInfoMqDto);
            return;
        }

        //需要打样逻辑
        if (BooleanType.TRUE.equals(developChildOrderPo.getIsSample())) {
            for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
                //推送PLM
                final DevelopCompleteInfoMqDto developCompleteInfoMqDto = new DevelopCompleteInfoMqDto();
                developCompleteInfoMqDto.setKey(developSampleOrderPo.getDevelopSampleOrderNo());
                developCompleteInfoMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
                developCompleteInfoMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
                developCompleteInfoMqDto.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developCompleteInfoMqDto.setDevelopSampleDestination(developSampleMethod.getDevelopSampleDestination());
                developCompleteInfoMqDto.setUserKey(GlobalContext.getUserKey());
                developCompleteInfoMqDto.setUsername(GlobalContext.getUsername());
                developCompleteInfoMqDto.setPlatCode(developChildOrderPo.getPlatform());
                developCompleteInfoMqDto.setCategoryId(developChildOrderPo.getCategoryId());

                String title = developChildOrderPo.getDevelopParentOrderNo() + developChildOrderPo.getDevelopChildOrderNo() + developSampleOrderPo.getDevelopSampleOrderNo() + developSampleOrderPo.getDevelopSampleMethod().getRemark();

                developCompleteInfoMqDto.setTitleCn(title);
                developCompleteInfoMqDto.setDescription(title);
                consistencySendMqService.execSendMq(DevelopCompleteInfoHandler.class, developCompleteInfoMqDto);
            }
        }
    }

    /**
     * 处理齐备信息返回
     *
     * @param message:
     * @return void
     * @author ChenWenLong
     * @date 2023/8/20 17:05
     */
    @Transactional(rollbackFor = Exception.class)
    @RedisLock(key = "#message.skuCode", prefix = ScmRedisConstant.SCM_DEVELOP_COMPLETE_RETURN)
    public void updateDevelopCompleteInfoReturn(DevelopCompleteInfoReturnMqDto message) {
        log.info("plm创建完sku推送mq的信息，Dto={}", JacksonUtil.parse2Str(message));
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(message.getDevelopChildOrderNo());
        Assert.notNull(developChildOrderPo, () -> new BizException("找不到对应开发子单，PLM推送齐备信息返回的信息错误！"));

        // 获取sku批次码
        if (StringUtils.isBlank(message.getSkuCode())) {
            throw new BizException("mq信息中没有sku，PLM推送齐备信息返回的信息错误！");
        }

        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        //无需打样逻辑处理
        if (BooleanType.FALSE.equals(developChildOrderPo.getIsSample())
                && StringUtils.isBlank(message.getDevelopSampleOrderNo())) {
            this.updateAllDevChildSpuByParentNo(message.getSpuCode(), message.getSkuCode(), developChildOrderPo, message.getGoodsPlatSpuId());
            developChildOrderDao.updateByIdVersion(developChildOrderPo);

            List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(List.of(developChildOrderPo.getDevelopChildOrderNo()), List.of(DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE));
            if (CollectionUtils.isEmpty(developOrderPricePoList)) {
                throw new BizException("无需打样时PLM创建SKU后创建商品调价获取不到对应的开发子单：{}渠道大货价格，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo());
            }

            //新增生产信息
            produceDataBaseService.addDevelopProduceData(developChildOrderPo, new ArrayList<>(), developPamphletOrderPo);
            // 更新商品采购价格
            ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
            produceDataUpdatePurchasePriceBo.setSku(developChildOrderPo.getSku());
            produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developOrderPricePoList.get(0).getPrice());
            produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(List.of(produceDataUpdatePurchasePriceBo));

            // 更新商品调价的价格
            List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();
            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
            goodsPriceAddBo.setSku(developChildOrderPo.getSku());
            goodsPriceAddBo.setSupplierCode(developChildOrderPo.getSupplierCode());
            goodsPriceAddBo.setChannelId(developOrderPricePoList.get(0).getChannelId());
            goodsPriceAddBo.setChannelPrice(developOrderPricePoList.get(0).getPrice());
            goodsPriceAddBoList.add(goodsPriceAddBo);
            // 更新商品调价的价格
            goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);

            return;
        }

        //非封样入库处理
        List<DevelopSampleOrderPo> developSampleOrderPoSealSampleList = developSampleOrderDao.getListByChildNoAndMethodAndType(developChildOrderPo.getDevelopChildOrderNo(),
                DevelopSampleMethod.SEAL_SAMPLE,
                DevelopSampleType.NORMAL);
        if (BooleanType.TRUE.equals(developChildOrderPo.getIsSample())
                && CollectionUtils.isEmpty(developSampleOrderPoSealSampleList)
                && StringUtils.isBlank(message.getDevelopSampleOrderNo())) {
            this.updateAllDevChildSpuByParentNo(message.getSpuCode(), message.getSkuCode(), developChildOrderPo, message.getGoodsPlatSpuId());
            developChildOrderDao.updateByIdVersion(developChildOrderPo);

            List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(List.of(developChildOrderPo.getDevelopChildOrderNo()), List.of(DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE));
            if (CollectionUtils.isEmpty(developOrderPricePoList)) {
                throw new BizException("非封样入库时PLM创建SKU后创建商品调价获取不到对应的开发子单：{}渠道大货价格，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo());
            }

            //新增生产信息
            produceDataBaseService.addDevelopProduceData(developChildOrderPo, new ArrayList<>(), developPamphletOrderPo);
            // 更新商品采购价格
            ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
            produceDataUpdatePurchasePriceBo.setSku(developChildOrderPo.getSku());
            produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developOrderPricePoList.get(0).getPrice());
            produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(List.of(produceDataUpdatePurchasePriceBo));

            // 更新商品调价的价格
            List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();
            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
            goodsPriceAddBo.setSku(developChildOrderPo.getSku());
            goodsPriceAddBo.setSupplierCode(developChildOrderPo.getSupplierCode());
            goodsPriceAddBo.setChannelId(developOrderPricePoList.get(0).getChannelId());
            goodsPriceAddBo.setChannelPrice(developOrderPricePoList.get(0).getPrice());
            goodsPriceAddBoList.add(goodsPriceAddBo);
            // 更新商品调价的价格
            goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);
            return;
        }

        //需要打样的封样入库和闪售处理
        if (StringUtils.isNotBlank(message.getDevelopSampleOrderNo())) {
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderDao.getOneByNo(message.getDevelopSampleOrderNo());
            Assert.notNull(developSampleOrderPo, () -> new BizException("找不到对应样品单，PLM推送齐备信息返回的信息错误！"));
            // 封样入库 处理逻辑
            if (DevelopSampleMethod.SEAL_SAMPLE.equals(developSampleOrderPo.getDevelopSampleMethod())) {
                this.developCompleteInfoReturnSealSample(message, developChildOrderPo, developSampleOrderPo, developPamphletOrderPo);
            }

            // 闪售 处理逻辑
            if (DevelopSampleMethod.SALE.equals(developSampleOrderPo.getDevelopSampleMethod())) {
                if (DevelopSampleType.PRENATAL_SAMPLE.equals(developSampleOrderPo.getDevelopSampleType())) {
                    this.developCompleteInfoReturnSealPrenatal(message, developChildOrderPo, developSampleOrderPo, developPamphletOrderPo);
                } else {
                    this.developCompleteInfoReturnSeal(message, developChildOrderPo, developSampleOrderPo, developPamphletOrderPo);
                }

            }

        }

    }

    private void updateAllDevChildSpuByParentNo(String spuCode, String skuCode, DevelopChildOrderPo developChildOrderPo, Long goodsPlatSpuId) {
        developChildOrderPo.setSpu(spuCode);
        developChildOrderPo.setSku(skuCode);
        if (goodsPlatSpuId != null) {
            developChildOrderPo.setPlatformId(goodsPlatSpuId);
        }
        List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
        for (DevelopChildOrderPo childOrderPo : developChildOrderPoList) {
            childOrderPo.setSpu(spuCode);
            if (goodsPlatSpuId != null) {
                childOrderPo.setPlatformId(goodsPlatSpuId);
            }
        }
        List<DevelopChildOrderPo> updateDevelopChildOrderPoList = developChildOrderPoList.stream()
                .filter(po -> !po.getDevelopChildOrderNo().equals(developChildOrderPo.getDevelopChildOrderNo()))
                .collect(Collectors.toList());

        developChildOrderDao.updateBatchByIdVersion(updateDevelopChildOrderPoList);
    }

    public DevelopReviewDetailVo developReviewDetail(DevelopReviewNoDto dto) {
        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNo(dto.getDevelopReviewOrderNo());
        Assert.notNull(developReviewOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        final List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByReviewNo(developReviewOrderPo.getDevelopReviewOrderNo());
        final List<String> developSampleNoList = developReviewSampleOrderPoList.stream().map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo).collect(Collectors.toList());
        final List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleNoList);
        // 查询关联样品单
        final Map<String, DevelopSampleOrderPo> developSampleOrderPoMap = developSampleOrderPoList.stream()
                .collect(Collectors.toMap(DevelopSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));

        // 获取开发子单关联审版单时克重最高的封样入库的审版样品单
        final List<String> developChildOrderNoList = developReviewSampleOrderPoList.stream()
                .map(DevelopReviewSampleOrderPo::getDevelopChildOrderNo)
                .distinct()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        Map<String, DevelopReviewSampleOrderPo> developChildOrderSealSampleMap = developSampleOrderBaseService.getSealSampleByChildOrderNoList(developChildOrderNoList);
        List<String> developSampleOrderNoList = developChildOrderSealSampleMap.values().stream()
                .map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo)
                .collect(Collectors.toList());

        // 查询样品单的渠道大货价格
        List<String> newDevelopSampleOrderNoList = new ArrayList<>(developSampleNoList);
        newDevelopSampleOrderNoList.addAll(developSampleOrderNoList);
        newDevelopSampleOrderNoList.addAll(developChildOrderNoList);
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(newDevelopSampleOrderNoList,
                List.of(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE, DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE));


        final List<Long> developReviewSampleOrderIdList = developReviewSampleOrderPoList.stream().map(DevelopReviewSampleOrderPo::getDevelopReviewSampleOrderId).collect(Collectors.toList());

        //查询版单获取款式和颜色图片信息
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByDevelopPamphletOrderNo(developReviewOrderPo.getDevelopPamphletOrderNo());
        Map<Long, List<String>> pamphletImageStyleMap = Collections.emptyMap();
        Map<Long, List<String>> pamphletImageColorMap = Collections.emptyMap();
        List<DevelopPamphletOrderRawPo> developPamphletOrderRawPoList = new ArrayList<>();
        if (null != developPamphletOrderPo) {
            pamphletImageStyleMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_PAMPHLET_STYLE, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));
            pamphletImageColorMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_PAMPHLET_COLOR, List.of(developPamphletOrderPo.getDevelopPamphletOrderId()));
            //关联版单原料和备注信息
            developPamphletOrderRawPoList = developPamphletOrderRawDao.getListByDevelopPamphletOrderNoAndType(developPamphletOrderPo.getDevelopPamphletOrderNo(), SampleRawBizType.FORMULA);
        }

        //查询开发单获取款式和颜色图片信息（针对产前样的样品单）
        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        Map<Long, List<String>> childImageStyleMap = Collections.emptyMap();
        Map<Long, List<String>> childImageColorMap = Collections.emptyMap();
        if (null != developChildOrderPo) {
            childImageStyleMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_CHILD_STYLE, List.of(developChildOrderPo.getDevelopChildOrderId()));
            childImageColorMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_CHILD_COLOR, List.of(developChildOrderPo.getDevelopChildOrderId()));
        }

        //关联样品单的图片
        Map<Long, List<String>> pamphletSampleImageEffectMap = Collections.emptyMap();
        Map<Long, List<String>> pamphletSampleImageDetailMap = Collections.emptyMap();
        if (CollectionUtils.isNotEmpty(developReviewSampleOrderIdList)) {
            pamphletSampleImageEffectMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT, developReviewSampleOrderIdList);
            pamphletSampleImageDetailMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL, developReviewSampleOrderIdList);
        }

        //产前样样品单的关联单号
        String sampleOrderNoJoining = null;
        if (DevelopReviewRelated.DEVELOP_SAMPLE.equals(developReviewOrderPo.getDevelopReviewRelated())) {
            sampleOrderNoJoining = developReviewSampleOrderPoList.stream()
                    .map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo)
                    .distinct()
                    .collect(Collectors.joining(","));
        }


        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(developSampleNoList);
        final Map<String, List<DevelopReviewSampleOrderInfoPo>> devSampleNoInfoPoListMap = developReviewSampleOrderInfoPoList.stream().collect(Collectors.groupingBy(DevelopReviewSampleOrderInfoPo::getDevelopSampleOrderNo));

        return SupplierDevelopConverter.developReviewPoToVo(developReviewOrderPo, developChildOrderAttrPoList,
                developReviewSampleOrderPoList, devSampleNoInfoPoListMap,
                pamphletImageStyleMap, pamphletImageColorMap,
                developPamphletOrderPo, pamphletSampleImageEffectMap,
                pamphletSampleImageDetailMap, developChildOrderPo,
                childImageStyleMap, childImageColorMap, sampleOrderNoJoining,
                developPamphletOrderRawPoList,
                developSampleOrderPoMap,
                developChildOrderSealSampleMap,
                developOrderPricePoList);
    }

    /**
     * 保存审版信息
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/22 15:26
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDevelopReview(DevelopReviewCompleteDto dto) {
        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNoAndVersion(dto.getDevelopReviewOrderNo(), dto.getVersion());
        Assert.notNull(developReviewOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        developReviewOrderPo.setDevelopReviewOrderStatus(developReviewOrderPo.getDevelopReviewOrderStatus().waitReviewToReviewing());
        this.updateDevelopReviewOrderPo(developReviewOrderPo, dto);

    }

    /**
     * 更新审版单数据
     *
     * @param developReviewOrderPo:
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/9/6 10:55
     */
    public void updateDevelopReviewOrderPo(DevelopReviewOrderPo developReviewOrderPo, DevelopReviewCompleteDto dto) {

        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByReviewNo(dto.getDevelopReviewOrderNo());
        final Map<String, DevelopReviewSampleOrderPo> devReviewSampleNoPoMap = developReviewSampleOrderPoList.stream().collect(Collectors.toMap(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));

        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNoList(new ArrayList<>(devReviewSampleNoPoMap.keySet()));
        final List<Long> developReviewSampleOrderIdList = developReviewSampleOrderInfoPoList.stream()
                .map(DevelopReviewSampleOrderInfoPo::getDevelopReviewSampleOrderId).collect(Collectors.toList());


        //获取样品单并更新信息
        List<String> developSampleOrderNoList = dto.getDevelopReviewSampleList().stream()
                .map(DevelopReviewSampleDto::getDevelopSampleOrderNo).distinct().collect(Collectors.toList());
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(developSampleOrderNoList);
        Map<String, DevelopSampleOrderPo> developSampleOrderPoMap = developSampleOrderPoList.stream()
                .collect(Collectors.toMap(DevelopSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));

        // 产前样验证
        for (DevelopReviewSampleDto developReviewSampleDto : dto.getDevelopReviewSampleList()) {
            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoMap.get(developReviewSampleDto.getDevelopSampleOrderNo());
            if (null != developSampleOrderPo && DevelopSampleType.PRENATAL_SAMPLE.equals(developSampleOrderPo.getDevelopSampleType())) {
                List<DevelopOrderPriceSaveDto> developOrderPriceList = developReviewSampleDto.getDevelopOrderPriceList();
                developChildBaseService.verifyDevelopOrderPrice(developOrderPriceList);
            }
        }

        // 新增生产信息
        List<String> developChildOrderNoList = developSampleOrderPoList.stream()
                .map(DevelopSampleOrderPo::getDevelopChildOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderDao.getMapByNoList(developChildOrderNoList);
        List<DevelopSampleOrderPo> addDevelopProduceDataSamplePoList = new ArrayList<>();


        //产前样的样品单闪售进行生产sku
        List<DevelopSampleOrderPo> saleSampleOrderPoList = new ArrayList<>();

        //创建开发审版关联样品单属性
        List<DevelopReviewSampleOrderInfoPo> insertDevelopReviewSampleOrderInfoPoList = new ArrayList<>();
        List<ScmImageBo> detailImageBoList = new ArrayList<>();
        List<ScmImageBo> effectImageBoList = new ArrayList<>();
        //审版结果
        ReviewResult reviewResult = ReviewResult.REVIEW_NO_PASS;
        //是否生成核价单
        boolean isCreateDevelopPricing = false;
        List<Long> imageBizIdDelList = new ArrayList<>();
        //产前样效果图落到生产资料封样图
        List<ProduceDataSkuSealImageBo> produceDataSkuSealImageBoList = new ArrayList<>();

        // 更新渠道大货价格
        List<DevelopOrderPriceCreateBo> developOrderPriceCreateBoList = new ArrayList<>();

        for (DevelopReviewSampleDto sampleDto : dto.getDevelopReviewSampleList()) {
            final DevelopReviewSampleOrderPo developReviewSampleOrderPo = devReviewSampleNoPoMap.get(sampleDto.getDevelopSampleOrderNo());
            if (null == developReviewSampleOrderPo) {
                throw new BizException("审版单找不到关联的样品单:{}", sampleDto.getDevelopSampleOrderNo());
            }
            developReviewSampleOrderPo.setGramWeight(sampleDto.getGramWeight());
            developReviewSampleOrderPo.setDevelopSampleDemand(sampleDto.getDevelopSampleDemand());
            developReviewSampleOrderPo.setDevelopSampleQuality(sampleDto.getDevelopSampleQuality());
            developReviewSampleOrderPo.setDevelopSampleMethod(sampleDto.getDevelopSampleMethod());
            developReviewSampleOrderPo.setDevelopSampleNewness(sampleDto.getDevelopSampleNewness());
            developReviewSampleOrderPo.setDevelopSampleStage(sampleDto.getDevelopSampleStage());
            developReviewSampleOrderPo.setDevelopSampleDevOpinion(sampleDto.getDevelopSampleDevOpinion());
            developReviewSampleOrderPo.setDevelopSampleQltyOpinion(sampleDto.getDevelopSampleQltyOpinion());
            developReviewSampleOrderPo.setAbnormalHair(sampleDto.getAbnormalHair());
            developReviewSampleOrderPo.setFloatingHair(sampleDto.getFloatingHair());
            developReviewSampleOrderPo.setMeshCapFit(sampleDto.getMeshCapFit());
            developReviewSampleOrderPo.setHairFeel(sampleDto.getHairFeel());
            developReviewSampleOrderPo.setDevelopReviewSampleSource(sampleDto.getDevelopReviewSampleSource());

            if (DevelopSampleMethod.SEAL_SAMPLE.equals(sampleDto.getDevelopSampleMethod())
                    && DevelopSampleQuality.QUALIFIED.equals(sampleDto.getDevelopSampleQuality())
                    && (DevelopSampleDemand.SATISFY.equals(sampleDto.getDevelopSampleDemand())
                    || DevelopSampleDemand.NEED_NOT_CONFIRMATION.equals(sampleDto.getDevelopSampleDemand()))) {
                reviewResult = ReviewResult.REVIEW_PASS;
            }
            if (ReviewResult.REVIEW_PASS.equals(reviewResult)) {
                isCreateDevelopPricing = true;
            }

            //生产属性
            sampleDto.getDevelopReviewSampleInfoList().forEach(infoDto -> {
                DevelopReviewSampleOrderInfoPo developReviewSampleOrderInfoPo = new DevelopReviewSampleOrderInfoPo();
                developReviewSampleOrderInfoPo.setDevelopChildOrderNo(developReviewSampleOrderPo.getDevelopChildOrderNo());
                developReviewSampleOrderInfoPo.setDevelopParentOrderNo(developReviewSampleOrderPo.getDevelopParentOrderNo());
                developReviewSampleOrderInfoPo.setDevelopReviewOrderNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());
                developReviewSampleOrderInfoPo.setDevelopSampleOrderNo(developReviewSampleOrderPo.getDevelopSampleOrderNo());
                developReviewSampleOrderInfoPo.setAttributeNameId(infoDto.getAttributeNameId());
                developReviewSampleOrderInfoPo.setSampleInfoKey(infoDto.getSampleInfoKey());
                developReviewSampleOrderInfoPo.setSampleInfoValue(infoDto.getSampleInfoValue());
                developReviewSampleOrderInfoPo.setEvaluationOpinion(infoDto.getEvaluationOpinion());
                insertDevelopReviewSampleOrderInfoPoList.add(developReviewSampleOrderInfoPo);
            });
            if (CollectionUtils.isNotEmpty(sampleDto.getDetailFileCodeList())) {
                ScmImageBo detailImageBo = new ScmImageBo();
                detailImageBo.setImageBizId(developReviewSampleOrderPo.getDevelopReviewSampleOrderId());
                detailImageBo.setFileCodeList(sampleDto.getDetailFileCodeList());
                detailImageBoList.add(detailImageBo);
            }
            if (CollectionUtils.isNotEmpty(sampleDto.getEffectFileCodeList())) {
                ScmImageBo effectImageBo = new ScmImageBo();
                effectImageBo.setImageBizId(developReviewSampleOrderPo.getDevelopReviewSampleOrderId());
                effectImageBo.setFileCodeList(sampleDto.getEffectFileCodeList());
                effectImageBoList.add(effectImageBo);
            }
            imageBizIdDelList.add(developReviewSampleOrderPo.getDevelopReviewSampleOrderId());

            DevelopSampleOrderPo developSampleOrderPo = developSampleOrderPoMap.get(sampleDto.getDevelopSampleOrderNo());
            //关联到样品单统一设置处理方式
            if (developSampleOrderPo != null
                    && DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderStatus())) {
                developSampleOrderPo.setDevelopSampleMethod(sampleDto.getDevelopSampleMethod());
            }

            //产前样的样品单处理
            if (developSampleOrderPo != null
                    && DevelopReviewOrderType.PRENATAL_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderType())
                    && ScmConstant.PROCESS_SUPPLIER_CODE.equals(developReviewOrderPo.getSupplierCode())
                    && DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderStatus())) {
                developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
                if (DevelopSampleMethod.SALE.equals(developSampleOrderPo.getDevelopSampleMethod())) {
                    saleSampleOrderPoList.add(developSampleOrderPo);
                }
            }

            // 产前样的样品单更新大货价格
            if (developSampleOrderPo != null
                    && DevelopReviewOrderType.PRENATAL_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderType())
                    && ScmConstant.PROCESS_SUPPLIER_CODE.equals(developReviewOrderPo.getSupplierCode())
                    && (DevelopSampleMethod.SALE.equals(sampleDto.getDevelopSampleMethod())
                    || DevelopSampleMethod.SEAL_SAMPLE.equals(sampleDto.getDevelopSampleMethod()))) {
                if (CollectionUtils.isEmpty(sampleDto.getDevelopOrderPriceList())) {
                    throw new BizException("样品单号:{}渠道大货价格不能为空", sampleDto.getDevelopSampleOrderNo());
                }
                // 更新渠道大货价格
                DevelopOrderPriceCreateBo developOrderPriceCreateBo = new DevelopOrderPriceCreateBo();
                developOrderPriceCreateBo.setDevelopOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
                developOrderPriceCreateBo.setDevelopOrderPriceType(DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE);
                List<DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo> developOrderPriceCreateItemBoList = Optional.ofNullable(sampleDto.getDevelopOrderPriceList()).orElse(new ArrayList<>()).stream().map(item -> {
                    DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo developOrderPriceCreateItemBo = new DevelopOrderPriceCreateBo.DevelopOrderPriceCreateItemBo();
                    developOrderPriceCreateItemBo.setChannelId(item.getChannelId());
                    developOrderPriceCreateItemBo.setPrice(item.getPrice());
                    return developOrderPriceCreateItemBo;
                }).collect(Collectors.toList());
                developOrderPriceCreateBo.setDevelopOrderPriceCreateItemBoList(developOrderPriceCreateItemBoList);
                developOrderPriceCreateBoList.add(developOrderPriceCreateBo);
            }

            // 产前样效果图增加到生产资料封样图（封样入库方式，闪售需要等PLM创建新sku后再进行）
            if (DevelopSampleMethod.SEAL_SAMPLE.equals(sampleDto.getDevelopSampleMethod())
                    && this.prenatalIsEffectiveWhere(developReviewOrderPo, developReviewSampleOrderPo)) {

                // 新增生产信息
                if (developSampleOrderPo != null) {
                    addDevelopProduceDataSamplePoList.add(developSampleOrderPo);
                }

                // 增加生产资料封样图
                if (developSampleOrderPo != null && CollectionUtils.isNotEmpty(sampleDto.getEffectFileCodeList())) {
                    ProduceDataSkuSealImageBo produceDataSkuSealImageBo = new ProduceDataSkuSealImageBo();
                    produceDataSkuSealImageBo.setFileCodeList(sampleDto.getEffectFileCodeList());
                    produceDataSkuSealImageBo.setSku(developSampleOrderPo.getSku());
                    produceDataSkuSealImageBo.setSourceOrderNo(sampleDto.getDevelopSampleOrderNo());
                    produceDataSkuSealImageBo.setSupplierCode(developSampleOrderPo.getSupplierCode());
                    produceDataSkuSealImageBoList.add(produceDataSkuSealImageBo);
                }

            }
        }

        if (CollectionUtils.isNotEmpty(imageBizIdDelList)) {
            scmImageBaseService.removeAllImageList(ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL, imageBizIdDelList);
            scmImageBaseService.removeAllImageList(ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT, imageBizIdDelList);
        }
        if (CollectionUtils.isNotEmpty(detailImageBoList)) {
            scmImageBaseService.insertBatchImageBo(detailImageBoList, ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL);
        }
        if (CollectionUtils.isNotEmpty(effectImageBoList)) {
            scmImageBaseService.insertBatchImageBo(effectImageBoList, ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT);
        }

        developReviewSampleOrderDao.updateBatchByIdVersion(developReviewSampleOrderPoList);

        //生产属性数据
        if (CollectionUtils.isNotEmpty(developReviewSampleOrderIdList)) {
            developReviewSampleOrderInfoDao.removeBatchByIds(developReviewSampleOrderIdList);
        }
        if (CollectionUtils.isNotEmpty(insertDevelopReviewSampleOrderInfoPoList)) {
            developReviewSampleOrderInfoDao.insertBatch(insertDevelopReviewSampleOrderInfoPoList);
        }

        //更新样品单
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

        //检验前端数据是否正确
        if (dto.getReviewResult() != null && !dto.getReviewResult().equals(reviewResult)) {
            throw new BizException("前后端数据检验不一致，后端审核结果是:{}", reviewResult.getRemark());
        }
        developReviewOrderPo.setReviewResult(reviewResult);

        // 产前样效果图增加到生产资料封样图写入数据
        produceDataBaseService.addProduceDataSkuSealImage(produceDataSkuSealImageBoList);

        // 更新产前样渠道大货价格
        developChildBaseService.developOrderPriceBatchSave(developOrderPriceCreateBoList);


        // 新增生产信息
        for (DevelopSampleOrderPo developSampleOrderPo : addDevelopProduceDataSamplePoList) {
            DevelopChildOrderPo developChildOrderProduceDataPo = developChildOrderPoMap.get(developSampleOrderPo.getDevelopChildOrderNo());
            Assert.notNull(developChildOrderProduceDataPo, () -> new BizException("样品单:{}找不到关联的开发子单信息，创建生产资料失败，请联系系统管理员！", developSampleOrderPo.getDevelopSampleOrderNo()));
            produceDataBaseService.addDevelopProduceData(developChildOrderProduceDataPo, List.of(developSampleOrderPo), null);
        }


        //如果是产前样采购审版
        if (DevelopReviewOrderType.PRENATAL_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderType())
                && !ScmConstant.PROCESS_SUPPLIER_CODE.equals(developReviewOrderPo.getSupplierCode())) {
            developReviewOrderDao.updateByIdVersion(developReviewOrderPo);
            return;
        }


        //如果是产前样样品单审版进行闪售生成SKU
        if (DevelopReviewOrderType.PRENATAL_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderType())) {
            if (CollectionUtils.isNotEmpty(saleSampleOrderPoList)
                    && DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderStatus())) {
                this.completeDevelopReviewSale(saleSampleOrderPoList);
            }
            developReviewOrderDao.updateByIdVersion(developReviewOrderPo);
            return;
        }

        DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        //验证开发子单
        Assert.notNull(developChildOrderPo, () -> new ParamIllegalException("数据异常开发子单数据已被修改或删除，请联系管理员后重试！"));

        final DevelopChildOrderChangePo developChildOrderChangePo = developChildOrderChangeDao.getOneByDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        if (developChildOrderChangePo == null) {
            throw new BizException("开发子单的数据查询不到，请刷新页面后重试！");
        }
        //针对退样处理方式进行对样品单状态修改
        if (ReviewResult.REVIEW_NO_PASS.equals(dto.getReviewResult())) {
            //异常处理
            developChildBaseService.createDevelopExceptionOrder(developReviewOrderPo.getDevelopParentOrderNo(),
                    developReviewOrderPo.getDevelopChildOrderNo(),
                    developReviewOrderPo.getDevelopPamphletOrderNo(),
                    developReviewOrderPo.getDevelopReviewOrderNo(),
                    developChildOrderPo);
        }

        // 赋值不良数量
        final long poorAmount = dto.getDevelopReviewSampleList().stream()
                .filter(sampleInfoDto -> DevelopSampleMethod.SAMPLE_RETURN.equals(sampleInfoDto.getDevelopSampleMethod())).count();
        developReviewOrderPo.setPoorAmount((int) poorAmount);

        //更新审版单
        developReviewOrderDao.updateByIdVersion(developReviewOrderPo);

        //审版完成更新子单状态
        if (DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderStatus())) {
            //更新开发子单状态
            if (isCreateDevelopPricing) {
                developChildOrderPo.setReviewResult(reviewResult);
                developChildBaseService.updateDevelopChildOrderStatus(developChildOrderPo, DevelopChildOrderStatus.PRICING, true, false);
            }

            developChildOrderChangePo.setReviewUser(developReviewOrderPo.getReviewUser());
            developChildOrderChangePo.setReviewUsername(developReviewOrderPo.getReviewUsername());
            developChildOrderChangePo.setReviewCompletionDate(developReviewOrderPo.getReviewDate());
            developChildOrderChangeDao.updateByIdVersion(developChildOrderChangePo);

        }

        // 开发子单操作内容
        String scenes = "进行审版，审版单" + developReviewOrderPo.getDevelopReviewOrderNo();

        if (isCreateDevelopPricing && DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderStatus())) {
            String developPricingOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_PRICING_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
            DevelopPricingOrderPo developPricingOrderPo = DevelopPricingOrderConverter.reviewOrderToPricingOrderPo(developPricingOrderNo,
                    developChildOrderPo,
                    developReviewOrderPo);
            developPricingOrderDao.insert(developPricingOrderPo);
            scenes = "生成核价单" + developPricingOrderPo.getDevelopPricingOrderNo();
        }
        // 开发子单的日志
        developChildBaseService.createStatusChangeLog(developChildOrderPo, scenes);

    }

    /**
     * 完成审版信息
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/22 15:26
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeDevelopReview(DevelopReviewCompleteDto dto) {
        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNoAndVersion(dto.getDevelopReviewOrderNo(), dto.getVersion());
        Assert.notNull(developReviewOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        developReviewOrderPo.setDevelopReviewOrderStatus(developReviewOrderPo.getDevelopReviewOrderStatus().toCompletedReview());
        if (dto.getReviewResult() == null) {
            throw new ParamIllegalException("请选择审版结果，刷新页面后重试！");
        }
        developReviewOrderPo.setReviewUser(GlobalContext.getUserKey());
        developReviewOrderPo.setReviewUsername(GlobalContext.getUsername());
        developReviewOrderPo.setReviewDate(LocalDateTime.now());

        this.updateDevelopReviewOrderPo(developReviewOrderPo, dto);

    }

    @Transactional(rollbackFor = Exception.class)
    public void startDevelopReview(DevelopReviewNoDto dto) {
        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNo(dto.getDevelopReviewOrderNo());
        Assert.notNull(developReviewOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));
        developReviewOrderPo.setDevelopReviewOrderStatus(developReviewOrderPo.getDevelopReviewOrderStatus().toReviewing());

        developReviewOrderDao.updateByIdVersion(developReviewOrderPo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitDevelopReview(DevelopReviewSubmitDto dto) {
        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNo(dto.getDevelopReviewOrderNo());
        Assert.notNull(developReviewOrderPo, () -> new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！"));

        //如果是产前样处理
        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewSampleOrderDao.getListByReviewNo(dto.getDevelopReviewOrderNo());
        final List<String> sampleOrderNoList = developReviewSampleOrderPoList.stream().map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo).collect(Collectors.toList());
        final List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByNoList(sampleOrderNoList);
        developSampleOrderPoList.forEach(po -> po.setDevelopSampleStatus(po.getDevelopSampleStatus().toAlreadyGetReview()));
        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoList);

        developReviewOrderPo.setReviewUser(dto.getSubmitReviewUser());
        developReviewOrderPo.setReviewUsername(dto.getSubmitReviewUsername());
        developReviewOrderPo.setSubmitReviewUser(GlobalContext.getUserKey());
        developReviewOrderPo.setSubmitReviewUsername(GlobalContext.getUsername());
        developReviewOrderPo.setSubmitReviewDate(LocalDateTime.now());
        developReviewOrderPo.setDevelopReviewOrderStatus(developReviewOrderPo.getDevelopReviewOrderStatus().toWaitReview());

        developReviewOrderDao.updateByIdVersion(developReviewOrderPo);

        // 开发子单日志
        DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        if (null != developChildOrderPo) {
            developChildBaseService.createStatusChangeLog(developChildOrderPo, "提交审版单" + developReviewOrderPo.getDevelopReviewOrderNo());
        }


    }

    /**
     * 获取开发子单状态栏信息
     *
     * @param :
     * @return List<DevelopChildOrderStatusVo>
     * @author ChenWenLong
     * @date 2023/8/24 15:46
     */
    public DevelopChildOrderStatusListVo developChildOrderStatus() {
        return developChildBaseService.developChildOrderStatus(Collections.emptyList());
    }

    /**
     * 开发子单下单采购单
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2024/4/8 09:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitOrder(DevelopChildSubmitOrderItemDto dto) {

        List<String> developChildOrderNoList = List.of(dto.getDevelopChildOrderNo());

        // 校验开发子单是否有产前样单
        final List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderPoList.stream()
                .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));

        // 判断入参开发子单号是否正确
        for (String developChildOrderNo : developChildOrderNoList) {
            DevelopChildOrderPo developChildOrderPo = developChildOrderPoMap.get(developChildOrderNo);
            Assert.notNull(developChildOrderPo, () -> new BizException("找不到对应的开发子单{}信息，开发子单数据错误，请联系系统管理员！", developChildOrderNo));

            if (StringUtils.isNotBlank(developChildOrderPo.getPurchaseParentOrderNo())) {
                throw new BizException("开发子单：{}，已经存在采购母单：{}，不允许重复下单！", developChildOrderPo.getDevelopChildOrderNo(),
                        developChildOrderPo.getPurchaseParentOrderNo());
            }

            // 针对旧数据进行过滤拦截
            if (StringUtils.isNotBlank(developChildOrderPo.getPrenatalSampleOrderNo())
                    && StringUtils.isNotBlank(developChildOrderPo.getFirstSampleOrderNo())) {
                throw new BizException("开发子单：{}，旧数据中已经存在产前样订单：{}和首单订单：{}，不允许重复下单！", developChildOrderPo.getDevelopChildOrderNo(),
                        developChildOrderPo.getPrenatalSampleOrderNo(),
                        developChildOrderPo.getFirstSampleOrderNo());
            }
        }

        // 获取开发子单sku的List
        final List<String> skuCodeList = Optional.ofNullable(dto.getDevelopChildSubmitOrderInfoList()).orElse(new ArrayList<>()
                ).stream()
                .map(DevelopChildSubmitOrderItemDto.DevelopChildSubmitOrderInfoDto::getSku)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        // 查询plm获取spu
        final Map<String, String> spuMap = plmRemoteService.getSpuMapBySkuList(skuCodeList);

        // 判断spuMap的value的spu是否存在不一样，存在不一样就抛出异常
        if (spuMap.values().stream().distinct().count() > 1) {
            throw new BizException("选择的开发子单存在不同的spu，必须是同一个spu，请联系管理员！");
        }

        // 验证采购需求单的参数是否正确
        if (!SkuType.SKU.equals(dto.getSkuType())) {
            throw new BizException("采购需求单的入参错误，参数需求对象只能是：{}，请联系管理员！", SkuType.SKU.getRemark());
        }
        if (!PurchaseDemandType.NORMAL.equals(dto.getPurchaseDemandType())) {
            throw new BizException("采购需求单的入参错误，参数需求类型只能是：{}，请联系管理员！", PurchaseDemandType.NORMAL.getRemark());
        }

        // 创建产前样单
        final DevChildPurchaseBo devReviewPurchaseBo = purchaseRefService.createPurchaseOrderByDevOrder(dto);

        // 赋值采购母单关联到开发子单

        DevelopChildOrderPo updateDevelopChildOrderPo = new DevelopChildOrderPo();
        String developChildOrderNo = devReviewPurchaseBo.getDevelopChildOrderNo();
        DevelopChildOrderPo developChildOrderPo = developChildOrderPoMap.get(developChildOrderNo);
        Assert.notNull(developChildOrderPo, () -> new BizException("找不到对应的开发子单{}信息，开发子单数据错误，请联系系统管理员！", developChildOrderNo));
        updateDevelopChildOrderPo.setDevelopChildOrderId(developChildOrderPo.getDevelopChildOrderId());
        updateDevelopChildOrderPo.setPurchaseParentOrderNo(devReviewPurchaseBo.getPurchaseParentOrderNo());

        developChildOrderDao.updateById(updateDevelopChildOrderPo);


    }

    @Transactional(rollbackFor = Exception.class)
    public void createDevelopReview(DevelopReviewCreateDto dto) {
        String developReviewOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_REVIEW_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
        if (DevelopReviewRelated.PURCHASE.equals(dto.getDevelopReviewRelated())) {
            Assert.notBlank(dto.getPrenatalSampleOrderNo(), () -> new ParamIllegalException("产前样采购单号不能为空"));
            if (dto.getDevelopReviewCreateItemList().size() != 1) {
                throw new BizException("创建审版提交数据错误，请联系系统管理员！");
            }
            final PurchaseChildOrderPo purchaseChildOrderPo = purchaseChildOrderDao.getOneByChildOrderNo(dto.getPrenatalSampleOrderNo());
            if (null == purchaseChildOrderPo) {
                throw new BizException("产前样单号:{}，查找不到对应的采购单!", dto.getPrenatalSampleOrderNo());
            }
            String developSampleOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_SAMPLE_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
            final DevelopReviewCreateItemDto developReviewCreateItemDto = dto.getDevelopReviewCreateItemList().get(0);
            developReviewCreateItemDto.setDevelopSampleOrderNo(developSampleOrderNo);
            dto.getDevelopReviewSampleInfoList().forEach(itemInfoDto -> itemInfoDto.setDevelopSampleOrderNo(developSampleOrderNo));

        }
        if (DevelopReviewRelated.DEVELOP_SAMPLE.equals(dto.getDevelopReviewRelated())) {
            dto.getDevelopReviewCreateItemList().forEach(itemDto -> Assert.notBlank(itemDto.getDevelopSampleOrderNo(),
                    () -> new ParamIllegalException("产前样的样品单号不能为空")));
            List<String> developSampleOrderNoList = dto.getDevelopReviewCreateItemList().stream()
                    .map(DevelopReviewCreateItemDto::getDevelopSampleOrderNo)
                    .distinct()
                    .collect(Collectors.toList());
            List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoExist = developReviewSampleOrderDao.getListByDevelopSampleOrderNoList(developSampleOrderNoList);
            if (CollectionUtils.isNotEmpty(developReviewSampleOrderPoExist)) {
                String developSampleOrderNoJoining = developReviewSampleOrderPoExist.stream()
                        .map(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo)
                        .distinct()
                        .collect(Collectors.joining(","));
                throw new ParamIllegalException("{}样品单存在已创建的审版单，请刷新页面后再提交！", developSampleOrderNoJoining);
            }
            //校验供应商
            if (StringUtils.isBlank(dto.getSupplierCode())) {
                throw new ParamIllegalException("产前样的样品单类型需要供应商，请刷新页面后再提交！");
            }
        }

        List<String> developSampleOrderNoList = Optional.ofNullable(dto.getDevelopReviewSampleInfoList())
                .orElse(new ArrayList<>())
                .stream()
                .map(DevelopReviewSampleInfoDto::getDevelopSampleOrderNo)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());

        // 获取样品单Map
        final Map<String, DevelopSampleOrderPo> developSampleOrderPoMap = developSampleOrderDao.getMapByNoList(developSampleOrderNoList);
        final SupplierPo supplierPo = supplierBaseService.getSupplierByCode(dto.getSupplierCode());
        final DevelopReviewAndSampleBo developReviewAndSampleBo = DevelopChildConverter.reviewCreateDtoToPo(dto,
                developReviewOrderNo,
                supplierPo,
                developSampleOrderPoMap);

        final DevelopReviewOrderPo developReviewOrderPo = developReviewAndSampleBo.getDevelopReviewOrderPo();
        developReviewOrderPo.setSpu(dto.getSpu());
        developReviewOrderDao.insert(developReviewOrderPo);

        final List<DevelopReviewSampleOrderPo> developReviewSampleOrderPoList = developReviewAndSampleBo.getDevelopReviewSampleOrderPoList();
        developReviewSampleOrderDao.insertBatch(developReviewSampleOrderPoList);

        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewAndSampleBo.getDevelopReviewSampleOrderInfoPoList();
        developReviewSampleOrderInfoDao.insertBatch(developReviewSampleOrderInfoPoList);

        final Map<String, DevelopReviewSampleOrderPo> developSampleNoPoMap = developReviewSampleOrderPoList.stream()
                .collect(Collectors.toMap(DevelopReviewSampleOrderPo::getDevelopSampleOrderNo, Function.identity()));
        List<ScmImageBo> effectScmImageBoList = new ArrayList<>();
        List<ScmImageBo> detailScmImageBoList = new ArrayList<>();
        dto.getDevelopReviewCreateItemList().forEach(developReviewCreateItemDto -> {
            final DevelopReviewSampleOrderPo developReviewSampleOrderPo = developSampleNoPoMap.get(developReviewCreateItemDto.getDevelopSampleOrderNo());
            if (CollectionUtils.isNotEmpty(developReviewCreateItemDto.getEffectFileCodeList())) {
                final ScmImageBo effectScmImageBo = new ScmImageBo();
                effectScmImageBo.setFileCodeList(developReviewCreateItemDto.getEffectFileCodeList());
                effectScmImageBo.setImageBizId(developReviewSampleOrderPo.getDevelopReviewSampleOrderId());
                effectScmImageBoList.add(effectScmImageBo);
            }
            if (CollectionUtils.isNotEmpty(developReviewCreateItemDto.getDetailFileCodeList())) {
                final ScmImageBo detailScmImageBo = new ScmImageBo();
                detailScmImageBo.setFileCodeList(developReviewCreateItemDto.getDetailFileCodeList());
                detailScmImageBo.setImageBizId(developReviewSampleOrderPo.getDevelopReviewSampleOrderId());
                detailScmImageBoList.add(detailScmImageBo);
            }
        });

        if (CollectionUtils.isNotEmpty(effectScmImageBoList)) {
            scmImageBaseService.insertBatchImageBo(effectScmImageBoList, ImageBizType.DEVELOP_REVIEW_SAMPLE_EFFECT);
        }
        if (CollectionUtils.isNotEmpty(detailScmImageBoList)) {
            scmImageBaseService.insertBatchImageBo(detailScmImageBoList, ImageBizType.DEVELOP_REVIEW_SAMPLE_DETAIL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createReviewUnusualReport(DevelopReviewUnusualDto dto) {
        final DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderDao.getOneBySampleOrderNo(dto.getDevelopSampleOrderNo());
        if (null == developReviewSampleOrderPo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试");
        }
        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());
        if (null == developReviewOrderPo) {
            throw new ParamIllegalException("数据已被更新,请刷新页面后重试");
        }

        DevelopReviewOrderUnusualPo developReviewOrderUnusualPo = developReviewOrderUnusualDao.getOneByDevSampleNo(dto.getDevelopSampleOrderNo());
        if (null == developReviewOrderUnusualPo) {
            developReviewOrderUnusualPo = new DevelopReviewOrderUnusualPo();
            developReviewOrderUnusualPo.setDevelopReviewOrderUnusualNo(idGenerateService.getConfuseCode(developReviewOrderPo.getPlatform(), TimeType.CN_DAY_YYYY, ConfuseLength.L_4));
            developReviewOrderUnusualPo.setDevelopReviewOrderNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());
            developReviewOrderUnusualPo.setDevelopSampleOrderNo(developReviewSampleOrderPo.getDevelopSampleOrderNo());
        }
        DevelopChildConverter.reviewUnusualDtoToPo(dto, developReviewOrderUnusualPo);

        developReviewOrderUnusualDao.insertOrUpdate(developReviewOrderUnusualPo);

        scmImageBaseService.removeAllImage(ImageBizType.DEVELOP_REVIEW_SAMPLE_EXCEPTION, developReviewOrderUnusualPo.getDevelopReviewOrderUnusualId());
        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.DEVELOP_REVIEW_SAMPLE_EXCEPTION, developReviewOrderUnusualPo.getDevelopReviewOrderUnusualId());
    }

    public DevelopReviewUnusualVo reviewUnusualReportDetail(DevelopSampleNoDto dto) {
        final DevelopReviewSampleOrderPo developReviewSampleOrderPo = developReviewSampleOrderDao.getOneBySampleOrderNo(dto.getDevelopSampleOrderNo());
        if (null == developReviewSampleOrderPo) {
            throw new BizException("审版样品单:{}不存在，创建异常报告失败，请联系系统管理员！", dto.getDevelopSampleOrderNo());
        }

        final DevelopReviewOrderUnusualPo developReviewOrderUnusualPo = developReviewOrderUnusualDao.getOneByDevSampleNo(dto.getDevelopSampleOrderNo());


        final DevelopReviewOrderPo developReviewOrderPo = developReviewOrderDao.getOneByNo(developReviewSampleOrderPo.getDevelopReviewOrderNo());
        if (null == developReviewOrderPo) {
            throw new BizException("审版单:{}不存在，创建异常报告失败，请联系系统管理员！", developReviewSampleOrderPo.getDevelopReviewOrderNo());
        }
        final List<DevelopReviewSampleOrderInfoPo> developReviewSampleOrderInfoPoList = developReviewSampleOrderInfoDao.getListByDevelopSampleOrderNo(dto.getDevelopSampleOrderNo());

        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developReviewOrderPo.getDevelopChildOrderNo());
        DevelopChildOrderChangePo developChildOrderChangePo = null;
        List<DevelopChildOrderAttrPo> developChildOrderAttrPoList = null;
        if (null != developChildOrderPo) {
            developChildOrderChangePo = developChildOrderChangeDao.getOneByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            developChildOrderAttrPoList = developChildOrderAttrDao.getListByDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
        }

        final DevelopReviewUnusualVo developReviewUnusualVo = DevelopChildConverter.reviewUnusualPoToVo(developReviewOrderPo,
                developReviewOrderUnusualPo,
                developChildOrderPo,
                developChildOrderChangePo,
                developChildOrderAttrPoList,
                developReviewSampleOrderInfoPoList,
                developReviewSampleOrderPo);
        if (null != developReviewOrderUnusualPo) {
            final List<String> fileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.DEVELOP_REVIEW_SAMPLE_EXCEPTION, Collections.singletonList(developReviewOrderUnusualPo.getDevelopReviewOrderUnusualId()));
            developReviewUnusualVo.setFileCodeList(fileCodeList);
        }

        return developReviewUnusualVo;
    }

    /**
     * 通过核价单查询样品单
     *
     * @param dto:
     * @return List<DevelopSampleOrderListVo>
     * @author ChenWenLong
     * @date 2023/8/29 09:58
     */
    public List<DevelopSampleOrderListVo> getDevelopSampleOrderList(DevelopPricingOrderNoListDto dto) {
        List<DevelopSampleOrderListVo> list = new ArrayList<>();
        List<DevelopPricingOrderPo> developPricingOrderPoList = developPricingOrderDao.getByDevelopPricingOrderNoList(dto.getDevelopPricingOrderNoList());
        if (CollectionUtils.isEmpty(developPricingOrderPoList)) {
            throw new ParamIllegalException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<String> developChildOrderNoList = developPricingOrderPoList.stream().map(DevelopPricingOrderPo::getDevelopChildOrderNo).distinct().collect(Collectors.toList());
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList).stream().collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));


        List<String> developPamphletOrderNoList = developPricingOrderPoList.stream().map(DevelopPricingOrderPo::getDevelopPamphletOrderNo).collect(Collectors.toList());

        //通过版单查询样品单
        List<DevelopPamphletOrderPo> developPamphletOrderPoList = developPamphletOrderDao.getListByNoList(developPamphletOrderNoList);
        Map<String, List<DevelopSampleOrderPo>> developSampleOrderPoMap = developSampleOrderDao.getListByDevelopPamphletOrderNoList(developPamphletOrderPoList.stream().map(DevelopPamphletOrderPo::getDevelopPamphletOrderNo).collect(Collectors.toList())).stream().filter(w -> !DevelopSampleMethod.SAMPLE_RETURN.equals(w.getDevelopSampleMethod())).collect(Collectors.groupingBy(DevelopSampleOrderPo::getDevelopPamphletOrderNo));

        for (DevelopPricingOrderPo developPricingOrderPo : developPricingOrderPoList) {
            DevelopSampleOrderListVo developSampleOrderListVo = new DevelopSampleOrderListVo();
            developSampleOrderListVo.setDevelopPricingOrderNo(developPricingOrderPo.getDevelopPricingOrderNo());
            developSampleOrderListVo.setDevelopChildOrderNo(developPricingOrderPo.getDevelopChildOrderNo());
            List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderPoMap.get(developPricingOrderPo.getDevelopPamphletOrderNo());
            if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
                developSampleOrderListVo.setDevelopSampleOrderItemList(DevelopSampleOrderConverter.INSTANCE.convertItem(developSampleOrderPoList));
            }
            DevelopChildOrderPo developChildOrderPo = developChildOrderPoMap.get(developPricingOrderPo.getDevelopChildOrderNo());
            if (developChildOrderPo != null) {
                developSampleOrderListVo.setIsSample(developChildOrderPo.getIsSample());
            }
            list.add(developSampleOrderListVo);
        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void getReviewExportList(DevelopReviewSearchDto dto) {
        consistencySendMqService.execSendMq(ScmExportHandler.class, new FileOperateMessageDto<>(GlobalContext.getUserKey(), GlobalContext.getUsername(), FileOperateBizType.SCM_DEV_REVIEW_EXPORT.getCode(), dto));
    }

    /**
     * 编辑要求打版完成时间
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/9/19 13:36
     */
    @Transactional(rollbackFor = Exception.class)
    public void editExpectedOnShelvesDate(DevelopExpectedOnShelvesDate dto) {

        final DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getByIdVersion(dto.getDevelopChildOrderId(), dto.getVersion());
        if (null == developChildOrderPo) {
            throw new ParamIllegalException("开发单数据已被修改或删除，请刷新页面后重试！");
        }

        developChildOrderPo.getDevelopChildOrderStatus().toReview();
        final DevelopPamphletOrderPo developPamphletOrderPo = developPamphletOrderDao.getByIdVersion(dto.getDevelopPamphletMsgVo().getDevelopPamphletOrderId(), dto.getDevelopPamphletMsgVo().getVersion());
        if (null == developPamphletOrderPo) {
            throw new ParamIllegalException("版单数据已被修改或删除，请刷新页面后重试！");
        }

        developPamphletOrderPo.getDevelopPamphletOrderStatus().verifyToBeConfirmedPamphlet();
        developPamphletOrderPo.setExpectedOnShelvesDate(dto.getDevelopPamphletMsgVo().getExpectedOnShelvesDate());
        developPamphletOrderDao.updateByIdVersion(developPamphletOrderPo);
    }

    /**
     * 增加产前样的样品单
     *
     * @param dto:
     * @return void
     * @author ChenWenLong
     * @date 2023/10/20 18:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPrenatalSampleOrder(DevelopAddPrenatalSampleOrderDto dto) {

        List<String> developChildOrderNoList = dto.getDevelopAddPrenatalSampleOrderItemList().stream()
                .map(DevelopAddPrenatalSampleOrderDto.DevelopAddPrenatalSampleOrderItem::getDevelopChildOrderNo)
                .distinct().collect(Collectors.toList());

        final List<DevelopChildOrderPo> developChildOrderPoList = developChildOrderDao.getListByDevelopChildOrderNoList(developChildOrderNoList);
        if (CollectionUtils.isEmpty(developChildOrderPoList)
                || developChildOrderNoList.size() != developChildOrderPoList.size()) {
            throw new ParamIllegalException("开发单数据已被修改或删除，请刷新页面后重试！");
        }
        Map<String, DevelopChildOrderPo> developChildOrderPoMap = developChildOrderPoList.stream()
                .collect(Collectors.toMap(DevelopChildOrderPo::getDevelopChildOrderNo, Function.identity()));

        //检验数据
        for (DevelopChildOrderPo developChildOrderPo : developChildOrderPoList) {
            if (!DevelopChildOrderStatus.COMPLETE.equals(developChildOrderPo.getDevelopChildOrderStatus())) {
                throw new ParamIllegalException("开发子单:{}，处于状态:{}，才能进行提交操作！", developChildOrderPo.getDevelopChildOrderNo(), DevelopChildOrderStatus.COMPLETE.getRemark());
            }
            if (!ScmConstant.PROCESS_SUPPLIER_CODE.equals(developChildOrderPo.getSupplierCode())) {
                throw new ParamIllegalException("存在非产前样的开发子单:{}，禁止提交操作！", developChildOrderPo.getDevelopChildOrderNo());
            }
        }
        List<DevelopSampleOrderPo> developSampleOrderPoList = developSampleOrderDao.getListByChildNoListOrType(developChildOrderNoList, DevelopSampleType.PRENATAL_SAMPLE);
        if (CollectionUtils.isNotEmpty(developSampleOrderPoList)) {
            throw new BizException("开发子单存在已创建产前样的样品单了，禁止进行操作！");
        }

        //创建样品单
        List<DevelopSampleOrderPo> insertDevelopSampleOrderPoList = new ArrayList<>();

        for (DevelopAddPrenatalSampleOrderDto.DevelopAddPrenatalSampleOrderItem item : dto.getDevelopAddPrenatalSampleOrderItemList()) {
            for (int i = 0; i < item.getProcessNum(); i++) {
                DevelopChildOrderPo developChildOrderByNoPo = developChildOrderPoMap.get(item.getDevelopChildOrderNo());
                DevelopSampleOrderPo developSampleOrderPo = new DevelopSampleOrderPo();
                String developSampleOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEVELOP_SAMPLE_ORDER_NO, TimeType.CN_DAY, ConfuseLength.L_4);
                developSampleOrderPo.setDevelopSampleOrderNo(developSampleOrderNo);
                developSampleOrderPo.setDevelopParentOrderNo(developChildOrderByNoPo.getDevelopParentOrderNo());
                developSampleOrderPo.setDevelopChildOrderNo(developChildOrderByNoPo.getDevelopChildOrderNo());
                developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_SEND_SAMPLES);
                developSampleOrderPo.setPlatform(developChildOrderByNoPo.getPlatform());
                developSampleOrderPo.setSupplierCode(developChildOrderByNoPo.getSupplierCode());
                developSampleOrderPo.setSupplierName(developChildOrderByNoPo.getSupplierName());
                developSampleOrderPo.setDevelopSampleType(DevelopSampleType.PRENATAL_SAMPLE);
                developSampleOrderPo.setSpu(developChildOrderByNoPo.getSpu());
                developSampleOrderPo.setSku(developChildOrderByNoPo.getSku());
                insertDevelopSampleOrderPoList.add(developSampleOrderPo);
            }
        }

        developSampleOrderDao.insertBatch(insertDevelopSampleOrderPoList);

    }


    /**
     * 审版完成时进行闪售
     *
     * @param developSampleOrderPoList:
     * @return void
     * @author ChenWenLong
     * @date 2023/10/25 14:12
     */
    public void completeDevelopReviewSale(List<DevelopSampleOrderPo> developSampleOrderPoList) {
        log.info("审版完成进行创建新SPU和SKU。POList={}", JacksonUtil.parse2Str(developSampleOrderPoList));
        String developChildOrderNoPrenatal = developSampleOrderPoList.get(0).getDevelopChildOrderNo();
        developSampleOrderPoList.forEach(po -> {
            if (!developChildOrderNoPrenatal.equals(po.getDevelopChildOrderNo())) {
                throw new ParamIllegalException("选择的开发样品单号:{}不属于同一开发子单，请重新选择！", po.getDevelopSampleOrderNo());
            }
        });
        DevelopChildOrderPo developChildOrderPo = developChildOrderDao.getOneByDevelopChildOrderNo(developChildOrderNoPrenatal);
        Assert.notNull(developChildOrderPo, () -> new ParamIllegalException("样品单数据异常开发子单数据已被修改或删除，请联系管理员后重试！"));
        for (DevelopSampleOrderPo developSampleOrderPo : developSampleOrderPoList) {
            //推送PLM
            final DevelopCompleteInfoMqDto developCompleteInfoMqDto = new DevelopCompleteInfoMqDto();
            developCompleteInfoMqDto.setKey(developSampleOrderPo.getDevelopSampleOrderNo());
            developCompleteInfoMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            developCompleteInfoMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            developCompleteInfoMqDto.setDevelopSampleOrderNo(developSampleOrderPo.getDevelopSampleOrderNo());
            developCompleteInfoMqDto.setDevelopSampleDestination(DevelopSampleDestination.SALE);
            developCompleteInfoMqDto.setUserKey(GlobalContext.getUserKey());
            developCompleteInfoMqDto.setUsername(GlobalContext.getUsername());
            developCompleteInfoMqDto.setPlatCode(developChildOrderPo.getPlatform());
            developCompleteInfoMqDto.setCategoryId(developChildOrderPo.getCategoryId());
            String title = developChildOrderPo.getDevelopParentOrderNo() + developChildOrderPo.getDevelopChildOrderNo() + developSampleOrderPo.getDevelopSampleOrderNo() + developSampleOrderPo.getDevelopSampleMethod().getRemark();
            developCompleteInfoMqDto.setTitleCn(title);
            developCompleteInfoMqDto.setDescription(title);
            consistencySendMqService.execSendMq(DevelopCompleteInfoHandler.class, developCompleteInfoMqDto);
        }
    }

    /**
     * 非封样入库的逻辑
     *
     * @param sampleInfoDto:
     * @param developChildOrderPo:
     * @param developPamphletOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2023/11/23 16:44
     */
    private void notSealSampleUpdate(DevelopSampleCompleteInfoDto sampleInfoDto,
                                     DevelopChildOrderPo developChildOrderPo,
                                     DevelopPamphletOrderPo developPamphletOrderPo,
                                     boolean isExistSpuAndSku) {
        if (isExistSpuAndSku) {
            List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoListAndTypeList(List.of(developChildOrderPo.getDevelopChildOrderNo()), List.of(DevelopOrderPriceType.DEVELOP_PURCHASE_PRICE));
            if (CollectionUtils.isEmpty(developOrderPricePoList)) {
                throw new BizException("非封样入库时创建商品调价获取不到对应的开发子单：{}渠道大货价格，请联系系统管理员！", developChildOrderPo.getDevelopChildOrderNo());
            }
            //推送PLM
            final DevelopSampleCompleteNoticeMqDto noticeMqDto = new DevelopSampleCompleteNoticeMqDto();
            noticeMqDto.setUserKey(GlobalContext.getUserKey());
            noticeMqDto.setUsername(GlobalContext.getUsername());
            noticeMqDto.setKey(developChildOrderPo.getDevelopParentOrderNo());
            noticeMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            noticeMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            noticeMqDto.setSpu(sampleInfoDto.getSpu());
            noticeMqDto.setSku(sampleInfoDto.getSku());
            consistencySendMqService.execSendMq(DevelopCompleteNoticeHandler.class, noticeMqDto);
            //新增生产信息
            produceDataBaseService.addDevelopProduceData(developChildOrderPo, new ArrayList<>(), developPamphletOrderPo);
            // 更新商品采购价格
            ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
            produceDataUpdatePurchasePriceBo.setSku(developChildOrderPo.getSku());
            produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developOrderPricePoList.get(0).getPrice());
            produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(List.of(produceDataUpdatePurchasePriceBo));

            // 更新商品调价的价格
            List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();
            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
            goodsPriceAddBo.setSku(developChildOrderPo.getSku());
            goodsPriceAddBo.setSupplierCode(developChildOrderPo.getSupplierCode());
            goodsPriceAddBo.setChannelId(developOrderPricePoList.get(0).getChannelId());
            goodsPriceAddBo.setChannelPrice(developOrderPricePoList.get(0).getPrice());
            goodsPriceAddBoList.add(goodsPriceAddBo);
            // 更新商品调价的价格
            goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);
        } else {
            //推送PLM的MQ创建对应的SPU和SKU
            final DevelopCompleteInfoMqDto developCompleteInfoMqDto = new DevelopCompleteInfoMqDto();
            developCompleteInfoMqDto.setKey(developChildOrderPo.getDevelopParentOrderNo());
            developCompleteInfoMqDto.setDevelopParentOrderNo(developChildOrderPo.getDevelopParentOrderNo());
            developCompleteInfoMqDto.setDevelopChildOrderNo(developChildOrderPo.getDevelopChildOrderNo());
            developCompleteInfoMqDto.setDevelopSampleDestination(DevelopSampleDestination.SEAL_SAMPLE);
            developCompleteInfoMqDto.setUserKey(GlobalContext.getUserKey());
            developCompleteInfoMqDto.setUsername(GlobalContext.getUsername());
            developCompleteInfoMqDto.setPlatCode(developChildOrderPo.getPlatform());
            developCompleteInfoMqDto.setCategoryId(developChildOrderPo.getCategoryId());
            String title = developChildOrderPo.getDevelopParentOrderNo() + developChildOrderPo.getDevelopChildOrderNo();
            developCompleteInfoMqDto.setTitleCn(title);
            developCompleteInfoMqDto.setDescription(title);
            consistencySendMqService.execSendMq(DevelopCompleteInfoHandler.class, developCompleteInfoMqDto);
        }


    }


    /**
     * 处理齐备信息返回-闪售
     *
     * @param message:
     * @param developChildOrderPo:
     * @param developSampleOrderPo:
     * @param developPamphletOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/22 16:11
     */
    private void developCompleteInfoReturnSeal(DevelopCompleteInfoReturnMqDto message,
                                               DevelopChildOrderPo developChildOrderPo,
                                               DevelopSampleOrderPo developSampleOrderPo,
                                               DevelopPamphletOrderPo developPamphletOrderPo) {

        //请求wms获取批次码
        SkuBatchCreate4defectNoListDto skuBatchCreate4defectNoListDto = new SkuBatchCreate4defectNoListDto();
        List<SkuBatchCreate4defectNoListDto.DefectHandlingDto> wmsSkuBatchDtoList = new ArrayList<>();

        SkuBatchCreate4defectNoListDto.DefectHandlingDto developSampleDto = new SkuBatchCreate4defectNoListDto.DefectHandlingDto();
        developSampleDto.setDefectHandlingNo(developSampleOrderPo.getDevelopSampleOrderNo());
        developSampleDto.setSupplierCode(developSampleOrderPo.getSupplierCode());
        developSampleDto.setSupplierName(developSampleOrderPo.getSupplierName());
        developSampleDto.setSkuCodeList(List.of(message.getSkuCode()));
        wmsSkuBatchDtoList.add(developSampleDto);

        List<SkuBatchCreate4defectNoListDto.DefectHandlingDto> defectHandlingDistinctDtoList = wmsSkuBatchDtoList.stream()
                .distinct()
                .collect(Collectors.toList());

        skuBatchCreate4defectNoListDto.setDefectHandlingNoList(defectHandlingDistinctDtoList);
        Map<String, List<SkuVo>> skuBatchCodeChildMap = wmsRemoteService.createBatchCode4DefectHandlingList(skuBatchCreate4defectNoListDto);

        // 获取样品单默认渠道大货价格
        Map<String, DevelopOrderPricePo> developOrderPriceDefaultPriceMap = developChildBaseService.getDevelopOrderPriceDefaultPo(List.of(developSampleOrderPo.getDevelopSampleOrderNo()),
                DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE);
        DevelopOrderPricePo developOrderPricePo = developOrderPriceDefaultPriceMap.get(developSampleOrderPo.getDevelopSampleOrderNo());
        if (null == developOrderPricePo) {
            throw new BizException("闪售样品单在PLM创建SKU回调时数据错误，获取不到样品单号:{}的默认渠道大货价格，请联系管理员！", developSampleOrderPo.getDevelopSampleOrderNo());
        }

        // 获取样品单渠道价格
        List<DevelopOrderPricePo> developOrderPricePoList = developOrderPriceDao.getListByNoAndType(developSampleOrderPo.getDevelopSampleOrderNo(), DevelopOrderPriceType.SAMPLE_PURCHASE_PRICE);

        // 更新WMS的批次码单价
        List<DevelopChildBatchCodeCostPriceBo> skuBatchCodePriceBoList = new ArrayList<>();

        // 更新商品调价的价格
        List<GoodsPriceAddBo> goodsPriceAddBoList = new ArrayList<>();

        // 封样入库处理逻辑
        developSampleOrderPo.setSpu(message.getSpuCode());
        developSampleOrderPo.setSku(message.getSkuCode());

        List<SkuVo> skuVoList = skuBatchCodeChildMap.get(developSampleOrderPo.getDevelopSampleOrderNo());
        if (CollectionUtils.isEmpty(skuVoList)) {
            throw new BizException("获取不到对应的批次码，请联系系统管理员！");
        }
        skuVoList.stream().findFirst().ifPresent(skuVo -> {
            developSampleOrderPo.setSkuBatchCode(skuVo.getBatchCode());
        });

        // 更新WMS的批次码单价
        DevelopChildBatchCodeCostPriceBo developChildBatchCodeCostPriceBo = new DevelopChildBatchCodeCostPriceBo();
        developChildBatchCodeCostPriceBo.setSkuBatchCode(developSampleOrderPo.getSkuBatchCode());
        developChildBatchCodeCostPriceBo.setPrice(developOrderPricePo.getPrice());
        developChildBatchCodeCostPriceBo.setSku(developSampleOrderPo.getSku());
        skuBatchCodePriceBoList.add(developChildBatchCodeCostPriceBo);

        // 增加商品调价信息
        for (DevelopOrderPricePo orderPricePo : developOrderPricePoList) {
            GoodsPriceAddBo goodsPriceAddBo = new GoodsPriceAddBo();
            goodsPriceAddBo.setSku(developSampleOrderPo.getSku());
            goodsPriceAddBo.setSupplierCode(developSampleOrderPo.getSupplierCode());
            goodsPriceAddBo.setChannelId(orderPricePo.getChannelId());
            goodsPriceAddBo.setChannelPrice(orderPricePo.getPrice());
            goodsPriceAddBoList.add(goodsPriceAddBo);
        }


        developSampleOrderDao.updateByIdVersion(developSampleOrderPo);

        // 新增生产信息
        produceDataBaseService.addDevelopProduceData(developChildOrderPo, List.of(developSampleOrderPo), developPamphletOrderPo);
        // 更新商品采购价格
        ProduceDataUpdatePurchasePriceBo produceDataUpdatePurchasePriceBo = new ProduceDataUpdatePurchasePriceBo();
        produceDataUpdatePurchasePriceBo.setSku(developSampleOrderPo.getSku());
        produceDataUpdatePurchasePriceBo.setGoodsPurchasePrice(developOrderPricePo.getPrice());
        produceDataBaseService.updateGoodsPurchasePriceBySkuBatch(List.of(produceDataUpdatePurchasePriceBo));

        // 更新批次码单价
        if (CollectionUtils.isNotEmpty(skuBatchCodePriceBoList)) {
            UpdateBatchCodePriceDto updateBatchCodePriceDto = new UpdateBatchCodePriceDto();
            updateBatchCodePriceDto.setBatchCodePriceList(developSampleOrderBaseService.calculatedSkuBatchSamplePrice(skuBatchCodePriceBoList));
            wmsMqBaseService.execSendUpdateBatchCodePriceMq(updateBatchCodePriceDto);
        }

        // 常规样品单闪售处理逻辑创建WMS的入库单MQ
        if (DevelopSampleType.NORMAL.equals(developSampleOrderPo.getDevelopSampleType())) {
            developSampleOrderBaseService.createReceiveOrder(List.of(developSampleOrderPo));
        }

        // 更新商品调价的价格
        goodsPriceBaseService.addGoodsPrice(goodsPriceAddBoList);

    }

    /**
     * 产前样样品单闪售
     *
     * @param message:
     * @param developChildOrderPo:
     * @param developSampleOrderPo:
     * @param developPamphletOrderPo:
     * @author ChenWenLong
     * @date 2024/11/1 10:40
     */
    private void developCompleteInfoReturnSealPrenatal(DevelopCompleteInfoReturnMqDto message,
                                                       DevelopChildOrderPo developChildOrderPo,
                                                       DevelopSampleOrderPo developSampleOrderPo,
                                                       DevelopPamphletOrderPo developPamphletOrderPo) {


        // 封样入库处理逻辑
        developSampleOrderPo.setSpu(message.getSpuCode());
        developSampleOrderPo.setSku(message.getSkuCode());
        developSampleOrderDao.updateByIdVersion(developSampleOrderPo);

        // 新增生产信息
        produceDataBaseService.addDevelopProduceData(developChildOrderPo, List.of(developSampleOrderPo), developPamphletOrderPo);


    }

    /**
     * 处理齐备信息返回-封样入库
     *
     * @param message:
     * @param developChildOrderPo:
     * @param developSampleOrderPo:
     * @param developPamphletOrderPo:
     * @return void
     * @author ChenWenLong
     * @date 2024/3/22 16:10
     */
    private void developCompleteInfoReturnSealSample(DevelopCompleteInfoReturnMqDto message,
                                                     DevelopChildOrderPo developChildOrderPo,
                                                     DevelopSampleOrderPo developSampleOrderPo,
                                                     DevelopPamphletOrderPo developPamphletOrderPo) {
        //取封样入库的样品单
        List<DevelopSampleOrderPo> sealDevSampleOrderPoList = developSampleOrderDao.getListByChildNoAndMethodAndType(developChildOrderPo.getDevelopChildOrderNo(),
                DevelopSampleMethod.SEAL_SAMPLE, DevelopSampleType.NORMAL);

        // 封样入库处理逻辑
        developSampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
        updateAllDevChildSpuByParentNo(message.getSpuCode(), message.getSkuCode(), developChildOrderPo, message.getGoodsPlatSpuId());

        List<DevelopSampleOrderPo> developSampleOrderPoChildList = sealDevSampleOrderPoList.stream()
                .filter(w -> !w.getDevelopSampleOrderNo().equals(developSampleOrderPo.getDevelopSampleOrderNo()))
                .collect(Collectors.toList());

        // 更新其他封样入库的样品单的状态
        for (DevelopSampleOrderPo sampleOrderPo : developSampleOrderPoChildList) {
            sampleOrderPo.setSpu(message.getSpuCode());
            sampleOrderPo.setSku(message.getSkuCode());
            sampleOrderPo.setDevelopSampleStatus(DevelopSampleStatus.WAIT_HANDLE);
        }

        developSampleOrderDao.updateBatchByIdVersion(developSampleOrderPoChildList);
        developChildOrderDao.updateByIdVersion(developChildOrderPo);

        developSampleOrderPo.setSpu(message.getSpuCode());
        developSampleOrderPo.setSku(message.getSkuCode());


        developSampleOrderDao.updateByIdVersion(developSampleOrderPo);

        // 新增生产信息
        produceDataBaseService.addDevelopProduceData(developChildOrderPo, List.of(developSampleOrderPo), developPamphletOrderPo);


    }

    /**
     * 下单获取SPU的全部SKU列表
     *
     * @param dto:
     * @return List<DevelopPlaceOrderVo>
     * @author ChenWenLong
     * @date 2024/8/26 18:35
     */
    public List<DevelopPlaceOrderVo> getSpuAllSkuList(DevelopPlaceOrderDto dto) {
        List<PlmSkuVo> plmSkuVos = plmRemoteService.getSkuBySpuCode(List.of(dto.getSpu()));
        if (CollectionUtils.isEmpty(plmSkuVos)) {
            return Collections.emptyList();
        }
        return plmSkuVos.stream().map(plmSkuVo -> {
            DevelopPlaceOrderVo developPlaceOrderVo = new DevelopPlaceOrderVo();
            developPlaceOrderVo.setSku(plmSkuVo.getSkuCode());
            developPlaceOrderVo.setSkuEncode(plmSkuVo.getSkuEncode());
            return developPlaceOrderVo;
        }).collect(Collectors.toList());

    }

    /**
     * 验证产前样的效果图/生产资料是否能写入sku的封样图基本条件（调用时需要区别入库还是闪售）
     *
     * @param developReviewOrderPo:必填
     * @param developReviewSampleOrderPo:非必填
     * @return Boolean
     * @author ChenWenLong
     * @date 2024/11/4 10:39
     */
    public Boolean prenatalIsEffectiveWhere(@NotNull DevelopReviewOrderPo developReviewOrderPo,
                                            DevelopReviewSampleOrderPo developReviewSampleOrderPo) {
        return DevelopReviewOrderType.PRENATAL_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderType())
                && developReviewSampleOrderPo != null
                && DevelopReviewOrderStatus.COMPLETED_REVIEW.equals(developReviewOrderPo.getDevelopReviewOrderStatus())
                && ScmConstant.PROCESS_SUPPLIER_CODE.equals(developReviewOrderPo.getSupplierCode())
                && DevelopSampleQuality.QUALIFIED.equals(developReviewSampleOrderPo.getDevelopSampleQuality())
                && (DevelopSampleDemand.SATISFY.equals(developReviewSampleOrderPo.getDevelopSampleDemand())
                || DevelopSampleDemand.NEED_NOT_CONFIRMATION.equals(developReviewSampleOrderPo.getDevelopSampleDemand()));
    }
}
