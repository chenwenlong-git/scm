package com.hete.supply.scm.demo;


import com.hete.supply.scm.api.scm.entity.dto.PurchaseSettleOrderSearchDto;
import com.hete.supply.scm.api.scm.entity.dto.SkuGetSuggestSupplierDto;
import com.hete.supply.scm.api.scm.entity.vo.PurchaseSettleOrderSkuExportVo;
import com.hete.supply.scm.api.scm.facade.PurchaseSettleOrderFacade;
import com.hete.supply.scm.common.util.ScmTimeUtil;
import com.hete.supply.scm.server.scm.adjust.service.init.GoodsPriceInitService;
import com.hete.supply.scm.server.scm.develop.service.init.DevelopChildInitService;
import com.hete.supply.scm.server.scm.ibfs.dao.FinanceRecoOrderDao;
import com.hete.supply.scm.server.scm.ibfs.entity.po.FinanceRecoOrderPo;
import com.hete.supply.scm.server.scm.ibfs.service.biz.RecoOrderBizService;
import com.hete.supply.scm.server.scm.settle.service.base.ProcessSettleOrderBaseService;
import com.hete.supply.scm.server.scm.settle.service.base.PurchaseSettleOrderBaseService;
import com.hete.supply.scm.server.scm.supplier.dao.SupplierDao;
import com.hete.supply.scm.server.scm.supplier.service.biz.SupplierBizService;
import com.hete.support.api.result.CommonResult;
import com.hete.support.api.result.ResultList;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenWenLong
 * @date 2022/11/19 16:47
 */
@ActiveProfiles("local")
@SpringBootTest
@RequiredArgsConstructor
@RunWith(SpringRunner.class)
public class SettleOrderTest {

    @Autowired
    private PurchaseSettleOrderBaseService purchaseSettleOrderBaseService;

    @Autowired
    private ProcessSettleOrderBaseService processSettleOrderBaseService;

    @Autowired
    private PurchaseSettleOrderFacade purchaseSettleOrderFacade;

    @Autowired
    private RecoOrderBizService recoOrderBizService;

    @Autowired
    private FinanceRecoOrderDao financeRecoOrderDao;

    @Autowired
    private SupplierDao supplierDao;

    @Autowired
    private GoodsPriceInitService goodsPriceInitService;

    @Autowired
    private SupplierBizService supplierBizService;

    @Autowired
    private DevelopChildInitService developChildInitService;


    @Test
    public void countPurchaseSettleOrder() {
        purchaseSettleOrderBaseService.countPurchaseSettleOrder(null, null, null);
        System.out.println(1122);
    }

    @Test
    public void countProcessSettleOrder() {
        processSettleOrderBaseService.countProcessSettleOrder(null, null, null);
        System.out.println(3344);
    }

    @Test
    public void purchaseSettleOrderExport() {
        PurchaseSettleOrderSearchDto dto = new PurchaseSettleOrderSearchDto();
        CommonResult<ResultList<PurchaseSettleOrderSkuExportVo>> exportSkuList = purchaseSettleOrderFacade.getExportSkuList(dto);
        System.out.println(exportSkuList);
    }

    @Test
    public void createFinanceRecoOrderTask() {
        goodsPriceInitService.initGoodsPriceTask("");
    }

    @Test
    public void collectFinanceRecoOrderTask() {
        LocalDateTime localDateTimeNow = ScmTimeUtil.strToLocalDateTime("2024-06-03");
        // 在 LocalDateTime 的基础上加一小时
        LocalDateTime newDateTime = localDateTimeNow.plusHours(1);
        System.out.println(newDateTime);
        FinanceRecoOrderPo financeRecoOrderPo = financeRecoOrderDao.getOneByNo("DZest2406010229");
        System.out.println(financeRecoOrderPo);
        recoOrderBizService.collectRecoOrder(financeRecoOrderPo);

    }

    @Test
    public void getDefaultSupplierList() {
        SkuGetSuggestSupplierDto requestDto = new SkuGetSuggestSupplierDto();
        // 组装测试数据
        SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto = new SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto();
        skuAndBusinessIdBatchDto.setBusinessId(1L);
        skuAndBusinessIdBatchDto.setLatestOnShelfTime(LocalDateTime.now().toLocalDate());
        skuAndBusinessIdBatchDto.setPlaceOrderCnt(1);
        skuAndBusinessIdBatchDto.setSku("MU55863133");
        SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto1 = new SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto();
        skuAndBusinessIdBatchDto1.setBusinessId(2L);
        skuAndBusinessIdBatchDto1.setLatestOnShelfTime(LocalDateTime.now().toLocalDate());
        skuAndBusinessIdBatchDto1.setPlaceOrderCnt(2);
        skuAndBusinessIdBatchDto1.setSku("MU56862557");
        SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto skuAndBusinessIdBatchDto2 = new SkuGetSuggestSupplierDto.SkuAndBusinessIdBatchDto();
        skuAndBusinessIdBatchDto2.setBusinessId(3L);
        skuAndBusinessIdBatchDto2.setLatestOnShelfTime(LocalDateTime.now().toLocalDate());
        skuAndBusinessIdBatchDto2.setPlaceOrderCnt(3);
        skuAndBusinessIdBatchDto2.setSku("MU8161925071");
        requestDto.setSkuAndBusinessIdBatchList(List.of(skuAndBusinessIdBatchDto, skuAndBusinessIdBatchDto1, skuAndBusinessIdBatchDto2));
        supplierBizService.getDefaultSupplierList(requestDto);
    }

    @Test
    public void queryPurchasePreOrderList() {
        developChildInitService.initDevelopOrderPricePrice();
    }
}
