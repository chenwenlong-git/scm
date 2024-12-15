package com.hete.supply.scm.demo;

import com.hete.supply.scm.server.scm.process.service.biz.CostCoefficientsBizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

/**
 * @Author: RockyHuas
 * @date: 2022/11/21 18:29
 */
@ActiveProfiles("local")
@SpringBootTest
public class GoodsProcessTest {

    @Autowired
    private CostCoefficientsBizService costCoefficientsBizService;

    /**
     * 测试商品工序数量
     */
    @Test
    public void getExportTotals() {
        BigDecimal latestCoefficient = costCoefficientsBizService.getLatestCoefficient();
        System.out.println(latestCoefficient);
    }

    /**
     * 测试
     */
    @Test
    public void getExportList() {
//        GoodsProcessQueryByApiDto goodsProcessQueryByApiDto = new GoodsProcessQueryByApiDto();
//        goodsProcessQueryByApiDto.setSpu("1");
//        CommonResult<ResultList<GoodsProcessExportVo>> exportList = goodsProcessFacadeImpl.getExportList(goodsProcessQueryByApiDto);
//        System.out.println(exportList);
    }
}
