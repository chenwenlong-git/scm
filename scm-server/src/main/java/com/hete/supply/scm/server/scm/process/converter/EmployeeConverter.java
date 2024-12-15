package com.hete.supply.scm.server.scm.process.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hete.supply.scm.server.scm.process.entity.vo.ProcessEmployeeVo;
import com.hete.supply.scm.server.supplier.entity.po.EmployeeGradeRelationPo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanjiawei
 * Created on 2024/1/11.
 */
public class EmployeeConverter {
    public static Page<ProcessEmployeeVo> convertToProcessEmployeeVoPage(Page<EmployeeGradeRelationPo> sourcePage) {
        List<ProcessEmployeeVo> processEmployeeVoList = sourcePage.getRecords()
                .stream()
                .map(EmployeeConverter::convertToProcessEmployeeVo)
                .collect(Collectors.toList());

        Page<ProcessEmployeeVo> targetPage
                = new Page<>(sourcePage.getCurrent(), sourcePage.getSize(), sourcePage.getTotal());
        targetPage.setRecords(processEmployeeVoList);

        return targetPage;
    }

    public static ProcessEmployeeVo convertToProcessEmployeeVo(EmployeeGradeRelationPo source) {
        ProcessEmployeeVo processEmployeeVo = new ProcessEmployeeVo();
        processEmployeeVo.setEmployeeNo(source.getEmployeeNo());
        processEmployeeVo.setEmployeeName(source.getEmployeeName());
        // 这里可以进行额外的字段映射或其他处理

        return processEmployeeVo;
    }
}
