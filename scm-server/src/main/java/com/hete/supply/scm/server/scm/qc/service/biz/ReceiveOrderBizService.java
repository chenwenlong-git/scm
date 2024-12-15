package com.hete.supply.scm.server.scm.qc.service.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hete.supply.scm.server.scm.qc.entity.bo.ReceiveOrderBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * ReceiveOrderBizService 是用于处理收货单业务逻辑的服务类，标记了 Spring Service 注解和 Lombok Log 注解。
 * 这个类包括两个方法，用于根据收货单号查询收货单信息，以及根据一组收货单号列表查询相应的收货单信息。
 *
 * @Author yanjiawei
 * @Date 2023/10/13
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ReceiveOrderBizService {

    /**
     * 根据收货单号查询收货单信息
     *
     * @param receiveOrderNo 收货单号
     * @return 收货单信息对象，如果输入为空或无效，返回空对象
     */
    public ReceiveOrderBo getReceiveOrderByReceiveOrderNo(String receiveOrderNo) {
        // 检查输入是否为空或无效
        if (StrUtil.isBlank(receiveOrderNo)) {
            return null;
        }
        // 在此处编写查询收货单信息的逻辑，将结果存储到 receiveOrderBo 对象中
        ReceiveOrderBo receiveOrderBo = new ReceiveOrderBo();

        // 返回收货单信息对象
        return receiveOrderBo;
    }

    /**
     * 根据一组收货单号列表，查询相应的收货单信息。
     *
     * @param receiveOrderNos 一组收货单号，用于检索对应的收货单信息。
     * @return 包含收货单信息的 ReceiveOrderBo 对象列表。如果未找到匹配的收货单则返回空列表。
     */
    public List<ReceiveOrderBo> getReceiveOrdersByReceiveOrderNos(Collection<String> receiveOrderNos) {
        // 判断输入列表是否为空，如果为空则返回空列表
        if (CollectionUtil.isEmpty(receiveOrderNos)) {
            return Collections.emptyList();
        }
        return null;
    }
}

