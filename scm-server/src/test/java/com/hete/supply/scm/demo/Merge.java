package com.hete.supply.scm.demo;

import com.alibaba.nacos.shaded.com.google.common.collect.Sets;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanjiawei
 * Created on 2024/9/26.
 */
public class Merge {
    public static void main(String[] args) {
        Set<String> goodAttrSkuList = Sets.newHashSet("1", "2");
        Set<String> supAttrSkuList = Sets.newHashSet("2", "4");
        Set<String> collect = Stream.of(goodAttrSkuList, supAttrSkuList).filter(CollectionUtils::isNotEmpty).flatMap(Set::stream).collect(Collectors.toSet());
        System.out.println(collect);
    }
}
