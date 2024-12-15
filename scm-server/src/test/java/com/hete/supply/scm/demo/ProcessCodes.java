package com.hete.supply.scm.demo;

import cn.hutool.core.util.StrUtil;

/**
 * @author yanjiawei
 * Created on 2023/11/22.
 */
public class ProcessCodes {
    public static void main(String[] args) {
        for (int i = 1; i < 155; i++) {
            System.out.println(StrUtil.format("private String processCode{};", i));
        }
    }
}
