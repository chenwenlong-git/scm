package com.hete.supply.scm.server.scm.sample.service.ref;

import com.hete.supply.scm.server.scm.sample.service.base.SampleBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weiwenxin
 * @date 2022/11/30 17:20
 */
@Service
@RequiredArgsConstructor
public class SampleRefService {
    private final SampleBaseService sampleBaseService;

    /**
     * 根据生产属性值查询sku列表
     *
     * @param properties
     * @return
     */
    public List<String> getSkuListByProperties(List<String> properties) {
        return sampleBaseService.getSkuListByProperties(properties);
    }
}
