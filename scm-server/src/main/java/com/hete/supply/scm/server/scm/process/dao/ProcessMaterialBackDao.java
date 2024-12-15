package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessMaterialBackPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 加工原料归还单 服务实现类
 * </p>
 *
 * @author mybatis-generator
 * @since 2023-03-14
 */
@Component
@Validated
public class ProcessMaterialBackDao extends BaseDao<ProcessMaterialBackMapper, ProcessMaterialBackPo> {

    /**
     * 通过收货单号查询
     *
     * @param receiptNo
     * @return
     */
    public ProcessMaterialBackPo getByReceiptNo(String receiptNo) {
        return getOne(Wrappers.<ProcessMaterialBackPo>lambdaQuery().eq(ProcessMaterialBackPo::getReceiptNo, receiptNo));
    }

    /**
     * 通过 messageKey 查询
     *
     * @param messageKey
     * @return
     */
    public ProcessMaterialBackPo getByMessageKey(String messageKey) {
        return getOne(Wrappers.<ProcessMaterialBackPo>lambdaQuery().eq(ProcessMaterialBackPo::getMessageKey, messageKey));
    }

    /**
     * 通过加工单号查询
     *
     * @param processOrderNo
     * @return
     */
    public List<ProcessMaterialBackPo> getByProcessOrderNo(String processOrderNo) {
        return list(Wrappers.<ProcessMaterialBackPo>lambdaQuery()
                .eq(ProcessMaterialBackPo::getProcessOrderNo, processOrderNo));
    }

    public List<ProcessMaterialBackPo> listByRepairOrderNo(String repairOrderNo) {
        if (StringUtils.isBlank(repairOrderNo)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<ProcessMaterialBackPo>lambdaQuery()
                .eq(ProcessMaterialBackPo::getRepairOrderNo, repairOrderNo));
    }

    public List<ProcessMaterialBackPo> listByRepairOrderNos(Collection<String> repairOrderNos) {
        return CollectionUtils.isEmpty(repairOrderNos) ? Collections.emptyList() :
                list(Wrappers.<ProcessMaterialBackPo>lambdaQuery()
                        .in(ProcessMaterialBackPo::getRepairOrderNo, repairOrderNos));
    }
}
