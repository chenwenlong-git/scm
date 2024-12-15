package com.hete.supply.scm.server.scm.sample.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.bss.api.oss.entity.vo.FileVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmAttrSkuVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmSkuVo;
import com.hete.supply.plm.api.goods.entity.vo.PlmVariantVo;
import com.hete.supply.scm.api.scm.entity.dto.SampleChildOrderResultSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SamplePurchaseSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SampleSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SpuDto;
import com.hete.supply.scm.api.scm.entity.enums.*;
import com.hete.supply.scm.api.scm.entity.vo.SampleChildOrderInfoVo;
import com.hete.supply.scm.api.scm.importation.entity.dto.SampleParentImportationDto;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.common.constant.ScmRedisConstant;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.remote.dubbo.BssRemoteService;
import com.hete.supply.scm.remote.dubbo.PlmRemoteService;
import com.hete.supply.scm.remote.dubbo.WmsRemoteService;
import com.hete.supply.scm.server.scm.entity.bo.RawDeliverOrderBo;
import com.hete.supply.scm.server.scm.entity.dto.*;
import com.hete.supply.scm.server.scm.entity.vo.RawDeliverVo;
import com.hete.supply.scm.server.scm.entity.vo.RawSampleItemVo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.enums.wms.ScmBizReceiveOrderType;
import com.hete.supply.scm.server.scm.handler.WmsReceiptHandler;
import com.hete.supply.scm.server.scm.purchase.converter.PurchaseConverter;
import com.hete.supply.scm.server.scm.purchase.entity.vo.PurchaseMsgParentVo;
import com.hete.supply.scm.server.scm.purchase.service.base.PurchaseBaseService;
import com.hete.supply.scm.server.scm.sample.converter.SampleConverter;
import com.hete.supply.scm.server.scm.sample.dao.*;
import com.hete.supply.scm.server.scm.sample.entity.bo.SampleParentAndInfoBo;
import com.hete.supply.scm.server.scm.sample.entity.dto.*;
import com.hete.supply.scm.server.scm.sample.entity.po.*;
import com.hete.supply.scm.server.scm.sample.entity.vo.*;
import com.hete.supply.scm.server.scm.sample.handler.SampleStatusHandler;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.sample.service.base.SampleReceiptBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.settle.entity.vo.PurchaseSettleSimpleVo;
import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.entity.vo.SkuBatchCodeVo;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderDao;
import com.hete.supply.scm.server.supplier.purchase.dao.PurchaseRawReceiptOrderItemDao;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderItemPo;
import com.hete.supply.scm.server.supplier.purchase.entity.po.PurchaseRawReceiptOrderPo;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleReturnOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.bo.SampleDetailExtraBo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleReturnOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleDeliverSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleReturnSimpleVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleSplitDetailVo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.SampleSplitItemVo;
import com.hete.supply.scm.server.supplier.sample.service.base.SampleDeliverBaseService;
import com.hete.supply.scm.server.supplier.sample.service.base.SampleReturnBaseService;
import com.hete.supply.wms.api.WmsEnum;
import com.hete.supply.wms.api.basic.entity.dto.SkuBatchCreateDto;
import com.hete.supply.wms.api.entry.entity.dto.ReceiveOrderGetDto;
import com.hete.supply.wms.api.entry.entity.vo.ReceiveOrderForScmVo;
import com.hete.supply.wms.api.interna.entity.dto.SkuInstockInventoryQueryDto;
import com.hete.supply.wms.api.interna.entity.vo.SkuInventoryVo;
import com.hete.supply.wms.api.leave.entity.vo.ProcessDeliveryOrderVo;
import com.hete.support.api.enums.BooleanType;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.service.IdGenerateService;
import com.hete.support.mybatis.plus.entity.bo.CompareResult;
import com.hete.support.mybatis.plus.util.DataCompareUtil;
import com.hete.support.redis.lock.annotation.RedisLock;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/7 11:29
 */
@Service
@RequiredArgsConstructor
public class SampleBizService {
    private final SampleParentOrderDao sampleParentOrderDao;
    private final SampleParentOrderInfoDao sampleParentOrderInfoDao;
    private final IdGenerateService idGenerateService;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final SampleBaseService sampleBaseService;
    private final SampleParentOrderChangeDao sampleParentOrderChangeDao;
    private final SampleChildOrderChangeDao sampleChildOrderChangeDao;
    private final SampleReturnOrderDao sampleReturnOrderDao;
    private final SampleReturnOrderItemDao sampleReturnOrderItemDao;
    private final ScmImageBaseService scmImageBaseService;
    private final SampleDeliverBaseService sampleDeliverBaseService;
    private final SampleReceiptBaseService sampleReceiptBaseService;
    private final SampleReturnBaseService sampleReturnBaseService;
    private final PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;
    private final SampleChildOrderInfoDao sampleChildOrderInfoDao;
    private final SupplierBaseService supplierBaseService;
    private final WmsRemoteService wmsRemoteService;
    private final PurchaseBaseService purchaseBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SampleChildOrderResultDao sampleChildOrderResultDao;
    private final LogBaseService logBaseService;
    private final SampleDingService sampleDingService;
    private final PlmRemoteService plmRemoteService;
    private final BssRemoteService bssRemoteService;
    private final SampleParentOrderRawDao sampleParentOrderRawDao;
    private final SampleParentOrderProcessDao sampleParentOrderProcessDao;
    private final SampleParentOrderProcessDescDao sampleParentOrderProcessDescDao;
    private final SampleChildOrderRawDao sampleChildOrderRawDao;
    private final SampleChildOrderProcessDao sampleChildOrderProcessDao;
    private final SampleChildOrderProcessDescDao sampleChildOrderProcessDescDao;
    private final PurchaseRawReceiptOrderDao purchaseRawReceiptOrderDao;
    private final PurchaseRawReceiptOrderItemDao purchaseRawReceiptOrderItemDao;
    private final SupplierDao supplierDao;

    @Transactional(rollbackFor = Exception.class)
    public SampleParentOrderPo createSample(SampleCreateDto dto) {
        if (BooleanType.FALSE.equals(dto.getIsSample())) {
            throw new BizException("错误的创建样品单调用，请联系系统管理员！");
        }

        if (!SampleDevType.DEFECTIVE_REOPEN.equals(dto.getSampleDevType())
                && StringUtils.isNotBlank(dto.getDefectiveSampleChildOrderNo())) {
            throw new ParamIllegalException("创建的样品开发类型不是{},不可填写次品样品单号", SampleDevType.DEFECTIVE_REOPEN.getRemark());
        }
        if (SampleDevType.DEFECTIVE_REOPEN.equals(dto.getSampleDevType())
                || SampleDevType.SUPPLY_RECOMMEND.equals(dto.getSampleDevType())) {
            throw new BizException("错误的调用，请联系系统管理员！");
        }

        ScmTimeUtil.checkDateBeforeNow(Collections.singletonList(dto.getDeliverDate()));

        SampleOrderStatus targetStatus = SampleOrderStatus.WAIT_DISBURSEMENT;

        return this.createSampleParentOrder(dto, targetStatus);
    }

    /**
     * 创建样品母单
     *
     * @param dto
     * @param targetStatus
     */
    private SampleParentOrderPo createSampleParentOrder(SampleCreateDto dto, SampleOrderStatus targetStatus) {
        SampleParentOrderPo sampleParentOrderPo = SampleConverter.createDtoToPo(dto, targetStatus);
        final String sampleParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.SAMPLE_PARENT_NO_PREFIX, ConfuseLength.L_10);
        sampleParentOrderPo.setSampleParentOrderNo(sampleParentOrderNo);

        sampleParentOrderDao.insert(sampleParentOrderPo);

        final SampleParentOrderChangePo sampleParentOrderChangePo = SampleConverter.parentPoToChangePo(sampleParentOrderPo);
        sampleParentOrderChangeDao.insert(sampleParentOrderChangePo);

        final List<SampleParentOrderInfoDto> sampleParentOrderInfoList = dto.getSampleParentOrderInfoList();
        List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = SampleConverter.infoListToPoList(sampleParentOrderInfoList, sampleParentOrderNo);
        sampleParentOrderInfoDao.insertBatch(sampleParentOrderInfoPoList);

        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.SAMPLE_PARENT_ORDER,
                sampleParentOrderPo.getSampleParentOrderId());

        sampleParentOrderPo.setUpdateTime(LocalDateTime.now());
        logBaseService.simpleLog(LogBizModule.SAMPLE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleParentOrderPo.getSampleParentOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        logBaseService.sampleParentVersionLog(LogBizModule.SAMPLE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                sampleParentOrderPo.getSampleParentOrderNo(), sampleParentOrderPo, sampleParentOrderInfoPoList);

        return sampleParentOrderPo;

    }

    @Transactional(rollbackFor = Exception.class)
    public SampleParentOrderPo createSpecialSample(SampleSpecialCreateDto dto) {
        if (!SampleDevType.DEFECTIVE_REOPEN.equals(dto.getSampleDevType())
                && !SampleDevType.SUPPLY_RECOMMEND.equals(dto.getSampleDevType())) {
            throw new BizException("错误的调用，请联系系统管理员！");
        }

        if (BooleanType.FALSE.equals(dto.getIsSample())) {
            throw new ParamIllegalException("{}与{}类型的样品单必须选择打样类型为需要打样，请重新填写后再提交!",
                    SampleDevType.DEFECTIVE_REOPEN.getRemark(), SampleDevType.SUPPLY_RECOMMEND.getRemark());
        }

        if (dto.getDeliverDate().isBefore(DateUtil.beginOfDay(new Date()).toLocalDateTime())) {
            throw new ParamIllegalException("业务约定交期不能小于当前时间");
        }

        SampleOrderStatus targetStatus = SampleOrderStatus.WAIT_SAMPLE;
        final SampleParentAndInfoBo sampleParentAndInfoBo = this.createSpecialSampleParent(targetStatus, dto, dto.getSampleChildItemList());

        // 若开发类型为次品重打，需要校验次品样品单号
        // 衍生样开款初始状态为待打版
        if (SampleDevType.DEFECTIVE_REOPEN.equals(dto.getSampleDevType())) {
            final SampleChildOrderResultPo sampleChildOrderResultPo = sampleChildOrderResultDao.getOneByResultNo(dto.getDefectiveSampleChildOrderNo());
            if (null == sampleChildOrderResultPo) {
                throw new ParamIllegalException("找不到对应的样品选样结果单号，新建样品单失败");
            }
            // 关联样品结果单据
            sampleChildOrderResultPo.setRelateOrderNo(sampleParentAndInfoBo.getSampleParentOrderPo().getSampleParentOrderNo());
            sampleChildOrderResultPo.setSampleResultStatus(SampleResultStatus.HANDLED);
            sampleChildOrderResultDao.updateByIdVersion(sampleChildOrderResultPo);
        }

        //获取供应商信息
        Map<String, String> supplierNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getSampleChildItemList())) {
            List<String> supplierCodeList = dto.getSampleChildItemList().stream().map(SampleSpecialItemDto::getSupplierCode).distinct().collect(Collectors.toList());
            supplierNameMap = supplierDao.getSupplierNameBySupplierCodeList(supplierCodeList);
        }

        this.createSampleChildBySpecialItem(dto.getSampleChildItemList(),
                sampleParentAndInfoBo.getSampleParentOrderPo(),
                sampleParentAndInfoBo.getSampleParentOrderInfoPoList(),
                supplierNameMap);


        return sampleParentAndInfoBo.getSampleParentOrderPo();
    }

    private <E extends SampleSpecialItemDto> SampleParentAndInfoBo createSpecialSampleParent(SampleOrderStatus targetStatus,
                                                                                             SampleCreateDto dto,
                                                                                             List<E> sampleSpecialItemList) {
        SampleParentOrderPo sampleParentOrderPo = SampleConverter.createDtoToPo(dto, targetStatus);
        final String sampleParentOrderNo = idGenerateService.getConfuseCode(ScmConstant.SAMPLE_PARENT_NO_PREFIX, ConfuseLength.L_10);
        sampleParentOrderPo.setSampleParentOrderNo(sampleParentOrderNo);

        final int purchaseSum = sampleSpecialItemList.stream()
                .mapToInt(SampleSpecialItemDto::getPurchaseCnt)
                .sum();
        sampleParentOrderPo.setPurchaseTotal(purchaseSum);

        sampleParentOrderDao.insert(sampleParentOrderPo);

        final SampleParentOrderChangePo sampleParentOrderChangePo = SampleConverter.parentPoToChangePo(sampleParentOrderPo);
        sampleParentOrderChangeDao.insert(sampleParentOrderChangePo);

        final List<SampleParentOrderInfoDto> sampleParentOrderInfoList = dto.getSampleParentOrderInfoList();
        List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = SampleConverter.infoListToPoList(sampleParentOrderInfoList, sampleParentOrderNo);
        sampleParentOrderInfoDao.insertBatch(sampleParentOrderInfoPoList);

        scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.SAMPLE_PARENT_ORDER,
                sampleParentOrderPo.getSampleParentOrderId());

        sampleParentOrderPo.setUpdateTime(LocalDateTime.now());
        logBaseService.simpleLog(LogBizModule.SAMPLE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleParentOrderPo.getSampleParentOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        logBaseService.sampleParentVersionLog(LogBizModule.SAMPLE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                sampleParentOrderPo.getSampleParentOrderNo(), sampleParentOrderPo, sampleParentOrderInfoPoList);

        return SampleParentAndInfoBo.builder()
                .sampleParentOrderPo(sampleParentOrderPo)
                .sampleParentOrderInfoPoList(sampleParentOrderInfoPoList)
                .build();
    }

    public CommonPageResult.PageInfo<SampleSearchVo> searchSample(SampleSearchDto dto) {
        List<String> skuParentOrderNoList = sampleBaseService.getSkuParentOrderNoList(dto);
        if (CollectionUtils.isNotEmpty(dto.getSkuList()) && CollectionUtils.isEmpty(skuParentOrderNoList)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<SampleSearchVo> pageInfo = sampleParentOrderDao.searchSample(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, skuParentOrderNoList);

        final List<SampleSearchVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new CommonPageResult.PageInfo<>();
        }

        List<Long> sampleParentOrderIdList = records.stream().map(SampleSearchVo::getSampleParentOrderId).collect(Collectors.toList());

        //获取第一张图片
        Map<Long, String> fileCodeMap = scmImageBaseService.getOneFileCodeMapByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER, sampleParentOrderIdList);
        List<String> fileCodeList = new ArrayList<>(fileCodeMap.values());
        List<FileVo> fileVoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileCodeList)) {
            ResultList<FileVo> fileVoResultList = bssRemoteService.getFileList(fileCodeList);
            fileVoList = fileVoResultList.getList();
        }
        List<FileVo> finalFileVoList = fileVoList;

        records.forEach(record ->
        {
            record.setWarehouseTypeList(FormatStringUtil.string2List(record.getWarehouseTypes(), ","));
            if (fileCodeMap.containsKey(record.getSampleParentOrderId())) {
                String fileCode = fileCodeMap.get(record.getSampleParentOrderId());
                finalFileVoList.stream().filter(customer -> fileCode.equals(customer.getFileCode())).findAny().ifPresent(fileVo -> record.setContrastFileUrl(fileVo.getFileUrl()));
            }
        });

        return pageInfo;
    }

    public SampleDetailVo sampleDetail(SampleParentNoDto dto) {
        SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(dto.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }


        List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = sampleParentOrderInfoDao.getInfoListByParentNo(dto.getSampleParentOrderNo());

        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNo(dto.getSampleParentOrderNo());
        final List<SampleParentOrderRawPo> sampleParentOrderRawPoList = sampleParentOrderRawDao.getListByNo(dto.getSampleParentOrderNo());
        final SampleDetailVo sampleDetailVo = SampleConverter.poToDetailVo(sampleParentOrderPo,
                sampleParentOrderInfoPoList, sampleChildOrderPoList, sampleParentOrderRawPoList);
        final List<String> fileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER,
                Collections.singletonList(sampleParentOrderPo.getSampleParentOrderId()));
        sampleDetailVo.setContrastFileCode(fileCodeList);

        SampleDetailExtraBo sampleDetailExtraBo = this.getSampleDetailExtraBo(sampleChildOrderPoList);
        sampleDetailVo.setSampleDeliverSimpleList(sampleDetailExtraBo.getSampleDeliverSimpleList());
        sampleDetailVo.setSampleReceiptSimpleList(sampleDetailExtraBo.getSampleReceiptSimpleList());
        sampleDetailVo.setSampleReturnSimpleVoList(sampleDetailExtraBo.getSampleReturnSimpleVoList());
        sampleDetailVo.setPurchaseSettleSimpleList(sampleDetailExtraBo.getPurchaseSettleSimpleList());

        final List<String> childOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        final List<SampleResultVo> sampleResultList = this.getSampleResultListByChildList(childOrderNoList);
        sampleDetailVo.setSampleResultList(sampleResultList);

        final List<SampleParentOrderProcessPo> sampleParentOrderProcessPoList = sampleParentOrderProcessDao.getListByNo(sampleParentOrderPo.getSampleParentOrderNo());
        sampleDetailVo.setSampleParentProcessList(SampleConverter.parentProcessPoToVo(sampleParentOrderProcessPoList));
        return sampleDetailVo;
    }


    public SampleDetailVo sampleDefectiveDetail(SampleResultNoDto dto) {
        final SampleChildOrderResultPo sampleChildOrderResultPo = sampleChildOrderResultDao.getOneByResultNo(dto.getSampleResultNo());
        Assert.notNull(sampleChildOrderResultPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final SampleChildOrderPo defectiveSamplePo = sampleChildOrderDao.getOneByChildOrderNo(sampleChildOrderResultPo.getSampleChildOrderNo());
        if (null == defectiveSamplePo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        final SampleParentNoDto sampleParentNoDto = new SampleParentNoDto();
        sampleParentNoDto.setSampleParentOrderNo(defectiveSamplePo.getSampleParentOrderNo());
        return sampleDetail(sampleParentNoDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitDisbursement(SampleParentIdAndVersionDto dto) {
        SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByIdVersion(dto.getSampleParentOrderId(), dto.getVersion());
        if (null == sampleParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        SampleParentOrderChangePo sampleParentOrderChangePo = sampleParentOrderChangeDao.getByParentId(sampleParentOrderPo.getSampleParentOrderId());
        if (null == sampleParentOrderChangePo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        final SampleOrderStatus sampleOrderStatus = sampleParentOrderPo.getSampleOrderStatus().toWaitTypeset();
        sampleParentOrderPo.setSampleOrderStatus(sampleOrderStatus);

        sampleParentOrderChangePo.setDisbursementTime(new DateTime().toLocalDateTime());
        sampleParentOrderChangePo.setDisburseUser(GlobalContext.getUserKey());
        sampleParentOrderChangePo.setDisburseUsername(GlobalContext.getUsername());

        sampleParentOrderChangeDao.updateByIdVersion(sampleParentOrderChangePo);

        sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);

        logBaseService.simpleLog(LogBizModule.SAMPLE_PARENT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleParentOrderPo.getSampleParentOrderNo(), sampleOrderStatus.getRemark(), Collections.emptyList());
        final SampleStatusDto sampleStatusDto = new SampleStatusDto();
        sampleStatusDto.setSpuCode(sampleParentOrderPo.getSpu());
        sampleStatusDto.setSampleOrderStatus(sampleOrderStatus);
        sampleStatusDto.setKey(sampleParentOrderPo.getSampleParentOrderNo());
        sampleStatusDto.setUserKey(GlobalContext.getUserKey());
        sampleStatusDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @RedisLock(prefix = ScmRedisConstant.SCM_SAMPLE_SPLIT, key = "#dto.sampleParentOrderNo", waitTime = 1, leaseTime = -1)
    public List<SampleChildOrderPo> splitChildOrder(SampleSplitDto dto) {
        SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(dto.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        // 校验供应商是否重复
        final List<SampleSplitDto.SampleSplitItem> sampleSplitItemList = dto.getSampleSplitItemList();
        final List<String> supplierCodeList = sampleSplitItemList.stream()
                .map(SampleSplitDto.SampleSplitItem::getSupplierCode)
                .distinct()
                .collect(Collectors.toList());
        supplierBaseService.checkSupplierExists(supplierCodeList);

        if (SampleOrderStatus.WAIT_DISBURSEMENT.equals(sampleParentOrderPo.getSampleOrderStatus())
                || SampleOrderStatus.REFUSED.equals(sampleParentOrderPo.getSampleOrderStatus())
                || SampleOrderStatus.SELECTED.equals(sampleParentOrderPo.getSampleOrderStatus())
                || SampleOrderStatus.PROOFING_FAIL.equals(sampleParentOrderPo.getSampleOrderStatus())) {
            throw new ParamIllegalException("该样品需求单处于以下状态【{}】【{}】【{}】【{}】,无法拆分子单，请刷新后重试！",
                    SampleOrderStatus.WAIT_DISBURSEMENT.getRemark(), SampleOrderStatus.REFUSED.getRemark(),
                    SampleOrderStatus.SELECTED.getRemark(), SampleOrderStatus.PROOFING_FAIL.getRemark());
        }

        //获取供应商信息
        Map<String, String> supplierNameMap = supplierDao.getSupplierNameBySupplierCodeList(supplierCodeList);


        final List<LocalDateTime> dateList = sampleSplitItemList.stream()
                .map(SampleSplitDto.SampleSplitItem::getDeliverDate)
                .collect(Collectors.toList());
        ScmTimeUtil.checkDateBeforeNow(dateList);

        int childOrderNoIndex = sampleBaseService.getLatestChildOrderNo(dto.getSampleParentOrderNo());

        List<SampleChildOrderPo> sampleChildOrderPoList = SampleConverter.splitDtoToChildOrderList(dto, childOrderNoIndex,
                sampleParentOrderPo, supplierNameMap);
        sampleChildOrderDao.insertBatch(sampleChildOrderPoList);

        List<SampleChildOrderChangePo> sampleChildOrderChangePoList = SampleConverter.childOrderToChildOrderChangeList(sampleChildOrderPoList);
        sampleChildOrderChangeDao.insertBatch(sampleChildOrderChangePoList);

        final List<String> sampleChildOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        // 子单继承母单的生产信息
        final List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = sampleParentOrderInfoDao.getInfoListByParentNo(sampleParentOrderPo.getSampleParentOrderNo());
        if (CollectionUtils.isNotEmpty(sampleParentOrderInfoPoList)) {
            final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = SampleConverter.childOrderNoListToInfoList(sampleChildOrderNoList, sampleParentOrderInfoPoList);
            sampleChildOrderInfoDao.insertBatch(sampleChildOrderInfoPoList);
        }

        // 更新母单采购数
        final int purchaseSum = sampleSplitItemList.stream()
                .mapToInt(SampleSplitDto.SampleSplitItem::getPurchaseCnt)
                .sum();
        sampleParentOrderPo.setPurchaseTotal(sampleParentOrderPo.getPurchaseTotal() + purchaseSum);

        // 更新母单状态
        SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(dto.getSampleParentOrderNo(),
                Collections.singletonList(SampleOrderStatus.WAIT_TYPESET));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
        }

        sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);

        if (SampleDevType.confirmSubmitAudited(sampleParentOrderPo.getSampleDevType())) {
            final List<SampleParentOrderProcessPo> sampleParentOrderProcessPoList = sampleParentOrderProcessDao.getListByNo(sampleParentOrderPo.getSampleParentOrderNo());
            final List<SampleParentOrderProcessDescPo> sampleParentOrderProcessDescPoList = sampleParentOrderProcessDescDao.getListByNo(sampleParentOrderPo.getSampleParentOrderNo());

            final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = SampleConverter.parentProcessToChildProcess(sampleParentOrderProcessPoList,
                    sampleChildOrderPoList);
            final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList = SampleConverter.parentProcessDescToChildProcessDesc(sampleParentOrderProcessDescPoList,
                    sampleChildOrderPoList);
            sampleChildOrderProcessDao.insertBatch(sampleChildOrderProcessPoList);
            sampleChildOrderProcessDescDao.insertBatch(sampleChildOrderProcessDescPoList);
        }

        // 日志
        sampleChildOrderPoList.forEach(po -> {
            po.setUpdateTime(LocalDateTime.now());
            logBaseService.sampleChildVersionLog(LogBizModule.SAMPLE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), po);
            logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), SampleOrderStatus.WAIT_TYPESET.getRemark(), Collections.emptyList());
        });

        return sampleChildOrderPoList;
    }


    public void typeset(SampleChildTypesetDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        if (null == sampleChildOrderPo) {
            throw new BizException("找不到对应的样品需求子单，请刷新页面后重试！");
        }
        SampleChildOrderChangePo sampleChildOrderChangePo = sampleChildOrderChangeDao.getByChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
        if (null == sampleChildOrderChangePo) {
            throw new BizException("找不到对应的样品需求子单，请刷新页面后重试！");
        }

        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("找不到对应的样品需求母单，请刷新页面后重试！");
        }
        final SampleParentOrderChangePo sampleParentOrderChangePo = sampleParentOrderChangeDao.getByParentId(sampleParentOrderPo.getSampleParentOrderId());
        if (null == sampleParentOrderChangePo) {
            throw new BizException("找不到对应的样品需求母单，请刷新页面后重试！");
        }

        if (SampleDevType.confirmSubmitAudited(sampleParentOrderPo.getSampleDevType())) {
            if (CollectionUtils.isEmpty(dto.getSampleRawList())) {
                throw new ParamIllegalException("{}、{}、{}类型的样品单必须填写原料bom信息！",
                        SampleDevType.LIMITED.getRemark(), SampleDevType.SMALL_INNOVATION.getRemark(), SampleDevType.OUTSOURCING_SAMPLE.getRemark());
            }
            if (StringUtils.isBlank(dto.getRawWarehouseCode())) {
                throw new ParamIllegalException("{}、{}、{}类型的样品单必须填写原料收货仓库！",
                        SampleDevType.LIMITED.getRemark(), SampleDevType.SMALL_INNOVATION.getRemark(), SampleDevType.OUTSOURCING_SAMPLE.getRemark());
            }

            //验证库存是否充足
            List<String> skuList = dto.getSampleRawList().stream().map(SampleRawDto::getSku).distinct().collect(Collectors.toList());
            SkuInstockInventoryQueryDto skuInstockInventoryQueryDto = new SkuInstockInventoryQueryDto();
            skuInstockInventoryQueryDto.setWarehouseCode(dto.getRawWarehouseCode());
            skuInstockInventoryQueryDto.setSkuCodes(skuList);
            skuInstockInventoryQueryDto.setProductQuality(WmsEnum.ProductQuality.GOOD);
            skuInstockInventoryQueryDto.setDeliveryType(WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);
            List<SkuInventoryVo> skuInventoryList = wmsRemoteService.getSkuInventoryList(skuInstockInventoryQueryDto);
            if (CollectionUtils.isEmpty(skuInventoryList)) {
                throw new ParamIllegalException("查询不到对应库存信息，禁止下发打版!");
            } else {
                // 判断是否有缺货的
                List<String> wmsSkuList = skuInventoryList.stream().map(SkuInventoryVo::getSkuCode).distinct().collect(Collectors.toList());
                dto.getSampleRawList().forEach(item -> {
                    if (!wmsSkuList.contains(item.getSku())) {
                        throw new ParamIllegalException("sku{}库存不足，禁止下发打版!", item.getSku());
                    }
                });
                skuInventoryList.forEach(item -> {
                    Integer inStockAmount = item.getInStockAmount();
                    int sum = dto.getSampleRawList().stream().filter(it -> it.getSku().equals(item.getSkuCode())).mapToInt(SampleRawDto::getDeliveryCnt).sum();
                    if (inStockAmount < (sum * sampleChildOrderPo.getPurchaseCnt())) {
                        throw new ParamIllegalException("sku{}库存不足，禁止下发打版!", item.getSkuCode());
                    }
                });
            }
        }
        // 变更子单状态
        SampleOrderStatus sampleOrderStatus = sampleChildOrderPo.getSampleOrderStatus();
        final SampleOrderStatus targetStatus = sampleOrderStatus.toWaitReceiveOrder();

        sampleBaseService.updateTypeset(dto, sampleChildOrderPo, targetStatus, sampleChildOrderChangePo, sampleParentOrderPo);


    }

    @Transactional(rollbackFor = Exception.class)
    public void editSample(SampleEditDto dto) {
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByIdVersion(dto.getSampleParentOrderId(), dto.getVersion());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final SampleOrderStatus sampleOrderStatus = sampleParentOrderPo.getSampleOrderStatus();
        Assert.equals(SampleOrderStatus.WAIT_DISBURSEMENT, sampleOrderStatus, () -> new ParamIllegalException("只有【{}】状态下的样品需求单才可编辑，请刷新后重试",
                SampleOrderStatus.WAIT_DISBURSEMENT.getRemark()));

        if (SampleDevType.DEFECTIVE_REOPEN.equals(dto.getSampleDevType())) {
            final SampleChildOrderPo defectiveSamplePo = sampleChildOrderDao.getOneByChildOrderNo(dto.getDefectiveSampleChildOrderNo());
            Assert.notNull(defectiveSamplePo, () -> new BizException("找不到对应的次品样品单号，请刷新页面后重试！"));
        }

        final Long sampleParentOrderId = dto.getSampleParentOrderId();
        SampleParentOrderPo updatePo = SampleConverter.createDtoToPo(dto, null);
        updatePo.setSampleParentOrderId(sampleParentOrderId);
        updatePo.setVersion(dto.getVersion());

        sampleParentOrderDao.updateByIdVersion(updatePo);

        sampleParentOrderInfoDao.deleteAllParentInfo(sampleParentOrderPo.getSampleParentOrderNo());
        final List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = SampleConverter.infoListToPoList(dto.getSampleParentOrderInfoList(),
                sampleParentOrderPo.getSampleParentOrderNo());
        sampleParentOrderInfoDao.insertBatch(sampleParentOrderInfoPoList);

        updatePo.setUpdateTime(LocalDateTime.now());
        logBaseService.sampleParentVersionLog(LogBizModule.SAMPLE_PARENT_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                sampleParentOrderPo.getSampleParentOrderNo(), updatePo, sampleParentOrderInfoPoList);

        final List<String> oldFileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER,
                Collections.singletonList(sampleParentOrderId));
        scmImageBaseService.editImage(dto.getFileCodeList(), oldFileCodeList, ImageBizType.SAMPLE_PARENT_ORDER, sampleParentOrderId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchCancelSampleOrder(SampleBatchIdDto dto) {
        final Set<Long> idList = dto.getSampleChildIdList();
        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getByIdList(idList);

        if (CollectionUtils.isEmpty(sampleChildOrderPoList)
                || idList.size() != sampleChildOrderPoList.size()) {
            throw new BizException("找不到对应的样品需求子单，请刷新页面后重试！");
        }

        final List<String> sampleParentOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleParentOrderNo)
                .distinct()
                .collect(Collectors.toList());

        if (sampleParentOrderNoList.size() > 1) {
            throw new ParamIllegalException("批量取消的子单必须为同一母单下的子单，请重新选择子单后再操作！");
        }

        // 更新母单状态
        final String sampleParentOrderNo = sampleParentOrderNoList.get(0);
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleParentOrderNo);

        Assert.notNull(sampleParentOrderPo, () -> new BizException("找不到对应的样品需求母单，请刷新页面后重试！"));

        final SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(sampleParentOrderNo, Collections.emptyList());
        if (!earliestStatus.equals(sampleParentOrderPo.getSampleOrderStatus())) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }
        // 最后删除
        sampleChildOrderDao.batchDeleteByIdList(idList);
    }

    public CommonPageResult.PageInfo<SamplePurchaseSearchVo> searchSamplePurchase(SamplePurchaseSearchDto dto) {
        //条件过滤
        if (null == sampleBaseService.getSearchSampleChildWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }
        final CommonPageResult.PageInfo<SamplePurchaseSearchVo> pageResult = sampleChildOrderDao.searchSamplePurchase(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<SamplePurchaseSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        List<Long> sampleParentOrderIdList = records.stream().map(SamplePurchaseSearchVo::getSampleParentOrderId).distinct().collect(Collectors.toList());
        List<String> skuList = records.stream().map(SamplePurchaseSearchVo::getSku).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        Map<String, String> skuMap = plmRemoteService.getSkuEncodeBySku(skuList).stream().collect(Collectors.toMap(PlmSkuVo::getSkuCode, PlmSkuVo::getSkuEncode));

        //获取第一张图片
        Map<Long, String> fileCodeMap = scmImageBaseService.getOneFileCodeMapByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER, sampleParentOrderIdList);
        List<String> fileCodeList = new ArrayList<>(fileCodeMap.values());
        List<FileVo> fileVoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileCodeList)) {
            ResultList<FileVo> fileVoResultList = bssRemoteService.getFileList(fileCodeList);
            fileVoList = fileVoResultList.getList();
        }
        List<FileVo> finalFileVoList = fileVoList;

        supplierBaseService.batchSetSupplierGrade(records);
        records.forEach(record -> {
            record.setWarehouseTypeList(FormatStringUtil.string2List(record.getWarehouseTypes(), ","));
            if (fileCodeMap.containsKey(record.getSampleParentOrderId())) {
                String fileCode = fileCodeMap.get(record.getSampleParentOrderId());
                finalFileVoList.stream().filter(customer -> fileCode.equals(customer.getFileCode())).findAny().ifPresent(fileVo -> record.setContrastFileUrl(fileVo.getFileUrl()));
            }
            record.setSkuEncode(skuMap.get(record.getSku()));
        });

        return pageResult;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelSampleOrder(SampleParentIdAndVersionDto dto) {
        SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByIdVersion(dto.getSampleParentOrderId(), dto.getVersion());
        if (null == sampleParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        final SampleOrderStatus sampleOrderStatus = sampleParentOrderPo.getSampleOrderStatus();
        if (!SampleOrderStatus.WAIT_DISBURSEMENT.equals(sampleOrderStatus) && !SampleOrderStatus.WAIT_TYPESET.equals(sampleOrderStatus)) {
            throw new ParamIllegalException("样品需求单状态不处于【{}】【{}】，无法进行取消采购操作，请刷新后重试！",
                    SampleOrderStatus.WAIT_DISBURSEMENT.getRemark(), SampleOrderStatus.WAIT_TYPESET.getRemark());
        }

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNo(sampleParentOrderPo.getSampleParentOrderNo());
        if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
            throw new ParamIllegalException("该样品需求单下存在样品采购子单，无法进行取消采购操作");
        }

        sampleParentOrderDao.removeByIdVersion(dto.getSampleParentOrderId(), dto.getVersion());
    }

    @Transactional(rollbackFor = Exception.class)
    public void editChildSample(SampleChildEditDto dto) {
        SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(dto.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (!SampleOrderStatus.WAIT_TYPESET.equals(sampleParentOrderPo.getSampleOrderStatus())) {
            throw new ParamIllegalException("样品需求单不在【{}】状态，编辑子单失败，请刷新后重试！",
                    SampleOrderStatus.WAIT_TYPESET.getRemark());
        }

        final List<SampleChildOrderDto> sampleSplitItemList = dto.getSampleSplitItemList();
        final Set<Long> sampleChildOrderIdList = sampleSplitItemList.stream()
                .map(SampleChildOrderDto::getSampleChildOrderId)
                .collect(Collectors.toSet());
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getByIdList(sampleChildOrderIdList);
        if (sampleChildOrderPoList.size() != sampleChildOrderIdList.size()) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        //获取供应商信息
        Map<String, String> supplierNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getSampleSplitItemList())) {
            List<String> supplierCodeList = dto.getSampleSplitItemList().stream().map(SampleChildOrderDto::getSupplierCode).distinct().collect(Collectors.toList());
            supplierNameMap = supplierDao.getSupplierNameBySupplierCodeList(supplierCodeList);
        }

        SampleConverter.sampleSplitItemDtoToChildOrderList(sampleChildOrderPoList, dto.getSampleSplitItemList(), supplierNameMap);

        sampleChildOrderDao.updateBatchByIdVersion(sampleChildOrderPoList);

        final int purchaseTotal = sampleSplitItemList.stream()
                .mapToInt(SampleChildOrderDto::getPurchaseCnt)
                .sum();
        sampleParentOrderPo.setPurchaseTotal(purchaseTotal);
        sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);

        final LocalDateTime now = LocalDateTime.now();
        sampleChildOrderPoList.forEach(po -> {
            po.setUpdateTime(now);
            logBaseService.sampleChildVersionLog(LogBizModule.SAMPLE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), po);
        });


    }

    public SamplePurchaseDetailVo samplePurchaseDetail(SampleChildNoDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(dto.getSampleChildOrderNo());
        if (null == sampleChildOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        List<String> skuList = new ArrayList<>();
        skuList.add(sampleChildOrderPo.getSku());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNoAndStatus(sampleChildOrderPo.getSampleParentOrderNo(),
                dto.getSampleOrderStatusList(), dto.getAuthSupplierCode());

        final List<SampleChildOrderInfoPo> sampleChildInfoPoList = sampleChildOrderInfoDao.getInfoListByChildNo(sampleChildOrderPo.getSampleChildOrderNo());

        final SamplePurchaseDetailVo samplePurchaseDetailVo = SampleConverter.poToPurchaseDetailVo(sampleChildOrderPo,
                sampleParentOrderPo, sampleChildInfoPoList, sampleChildOrderPoList);

        final Long sampleChildOrderId = sampleChildOrderPo.getSampleChildOrderId();
        final List<String> fileCodeList = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SAMPLE_CTRL_SUCCESS,
                Collections.singletonList(sampleChildOrderId));
        samplePurchaseDetailVo.setFileCodeList(fileCodeList);

        final List<String> contrastFileCode = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER,
                Collections.singletonList(sampleParentOrderPo.getSampleParentOrderId()));
        samplePurchaseDetailVo.setContrastFileCode(contrastFileCode);

        final List<String> reSampleFileCode = scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.SAMPLE_CHANGE,
                Collections.singletonList(sampleChildOrderPo.getSampleChildOrderId()));
        samplePurchaseDetailVo.setReSampleFileCode(reSampleFileCode);

        SampleDetailExtraBo sampleDetailExtraBo = this.getSampleDetailExtraBo(Collections.singletonList(sampleChildOrderPo));
        samplePurchaseDetailVo.setSampleDeliverSimpleList(sampleDetailExtraBo.getSampleDeliverSimpleList());
        samplePurchaseDetailVo.setSampleReceiptSimpleList(sampleDetailExtraBo.getSampleReceiptSimpleList());
        samplePurchaseDetailVo.setSampleReturnSimpleVoList(sampleDetailExtraBo.getSampleReturnSimpleVoList());
        samplePurchaseDetailVo.setPurchaseSettleSimpleList(sampleDetailExtraBo.getPurchaseSettleSimpleList());

        final List<SampleResultVo> sampleResultList = this.getSampleResultListByChildList(Collections.singletonList(sampleChildOrderPo.getSampleChildOrderNo()));
        samplePurchaseDetailVo.setSampleResultList(sampleResultList);

        supplierBaseService.batchSetSupplierGrade(Collections.singletonList(samplePurchaseDetailVo));

        final Integer resultCnt = sampleChildOrderResultDao.getCntByChildOrderNo(dto.getSampleChildOrderNo());
        samplePurchaseDetailVo.setReSampleTimes(resultCnt > 0 ? resultCnt - 1 : 0);

        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(sampleChildOrderPo.getSampleChildOrderNo(),
                SampleRawBizType.FORMULA);
        final List<SampleRawVo> sampleRawVoList = SampleConverter.childRawPoToVo(sampleChildOrderRawPoList, skuEncodeMap);
        final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = sampleChildOrderProcessDao.getListByNo(sampleChildOrderPo.getSampleChildOrderNo());
        final List<SampleProcessVo> sampleProcessVoList = SampleConverter.childProcessPoToVo(sampleChildOrderProcessPoList);
        final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList = sampleChildOrderProcessDescDao.getListByNo(sampleChildOrderPo.getSampleChildOrderNo());
        final List<SampleProcessDescVo> sampleProcessDescList = SampleConverter.childProcessDescPoToVo(sampleChildOrderProcessDescPoList);
        final List<SampleChildOrderRawPo> demandSampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(dto.getSampleChildOrderNo(),
                SampleRawBizType.ACTUAL_DELIVER);
        final List<RawSampleItemVo> rawSampleItemVoList = SampleConverter.rawPoListToVoList(demandSampleChildOrderRawPoList);
        samplePurchaseDetailVo.setSampleRawList(sampleRawVoList);
        samplePurchaseDetailVo.setSampleProcessList(sampleProcessVoList);
        samplePurchaseDetailVo.setSampleProcessDescList(sampleProcessDescList);
        samplePurchaseDetailVo.setRawSampleItemList(rawSampleItemVoList);
        samplePurchaseDetailVo.setSkuEncode(skuEncodeMap.get(sampleChildOrderPo.getSku()));

        return samplePurchaseDetailVo;
    }

    /**
     * 批量打印样品打样单
     *
     * @param dto
     * @return
     */
    public List<SamplePurchasePrintVo> batchPrintSamplePurchase(SampleChildNoListDto dto) {
        List<String> sampleChildOrderNoList = dto.getSampleChildOrderNoList();
        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildOrderNoList);

        if (CollectionUtils.isEmpty(sampleChildOrderPoList)) {
            throw new ParamIllegalException("样品打样单不存在");
        }
        // 获取生产信息
        Map<String, List<SampleChildOrderInfoPo>> sampleChildOrderInfoMap = sampleChildOrderInfoDao.getMapByChildNoList(sampleChildOrderNoList);

        // 获取参考图片
        List<String> sampleParentOrderNoList = sampleChildOrderPoList.stream().map(SampleChildOrderPo::getSampleParentOrderNo).collect(Collectors.toList());
        List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentOrderNoList);

        Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER,
                sampleParentOrderPoList.stream().map(SampleParentOrderPo::getSampleParentOrderId).collect(Collectors.toList()));

        //获取工序列表
        List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = sampleChildOrderProcessDao.getListByNoList(sampleChildOrderNoList);
        final List<SampleProcessVo> sampleProcessVoList = SampleConverter.childProcessPoToVo(sampleChildOrderProcessPoList);
        final Map<String, List<SampleProcessVo>> sampleChildOrderNoProcessVoMap = sampleProcessVoList.stream()
                .collect(Collectors.groupingBy(SampleProcessVo::getSampleChildOrderNo));

        //获取工序描述列表
        final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList = sampleChildOrderProcessDescDao.getListByNoList(sampleChildOrderNoList);
        final List<SampleProcessDescVo> sampleProcessDescVoList = SampleConverter.childProcessDescPoToVo(sampleChildOrderProcessDescPoList);
        final Map<String, List<SampleProcessDescVo>> sampleChildOrderNoProcessDescVoMap = sampleProcessDescVoList.stream()
                .collect(Collectors.groupingBy(SampleProcessDescVo::getSampleChildOrderNo));

        //获取原料bom表信息
        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoListAndType(sampleChildOrderNoList, SampleRawBizType.FORMULA);
        final List<String> skuList = sampleChildOrderRawPoList.stream()
                .map(SampleChildOrderRawPo::getSku)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> skuEncodeMap = plmRemoteService.getSkuEncodeMapBySku(skuList);
        final List<SampleRawVo> sampleRawVoList = SampleConverter.childRawPoToVo(sampleChildOrderRawPoList, skuEncodeMap);
        final Map<String, List<SampleRawVo>> sampleChildOrderNoRawVoMap = sampleRawVoList.stream()
                .collect(Collectors.groupingBy(SampleRawVo::getSampleChildOrderNo));

        return sampleChildOrderPoList.stream().map(item -> {
            SamplePurchasePrintVo samplePurchasePrintVo = new SamplePurchasePrintVo();
            samplePurchasePrintVo.setSampleChildOrderId(item.getSampleChildOrderId());
            samplePurchasePrintVo.setSampleChildOrderNo(item.getSampleChildOrderNo());
            samplePurchasePrintVo.setVersion(item.getVersion());
            samplePurchasePrintVo.setSupplierCode(item.getSupplierCode());
            samplePurchasePrintVo.setSupplierName(item.getSupplierName());
            samplePurchasePrintVo.setWarehouseCode(item.getWarehouseCode());
            samplePurchasePrintVo.setWarehouseName(item.getWarehouseName());
            samplePurchasePrintVo.setWarehouseTypeList(FormatStringUtil.string2List(item.getWarehouseTypes(), ","));
            samplePurchasePrintVo.setPurchaseCnt(item.getPurchaseCnt());
            samplePurchasePrintVo.setSupplierProduction(item.getSupplierProduction());
            samplePurchasePrintVo.setDeliverDate(item.getDeliverDate());
            samplePurchasePrintVo.setDemandDescribe(item.getDemandDescribe());
            samplePurchasePrintVo.setSampleChildProcessList(sampleChildOrderNoProcessVoMap.get(item.getSampleChildOrderNo()));
            samplePurchasePrintVo.setSampleProcessDescList(sampleChildOrderNoProcessDescVoMap.get(item.getSampleChildOrderNo()));
            samplePurchasePrintVo.setSampleRawList(sampleChildOrderNoRawVoMap.get(item.getSampleChildOrderNo()));

            // 参考图片
            Optional<SampleParentOrderPo> first = sampleParentOrderPoList.stream().filter(it -> item.getSampleParentOrderNo().equals(it.getSampleParentOrderNo()))
                    .findFirst();
            if (first.isPresent()) {
                SampleParentOrderPo sampleParentOrderPo = first.get();
                List<String> contrastFileCode = fileCodeMap.get(sampleParentOrderPo.getSampleParentOrderId());
                samplePurchasePrintVo.setContrastFileCode(contrastFileCode);
            }

            // 生产信息
            List<SampleChildOrderInfoPo> sampleChildOrderInfoPos = sampleChildOrderInfoMap.get(item.getSampleChildOrderNo());
            if (CollectionUtils.isNotEmpty(sampleChildOrderInfoPos)) {
                List<SampleChildOrderInfoVo> sampleChildOrderInfoVos = SampleConverter.childInfoPoListToVoList(sampleChildOrderInfoPos);
                samplePurchasePrintVo.setSampleChildOrderInfoList(sampleChildOrderInfoVos);
            }
            return samplePurchasePrintVo;
        }).collect(Collectors.toList());
    }

    private SampleDetailExtraBo getSampleDetailExtraBo(List<SampleChildOrderPo> sampleChildOrderPoList) {
        final List<String> childOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderNo)
                .collect(Collectors.toList());

        final List<SampleDeliverSimpleVo> sampleDeliverSimpleList = sampleDeliverBaseService.getSampleSimpleListByChildList(childOrderNoList);
        final List<SampleReceiptSimpleVo> sampleReceiptSimpleList = sampleReceiptBaseService.getSimpleVoListByChildList(childOrderNoList);
        final List<SampleReturnSimpleVo> sampleReturnSimpleList = sampleReturnBaseService.getSimpleVoListByChildList(childOrderNoList);
        final List<PurchaseSettleSimpleVo> purchaseSettleSimpleList = purchaseSettleOrderBaseService.getPurchaseSettleByBusinessNo(childOrderNoList);

        final SampleDetailExtraBo sampleDetailExtraBo = new SampleDetailExtraBo();
        sampleDetailExtraBo.setSampleDeliverSimpleList(sampleDeliverSimpleList);
        sampleDetailExtraBo.setSampleReceiptSimpleList(sampleReceiptSimpleList);
        sampleDetailExtraBo.setSampleReturnSimpleVoList(sampleReturnSimpleList);
        sampleDetailExtraBo.setPurchaseSettleSimpleList(purchaseSettleSimpleList);

        return sampleDetailExtraBo;
    }

    private List<SampleResultVo> getSampleResultListByChildList(List<String> sampleChildOrderNoList) {
        final List<SampleChildOrderResultPo> sampleChildOrderResultPoList = sampleChildOrderResultDao.selectListByChildNo(sampleChildOrderNoList);
        return sampleChildOrderResultPoList.stream()
                .map(po -> {
                    final SampleResultVo sampleResultVo = new SampleResultVo();
                    sampleResultVo.setSampleUsername(po.getCreateUsername());
                    sampleResultVo.setSampleTime(po.getCreateTime());
                    sampleResultVo.setSampleWarehousingOrderNo(po.getRelateOrderNo());
                    sampleResultVo.setSampleResult(po.getSampleResult());
                    sampleResultVo.setSampleCnt(po.getSampleCnt());
                    sampleResultVo.setSampleResultNo(po.getSampleResultNo());
                    sampleResultVo.setRemark(po.getRemark());

                    return sampleResultVo;
                }).collect(Collectors.toList());
    }


    public SampleSplitDetailVo getSplitDetail(SampleParentNoDto dto) {
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(dto.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNo(dto.getSampleParentOrderNo());
        List<SampleSplitItemVo> sampleSplitItemList = SampleConverter.childPoToItemVo(sampleChildOrderPoList);

        List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = sampleParentOrderInfoDao.getInfoListByParentNo(dto.getSampleParentOrderNo());
        final List<SampleParentOrderInfoVo> sampleParentOrderInfoList = SampleConverter.infoPoListToVoList(sampleParentOrderInfoPoList);

        final SampleSplitDetailVo sampleSplitDetailVo = new SampleSplitDetailVo();
        sampleSplitDetailVo.setSpu(sampleParentOrderPo.getSpu());
        sampleSplitDetailVo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
        sampleSplitDetailVo.setSampleSplitItemList(sampleSplitItemList);
        sampleSplitDetailVo.setSampleParentOrderInfoList(sampleParentOrderInfoList);

        return sampleSplitDetailVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void forceFinishSample(SampleChildIdAndVersionDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));

        final SampleOrderStatus targetStatus = sampleChildOrderPo.getSampleOrderStatus().toSettle();
        sampleChildOrderPo.setSampleOrderStatus(targetStatus);
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);


        final SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(sampleChildOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(targetStatus));

        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }
        logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleChildOrderPo.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        final SampleStatusDto sampleStatusDto = new SampleStatusDto();
        sampleStatusDto.setSpuCode(sampleParentOrderPo.getSpu());
        sampleStatusDto.setSampleOrderStatus(targetStatus);
        sampleStatusDto.setKey(sampleChildOrderPo.getSampleChildOrderNo());
        sampleStatusDto.setUserKey(GlobalContext.getUserKey());
        sampleStatusDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reSample(ReSampleDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, "找不到对应的样品采购子单，打样操作失败");

        SampleChildOrderChangePo sampleChildOrderChangePo = sampleChildOrderChangeDao.getByChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
        Assert.notNull(sampleChildOrderChangePo, "找不到对应的样品采购子单,打样操作失败");

        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, "找不到对应的样品采购母单,打样操作失败");

        final SampleParentOrderChangePo sampleParentOrderChangePo = sampleParentOrderChangeDao.getByParentId(sampleParentOrderPo.getSampleParentOrderId());
        Assert.notNull(sampleParentOrderChangePo, "找不到对应的样品采购母单,打样操作失败");

        final SampleOrderStatus targetStatus = sampleChildOrderPo.getSampleOrderStatus().toReSample();

        sampleChildOrderPo.setSampleOrderStatus(targetStatus);
        sampleChildOrderPo.setSampleImprove(dto.getSampleImprove());
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);

        // 更新change
        sampleChildOrderChangePo.setSampleTime(new DateTime().toLocalDateTime());
        sampleChildOrderChangePo.setSampleUser(GlobalContext.getUserKey());
        sampleChildOrderChangePo.setSampleUsername(GlobalContext.getUsername());
        sampleChildOrderChangeDao.updateByIdVersion(sampleChildOrderChangePo);

        // 更新母单状态
        SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(sampleChildOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(sampleChildOrderPo.getSampleOrderStatus()));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }
        // 更新母单change
        sampleParentOrderChangePo.setSampleTime(new DateTime().toLocalDateTime());
        sampleParentOrderChangeDao.updateByIdVersion(sampleParentOrderChangePo);

        logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleChildOrderPo.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
        final SampleStatusDto sampleStatusDto = new SampleStatusDto();
        sampleStatusDto.setSpuCode(sampleParentOrderPo.getSpu());
        sampleStatusDto.setSampleOrderStatus(targetStatus);
        sampleStatusDto.setKey(sampleChildOrderPo.getSampleChildOrderNo());
        sampleStatusDto.setUserKey(GlobalContext.getUserKey());
        sampleStatusDto.setUsername(GlobalContext.getUsername());
        consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);

        // 上传样品改善图片
        if (CollectionUtils.isNotEmpty(dto.getFileCodeList())) {
            scmImageBaseService.insertBatchImage(dto.getFileCodeList(), ImageBizType.SAMPLE_CHANGE, dto.getSampleChildOrderId());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void sampleReturnGoods(SampleReturnCreateDto dto) {
        final List<SimpleReturnItemDto> sampleReturnItemList = dto.getSampleReturnItemList();
        final List<String> sampleChildOrderNoList = sampleReturnItemList.stream()
                .map(SimpleReturnItemDto::getSampleChildOrderNo)
                .collect(Collectors.toList());
        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildOrderNoList);

        if (CollectionUtils.isEmpty(sampleChildOrderPoList)) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        //获取供应商信息
        List<String> supplierCodeList = sampleChildOrderPoList.stream().map(SampleChildOrderPo::getSupplierCode).distinct().collect(Collectors.toList());
        List<SupplierPo> supplierPoList = supplierDao.getBySupplierCodeListAndType(supplierCodeList, List.of(SupplierType.ONESELF_BUSINESS));
        if (CollectionUtils.isNotEmpty(supplierPoList)) {
            throw new BizException("选样结果关联的样品子单存在自营供应商，禁止样品退货操作！");
        }


        // 更新样品结果关联单据号
        final Map<String, SimpleReturnItemDto> resultNoMap = sampleReturnItemList.stream()
                .collect(Collectors.toMap(SimpleReturnItemDto::getSampleResultNo, Function.identity()));
        final List<SampleChildOrderResultPo> sampleChildOrderResultPoList = sampleChildOrderResultDao.getListByNoList(resultNoMap.keySet());

        // 校验样品结果状态
        this.checkSampleReturnCreateStatus(sampleChildOrderResultPoList);


        SampleReturnOrderPo sampleReturnOrderPo = SampleConverter.returnDtoToReturnPo(dto);
        final int totalReturnCnt = sampleReturnItemList.stream()
                .mapToInt(SimpleReturnItemDto::getReturnCnt)
                .sum();
        sampleReturnOrderPo.setReturnCnt(totalReturnCnt);
        final String sampleReturnOrderNo = idGenerateService.getConfuseCode(ScmConstant.SAMPLE_RETURN_ORDER_NO_PREFIX, ConfuseLength.L_10);
        sampleReturnOrderPo.setSampleReturnOrderNo(sampleReturnOrderNo);
        sampleReturnOrderDao.insert(sampleReturnOrderPo);
        logBaseService.simpleLog(LogBizModule.SUPPLIER_SAMPLE_RETURN_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleReturnOrderPo.getSampleReturnOrderNo(), ReceiptOrderStatus.WAIT_RECEIVE.getRemark(), Collections.emptyList());

        List<SampleReturnOrderItemPo> newSampleReturnOrderItemPoList = SampleConverter.childOrderPoListToReturnItemList(sampleChildOrderPoList,
                sampleReturnItemList, sampleReturnOrderNo);
        sampleReturnOrderItemDao.insertBatch(newSampleReturnOrderItemPoList);


        sampleChildOrderResultPoList.forEach(po -> {
            if (StringUtils.isNotBlank(po.getRelateOrderNo())) {
                throw new BizException("样品选样结果:{}已经存在退货单据，样品退货失败", po.getSampleResultNo());
            }
            final SimpleReturnItemDto simpleReturnItemDto = resultNoMap.get(po.getSampleResultNo());
            po.setRelateOrderNo(sampleReturnOrderNo);
            po.setSampleResultStatus(SampleResultStatus.HANDLED);
        });
        sampleChildOrderResultDao.updateBatchByIdVersion(sampleChildOrderResultPoList);

        sampleChildOrderPoList.forEach(po -> {
            sampleDingService.sendChildReturnDingTalkMsg(po, sampleReturnOrderPo);
        });
    }

    private void checkSampleReturnCreateStatus(List<SampleChildOrderResultPo> sampleChildOrderResultPoList) {
        sampleChildOrderResultPoList.forEach(po -> {
            if (!SampleResult.SAMPLE_RETURN.equals(po.getSampleResult())) {
                throw new ParamIllegalException("选样结果id{}当前选样结果为{},无法发起退货", po.getSampleResultNo(), po.getSampleResult().getRemark());
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void editWarehouse(SampleEditWarehouseDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final SampleOrderStatus sampleOrderStatus = sampleChildOrderPo.getSampleOrderStatus();
        if (!SampleOrderStatus.RECEIVED_ORDER.equals(sampleOrderStatus)
                && !SampleOrderStatus.TYPESETTING.equals(sampleOrderStatus)
                && !SampleOrderStatus.WAIT_RECEIVED_SAMPLE.equals(sampleOrderStatus)) {
            throw new BizException("当前样品采购单不处于【{}】、【{}】、【{}】",
                    SampleOrderStatus.RECEIVED_ORDER.getRemark(), SampleOrderStatus.TYPESETTING.getRemark(),
                    SampleOrderStatus.WAIT_RECEIVED_SAMPLE.getRemark());
        }

        sampleChildOrderPo.setWarehouseCode(dto.getWarehouseCode());
        sampleChildOrderPo.setWarehouseName(dto.getWarehouseName());
        sampleChildOrderPo.setWarehouseTypes(String.join(",", Optional.ofNullable(dto.getWarehouseTypeList())
                .orElse(new ArrayList<>())));

        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);
    }

    public List<String> getDefectiveSampleNoList(SampleDefNoDto dto) {
        ArrayList<String> sampleResultNoList = new ArrayList<>();

        final List<SampleChildOrderResultPo> sampleChildOrderResultPoList = sampleChildOrderResultDao.getDefectiveSampleNoList(dto.getSampleResultNo(), List.of(SampleResult.FAIL_SALE, SampleResult.SAMPLE_RETURN));
        List<String> sampleChildOrderNoList = sampleChildOrderResultPoList.stream().map(SampleChildOrderResultPo::getSampleChildOrderNo).distinct().collect(Collectors.toList());

        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildOrderNoList);
        Map<String, SampleChildOrderPo> sampleChildOrderPoMap = sampleChildOrderPoList.stream().collect(Collectors.toMap(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderPo -> sampleChildOrderPo));
        //获取供应商信息
        List<String> supplierCodeList = sampleChildOrderPoList.stream().map(SampleChildOrderPo::getSupplierCode).distinct().collect(Collectors.toList());
        Map<String, SupplierPo> supplierPoMap = supplierDao.getBySupplierCodeList(supplierCodeList).stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, supplierPo -> supplierPo));

        //处理失败闪售数据
        for (SampleChildOrderResultPo sampleChildOrderResultPo : sampleChildOrderResultPoList.stream().filter(p -> SampleResult.FAIL_SALE.equals(p.getSampleResult())).collect(Collectors.toList())) {
            sampleResultNoList.add(sampleChildOrderResultPo.getSampleResultNo());
        }

        //处理失败退样数据
        for (SampleChildOrderResultPo sampleChildOrderResultPo : sampleChildOrderResultPoList.stream().filter(p -> SampleResult.SAMPLE_RETURN.equals(p.getSampleResult())).collect(Collectors.toList())) {
            SampleChildOrderPo sampleChildOrderPo = sampleChildOrderPoMap.get(sampleChildOrderResultPo.getSampleChildOrderNo());
            if (null != sampleChildOrderPo) {
                SupplierPo supplierPo = supplierPoMap.get(sampleChildOrderPo.getSupplierCode());
                if (null != supplierPo && SupplierType.ONESELF_BUSINESS.equals(supplierPo.getSupplierType())) {
                    sampleResultNoList.add(sampleChildOrderResultPo.getSampleResultNo());
                }
            }
        }
        return new ArrayList<>(new LinkedHashSet<>(sampleResultNoList));
    }

    public SampleMsgVo getSampleMsgBySpuList(SampleSpuListDto dto) {
        final SampleMsgVo sampleMsgVo = new SampleMsgVo();
        final List<SampleMsgParentVo> sampleParentMsgBySpuList = this.getSampleParentMsgBySpuList(dto.getSpuList());
        final List<PurchaseMsgParentVo> purchaseParentList = purchaseBaseService.getPurchaseParentList(dto.getSpuList());

        sampleMsgVo.setSampleMsgParentList(sampleParentMsgBySpuList);
        sampleMsgVo.setPurchaseMsgParentList(purchaseParentList);

        return sampleMsgVo;
    }

    public List<SampleMsgParentVo> getSampleParentMsgBySpuList(List<String> spuList) {
        final List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getListBySpuList(spuList);
        if (CollectionUtils.isEmpty(sampleParentOrderPoList)) {
            return Collections.emptyList();
        }
        final List<String> sampleParentOrderNoList = sampleParentOrderPoList.stream()
                .map(SampleParentOrderPo::getSampleParentOrderNo)
                .collect(Collectors.toList());

        final List<Long> idList = sampleParentOrderPoList.stream()
                .map(SampleParentOrderPo::getSampleParentOrderId)
                .distinct()
                .collect(Collectors.toList());

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListByChildNoList(sampleParentOrderNoList);

        List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = sampleParentOrderInfoDao.getInfoListByParentNoList(sampleParentOrderNoList);
        final List<SampleParentOrderInfoVo> sampleParentOrderInfoVoList = SampleConverter.infoPoListToVoList(sampleParentOrderInfoPoList);

        final Map<Long, List<String>> fileCodeMap = scmImageBaseService.getFileCodeMapByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER,
                idList);

        return SampleConverter.poListToMsgVoList(sampleChildOrderPoList, sampleParentOrderPoList,
                sampleParentOrderInfoVoList, fileCodeMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void productDeliver(SampleProductDeliverDto dto) {
        final List<SampleProductDeliverDetail> sampleProductDeliverDetailList = dto.getSampleProductDeliverDetailList();
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleProductDeliverDetailList.stream().map(SampleProductDeliverDetail::getSampleChildOrderNo).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(sampleChildOrderPoList)) {
            throw new BizException("找不到对应的样品采购子单，请刷新页面！");
        }
        Map<String, SampleChildOrderPo> sampleChildOrderPoMap = sampleChildOrderPoList.stream().collect(Collectors.toMap(SampleChildOrderPo::getSampleChildOrderNo, sampleChildOrderPo -> sampleChildOrderPo));
        //检验数据是否正确
        for (SampleProductDeliverDetail sampleProductDeliverDetail : sampleProductDeliverDetailList) {
            SampleChildOrderPo sampleChildOrderPo = sampleChildOrderPoMap.get(sampleProductDeliverDetail.getSampleChildOrderNo());
            Assert.notNull(sampleChildOrderPo, "找不到对应的样品采购子单，成品入仓失败");
            if (StringUtils.isNotBlank(sampleChildOrderPo.getSampleReceiptOrderNo())) {
                throw new ParamIllegalException("当前样品子单已经发起了成品入库，请刷新页面！");
            }
        }

        for (SampleProductDeliverDetail sampleProductDeliverDetail : sampleProductDeliverDetailList) {
            SampleChildOrderPo sampleChildOrderPo = sampleChildOrderPoMap.get(sampleProductDeliverDetail.getSampleChildOrderNo());
            final ReceiveOrderCreateMqDto.ReceiveOrderCreateItem receiveOrderCreateItem = new ReceiveOrderCreateMqDto.ReceiveOrderCreateItem();
            receiveOrderCreateItem.setReceiveType(ReceiveType.SAMPLE);
            receiveOrderCreateItem.setScmBizNo(sampleProductDeliverDetail.getSampleResultNo());
            receiveOrderCreateItem.setSupplierCode(sampleChildOrderPo.getSupplierCode());
            receiveOrderCreateItem.setSupplierName(sampleChildOrderPo.getSupplierName());
            receiveOrderCreateItem.setWarehouseCode(dto.getWarehouseCode());
            receiveOrderCreateItem.setPurchaseOrderType(PurchaseOrderType.NORMAL);
            receiveOrderCreateItem.setIsDirectSend(BooleanType.FALSE);
            receiveOrderCreateItem.setIsUrgentOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setIsNormalOrder(BooleanType.FALSE);
            receiveOrderCreateItem.setQcType(WmsEnum.QcType.NOT_CHECK);
            receiveOrderCreateItem.setOperator(GlobalContext.getUserKey());
            receiveOrderCreateItem.setOperatorName(GlobalContext.getUsername());
            receiveOrderCreateItem.setUnionKey(sampleProductDeliverDetail.getSampleResultNo() + ScmConstant.SCM_BIZ_RECEIVE_TYPE_LINK + ScmBizReceiveOrderType.SEAL_SAMPLE.name());

            final LocalDateTime now = LocalDateTime.now();
            receiveOrderCreateItem.setSendTime(now);
            receiveOrderCreateItem.setPlaceOrderTime(now);

            final ReceiveOrderCreateMqDto.ReceiveOrderDetail receiveOrderDetail = new ReceiveOrderCreateMqDto.ReceiveOrderDetail();
            receiveOrderDetail.setBatchCode(sampleProductDeliverDetail.getSkuBatchCode());
            receiveOrderDetail.setSkuCode(sampleProductDeliverDetail.getSku());
            receiveOrderDetail.setSpu(sampleChildOrderPo.getSpu());
            receiveOrderDetail.setPurchaseAmount(sampleProductDeliverDetail.getPurchaseCnt());
            receiveOrderDetail.setDeliveryAmount(sampleProductDeliverDetail.getDeliveryCnt());
            receiveOrderCreateItem.setDetailList(Collections.singletonList(receiveOrderDetail));

            final ReceiveOrderCreateMqDto receiveOrderCreateMqDto = new ReceiveOrderCreateMqDto();
            receiveOrderCreateMqDto.setReceiveOrderCreateItemList(Collections.singletonList(receiveOrderCreateItem));
            receiveOrderCreateMqDto.setKey(idGenerateService.getSnowflakeCode(sampleProductDeliverDetail.getSampleChildOrderNo() + "-"));

            consistencySendMqService.execSendMq(WmsReceiptHandler.class, receiveOrderCreateMqDto);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncReceiptMsg(ReceiveOrderChangeMqDto message) {
        if (WmsEnum.ReceiveOrderState.WAIT_RECEIVE.equals(message.getReceiveOrderState())) {
            final String scmBizNo = message.getScmBizNo();
            final SampleChildOrderResultPo sampleChildOrderResultPo = sampleChildOrderResultDao.getOneByResultNo(scmBizNo);
            Assert.notNull(sampleChildOrderResultPo, () -> new BizException("查找不到对应的样品子单结果，请刷新页面后重试！"));

            final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(sampleChildOrderResultPo.getSampleChildOrderNo());
            Assert.notNull(sampleChildOrderPo, () -> new BizException("查找不到对应的样品子单，请刷新页面后重试！"));

            final SampleChildOrderPo updatePo = sampleChildOrderPo.setSampleReceiptOrderNo(message.getReceiveOrderNo());
            sampleChildOrderDao.updateByIdVersion(updatePo);

            sampleChildOrderResultPo.setRelateOrderNo(message.getReceiveOrderNo());
            sampleChildOrderResultPo.setSampleResultStatus(SampleResultStatus.HANDLED);
            sampleChildOrderResultDao.updateByIdVersion(sampleChildOrderResultPo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(SampleChildIdAndVersionDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("数据已被修改或删除，作废失败，请刷新页面后重试！"));
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("数据已被修改或删除，作废失败，请刷新页面后重试！"));


        if (!SampleOrderStatus.RECEIVED_ORDER.equals(sampleChildOrderPo.getSampleOrderStatus())
                && !SampleOrderStatus.TYPESETTING.equals(sampleChildOrderPo.getSampleOrderStatus())) {
            throw new BizException("当前样品子单状态不处于【{}】【{}】,无法进行作废操作",
                    SampleOrderStatus.RECEIVED_ORDER.getRemark(), SampleOrderStatus.TYPESETTING.getRemark());
        }
        final SampleOrderStatus targetStatus = SampleOrderStatus.DELETED;
        sampleChildOrderPo.setSampleOrderStatus(targetStatus);
        sampleChildOrderDao.updateByIdVersion(sampleChildOrderPo);

        // 更新母单
        final SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(sampleParentOrderPo.getSampleParentOrderNo(),
                Collections.singletonList(targetStatus));
        if (!sampleParentOrderPo.getSampleOrderStatus().equals(earliestStatus)) {
            sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
            sampleParentOrderDao.updateByIdVersion(sampleParentOrderPo);
        }

        logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleChildOrderPo.getSampleChildOrderNo(), targetStatus.getRemark(), Collections.emptyList());
    }

    public List<SkuBatchCodeVo> printBatchCode(SampleChildNoListDto dto) {
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(dto.getSampleChildOrderNoList());
        Assert.notEmpty(sampleChildOrderPoList, () -> new BizException("获取不到样品采购子单，打印批次码失败，请联系系统管理员！"));

        final List<String> skuList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSku)
                .collect(Collectors.toList());

        final List<PlmVariantVo> variantAttrList = plmRemoteService.getVariantAttr(skuList);
        final Map<String, List<PlmAttrSkuVo>> skuVariantAttrMap = variantAttrList.stream()
                .collect(Collectors.toMap(PlmVariantVo::getSkuCode, PlmVariantVo::getVariantSkuList));

        return sampleChildOrderPoList.stream()
                .map(po -> {
                    final SkuBatchCodeVo skuBatchCodeVo = new SkuBatchCodeVo();
                    skuBatchCodeVo.setSku(po.getSku());
                    skuBatchCodeVo.setSkuBatchCode(po.getSkuBatchCode());
                    skuBatchCodeVo.setSupplierCode(po.getSupplierCode());
                    final List<PlmAttrSkuVo> variantSkuList = skuVariantAttrMap.get(po.getSku());
                    skuBatchCodeVo.setVariantSkuList(variantSkuList);
                    skuBatchCodeVo.setPurchaseCnt(po.getPurchaseCnt());

                    return skuBatchCodeVo;
                }).collect(Collectors.toList());
    }

    public SampleParentOrderPo isSampleEndStatus(SampleChildIdAndVersionDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        if (null == sampleChildOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        if (null == sampleParentOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }

        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderByParentNo(sampleChildOrderPo.getSampleParentOrderNo());

        final List<SampleChildOrderPo> remainChildOrderPoList = sampleChildOrderPoList.stream()
                .filter(po -> !po.getSampleChildOrderNo().equals(sampleChildOrderPo.getSampleChildOrderNo()))
                .filter(po -> !SampleOrderStatus.DELETED.equals(po.getSampleOrderStatus()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(remainChildOrderPoList)) {
            return sampleParentOrderPo;
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void importParentData(SampleParentImportationDto.ImportationDetail req) {
        final SampleDevType sampleDevType = SampleDevType.getByRemark(req.getSampleDevType());
        if (SampleDevType.DEFECTIVE_REOPEN.equals(sampleDevType)
                || SampleDevType.SUPPLY_RECOMMEND.equals(sampleDevType)) {
            throw new BizException("错误的调用，请联系系统管理员！");
        }

        SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(req.getSampleParentOrderNo());
        Assert.isNull(sampleParentOrderPo, () -> new BizException("样品单号:{}已存在样品单", req.getSampleParentOrderNo()));

        sampleParentOrderPo = new SampleParentOrderPo();
        sampleParentOrderPo.setSampleOrderStatus(SampleOrderStatus.WAIT_DISBURSEMENT);
        sampleParentOrderPo.setSampleDevType(sampleDevType);
        sampleParentOrderPo.setCategoryName(req.getCategoryName());
        sampleParentOrderPo.setSpu(req.getSpu());
        sampleParentOrderPo.setPlatform(req.getPlatform());
        sampleParentOrderPo.setWarehouseCode(req.getWarehouseCode());
        sampleParentOrderPo.setWarehouseName(req.getWarehouseName());
        sampleParentOrderPo.setWarehouseTypes(req.getWarehouseTypes());
        sampleParentOrderPo.setDeliverDate(req.getDeliverDate());
        sampleParentOrderPo.setPurchasePredictPrice(req.getPurchasePredictPrice());
        sampleParentOrderPo.setIsFirstOrder(BooleanType.getTypeByValue(req.getIsFirstOrder()));
        sampleParentOrderPo.setIsUrgentOrder(BooleanType.getTypeByValue(req.getIsUrgentOrder()));
        sampleParentOrderPo.setSourceMaterial(req.getSourceMaterial());
        sampleParentOrderPo.setSampleDescribe(req.getSampleDescribe());
        sampleParentOrderPo.setSampleParentOrderNo(req.getSampleParentOrderNo());
        sampleParentOrderPo.setIsSample(BooleanType.TRUE);

        sampleParentOrderDao.insert(sampleParentOrderPo);

        final SampleParentOrderChangePo sampleParentOrderChangePo = SampleConverter.parentPoToChangePo(sampleParentOrderPo);
        sampleParentOrderChangeDao.insert(sampleParentOrderChangePo);

        final List<String> sampleInfoKeyList = plmRemoteService.getCategoryAttr(Long.valueOf(req.getCategoryName()));
        if (CollectionUtils.isNotEmpty(sampleInfoKeyList)) {
            final List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList = sampleInfoKeyList.stream().map(infoKey -> {
                final SampleParentOrderInfoPo sampleParentOrderInfoPo = new SampleParentOrderInfoPo();
                sampleParentOrderInfoPo.setSampleInfoKey(infoKey);
                sampleParentOrderInfoPo.setSampleParentOrderNo(req.getSampleParentOrderNo());
                return sampleParentOrderInfoPo;
            }).collect(Collectors.toList());

            sampleParentOrderInfoDao.insertBatch(sampleParentOrderInfoPoList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public SampleParentOrderPo createSettleSample(SampleSettleCreateDto dto) {
        // 校验
        this.checkSettleSampleParam(dto);
        //获取供应商信息
        Map<String, String> supplierNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(dto.getSampleChildItemList())) {
            List<String> supplierCodeList = dto.getSampleChildItemList().stream().map(SampleChildItemDto::getSupplierCode).distinct().collect(Collectors.toList());
            supplierNameMap = supplierDao.getSupplierNameBySupplierCodeList(supplierCodeList);
        }

        SampleOrderStatus targetStatus = SampleOrderStatus.SETTLE;
        final SampleParentAndInfoBo sampleParentAndInfoBo = this.createSpecialSampleParent(targetStatus,
                dto, dto.getSampleChildItemList());
        final SampleParentOrderPo sampleParentOrderPo = sampleParentAndInfoBo.getSampleParentOrderPo();
        // 子单
        final List<SampleChildOrderPo> sampleChildOrderPoList = this.createSampleChildBySampleChildItem(dto.getSampleChildItemList(),
                sampleParentOrderPo, targetStatus, sampleParentAndInfoBo.getSampleParentOrderInfoPoList(),
                supplierNameMap);

        //limited类型需要单独处理
        if (SampleDevType.limitedAudited(dto.getSampleDevType())) {
            // 原料
            sampleParentOrderPo.setRawWarehouseCode(dto.getRawWarehouseCode());
            sampleParentOrderPo.setRawWarehouseName(dto.getRawWarehouseName());

            final List<SampleParentOrderRawPo> sampleParentOrderRawPoList = dto.getSampleRawList()
                    .stream()
                    .map(sampleRaw -> {
                        final SampleParentOrderRawPo sampleParentOrderRawPo = new SampleParentOrderRawPo();
                        sampleParentOrderRawPo.setSampleParentOrderId(sampleParentOrderPo.getSampleParentOrderId());
                        sampleParentOrderRawPo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
                        sampleParentOrderRawPo.setSku(sampleRaw.getSku());
                        sampleParentOrderRawPo.setDeliveryCnt(sampleRaw.getDeliveryCnt());
                        return sampleParentOrderRawPo;
                    }).collect(Collectors.toList());
            final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = dto.getSampleRawList()
                    .stream()
                    .map(sampleRaw -> Optional.of(sampleChildOrderPoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(sampleChildOrderPo -> {
                                final SampleChildOrderRawPo sampleChildOrderRawPo = new SampleChildOrderRawPo();
                                sampleChildOrderRawPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                                sampleChildOrderRawPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                                sampleChildOrderRawPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                                sampleChildOrderRawPo.setSku(sampleRaw.getSku());
                                sampleChildOrderRawPo.setDeliveryCnt(sampleRaw.getDeliveryCnt());
                                sampleChildOrderRawPo.setSampleRawBizType(SampleRawBizType.FORMULA);

                                return sampleChildOrderRawPo;
                            }).collect(Collectors.toList()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            sampleParentOrderRawDao.insertBatch(sampleParentOrderRawPoList);
            sampleChildOrderRawDao.insertBatch(sampleChildOrderRawPoList);
            // 工序
            final List<SampleParentOrderProcessPo> sampleParentOrderProcessPoList = dto.getSampleProcessList()
                    .stream()
                    .map(processDto -> {
                        final SampleParentOrderProcessPo sampleParentOrderProcessPo = new SampleParentOrderProcessPo();
                        sampleParentOrderProcessPo.setSampleParentOrderId(sampleParentOrderPo.getSampleParentOrderId());
                        sampleParentOrderProcessPo.setSampleParentOrderNo(sampleParentOrderPo.getSampleParentOrderNo());
                        sampleParentOrderProcessPo.setProcessCode(processDto.getProcessCode());
                        sampleParentOrderProcessPo.setProcessName(processDto.getProcessName());
                        sampleParentOrderProcessPo.setProcessSecondCode(processDto.getProcessSecondCode());
                        sampleParentOrderProcessPo.setProcessSecondName(processDto.getProcessSecondName());
                        sampleParentOrderProcessPo.setProcessLabel(processDto.getProcessLabel());
                        return sampleParentOrderProcessPo;
                    }).collect(Collectors.toList());

            final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = dto.getSampleProcessList()
                    .stream()
                    .map(processDto -> {
                        return Optional.of(sampleChildOrderPoList)
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(sampleChildOrderPo -> {
                                    final SampleChildOrderProcessPo sampleChildOrderProcessPo = new SampleChildOrderProcessPo();
                                    sampleChildOrderProcessPo.setSampleChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
                                    sampleChildOrderProcessPo.setSampleParentOrderNo(sampleChildOrderPo.getSampleParentOrderNo());
                                    sampleChildOrderProcessPo.setSampleChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
                                    sampleChildOrderProcessPo.setProcessCode(processDto.getProcessCode());
                                    sampleChildOrderProcessPo.setProcessName(processDto.getProcessName());
                                    sampleChildOrderProcessPo.setProcessSecondCode(processDto.getProcessSecondCode());
                                    sampleChildOrderProcessPo.setProcessSecondName(processDto.getProcessSecondName());
                                    sampleChildOrderProcessPo.setProcessLabel(processDto.getProcessLabel());
                                    return sampleChildOrderProcessPo;
                                }).collect(Collectors.toList());

                    }).flatMap(Collection::stream)
                    .collect(Collectors.toList());
            sampleParentOrderProcessDao.insertBatch(sampleParentOrderProcessPoList);
            sampleChildOrderProcessDao.insertBatch(sampleChildOrderProcessPoList);
        }

        return sampleParentOrderPo;
    }

    private List<SampleChildOrderPo> createSampleChildBySampleChildItem(List<SampleChildItemDto> sampleChildItemList,
                                                                        SampleParentOrderPo sampleParentOrderPo,
                                                                        SampleOrderStatus targetStatus,
                                                                        List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList,
                                                                        Map<String, String> supplierNameMap) {
        final List<String> supplierCodeList = sampleChildItemList.stream()
                .map(SampleChildItemDto::getSupplierCode)
                .collect(Collectors.toList());
        supplierBaseService.checkSupplierExists(supplierCodeList);

        final List<String> skuList = sampleChildItemList.stream()
                .map(SampleChildItemDto::getSku)
                .collect(Collectors.toList());

        Map<String, SampleProduceLabel> sampleProduceLabelMap = sampleBaseService.getSampleChildOrderMapBatchSku(skuList);

        List<SampleChildOrderPo> sampleChildOrderPoList = SampleConverter.sampleChildItemDtoToChildOrderList(sampleChildItemList,
                sampleParentOrderPo, targetStatus, sampleProduceLabelMap, supplierNameMap);
        sampleChildOrderDao.insertBatch(sampleChildOrderPoList);

        List<SampleChildOrderChangePo> sampleChildOrderChangePoList = SampleConverter.childOrderToChildOrderChangeList(sampleChildOrderPoList);
        sampleChildOrderChangeDao.insertBatch(sampleChildOrderChangePoList);

        final List<String> sampleChildOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        // 子单继承母单的生产信息
        final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = SampleConverter.childOrderNoListToInfoList(sampleChildOrderNoList, sampleParentOrderInfoPoList);
        sampleChildOrderInfoDao.insertBatch(sampleChildOrderInfoPoList);

        // 日志
        sampleChildOrderPoList.forEach(po -> {
            po.setUpdateTime(LocalDateTime.now());
            logBaseService.sampleChildVersionLog(LogBizModule.SAMPLE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), po);
            logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), SampleOrderStatus.SELECTED.getRemark(), Collections.emptyList());
        });

        return sampleChildOrderPoList;
    }

    private void createSampleChildBySpecialItem(List<SampleSpecialItemDto> sampleSpecialItemList,
                                                SampleParentOrderPo sampleParentOrderPo,
                                                List<SampleParentOrderInfoPo> sampleParentOrderInfoPoList,
                                                Map<String, String> supplierNameMap) {
        final List<String> supplierCodeList = sampleSpecialItemList.stream()
                .map(SampleSpecialItemDto::getSupplierCode)
                .collect(Collectors.toList());
        supplierBaseService.checkSupplierExists(supplierCodeList);

        List<SampleChildOrderPo> sampleChildOrderPoList = SampleConverter.specialItemDtoToChildOrderList(sampleSpecialItemList,
                sampleParentOrderPo, supplierNameMap);
        sampleChildOrderDao.insertBatch(sampleChildOrderPoList);

        List<SampleChildOrderChangePo> sampleChildOrderChangePoList = SampleConverter.childOrderToChildOrderChangeList(sampleChildOrderPoList);
        sampleChildOrderChangeDao.insertBatch(sampleChildOrderChangePoList);

        final List<String> sampleChildOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        // 子单继承母单的生产信息
        final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = SampleConverter.childOrderNoListToInfoList(sampleChildOrderNoList, sampleParentOrderInfoPoList);
        sampleChildOrderInfoDao.insertBatch(sampleChildOrderInfoPoList);

        // 日志
        sampleChildOrderPoList.forEach(po -> {
            po.setUpdateTime(LocalDateTime.now());
            logBaseService.sampleChildVersionLog(LogBizModule.SAMPLE_CHILD_VERSION, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), po);
            logBaseService.simpleLog(LogBizModule.SAMPLE_CHILD_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                    po.getSampleChildOrderNo(), SampleOrderStatus.WAIT_SAMPLE.getRemark(), Collections.emptyList());
        });
    }

    private void checkSettleSampleParam(SampleSettleCreateDto dto) {
        if (BooleanType.TRUE.equals(dto.getIsSample())) {
            throw new BizException("错误的创建样品单调用，请联系系统管理员！");
        }

        if (CollectionUtils.isEmpty(dto.getSampleChildItemList())) {
            throw new ParamIllegalException("创建不需要打样类型的样品单，必须填写子单信息，请重新填写后提交！");
        }

        if (CollectionUtils.isEmpty(dto.getFileCodeList())) {
            throw new ParamIllegalException("创建不需要打样类型的样品单，必须上传参考图片，请重新填写后提交！");
        }

        if (SampleDevType.limitedAudited(dto.getSampleDevType())) {
            if (CollectionUtils.isEmpty(dto.getSampleRawList())) {
                throw new ParamIllegalException("创建不需要打样类型的limited样品单，必须填写原料bom表信息，请重新填写后提交！");
            }
            if (CollectionUtils.isEmpty(dto.getSampleProcessList())) {
                throw new ParamIllegalException("创建不需要打样类型的limited样品单，必须填写工序信息，请重新填写后提交！");
            }
        }
        if (SampleDevType.proofingAudited(dto.getSampleDevType())) {
            throw new ParamIllegalException("当前选择的" + dto.getSampleDevType().getRemark() + "是需要打样，请重新选择！");
        }
        if (SampleDevType.createSettleSampleAudited(dto.getSampleDevType())) {
            for (SampleParentOrderInfoDto sampleParentOrderInfoDto : dto.getSampleParentOrderInfoList()) {
                if (StringUtils.isBlank(sampleParentOrderInfoDto.getSampleInfoValue())) {
                    throw new ParamIllegalException("生产属性值必填！");
                }
            }
        }
    }

    public SampleToastMsgVo sampleSuccessToastMsg(SampleChildNoDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(dto.getSampleChildOrderNo());
        Assert.notNull(sampleChildOrderPo, () -> new BizException("样品子单号：{}，查找不到对应的样品子单，打样操作失败！", dto.getSampleChildOrderNo()));
        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, () -> new BizException("样品母单号：{}，查找不到对应的样品母单，打样操作失败！", sampleChildOrderPo.getSampleParentOrderNo()));
        if (!SampleDevType.confirmSubmitAudited(sampleParentOrderPo.getSampleDevType())) {
            throw new BizException("样品母单不属于【{}、{}、{}】类型，调用失败！",
                    SampleDevType.LIMITED.getRemark(), SampleDevType.SMALL_INNOVATION.getRemark(), SampleDevType.OUTSOURCING_SAMPLE.getRemark());
        }

        final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = sampleChildOrderProcessDao.getListByNo(dto.getSampleChildOrderNo());
        final List<SampleProcessVo> sampleProcessList = SampleConverter.childProcessPoToVo(sampleChildOrderProcessPoList);
        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(dto.getSampleChildOrderNo(), SampleRawBizType.FORMULA);
        final List<SampleRawVo> sampleRawList = SampleConverter.childRawPoToVo(sampleChildOrderRawPoList, Collections.emptyMap());

        return SampleToastMsgVo.builder()
                .rawWarehouseCode(sampleChildOrderPo.getRawWarehouseCode())
                .rawWarehouseName(sampleChildOrderPo.getRawWarehouseName())
                .sampleProcessList(sampleProcessList)
                .sampleRawList(sampleRawList)
                .build();

    }

    public CommonPageResult.PageInfo<String> getSamplePicBySpu(SpuDto dto) {
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getListBySpuAndStatus(dto.getSpuCode(),
                Arrays.asList(SampleOrderStatus.SELECTED, SampleOrderStatus.SETTLE));

        if (CollectionUtils.isEmpty(sampleChildOrderPoList)) {
            return new CommonPageResult.PageInfo<>();
        }

        final List<Long> idList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderId)
                .collect(Collectors.toList());
        return scmImageBaseService.getFileCodeListByIdAndTypePage(PageDTO.of(dto.getPageNo(), dto.getPageSize()),
                ImageBizType.SAMPLE_CTRL_SUCCESS, idList);
    }

    public List<RawDeliverVo> rawDeliveryOrder(SampleChildNoDto dto) {
        final List<SampleChildOrderRawPo> demandSampleChildRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(dto.getSampleChildOrderNo(),
                SampleRawBizType.DEMAND);

        List<PurchaseRawReceiptOrderPo> purchaseRawReceiptOrderPoList = purchaseRawReceiptOrderDao.getListBySampleChildNo(dto.getSampleChildOrderNo());

        final List<String> purchaseRawReceiptOrderNoList = Optional.ofNullable(purchaseRawReceiptOrderPoList)
                .orElse(Collections.emptyList())
                .stream()
                .map(PurchaseRawReceiptOrderPo::getPurchaseRawReceiptOrderNo)
                .collect(Collectors.toList());

        final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList = purchaseRawReceiptOrderItemDao.getListByNoList(purchaseRawReceiptOrderNoList);

        List<ProcessDeliveryOrderVo> processDeliveryOrderList = wmsRemoteService.getProcessDeliveryOrder(dto.getSampleChildOrderNo(),
                WmsEnum.DeliveryType.SAMPLE_RAW_MATERIAL);

        final Map<String, List<PurchaseRawReceiptOrderItemPo>> skuPurchaseRawItemMap = Optional.ofNullable(purchaseRawReceiptOrderItemPoList)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(PurchaseRawReceiptOrderItemPo::getSku));

        final List<RawDeliverOrderBo> rawDeliverOrderBoList = Optional.ofNullable(processDeliveryOrderList)
                .orElse(Collections.emptyList())
                .stream()
                .map(vo -> {
                    final List<ProcessDeliveryOrderVo.DeliveryProduct> products = vo.getProducts();
                    if (CollectionUtils.isEmpty(products)) {
                        return new ArrayList<RawDeliverOrderBo>();
                    }

                    return products.stream()
                            .map(product -> {
                                final RawDeliverOrderBo rawDeliverOrderBo = new RawDeliverOrderBo();
                                rawDeliverOrderBo.setSku(product.getSkuCode());
                                rawDeliverOrderBo.setSkuBatchCode(product.getBatchCode());
                                rawDeliverOrderBo.setDeliveryOrderNo(vo.getDeliveryOrderNo());
                                rawDeliverOrderBo.setDeliveryAmount(product.getAmount());
                                rawDeliverOrderBo.setDeliveryState(vo.getDeliveryState());
                                rawDeliverOrderBo.setWarehouseName(vo.getWarehouseName());

                                return rawDeliverOrderBo;
                            }).collect(Collectors.toList());
                }).flatMap(Collection::stream)
                .collect(Collectors.toList());

        final Map<String, List<RawDeliverOrderBo>> skuProcessOrderDeliverBoMap = Optional.of(rawDeliverOrderBoList)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(RawDeliverOrderBo::getSku));

        final Map<String, SampleChildOrderRawPo> skuPurchaseRawMap = Optional.ofNullable(demandSampleChildRawPoList)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(SampleChildOrderRawPo::getSku, Function.identity()));

        return skuPurchaseRawMap.keySet().stream()
                .map(sku -> {
                    final SampleChildOrderRawPo sampleChildOrderRawPo = skuPurchaseRawMap.get(sku);
                    final RawDeliverVo rawDeliverVo = new RawDeliverVo();
                    rawDeliverVo.setSku(sku);
                    rawDeliverVo.setAmount(sampleChildOrderRawPo.getDeliveryCnt());

                    final List<PurchaseRawReceiptOrderItemPo> purchaseRawReceiptOrderItemPoList1 = skuPurchaseRawItemMap.get(sku);
                    if (CollectionUtils.isNotEmpty(purchaseRawReceiptOrderItemPoList1)) {
                        final PurchaseRawReceiptOrderItemPo purchaseRawReceiptOrderItemPo = purchaseRawReceiptOrderItemPoList1.get(0);
                        final int deliverCntSum = purchaseRawReceiptOrderItemPoList1.stream()
                                .mapToInt(PurchaseRawReceiptOrderItemPo::getDeliverCnt)
                                .sum();
                        final int receiptCntSum = purchaseRawReceiptOrderItemPoList1.stream()
                                .mapToInt(PurchaseRawReceiptOrderItemPo::getReceiptCnt)
                                .sum();
                        rawDeliverVo.setSku(purchaseRawReceiptOrderItemPo.getSku());
                        rawDeliverVo.setReceiptNum(receiptCntSum);
                    }

                    final List<RawDeliverVo.DeliveryOrderVo> deliveryOrderVoList = PurchaseConverter.purchaseOrderDeliverBoToRawDeliverVo(skuProcessOrderDeliverBoMap.get(sku));
                    rawDeliverVo.setDeliveryOrderVoList(deliveryOrderVoList);

                    return rawDeliverVo;
                }).collect(Collectors.toList());
    }

    public CommonPageResult.PageInfo<SampleChildOrderResultSearchVo> searchSampleChildOrderResult(SampleChildOrderResultSearchDto dto) {
        //条件过滤
        if (null == sampleBaseService.getSampleResultSearchWhere(dto)) {
            return new CommonPageResult.PageInfo<>();
        }

        CommonPageResult.PageInfo<SampleChildOrderResultSearchVo> pageResult = sampleChildOrderResultDao.searchSampleChildOrderResult(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto);

        final List<SampleChildOrderResultSearchVo> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageResult;
        }

        List<String> sampleParentOrderNoList = records.stream().map(SampleChildOrderResultSearchVo::getSampleParentOrderNo).collect(Collectors.toList());
        List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentOrderNoList);
        List<Long> sampleParentOrderIdList = sampleParentOrderPoList.stream().map(SampleParentOrderPo::getSampleParentOrderId).collect(Collectors.toList());
        Map<String, Long> sampleParentOrderNoMap = sampleParentOrderPoList.stream().collect(Collectors.toMap(SampleParentOrderPo::getSampleParentOrderNo, SampleParentOrderPo::getSampleParentOrderId));

        //关联单据
        List<String> relateOrderNoList = records.stream().map(SampleChildOrderResultSearchVo::getRelateOrderNo).collect(Collectors.toList());
        //获取打样成功关联收货单
        ReceiveOrderGetDto receiveOrderGetDto = new ReceiveOrderGetDto();
        receiveOrderGetDto.setReceiveOrderNoList(relateOrderNoList);
        Map<String, ReceiveOrderForScmVo> receiveOrderForScmVoMap = wmsRemoteService.getReceiveOrderList(receiveOrderGetDto).stream().collect(Collectors.toMap(ReceiveOrderForScmVo::getReceiveOrderNo, receiveOrderForScmVo -> receiveOrderForScmVo));

        //获取打样失败关联
        Map<String, SampleReturnOrderPo> sampleReturnOrderPoMap = sampleReturnOrderDao.getListByNoList(relateOrderNoList).stream().collect(Collectors.toMap(SampleReturnOrderPo::getSampleReturnOrderNo, sampleReturnOrderPo -> sampleReturnOrderPo));

        //获取失败闪售
        Map<String, List<SampleChildOrderPo>> sampleChildOrderPoMap = sampleChildOrderDao.getMapByParentNoList(relateOrderNoList);

        //获取供应商信息
        List<String> supplierCodeList = records.stream().map(SampleChildOrderResultSearchVo::getSupplierCode).distinct().collect(Collectors.toList());
        Map<String, SupplierPo> supplierPoMap = supplierDao.getBySupplierCodeList(supplierCodeList).stream().collect(Collectors.toMap(SupplierPo::getSupplierCode, supplierPo -> supplierPo));

        //获取第一张图片
        if (CollectionUtils.isNotEmpty(sampleParentOrderIdList)) {
            Map<Long, String> fileCodeMap = scmImageBaseService.getOneFileCodeMapByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER, sampleParentOrderIdList);
            List<String> fileCodeList = new ArrayList<>(fileCodeMap.values());
            List<FileVo> fileVoList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(fileCodeList)) {
                ResultList<FileVo> fileVoResultList = bssRemoteService.getFileList(fileCodeList);
                fileVoList = fileVoResultList.getList();
            }
            List<FileVo> finalFileVoList = fileVoList;

            records.forEach(record -> {
                String fileCode = fileCodeMap.get(sampleParentOrderNoMap.get(record.getSampleParentOrderNo()));
                if (StringUtils.isNotBlank(fileCode)) {
                    finalFileVoList.stream().filter(customer -> fileCode.equals(customer.getFileCode())).findAny().ifPresent(fileVo -> record.setContrastFileUrl(fileVo.getFileUrl()));
                }
                if (SampleResult.SAMPLE_SUCCESS.equals(record.getSampleResult())) {
                    ReceiveOrderForScmVo receiveOrderForScmVo = receiveOrderForScmVoMap.get(record.getRelateOrderNo());
                    if (receiveOrderForScmVo != null) {
                        record.setRelateOrderAmount(receiveOrderForScmVo.getDeliveryAmount());
                        record.setHandlesTime(receiveOrderForScmVo.getCreateTime());
                    }
                }

                if (SampleResult.SAMPLE_RETURN.equals(record.getSampleResult())) {
                    SampleReturnOrderPo sampleReturnOrderPo = sampleReturnOrderPoMap.get(record.getRelateOrderNo());
                    if (sampleReturnOrderPo != null) {
                        record.setRelateOrderAmount(sampleReturnOrderPo.getReturnCnt());
                        record.setHandlesTime(sampleReturnOrderPo.getCreateTime());
                    }
                }

                if (SampleResult.FAIL_SALE.equals(record.getSampleResult())) {
                    List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderPoMap.get(record.getRelateOrderNo());
                    if (CollectionUtils.isNotEmpty(sampleChildOrderPoList)) {
                        record.setRelateOrderAmount(sampleChildOrderPoList.stream().mapToInt(SampleChildOrderPo::getPurchaseCnt).sum());//取采购数
                        record.setHandlesTime(sampleChildOrderPoList.stream().sorted(Comparator.comparing(SampleChildOrderPo::getCreateTime)).collect(Collectors.toList()).get(0).getCreateTime());
                    }
                }
                if (StringUtils.isNotBlank(record.getSupplierCode()) && supplierPoMap.containsKey(record.getSupplierCode())) {
                    record.setSupplierType(supplierPoMap.get(record.getSupplierCode()).getSupplierType());
                }

            });
        }
        return pageResult;
    }


    public void sampleSelection(SampleSelectionDto dto) {
        if (dto.getSuccessNum() <= 0 && dto.getReturnNum() <= 0 && dto.getSaleNum() <= 0) {
            throw new ParamIllegalException("请填写选样结果大于0的数！");
        }

        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getByIdVersion(dto.getSampleChildOrderId(), dto.getVersion());
        Assert.notNull(sampleChildOrderPo, "找不到对应的样品采购子单，打样操作失败");

        SampleChildOrderChangePo sampleChildOrderChangePo = sampleChildOrderChangeDao.getByChildOrderId(sampleChildOrderPo.getSampleChildOrderId());
        Assert.notNull(sampleChildOrderChangePo, "找不到对应的样品采购子单,打样操作失败");

        final SampleParentOrderPo sampleParentOrderPo = sampleParentOrderDao.getByParentNo(sampleChildOrderPo.getSampleParentOrderNo());
        Assert.notNull(sampleParentOrderPo, "找不到对应的样品采购母单,打样操作失败");

        final SampleParentOrderChangePo sampleParentOrderChangePo = sampleParentOrderChangeDao.getByParentId(sampleParentOrderPo.getSampleParentOrderId());
        Assert.notNull(sampleParentOrderChangePo, "找不到对应的样品采购母单,打样操作失败");

        if (SampleDevType.DEFECTIVE_REOPEN.equals(sampleChildOrderPo.getSampleDevType()) || SampleDevType.SUPPLY_RECOMMEND.equals(sampleChildOrderPo.getSampleDevType())) {
            if ((dto.getSuccessNum() + dto.getReturnNum() + dto.getSaleNum()) != sampleChildOrderPo.getPurchaseCnt()) {
                throw new ParamIllegalException("填写选样结果三个数的总和不等样品子单的采购数！");
            }
        } else {
            if ((dto.getSuccessNum() + dto.getReturnNum() + dto.getSaleNum()) != sampleChildOrderPo.getReceiptCnt()) {
                throw new ParamIllegalException("填写选样结果三个数的总和不等样品子单的收货数！");
            }
        }

        SampleOrderStatus targetStatus = sampleChildOrderPo.getSampleOrderStatus().toProofingFail();

        //打样成功验证
        Map<String, String> skuBatchCodeMap = new HashMap<>();
        if (dto.getSuccessNum() > 0) {
            if (StringUtils.isBlank(dto.getSku())) {
                throw new ParamIllegalException("正品sku不能为空");
            }
            if (dto.getSku().length() > 100) {
                throw new ParamIllegalException("原料sku长度不能超过100个字符");
            }
            if (dto.getCostPrice() == null) {
                throw new ParamIllegalException("样品成本单价不能为空");
            }
            if (CollectionUtils.isEmpty(dto.getFileCodeList())) {
                throw new ParamIllegalException("选中样实拍图不能为空");
            }
            if (SampleDevType.confirmSubmitAudited(sampleChildOrderPo.getSampleDevType())) {
                if (CollectionUtils.isEmpty(dto.getSampleRawList())) {
                    throw new ParamIllegalException("原料产品明细列表不能为空");
                }
                if (CollectionUtils.isEmpty(dto.getSampleProcessList())) {
                    throw new ParamIllegalException("样品工序列表不能为空");
                }
            }
            // 获取sku批次码
            final SkuBatchCreateDto skuBatchCreateDto = new SkuBatchCreateDto();
            skuBatchCreateDto.setPurchaseChildOrderNo(sampleChildOrderPo.getSampleChildOrderNo());
            skuBatchCreateDto.setSkuCodeList(Collections.singletonList(dto.getSku()));
            skuBatchCreateDto.setSupplierCode(sampleChildOrderPo.getSupplierCode());
            skuBatchCreateDto.setSupplierName(sampleChildOrderPo.getSupplierName());
            skuBatchCodeMap = wmsRemoteService.batchCreateBatchCode(skuBatchCreateDto);

            targetStatus = sampleChildOrderPo.getSampleOrderStatus().toSelected();
        }

        //创建数据执行事务
        sampleBaseService.createSuccessSample(dto,
                sampleChildOrderPo,
                sampleParentOrderPo,
                sampleChildOrderChangePo,
                sampleParentOrderChangePo,
                targetStatus,
                skuBatchCodeMap);


    }

    /**
     * 编辑已选样和已结算状态的样品子单可以编辑选样信息
     *
     * @author ChenWenLong
     * @date 2023/4/25 09:45
     */
    @Transactional(rollbackFor = Exception.class)
    public void editChildSampleOrder(SampleChildEditDetailDto dto) {
        final SampleChildOrderPo sampleChildOrderPo = sampleChildOrderDao.getOneByChildOrderNo(dto.getSampleChildOrderNo());
        if (null == sampleChildOrderPo) {
            throw new BizException("数据已被修改或删除，请刷新页面后重试！");
        }
        if (!SampleOrderStatus.editChildCheck(sampleChildOrderPo.getSampleOrderStatus())) {
            throw new BizException("样品需求子单不在【{}、{}】状态，编辑子单失败，请刷新后重试！",
                    SampleOrderStatus.SELECTED.getRemark(),
                    SampleOrderStatus.SETTLE.getRemark());
        }

        //编辑生产属性
        final List<SampleChildOrderInfoPo> sampleChildOrderInfoDtoList = SampleConverter.childOrderInfoDtoListToPo(dto.getSampleChildOrderInfoList(), sampleChildOrderPo.getSampleChildOrderNo());
        final List<SampleChildOrderInfoPo> sampleChildOrderInfoPoList = sampleChildOrderInfoDao.getInfoListByChildNo(sampleChildOrderPo.getSampleChildOrderNo());
        CompareResult<SampleChildOrderInfoPo> infoResult = DataCompareUtil.compare(sampleChildOrderInfoDtoList, sampleChildOrderInfoPoList, SampleChildOrderInfoPo::getSampleChildOrderInfoId);
        List<SampleChildOrderInfoPo> collect = new ArrayList<>(infoResult.getNewItems());
        sampleChildOrderInfoDao.insertBatch(collect);
        sampleChildOrderInfoDao.updateBatchByIdVersion(infoResult.getExistingItems());
        sampleChildOrderInfoDao.removeBatchByIds(infoResult.getDeletedItems());


        //编辑原料bom表信息
        final List<SampleChildOrderRawPo> sampleChildOrderRawPoList = sampleChildOrderRawDao.getListByChildNoAndType(sampleChildOrderPo.getSampleChildOrderNo(), SampleRawBizType.FORMULA);
        final List<SampleChildOrderRawPo> sampleChildOrderRawPoDtoList = SampleConverter.editRawDtoToPo(sampleChildOrderPo, dto.getSampleRawList());
        CompareResult<SampleChildOrderRawPo> rawResult = DataCompareUtil.compare(sampleChildOrderRawPoDtoList, sampleChildOrderRawPoList, SampleChildOrderRawPo::getSampleChildOrderRawId);
        List<SampleChildOrderRawPo> rawCollect = new ArrayList<>(rawResult.getNewItems());
        sampleChildOrderRawDao.insertBatch(rawCollect);
        sampleChildOrderRawDao.updateBatchByIdVersion(rawResult.getExistingItems());
        sampleChildOrderRawDao.removeBatchByIds(rawResult.getDeletedItems());


        //编辑工序信息
        final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoList = sampleChildOrderProcessDao.getListByNo(sampleChildOrderPo.getSampleChildOrderNo());
        final List<SampleChildOrderProcessPo> sampleChildOrderProcessPoDtoList = SampleConverter.editProcessDtoToPo(sampleChildOrderPo, dto.getSampleProcessList());
        CompareResult<SampleChildOrderProcessPo> processResult = DataCompareUtil.compare(sampleChildOrderProcessPoDtoList, sampleChildOrderProcessPoList, SampleChildOrderProcessPo::getSampleChildOrderProcessId);
        List<SampleChildOrderProcessPo> processCollect = new ArrayList<>(processResult.getNewItems());
        sampleChildOrderProcessDao.insertBatch(processCollect);
        sampleChildOrderProcessDao.updateBatchByIdVersion(processResult.getExistingItems());
        sampleChildOrderProcessDao.removeBatchByIds(processResult.getDeletedItems());


        //编辑工序描述信息
        final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoList = sampleChildOrderProcessDescDao.getListByNo(sampleChildOrderPo.getSampleChildOrderNo());
        final List<SampleChildOrderProcessDescPo> sampleChildOrderProcessDescPoDtoList = SampleConverter.editProcessDescDtoToPo(sampleChildOrderPo, dto.getSampleProcessDescList());
        CompareResult<SampleChildOrderProcessDescPo> processDescResult = DataCompareUtil.compare(sampleChildOrderProcessDescPoDtoList, sampleChildOrderProcessDescPoList, SampleChildOrderProcessDescPo::getSampleChildOrderProcessDescId);
        List<SampleChildOrderProcessDescPo> processDescCollect = new ArrayList<>(processDescResult.getNewItems());
        sampleChildOrderProcessDescDao.insertBatch(processDescCollect);
        sampleChildOrderProcessDescDao.updateBatchByIdVersion(processDescResult.getExistingItems());
        sampleChildOrderProcessDescDao.removeBatchByIds(processDescResult.getDeletedItems());


    }

    public List<SampleChildOrderSkuVo> searchSampleChildOrderSku(SampleChildOrderSkuDto dto) {
        List<SampleChildOrderSkuVo> list = new ArrayList<>();
        List<PlmVariantVo> variantAttr = plmRemoteService.getVariantAttr(dto.getSkuList());
        for (PlmVariantVo plmVariantVo : variantAttr) {
            SampleChildOrderSkuVo sampleChildOrderSkuVo = new SampleChildOrderSkuVo();
            sampleChildOrderSkuVo.setSku(plmVariantVo.getSkuCode());
            sampleChildOrderSkuVo.setSkuEncode(plmVariantVo.getSkuEncode());
            sampleChildOrderSkuVo.setVariantSkuList(plmVariantVo.getVariantSkuList());
            list.add(sampleChildOrderSkuVo);
        }
        return list;
    }

    /**
     * 添加生产标签
     *
     * @author ChenWenLong
     * @date 2023/5/12 16:29
     */
    @Transactional(rollbackFor = Exception.class)
    public void addProduceLabel(SampleAddProduceLabelDto dto) {
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(dto.getSampleChildOrderNoList());
        if (CollectionUtils.isEmpty(sampleChildOrderPoList)) {
            throw new ParamIllegalException("样品打样单不存在");
        }

        List<SampleChildOrderPo> sampleChildOrderPos = sampleChildOrderPoList.stream()
                .map(po -> {
                    po.getSampleOrderStatus().toProduceLabelCheck();
                    SampleChildOrderPo sampleChildOrderPo = new SampleChildOrderPo();
                    sampleChildOrderPo.setSampleProduceLabel(dto.getSampleProduceLabel());
                    sampleChildOrderPo.setSampleChildOrderId(po.getSampleChildOrderId());
                    sampleChildOrderPo.setVersion(po.getVersion());
                    return sampleChildOrderPo;
                }).collect(Collectors.toList());

        sampleChildOrderDao.updateBatchByIdVersion(sampleChildOrderPos);
    }
}
