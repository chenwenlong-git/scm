package com.hete.supply.scm.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 18:29
 */
@ActiveProfiles("local")
@SpringBootTest
public class ProcessOrderTest {

    @Autowired
//    private ProcessOrderFacadeImpl processOrderFacadeImpl;

    /**
     * 测试商品工序数量
     */
    @Test
    public void getExportTotalsByOrder() {
//        ProcessOrderQueryByApiDto processOrderQueryByApiDto = new ProcessOrderQueryByApiDto();
//        processOrderQueryByApiDto.setPlatform("shopify");

//        CommonResult<Integer> exportTotalsByOrder = processOrderFacadeImpl.getExportTotalsByOrder(processOrderQueryByApiDto);
//        System.out.println(exportTotalsByOrder);
    }

    /**
     * 测试
     */
    @Test
    public void getExportListByOrder() {
//        ProcessOrderQueryByApiDto processOrderQueryByApiDto = new ProcessOrderQueryByApiDto();
//        CommonResult<ResultList<ProcessOrderExportByOrderVo>> exportListByOrder = processOrderFacadeImpl.getExportListByOrder(processOrderQueryByApiDto);
//        System.out.println(exportListByOrder);
    }
}
