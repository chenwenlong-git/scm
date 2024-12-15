package com.hete.supply.scm.server.scm.supplier.service.biz;

import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierSubjectDao;
import com.hete.supply.scm.server.scm.supplier.entity.dto.SupplierCodeListDto;
import com.hete.supply.scm.server.scm.supplier.entity.po.SupplierSubjectPo;
import com.hete.supply.scm.server.scm.supplier.entity.vo.SupplierSubjectDropDownVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2024/5/16 17:37
 */
@Service
@RequiredArgsConstructor
public class SupplierSubjectBizService {

    private final SupplierSubjectDao supplierSubjectDao;
    private final SupplierDao supplierDao;

    /**
     * 下拉获取供应商主体信息列表
     *
     * @param dto:
     * @return List<SupplierSubjectDropDownVo>
     * @author ChenWenLong
     * @date 2024/5/16 17:54
     */
    public List<SupplierSubjectDropDownVo> getSupplierSubjectList(SupplierCodeListDto dto) {
        List<SupplierSubjectDropDownVo> voList = new ArrayList<>();

        final List<SupplierSubjectPo> supplierSubjectPoList = supplierSubjectDao.getListBySupplierCodeList(dto.getSupplierCodeList());
        final Map<String, String> supplierPoMap = supplierDao.getSupplierNameBySupplierCodeList(dto.getSupplierCodeList());

        Map<String, List<SupplierSubjectPo>> supplierSubjectPoMap = supplierSubjectPoList.stream().collect(Collectors.groupingBy(SupplierSubjectPo::getSupplierCode));
        supplierSubjectPoMap.forEach((String supplierCode, List<SupplierSubjectPo> supplierSubjectPos) -> {
            SupplierSubjectDropDownVo supplierSubjectDropDownVo = new SupplierSubjectDropDownVo();
            supplierSubjectDropDownVo.setSupplierCode(supplierCode);
            supplierSubjectDropDownVo.setSupplierName(supplierPoMap.get(supplierCode));

            List<SupplierSubjectDropDownVo.SupplierSubjectDropDownItemVo> supplierSubjectDropDownItemList = supplierSubjectPos.stream().map(supplierSubjectPo -> {
                SupplierSubjectDropDownVo.SupplierSubjectDropDownItemVo supplierSubjectDropDownItemVo = new SupplierSubjectDropDownVo.SupplierSubjectDropDownItemVo();
                supplierSubjectDropDownItemVo.setSubject(supplierSubjectPo.getSubject());
                return supplierSubjectDropDownItemVo;
            }).collect(Collectors.toList());

            supplierSubjectDropDownVo.setSupplierSubjectDropDownItemList(supplierSubjectDropDownItemList);
            voList.add(supplierSubjectDropDownVo);
        });

        return voList;
    }

}
