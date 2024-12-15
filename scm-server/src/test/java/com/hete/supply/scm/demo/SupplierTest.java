package com.hete.supply.scm.demo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.hete.supply.scm.api.scm.entity.enums.SampleProduceLabel;
import com.hete.supply.scm.common.constant.ScmConstant;
import com.hete.supply.scm.server.scm.sample.entity.po.SampleChildOrderPo;
import com.hete.supply.scm.server.scm.supplier.api.provider.SupplierFacadeImpl;
import com.hete.supply.scm.server.scm.supplier.service.base.SupplierBaseService;
import com.hete.supply.scm.server.supplier.purchase.service.base.PurchaseDeliverBaseService;
import com.hete.support.id.enums.ConfuseLength;
import com.hete.support.id.enums.TimeType;
import com.hete.support.id.service.IdGenerateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ChenWenLong
 * @date 2022/11/19 16:47
 */
@ActiveProfiles("local")
@SpringBootTest
public class SupplierTest {
    @Autowired
    private SupplierFacadeImpl supplierServiceImpl;

    @Autowired
    private IdGenerateService idGenerateService;

    @Autowired
    private SupplierBaseService supplierBaseService;

    @Autowired
    private PurchaseDeliverBaseService purchaseDeliverBaseService;


    @Test
    public void getBySupplierCode() {
        String today = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        String deductOrderNo = idGenerateService.getConfuseCode(ScmConstant.DEDUCT_ORDER_NO_PREFIX + today, ConfuseLength.L_4);
        String deductOrderNo1 = idGenerateService.getIncrCode(ScmConstant.DEDUCT_ORDER_NO_PREFIX, TimeType.CN_DAY_YYYY, 4);
        System.out.println(deductOrderNo);
        System.out.println(deductOrderNo1);
    }

    @Test
    public void test() {
        System.out.println(supplierBaseService.getSupplierAliasByCode("111"));
    }

    @Test
    public void testTimeliness() {
        List<SampleChildOrderPo> poList = new ArrayList<>(); // 你的SampleChildOrderPo列表
        SampleChildOrderPo po1 = new SampleChildOrderPo();
        po1.setSampleProduceLabel(SampleProduceLabel.FIRST);
        po1.setSampleChildOrderNo("1");
        po1.setDeliverDate(LocalDateTime.of(2023, 10, 13, 14, 42, 34));
        poList.add(po1);

        SampleChildOrderPo po2 = new SampleChildOrderPo();
        po2.setSampleProduceLabel(SampleProduceLabel.FIRST);
        po2.setSampleChildOrderNo("2");
        po2.setDeliverDate(LocalDateTime.of(2023, 10, 13, 14, 43, 34));
        poList.add(po2);

        SampleChildOrderPo po3 = new SampleChildOrderPo();
        po3.setSampleProduceLabel(SampleProduceLabel.EFFECTIVE);
        po3.setSampleChildOrderNo("3");
        po3.setDeliverDate(LocalDateTime.of(2023, 10, 13, 14, 44, 34));
        poList.add(po3);

        SampleChildOrderPo po4 = new SampleChildOrderPo();
        po4.setSampleProduceLabel(SampleProduceLabel.EFFECTIVE);
        po4.setSampleChildOrderNo("4");
        po4.setDeliverDate(LocalDateTime.of(2023, 10, 13, 14, 45, 34));
        poList.add(po4);

        // 对poList进行排序
        List<SampleChildOrderPo> sortedList = poList.stream()
                .sorted(Comparator.comparing(SampleChildOrderPo::getSampleProduceLabel, Comparator.naturalOrder())
                        .thenComparing(SampleChildOrderPo::getDeliverDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        List<SampleChildOrderPo> sortedList1 = poList.stream()
                .sorted(Comparator.comparing(SampleChildOrderPo::getDeliverDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        // 打印排序后的结果
        sortedList.forEach(po -> {
            System.out.println("no: " + po.getSampleChildOrderNo() + " ，SampleProduceLabel: " + po.getSampleProduceLabel() +
                    ", SampleTime: " + po.getDeliverDate());
        });
        System.out.println("------------------");
        // 打印排序后的结果1
        sortedList1.forEach(po -> {
            System.out.println("no: " + po.getSampleChildOrderNo() + " ，SampleProduceLabel: " + po.getSampleProduceLabel() +
                    ", SampleTime: " + po.getDeliverDate());
        });
        //ProduceDataDisposableService.produceDataUpdateSpuImgTask();

    }

    @Test
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            Date date = sdf.parse("2023-08-03T18:19");
            System.out.println(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
