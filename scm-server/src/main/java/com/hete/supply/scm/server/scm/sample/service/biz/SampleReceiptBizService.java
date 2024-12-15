package com.hete.supply.scm.server.scm.sample.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.bss.api.oss.entity.vo.FileVo;
import com.hete.supply.scm.api.scm.entity.dto.SampleReceiptSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleReceiptOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.remote.dubbo.BssRemoteService;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.sample.converter.SampleConverter;
import com.hete.supply.scm.server.scm.sample.converter.SampleReceiptConverter;
import com.hete.supply.scm.server.scm.sample.dao.*;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleReceiptDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleReceiptNoDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleReceiptOrderItemDto;
import com.hete.supply.scm.server.scm.sample.entity.dto.SampleStatusDto;
import com.hete.supply.scm.server.scm.sample.entity.po.*;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptDetailVo;
import com.hete.supply.scm.server.scm.sample.entity.vo.SampleReceiptSearchVo;
import com.hete.supply.scm.server.scm.sample.handler.SampleStatusHandler;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.sample.service.base.SampleReceiptBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderDao;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.exception.ParamIllegalException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.api.result.ResultList;
import com.hete.support.consistency.core.service.ConsistencySendMqService;
import com.hete.support.core.holder.GlobalContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/13 23:58
 */
@Service
@RequiredArgsConstructor
public class SampleReceiptBizService {
    private final SampleReceiptOrderDao sampleReceiptOrderDao;
    private final SampleReceiptOrderItemDao sampleReceiptOrderItemDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final SampleDeliverOrderDao sampleDeliverOrderDao;
    private final SupplierBaseService supplierBaseService;
    private final SampleReceiptBaseService sampleReceiptBaseService;
    private final SampleChildOrderChangeDao sampleChildOrderChangeDao;
    private final LogBaseService logBaseService;
    private final SampleParentOrderDao sampleParentOrderDao;
    private final SampleBaseService sampleBaseService;
    private final ConsistencySendMqService consistencySendMqService;
    private final SampleParentOrderChangeDao sampleParentOrderChangeDao;
    private final ScmImageBaseService scmImageBaseService;
    private final BssRemoteService bssRemoteService;

    public CommonPageResult.PageInfo<SampleReceiptSearchVo> searchSampleReceipt(SampleReceiptSearchDto dto) {

        final List<String> sampleReceiptOrderNoList = sampleReceiptBaseService.getSampleReceiptOrderNoList(dto);
        if ((StringUtils.isNotBlank(dto.getSampleChildOrderNo()) || StringUtils.isNotBlank(dto.getSpu()))
                && CollectionUtils.isEmpty(sampleReceiptOrderNoList)) {
            return new CommonPageResult.PageInfo<>();
        }

        final CommonPageResult.PageInfo<SampleReceiptSearchVo> pageInfo = sampleReceiptOrderDao.searchSampleReceipt(PageDTO.of(dto.getPageNo(), dto.getPageSize()),
                dto, sampleReceiptOrderNoList);

        final List<SampleReceiptSearchVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageInfo;
        }

        //获取打样第一张图片
        List<String> sampleReceiptOrderNos = records.stream().map(SampleReceiptSearchVo::getSampleReceiptOrderNo).distinct().collect(Collectors.toList());
        List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListBySampleReceiptOrderNoList(sampleReceiptOrderNos);
        //获取样品收货单明细进行分组
        Map<String, List<SampleReceiptOrderItemPo>> itemPoGroup = sampleReceiptOrderItemPoList.stream().collect(Collectors.groupingBy(SampleReceiptOrderItemPo::getSampleReceiptOrderNo));
        //获取样品收货单明细样品采购子单号
        List<String> sampleParentOrderNoList = sampleReceiptOrderItemPoList.stream().map(SampleReceiptOrderItemPo::getSampleParentOrderNo).distinct().collect(Collectors.toList());

        List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentOrderNoList);
        //获取采购子单号和采购子单ID
        Map<String, Long> sampleParentOrderPoMap = sampleParentOrderPoList.stream().collect(Collectors.toMap(SampleParentOrderPo::getSampleParentOrderNo, SampleParentOrderPo::getSampleParentOrderId));
        List<Long> sampleParentOrderIdList = sampleParentOrderPoList.stream().map(SampleParentOrderPo::getSampleParentOrderId).distinct().collect(Collectors.toList());
        //采购子单ID来获取首图
        Map<Long, String> fileCodeMap = new HashMap<>();
        List<FileVo> fileVoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sampleParentOrderIdList)) {
            fileCodeMap = scmImageBaseService.getOneFileCodeMapByIdAndType(ImageBizType.SAMPLE_PARENT_ORDER, sampleParentOrderIdList);
            List<String> fileCodeList = new ArrayList<>(fileCodeMap.values());
            if (CollectionUtil.isNotEmpty(fileCodeList)) {
                ResultList<FileVo> fileVoResultList = bssRemoteService.getFileList(fileCodeList);
                fileVoList = fileVoResultList.getList();
            }
        }
        Map<Long, String> finalFileCodeMap = fileCodeMap;
        List<FileVo> finalFileVoList = fileVoList;

        records.forEach(vo -> {
            if (itemPoGroup.containsKey(vo.getSampleReceiptOrderNo())) {
                List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPo = itemPoGroup.get(vo.getSampleReceiptOrderNo());
                if (CollectionUtil.isNotEmpty(sampleReceiptOrderItemPo)) {
                    String fileCode = finalFileCodeMap.get(sampleParentOrderPoMap.get(sampleReceiptOrderItemPo.get(0).getSampleParentOrderNo()));
                    if (StringUtils.isNotBlank(fileCode)) {
                        finalFileVoList.stream().filter(customer -> fileCode.equals(customer.getFileCode())).findAny().ifPresent(fileVo -> vo.setContrastFileUrl(fileVo.getFileUrl()));
                    }
                }
            }
        });

        supplierBaseService.batchSetSupplierGrade(records);
        return pageInfo;
    }

    public SampleReceiptDetailVo sampleReceiptDetail(SampleReceiptNoDto dto) {
        SampleReceiptOrderPo sampleReceiptOrderPo = sampleReceiptOrderDao.getOneByReceiptOrderNo(dto.getSampleReceiptOrderNo());
        if (null == sampleReceiptOrderPo) {
            final List<SampleReceiptOrderPo> sampleReceiptOrderPoList = sampleReceiptOrderDao.getListByTrackingNo(dto.getSampleReceiptOrderNo());
            if (CollectionUtils.isEmpty(sampleReceiptOrderPoList) || sampleReceiptOrderPoList.size() > 1) {
                throw new ParamIllegalException("当前单号{}无法查找到样品收货单，获取样品收货单详情失败",
                        dto.getSampleReceiptOrderNo());
            }
            sampleReceiptOrderPo = sampleReceiptOrderPoList.get(0);
        }

        Assert.notNull(sampleReceiptOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListByReceiptNo(sampleReceiptOrderPo.getSampleReceiptOrderNo());

        final List<String> sampleChildNoList = sampleReceiptOrderItemPoList.stream()
                .map(SampleReceiptOrderItemPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildNoList);

        return SampleReceiptConverter.receiptPoToDetailVo(sampleReceiptOrderPo, sampleReceiptOrderItemPoList, sampleChildOrderPoList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmReceipt(SampleReceiptDto dto) {
        this.checkReceiptCnt(dto.getSampleReceiptOrderItemList());

        final SampleReceiptOrderPo sampleReceiptOrderPo = sampleReceiptOrderDao.getByIdVersion(dto.getSampleReceiptOrderId(), dto.getVersion());
        Assert.notNull(sampleReceiptOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<SampleReceiptOrderItemPo> sampleReceiptOrderItemPoList = sampleReceiptOrderItemDao.getListByReceiptNo(sampleReceiptOrderPo.getSampleReceiptOrderNo());
        Assert.notEmpty(sampleReceiptOrderItemPoList, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final SampleDeliverOrderPo sampleDeliverOrderPo = sampleDeliverOrderDao.getDeliverPoByNo(sampleReceiptOrderPo.getSampleDeliverOrderNo());
        Assert.notNull(sampleDeliverOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<SampleReceiptOrderItemDto> sampleReceiptOrderItemList = dto.getSampleReceiptOrderItemList();
        final List<String> sampleChildOrderNoList = sampleReceiptOrderItemList.stream()
                .map(SampleReceiptOrderItemDto::getSampleChildOrderNo)
                .distinct()
                .collect(Collectors.toList());
        Assert.equals(sampleChildOrderNoList.size(), sampleReceiptOrderItemList.size(),
                () -> new BizException("样品收货明细错误，同个样品子单存在多条收货明细记录"));

        final List<String> receiptChildOrderNoList = sampleReceiptOrderItemPoList.stream()
                .map(SampleReceiptOrderItemPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        if (!receiptChildOrderNoList.containsAll(sampleChildOrderNoList)) {
            throw new BizException("样品收货单:{}，收货信息有误，传入的样品子单号为：{}", sampleReceiptOrderPo.getSampleReceiptOrderNo(),
                    sampleChildOrderNoList);
        }

        final int totalReceipt = sampleReceiptOrderItemList.stream()
                .mapToInt(SampleReceiptOrderItemDto::getReceiptCnt)
                .sum();

        final SampleReceiptOrderStatus sampleReceiptOrderStatus = sampleReceiptOrderPo.getReceiptOrderStatus().toReceivedSample();
        sampleReceiptOrderPo.setReceiptOrderStatus(sampleReceiptOrderStatus);
        sampleReceiptOrderPo.setReceiptTime(LocalDateTimeUtil.now());
        sampleReceiptOrderPo.setReceiptUser(GlobalContext.getUserKey());
        sampleReceiptOrderPo.setReceiptUsername(GlobalContext.getUsername());
        sampleReceiptOrderPo.setTotalReceipt(totalReceipt);
        sampleReceiptOrderDao.updateByIdVersion(sampleReceiptOrderPo);

        final SampleDeliverOrderStatus sampleDeliverOrderStatus = sampleDeliverOrderPo.getSampleDeliverOrderStatus().toReceivedSample();
        sampleDeliverOrderPo.setSampleDeliverOrderStatus(sampleDeliverOrderStatus);
        sampleDeliverOrderDao.updateByIdVersion(sampleDeliverOrderPo);
        logBaseService.simpleLog(LogBizModule.SUPPLIER_SAMPLE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleDeliverOrderPo.getSampleDeliverOrderNo(), sampleDeliverOrderStatus.getRemark(), Collections.emptyList());

        List<SampleReceiptOrderItemPo> updateSampleReceiptOrderItemPoList = SampleConverter.receiptItemDtoToPo(sampleReceiptOrderItemList);
        sampleReceiptOrderItemDao.updateBatchByIdVersion(updateSampleReceiptOrderItemPoList);

        final Map<String, Integer> childNoDeliverCntMap = sampleReceiptOrderItemList.stream()
                .collect(Collectors.toMap(SampleReceiptOrderItemDto::getSampleChildOrderNo, SampleReceiptOrderItemDto::getReceiptCnt));

        List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildOrderNoList);
        sampleChildOrderPoList.forEach(po -> {
            final SampleOrderStatus sampleOrderStatus = po.getSampleOrderStatus().toWaitSample();
            po.setSampleOrderStatus(sampleOrderStatus);
            po.setReceiptCnt(childNoDeliverCntMap.get(po.getSampleChildOrderNo()));

            final SampleStatusDto sampleStatusDto = new SampleStatusDto();
            sampleStatusDto.setSpuCode(po.getSpu());
            sampleStatusDto.setSampleOrderStatus(sampleOrderStatus);
            sampleStatusDto.setKey(po.getSampleChildOrderNo());
            sampleStatusDto.setUserKey(GlobalContext.getUserKey());
            sampleStatusDto.setUsername(GlobalContext.getUsername());
            consistencySendMqService.execSendMq(SampleStatusHandler.class, sampleStatusDto);
        });
        sampleChildOrderDao.updateBatchByIdVersion(sampleChildOrderPoList);

        // 更新母单状态
        final List<String> sampleParentOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleParentOrderNo)
                .collect(Collectors.toList());
        final List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentOrderNoList);
        List<SampleParentOrderPo> updatePoList = new ArrayList<>();
        sampleParentOrderPoList.forEach(po -> {
            final SampleOrderStatus earliestStatus = sampleBaseService.getEarliestStatus(po.getSampleParentOrderNo(), Collections.singletonList(SampleOrderStatus.WAIT_SAMPLE));
            if (!po.getSampleOrderStatus().equals(earliestStatus)) {
                po.setSampleOrderStatus(earliestStatus);
                updatePoList.add(po);
            }
        });
        sampleParentOrderDao.updateBatchByIdVersion(updatePoList);


        // 更新change
        final LocalDateTime now = LocalDateTime.now();
        final List<Long> parentIdSet = sampleParentOrderPoList.stream()
                .map(SampleParentOrderPo::getSampleParentOrderId)
                .distinct()
                .collect(Collectors.toList());

        final List<SampleParentOrderChangePo> sampleParentOrderChangePoList = sampleParentOrderChangeDao.getByParentIdList(parentIdSet);
        sampleParentOrderChangePoList.forEach(po -> po.setReceiptTime(now));
        sampleParentOrderChangeDao.updateBatchByIdVersion(sampleParentOrderChangePoList);

        final Set<Long> childIdSet = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleChildOrderId)
                .collect(Collectors.toSet());
        final List<SampleChildOrderChangePo> sampleChildOrderChangePoList = sampleChildOrderChangeDao.getByChildOrderIdList(childIdSet);
        sampleChildOrderChangePoList.forEach(po -> {
            po.setSampleDeliverOrderNo(sampleReceiptOrderPo.getSampleDeliverOrderNo());
            po.setSampleReceiptOrderNo(sampleReceiptOrderPo.getSampleReceiptOrderNo());
            po.setReceiptTime(now);
        });
        sampleChildOrderChangeDao.updateBatchByIdVersion(sampleChildOrderChangePoList);

        logBaseService.simpleLog(LogBizModule.SAMPLE_RECEIPT_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleDeliverOrderPo.getSampleDeliverOrderNo(), sampleReceiptOrderStatus.getRemark(), Collections.emptyList());
    }

    private void checkReceiptCnt(List<SampleReceiptOrderItemDto> sampleReceiptOrderItemList) {
        sampleReceiptOrderItemList.forEach(item -> Assert.isTrue(item.getReceiptCnt() <= item.getDeliverCnt(),
                () -> new ParamIllegalException("收货数不能大于发货数")));
    }
}
