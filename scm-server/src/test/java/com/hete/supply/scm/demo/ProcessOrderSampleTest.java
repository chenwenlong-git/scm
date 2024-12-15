package com.hete.supply.scm.demo;

import com.hete.supply.scm.server.scm.entity.bo.ProduceDataAttrBo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yanjiawei
 * Created on 2023/10/24.
 */
public class ProcessOrderSampleTest {
    @Test
    public void testToProcessOrderSampleBos() {
//        // 创建一些 ProduceDataAttrBo 对象用于测试
//        List<ProduceDataAttrBo> produceDataAttrBoList = createTestDataWithEmptyValues();
//
//        // 调用被测试的方法
//        List<ProcessOrderSampleBo> result = ProcessOrderSampleConverter.toProcessOrderSampleBos(produceDataAttrBoList);
//
//        // 在这里编写断言，验证结果是否符合预期
//        // 例如，验证 result 的大小、每个元素的属性值等等
//        System.out.println(JSON.toJSONString(result));
    }

    private List<ProduceDataAttrBo> createTestData() {
        List<ProduceDataAttrBo> testData = new ArrayList<>();

        // 创建 ProduceDataAttrBo 对象并设置属性值
        ProduceDataAttrBo attr1 = new ProduceDataAttrBo();
        attr1.setAttributeNameId(1L);
        attr1.setAttrName("Attribute1");
        attr1.setAttrValue("Value1");

        ProduceDataAttrBo attr2 = new ProduceDataAttrBo();
        attr2.setAttributeNameId(1L);
        attr2.setAttrName("Attribute1");
        attr2.setAttrValue("Value2");

        // 将对象添加到测试数据列表
        testData.add(attr1);
        testData.add(attr2);

        // 添加更多测试数据...

        return testData;
    }

    private List<ProduceDataAttrBo> createEmptyTestData() {
        return Collections.emptyList();
    }


    private List<ProduceDataAttrBo> createTestDataWithEmptyValues() {
        List<ProduceDataAttrBo> testData = new ArrayList<>();

        ProduceDataAttrBo attr1 = new ProduceDataAttrBo();
        attr1.setAttributeNameId(1L);
        attr1.setAttrName("Attribute1");
        attr1.setAttrValue("");

        // 添加更多属性值为空的测试数据...

        return testData;
    }

    private List<ProduceDataAttrBo> createTestDataWithDuplicateIds() {
        List<ProduceDataAttrBo> testData = new ArrayList();

        ProduceDataAttrBo attr1 = new ProduceDataAttrBo();
        attr1.setAttributeNameId(1L);
        attr1.setAttrName("Attribute1");
        attr1.setAttrValue("Value1");

        ProduceDataAttrBo attr2 = new ProduceDataAttrBo();
        attr2.setAttributeNameId(1L);
        attr2.setAttrName("Attribute1");
        attr2.setAttrValue("Value2");

        // 添加更多相同 attributeNameId 的测试数据...

        return testData;
    }


}
