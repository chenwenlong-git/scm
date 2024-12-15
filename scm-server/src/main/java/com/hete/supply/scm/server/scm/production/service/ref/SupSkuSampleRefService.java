package com.hete.supply.scm.server.scm.production.service.ref;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.util.ParamValidUtils;
import com.hete.supply.scm.server.scm.dao.ScmImageDao;
import com.hete.supply.scm.server.scm.entity.po.ScmImagePo;
import com.hete.supply.scm.server.scm.production.builder.ProdBuilder;
import com.hete.supply.scm.server.scm.production.dao.SupplierSkuSampleDao;
import com.hete.supply.scm.server.scm.production.entity.bo.CreateSkuSupSampInfo;
import com.hete.supply.scm.server.scm.production.entity.po.SupplierSkuSamplePo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/9/29.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SupSkuSampleRefService {
    private final ScmImageDao scmImageDao;
    private final SupplierSkuSampleDao supplierSkuSampleDao;

    public void createSkuSupplierSamplePic(List<CreateSkuSupSampInfo> skuSupSampInfoList) {
        if (CollectionUtils.isEmpty(skuSupSampInfoList)) {
            log.info("没有需要创建的商品和供应商封样图文件信息，跳过创建。");
            return;
        }

        //校验单号重复
        boolean existsSameSourceOrderNo = skuSupSampInfoList.stream()
                .filter(skuSupSampInfo -> StrUtil.isNotBlank(skuSupSampInfo.getSourceOrderNo()))
                .collect(Collectors.groupingBy(CreateSkuSupSampInfo::getSourceOrderNo, Collectors.counting()))
                .values().stream().anyMatch(count -> count > 1);
        if (existsSameSourceOrderNo) {
            throw new IllegalArgumentException("保存商品供应商样品信息失败！来源单号重复。");
        }

        for (CreateSkuSupSampInfo createSkuSupSampInfo : skuSupSampInfoList) {
            //校验
            String sourceOrderNo = ParamValidUtils.requireNotBlank(createSkuSupSampInfo.getSourceOrderNo(), "保存商品供应商样品信息失败！来源单号为空");
            String supplierCode = ParamValidUtils.requireNotBlank(createSkuSupSampInfo.getSupplierCode(), "保存商品供应商样品信息失败！供应商编码为空");
            String sku = ParamValidUtils.requireNotBlank(createSkuSupSampInfo.getSku(), "保存商品供应商样品信息失败！SKU编码为空");
            Set<String> samplePicFileCodeList = ParamValidUtils.requireNotEmpty(createSkuSupSampInfo.getSamplePicFileCodeList(), "保存商品供应商样品信息失败！封样图文件编码列表为空");

            SupplierSkuSamplePo supplierSkuSamplePo = ProdBuilder.buildSupplierSkuSamplePo(sku, supplierCode, sourceOrderNo);
            supplierSkuSampleDao.insert(supplierSkuSamplePo);
            Long supplierSkuSampleId = supplierSkuSamplePo.getSupplierSkuSampleId();

            List<ScmImagePo> scmImagePos = ProdBuilder.buildScmImagePoList(supplierSkuSampleId, samplePicFileCodeList);
            scmImageDao.insertBatch(scmImagePos);
        }
    }
}
