package com.hete.supply.scm.server.scm.process.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hete.supply.scm.server.scm.process.entity.po.ProcessUserPo;
import com.hete.support.mybatis.plus.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 绑定用户Dao
 * </p>
 *
 * @author mybatis-generator
 * @since 2022-11-04
 */
@Component
@Validated
public class ProcessUserDao extends BaseDao<ProcessUserMapper, ProcessUserPo> {

    /**
     * 通过工序 id 查询
     *
     * @param processId
     * @return
     */
    public List<ProcessUserPo> getByProcessId(Long processId) {
        return list(Wrappers.<ProcessUserPo>lambdaQuery()
                .eq(ProcessUserPo::getProcessId, processId));
    }

    /**
     * 通过多个工序 id 查询
     *
     * @param processIds
     * @return
     */
    public List<ProcessUserPo> getByProcessIds(List<Long> processIds) {
        return list(Wrappers.<ProcessUserPo>lambdaQuery()
                .in(ProcessUserPo::getProcessId, processIds));
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }
}
