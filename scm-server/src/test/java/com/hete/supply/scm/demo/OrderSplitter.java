package com.hete.supply.scm.demo;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yanjiawei
 * Created on 2024/1/2.
 */
public class OrderSplitter {
    public static void main(String[] args) {
        List<Map<String, Integer>> items = List.of(Map.of("SKU1", 4), Map.of("SKU2", 4), Map.of("SKU3", 3));
        Map<Integer, List<Map<String, Integer>>> integerListMap = splitOrders(items, 10);
        System.out.println(integerListMap);
    }

    public static Map<Integer, List<Map<String, Integer>>> splitOrders(List<Map<String, Integer>> items,
                                                                       int batchSize) {
        if (CollectionUtil.isEmpty(items)) {
            return new HashMap<>(0);
        }

        // 创建一个数量优先级队列
        Comparator<Map<String, Integer>> mapComparator
                = Comparator.comparing(map -> map.values()
                .iterator()
                .next(), Comparator.reverseOrder());
        PriorityQueue<Map<String, Integer>> priorityQueue = new PriorityQueue<>(mapComparator);
        priorityQueue.addAll(items);

        Map<Integer, List<Map<String, Integer>>> result = new HashMap<>(16);
        AtomicInteger no = new AtomicInteger(1);

        int current = 0;
        List<Map<String, Integer>> itemCaches = Lists.newArrayList();
        while (!priorityQueue.isEmpty()) {
            Map<String, Integer> item = priorityQueue.poll();
            for (Map.Entry<String, Integer> entry : item.entrySet()) {
                String key = entry.getKey();
                Integer quantity = entry.getValue();
                current += quantity;

                if (current >= batchSize) {
                    // 归还数量 = 当前数量-批次数量
                    int backNum = current - batchSize;
                    if (backNum > 0) {
                        priorityQueue.offer(Map.of(key, backNum));
                    }

                    // 取用数量 = 当前数量-归还数量
                    int useNum = current - backNum;
                    itemCaches.add(Map.of(key, useNum));

                    result.put(no.getAndIncrement(), deepCopyList(itemCaches));
                    current = 0;
                    itemCaches.clear();
                } else {
                    itemCaches.add(Map.of(key, quantity));
                }
            }
        }

        if (CollectionUtil.isNotEmpty(itemCaches)) {
            result.put(no.getAndIncrement(), deepCopyList(itemCaches));
        }
        return result;
    }

    private static List<Map<String, Integer>> deepCopyList(List<Map<String, Integer>> originalList) {
        List<Map<String, Integer>> copyList = new ArrayList<>();

        for (Map<String, Integer> originalMap : originalList) {
            // 对每个Map进行深拷贝
            Map<String, Integer> copyMap = new HashMap<>(originalMap);
            copyList.add(copyMap);
        }

        return copyList;
    }
}
