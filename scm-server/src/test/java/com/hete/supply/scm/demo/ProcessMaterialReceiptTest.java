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
public class ProcessMaterialReceiptTest {

//    @Autowired
//    private ProcessMaterialReceiptFacadeImpl processMaterialReceiptFacadeImpl;

    /**
     * 测试数量
     */
    @Test
    public void getExportTotals() {
//        ProcessMaterialReceiptQueryByApiDto processMaterialReceiptQueryByApiDto = new ProcessMaterialReceiptQueryByApiDto();
//        processMaterialReceiptQueryByApiDto.setDeliveryNo("2432");
//        CommonResult<Integer> exportTotals = processMaterialReceiptFacadeImpl.getExportTotals(processMaterialReceiptQueryByApiDto);
//        System.out.println(exportTotals);
    }

    /**
     * 测试列表
     */
    @Test
    public void getExportList() {
//        ProcessMaterialReceiptQueryByApiDto processMaterialReceiptQueryByApiDto = new ProcessMaterialReceiptQueryByApiDto();
//        processMaterialReceiptQueryByApiDto.setDeliveryNo("2432");
//        CommonResult<ResultList<ProcessMaterialReceiptExportVo>> exportList = processMaterialReceiptFacadeImpl.getExportList(processMaterialReceiptQueryByApiDto);
//        System.out.println(exportList);
    }
}
