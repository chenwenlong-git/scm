package com.hete.supply.scm.server.supplier.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.supplier.dao.OverseasWarehouseMsgDao;
import com.hete.supply.scm.server.supplier.dao.OverseasWarehouseMsgItemDao;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgItemPo;
import com.hete.supply.scm.server.supplier.entity.po.OverseasWarehouseMsgPo;
import com.hete.supply.scm.server.supplier.entity.vo.OverseasWarehouseMsgItemVo;
import com.hete.supply.scm.server.supplier.entity.vo.OverseasWarehouseMsgVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/2/13 19:29
 */
@Service
@RequiredArgsConstructor
public class OverseasBizService {
    private final OverseasWarehouseMsgDao overseasWarehouseMsgDao;
    private final OverseasWarehouseMsgItemDao overseasWarehouseMsgItemDao;
    private final ScmImageBaseService scmImageBaseService;

    /**
     * 根据采购子单号获取海外仓文件信息
     *
     * @param purchaseChildOrderNo
     * @return
     */
    public OverseasWarehouseMsgVo getOverseasMsgByPurchaseChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }
        final OverseasWarehouseMsgPo overseasWarehouseMsgPo = overseasWarehouseMsgDao.getByPurchaseChildNo(purchaseChildOrderNo);
        return getOverseasWarehouseMsgVo(overseasWarehouseMsgPo);
    }

    /**
     * 根据采购子单号获取海外仓文件信息
     *
     * @param overseasShippingMarkNo
     * @return
     */
    public OverseasWarehouseMsgVo getOverseasMsgByOverseasShippingMarkNo(String overseasShippingMarkNo) {
        if (StringUtils.isBlank(overseasShippingMarkNo)) {
            return null;
        }
        final OverseasWarehouseMsgPo overseasWarehouseMsgPo = overseasWarehouseMsgDao.getByNo(overseasShippingMarkNo);
        return getOverseasWarehouseMsgVo(overseasWarehouseMsgPo);
    }

    private OverseasWarehouseMsgVo getOverseasWarehouseMsgVo(OverseasWarehouseMsgPo overseasWarehouseMsgPo) {
        if (null == overseasWarehouseMsgPo) {
            return null;
        }
        final OverseasWarehouseMsgVo overseasWarehouseMsgVo = new OverseasWarehouseMsgVo();
        final Long overseasWarehouseMsgId = overseasWarehouseMsgPo.getOverseasWarehouseMsgId();
        overseasWarehouseMsgVo.setOverseasWarehouseMsgId(overseasWarehouseMsgId);
        overseasWarehouseMsgVo.setVersion(overseasWarehouseMsgPo.getVersion());
        overseasWarehouseMsgVo.setOverseasShippingMarkNo(overseasWarehouseMsgPo.getOverseasShippingMarkNo());
        overseasWarehouseMsgVo.setOverseasShippingFileCode(scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.OVERSEAS_SHIPPING_MARK, Collections.singletonList(overseasWarehouseMsgId)));
        overseasWarehouseMsgVo.setTrackingNo(overseasWarehouseMsgPo.getTrackingNo());
        overseasWarehouseMsgVo.setTrackingNoFileCode(scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.OVERSEAS_TRACKING, Collections.singletonList(overseasWarehouseMsgId)));
        overseasWarehouseMsgVo.setOverseasBarCodeFileCode(scmImageBaseService.getFileCodeListByIdAndType(ImageBizType.OVERSEAS_BAR_CODE, Collections.singletonList(overseasWarehouseMsgId)));

        final List<OverseasWarehouseMsgItemPo> overseasWarehouseMsgItemPoList = overseasWarehouseMsgItemDao.getListByOverseasMsgId(overseasWarehouseMsgPo.getOverseasWarehouseMsgId());
        if (CollectionUtils.isEmpty(overseasWarehouseMsgItemPoList)) {
            return overseasWarehouseMsgVo;
        }

        final List<OverseasWarehouseMsgItemVo> overseasWarehouseMsgItemList = overseasWarehouseMsgItemPoList.stream().
                map(po -> {
                    final OverseasWarehouseMsgItemVo overseasWarehouseMsgItemVo = new OverseasWarehouseMsgItemVo();
                    overseasWarehouseMsgItemVo.setPurchaseChildOrderNo(po.getPurchaseChildOrderNo());
                    overseasWarehouseMsgItemVo.setSku(po.getSku());
                    overseasWarehouseMsgItemVo.setSkuBatchCode(po.getSkuBatchCode());
                    overseasWarehouseMsgItemVo.setOverseasWarehouseBarCode(po.getOverseasWarehouseBarCode());
                    return overseasWarehouseMsgItemVo;
                }).collect(Collectors.toList());

        overseasWarehouseMsgVo.setOverseasWarehouseMsgItemList(overseasWarehouseMsgItemList);
        return overseasWarehouseMsgVo;
    }

    /**
     * 判断该采购子单号是否存在海外仓文件信息
     *
     * @param purchaseChildOrderNo
     */
    public OverseasWarehouseMsgPo hasOverseasMsgByPurchaseChildNo(String purchaseChildOrderNo) {
        if (StringUtils.isBlank(purchaseChildOrderNo)) {
            return null;
        }

        return overseasWarehouseMsgDao.getByPurchaseChildNo(purchaseChildOrderNo);
    }
}
