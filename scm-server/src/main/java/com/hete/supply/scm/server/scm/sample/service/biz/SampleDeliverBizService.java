package com.hete.supply.scm.server.scm.sample.service.biz;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.hete.supply.scm.api.scm.entity.dto.SampleDeliverSearchDto;
import com.hete.supply.scm.api.scm.entity.enums.SampleDeliverOrderStatus;
import com.hete.supply.scm.api.scm.entity.enums.SampleOrderStatus;
import com.hete.supply.scm.common.constant.LogBizModule;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.sample.converter.SampleConverter;
import com.hete.supply.scm.server.scm.sample.dao.SampleChildOrderDao;
import com.hete.supply.scm.server.scm.sample.dao.SampleParentOrderDao;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleParentOrderPo;
import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import com.hete.supply.scm.server.scm.service.base.LogBaseService;
import com.hete.supply.scm.server.supplier.dao.ShippingMarkDao;
import com.hete.supply.scm.server.supplier.entity.po.ShippingMarkPo;
import com.hete.supply.scm.server.supplier.purchase.service.base.ShippingMarkBaseService;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderDao;
import com.hete.supply.scm.server.supplier.sample.dao.SampleDeliverOrderItemDao;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverIdAndVersionDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverNoListDto;
import com.hete.supply.scm.server.supplier.sample.entity.dto.SampleDeliverOrderNoDto;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderItemPo;
import com.hete.supply.scm.server.supplier.sample.entity.po.SampleDeliverOrderPo;
import com.hete.supply.scm.server.supplier.sample.entity.vo.*;
import com.hete.supply.scm.server.supplier.sample.service.base.SampleReturnBaseService;
import com.hete.support.api.exception.BizException;
import com.hete.support.api.result.CommonPageResult;
import com.hete.support.core.holder.GlobalContext;
import com.hete.support.core.util.FormatStringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2022/11/14 11:28
 */
@Service
@RequiredArgsConstructor
public class SampleDeliverBizService {
    private final SampleDeliverOrderDao sampleDeliverOrderDao;
    private final SampleDeliverOrderItemDao sampleDeliverOrderItemDao;
    private final SampleChildOrderDao sampleChildOrderDao;
    private final SampleParentOrderDao sampleParentOrderDao;
    private final SampleBaseService sampleBaseService;
    private final SampleReturnBaseService sampleReturnBaseService;
    private final LogBaseService logBaseService;
    private final ShippingMarkBaseService shippingMarkBaseService;
    private final ShippingMarkDao shippingMarkDao;

    public CommonPageResult.PageInfo<SampleDeliverVo> searchDeliver(SampleDeliverSearchDto dto) {
        List<String> deliverNoList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getSampleParentOrderNo())) {
            List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByLikeParentNo(dto.getSampleParentOrderNo());
            if (CollectionUtils.isEmpty(sampleDeliverOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            deliverNoList.addAll(sampleDeliverOrderItemPoList.stream()
                    .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                    .collect(Collectors.toList()));
        }

        if (StringUtils.isNotBlank(dto.getSampleChildOrderNo())) {
            List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByLikeChildNo(dto.getSampleChildOrderNo());
            if (CollectionUtils.isEmpty(sampleDeliverOrderItemPoList)) {
                return new CommonPageResult.PageInfo<>();
            }
            deliverNoList.addAll(sampleDeliverOrderItemPoList.stream()
                    .map(SampleDeliverOrderItemPo::getSampleDeliverOrderNo)
                    .collect(Collectors.toList()));
        }

        final CommonPageResult.PageInfo<SampleDeliverVo> pageInfo = sampleDeliverOrderDao.searchDeliver(PageDTO.of(dto.getPageNo(), dto.getPageSize()), dto, deliverNoList);
        final List<SampleDeliverVo> records = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return pageInfo;
        }

        final List<String> sampleDeliverOrderNoList = records.stream()
                .map(SampleDeliverVo::getSampleDeliverOrderNo)
                .collect(Collectors.toList());

        final List<String> shippingMarkNoList = records.stream()
                .map(SampleDeliverVo::getShippingMarkNo)
                .collect(Collectors.toList());

        final Map<String, List<SampleDeliverOrderItemPo>> deliverOrderNoItemPoMap = sampleDeliverOrderItemDao.getMapByDeliverOrderNoList(sampleDeliverOrderNoList);
        final List<ShippingMarkPo> shippingMarkPoList = shippingMarkDao.getListByNoList(shippingMarkNoList);
        final Map<String, ShippingMarkPo> shippingMarkNoMap = shippingMarkPoList.stream()
                .collect(Collectors.toMap(ShippingMarkPo::getShippingMarkNo, Function.identity()));

        records.forEach(record -> {
            final List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = deliverOrderNoItemPoMap.get(record.getSampleDeliverOrderNo());
            final SampleDeliverOrderItemPo sampleDeliverOrderItemPo = sampleDeliverOrderItemPoList.get(0);
            record.setSampleChildOrderNo(sampleDeliverOrderItemPo.getSampleChildOrderNo());
            record.setWarehouseTypeList(FormatStringUtil.string2List(record.getWarehouseTypes(), ","));

            final ShippingMarkPo shippingMarkPo = shippingMarkNoMap.get(record.getShippingMarkNo());
            if (null != shippingMarkPo) {
                record.setShippingMarkStatus(shippingMarkPo.getShippingMarkStatus());
            }
        });
        return pageInfo;
    }

    public SampleDeliverDetailVo deliverDetail(SampleDeliverOrderNoDto dto) {
        SampleDeliverOrderPo sampleDeliverOrderPo = sampleDeliverOrderDao.getDeliverPoByNo(dto.getSampleDeliverOrderNo());
        Assert.notNull(sampleDeliverOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByDeliverOrderNo(sampleDeliverOrderPo.getSampleDeliverOrderNo());

        final SampleDeliverDetailVo sampleDeliverDetailVo = SampleConverter.deliverPoToDetailVo(sampleDeliverOrderPo, sampleDeliverOrderItemPoList);
        final List<String> childOrderNoList = sampleDeliverOrderItemPoList.stream()
                .map(SampleDeliverOrderItemPo::getSampleChildOrderNo)
                .collect(Collectors.toList());

        final List<SampleReturnSimpleVo> sampleReturnSimpleVoList = sampleReturnBaseService.getSimpleVoListByChildList(childOrderNoList);
        sampleDeliverDetailVo.setSampleReturnSimpleVoList(sampleReturnSimpleVoList);


        return sampleDeliverDetailVo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelDeliver(SampleDeliverIdAndVersionDto dto) {
        final SampleDeliverOrderPo sampleDeliverOrderPo = sampleDeliverOrderDao.getByIdVersion(dto.getSampleDeliverOrderId(), dto.getVersion());
        Assert.notNull(sampleDeliverOrderPo, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemDao.getListByDeliverOrderNo(sampleDeliverOrderPo.getSampleDeliverOrderNo());
        Assert.notEmpty(sampleDeliverOrderItemPoList, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<String> sampleChildOrderNoList = sampleDeliverOrderItemPoList.stream()
                .map(SampleDeliverOrderItemPo::getSampleChildOrderNo)
                .collect(Collectors.toList());
        final List<SampleChildOrderPo> sampleChildOrderPoList = sampleChildOrderDao.getChildOrderListByChildNoList(sampleChildOrderNoList);
        Assert.notEmpty(sampleChildOrderPoList, () -> new BizException("数据已被修改或删除，请刷新页面后重试！"));
        final List<String> sampleParentOrderNoList = sampleChildOrderPoList.stream()
                .map(SampleChildOrderPo::getSampleParentOrderNo)
                .collect(Collectors.toList());
        final List<SampleParentOrderPo> sampleParentOrderPoList = sampleParentOrderDao.getByParentNoList(sampleParentOrderNoList);
        shippingMarkBaseService.checkDeliverNoHasShippingMark(sampleDeliverOrderPo.getSampleDeliverOrderNo());

        final SampleDeliverOrderStatus sampleDeliverOrderStatus = sampleDeliverOrderPo.getSampleDeliverOrderStatus().toCanceled();
        sampleDeliverOrderPo.setSampleDeliverOrderStatus(sampleDeliverOrderStatus);
        sampleDeliverOrderDao.updateByIdVersion(sampleDeliverOrderPo);
        logBaseService.simpleLog(LogBizModule.SUPPLIER_SAMPLE_DELIVER_SIMPLE, ScmConstant.PURCHASE_LOG_VERSION,
                sampleDeliverOrderPo.getSampleDeliverOrderNo(), sampleDeliverOrderStatus.getRemark(), Collections.emptyList());

        // 样品子单状态回退
        sampleChildOrderPoList.forEach(po -> {
            final SampleOrderStatus sampleOrderStatus = po.getSampleOrderStatus().toCancelDeliver();
            po.setSampleOrderStatus(sampleOrderStatus);
        });
        sampleChildOrderDao.updateBatchByIdVersion(sampleChildOrderPoList);

        List<SampleParentOrderPo> updateParentOrderPoList = new ArrayList<>();
        final Map<String, SampleOrderStatus> earliestStatusMap = sampleBaseService.getEarliestStatusMapByParentNoList(sampleParentOrderNoList,
                Collections.singletonList(SampleOrderStatus.TYPESETTING));

        for (SampleParentOrderPo sampleParentOrderPo : sampleParentOrderPoList) {
            final SampleOrderStatus earliestStatus = earliestStatusMap.get(sampleParentOrderPo.getSampleParentOrderNo());
            if (!earliestStatus.equals(sampleParentOrderPo.getSampleOrderStatus())) {
                sampleParentOrderPo.setSampleOrderStatus(earliestStatus);
                updateParentOrderPoList.add(sampleParentOrderPo);
            }
        }
        sampleParentOrderDao.updateBatchByIdVersion(updateParentOrderPoList);
    }

    public List<SampleDeliverPrintVo> printDeliverOrder(SampleDeliverNoListDto dto) {
        final List<SampleDeliverOrderPo> sampleDeliverOrderPoList = sampleDeliverOrderDao.getDeliverPoListByNoList(dto.getSampleDeliverOrderNoList());
        Assert.notEmpty(sampleDeliverOrderPoList, () -> new BizException("查找不到对应的样品发货单，请联系系统管理员！"));
        final Map<String, List<SampleDeliverOrderItemPo>> sampleDeliverOrderItemPoMap = sampleDeliverOrderItemDao.getMapByDeliverOrderNoList(dto.getSampleDeliverOrderNoList());
        Assert.notNull(sampleDeliverOrderItemPoMap, () -> new BizException("查找不到对应的样品发货单，请联系系统管理员！"));

        return sampleDeliverOrderPoList.stream()
                .map(po -> {
                    final SampleDeliverPrintVo sampleDeliverPrintVo = new SampleDeliverPrintVo();
                    sampleDeliverPrintVo.setSupplierCode(po.getSupplierCode());
                    sampleDeliverPrintVo.setSupplierName(po.getSupplierName());
                    sampleDeliverPrintVo.setWarehouseName(po.getWarehouseName());
                    sampleDeliverPrintVo.setPrintUsername(GlobalContext.getUsername());
                    final List<SampleDeliverOrderItemPo> sampleDeliverOrderItemPoList = sampleDeliverOrderItemPoMap.get(po.getSampleDeliverOrderNo());
                    final List<SampleDeliverPrintItemVo> sampleDeliverPrintItemVoList = Optional.ofNullable(sampleDeliverOrderItemPoList)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(item -> {
                                final SampleDeliverPrintItemVo sampleDeliverPrintItemVo = new SampleDeliverPrintItemVo();
                                sampleDeliverPrintItemVo.setSampleDeliverOrderNo(item.getSampleDeliverOrderNo());
                                sampleDeliverPrintItemVo.setSpu(item.getSpu());
                                sampleDeliverPrintItemVo.setDeliverCnt(item.getDeliverCnt());
                                sampleDeliverPrintItemVo.setSampleChildOrderNo(item.getSampleChildOrderNo());
                                return sampleDeliverPrintItemVo;
                            }).collect(Collectors.toList());
                    sampleDeliverPrintVo.setSampleDeliverPrintItemVoList(sampleDeliverPrintItemVoList);
                    return sampleDeliverPrintVo;
                }).collect(Collectors.toList());


    }
}
