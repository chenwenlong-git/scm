package com.hete.supply.scm.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 18:29
 */
@ActiveProfiles("local")
@SpringBootTest
public class ProcessOrderScanTest {

//    @Autowired
//    private ProcessOrderScanFacadeImpl processOrderScanFacadeImpl;

    /**
     * 测试数量
     */
    @Test
    public void getExportTotals() {
//        ProcessOrderScanQueryByApiDto processOrderScanQueryByApiDto = new ProcessOrderScanQueryByApiDto();
//        CommonResult<Integer> exportTotals = processOrderScanFacadeImpl.getExportTotals(processOrderScanQueryByApiDto);
//        System.out.println(exportTotals);
    }

    /**
     * 测试列表
     */
    @Test
    public void getExportList() {
//        ProcessOrderScanQueryByApiDto processOrderScanQueryByApiDto = new ProcessOrderScanQueryByApiDto();
//        processOrderScanQueryByApiDto.setProcessOrderNo("24234");
//        CommonResult<ResultList<ProcessOrderScanExportVo>> exportList = processOrderScanFacadeImpl.getExportList(processOrderScanQueryByApiDto);
//        System.out.println(exportList);
    }
}
