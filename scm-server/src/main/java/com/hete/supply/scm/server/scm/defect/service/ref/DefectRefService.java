package com.hete.supply.scm.server.scm.defect.service.ref;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.defect.converter.DefectHandlingConverter;
import com.hete.supply.scm.server.scm.defect.dao.DefectHandlingDao;
import com.hete.supply.scm.server.scm.defect.entity.bo.DefectHandlingCreateBo;
import com.hete.supply.scm.server.scm.entity.bo.ScmImageBo;
import com.hete.supply.scm.server.scm.entity.po.DefectHandlingPo;
import com.hete.supply.scm.server.scm.enums.ImageBizType;
import com.hete.supply.scm.server.scm.service.base.ScmImageBaseService;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierPo;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author weiwenxin
 * @date 2023/6/21 16:08
 */
@Service
@RequiredArgsConstructor
@Validated
public class DefectRefService {
    private final IdGenerateService idGenerateService;
    private final DefectHandlingDao defectHandlingDao;
    private final ScmImageBaseService scmImageBaseService;
    private final SupplierBaseService supplierBaseService;

    public List<DefectHandlingPo> createDefectHandling(@NotEmpty @Valid List<DefectHandlingCreateBo> defectHandlingCreateBoList) {

        defectHandlingCreateBoList.forEach(bo -> {
            final String defectHandlingNo = idGenerateService.getConfuseCode(ScmConstant.DEFECT_HANDLING_NO_PREFIX, TimeType.CN_DAY_YYYY, ConfuseLength.L_4);
            bo.setDefectHandlingNo(defectHandlingNo);
        });

        // 根据供应商代码查询供应商名称，兼容部分系统对接没有保存供应商名称的问题
        final List<String> supplierCodeList = defectHandlingCreateBoList.stream()
                .map(DefectHandlingCreateBo::getSupplierCode)
                .collect(Collectors.toList());
        final List<SupplierPo> supplierPoList = supplierBaseService.getBySupplierCodeList(supplierCodeList);
        final Map<String, SupplierPo> supplierCodePoMap = supplierPoList.stream()
                .collect(Collectors.toMap(SupplierPo::getSupplierCode, Function.identity()));


        final List<DefectHandlingPo> defectHandlingPoList = DefectHandlingConverter.defectHandlingBoToPo(defectHandlingCreateBoList, supplierCodePoMap);
        defectHandlingDao.insertBatch(defectHandlingPoList);


        // 保存图片
        final Map<String, Long> defectNoAndIdMap = defectHandlingPoList.stream()
                .collect(Collectors.toMap(DefectHandlingPo::getDefectHandlingNo, DefectHandlingPo::getDefectHandlingId));

        final List<ScmImageBo> scmImageBoList = defectHandlingCreateBoList.stream()
                .filter(bo -> CollectionUtils.isNotEmpty(bo.getFileCodeList()))
                .map(bo -> {
                    final ScmImageBo scmImageBo = new ScmImageBo();
                    scmImageBo.setImageBizId(defectNoAndIdMap.get(bo.getDefectHandlingNo()));
                    scmImageBo.setFileCodeList(bo.getFileCodeList());
                    return scmImageBo;
                }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(scmImageBoList)) {
            scmImageBaseService.insertBatchImageBo(scmImageBoList, ImageBizType.DEFECT_HANDLING);
        }

        return defectHandlingPoList;
    }

    public List<DefectHandlingPo> getListByQcNo(String qcOrderNo) {
        return defectHandlingDao.selectListByQcOrderNo(qcOrderNo);
    }
}
