package com.hete.supply.scm.demo;

import com.hete.supply.scm.server.scm.entity.bo.CalPreShelfTimeBo;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * @author yanjiawei
 * Created on 2024/8/16.
 */
public class QuDemo {

    @Test
    public void test() {

    }

    @Test
    public void test1() {

    }

    public static void main(String[] args) {
        Deque<LocalDate> dateDeque = new LinkedList<>();
        for (int i = 1; i <= 3; i++) {
            dateDeque.offer(LocalDate.now().plusDays(i));
        }

        int s = dateDeque.size();
//        for (int i = 0; i < s; i++) {
//            System.out.println(dateDeque.pollFirst());
//        }

        for (int i = 0; i < s; i++) {
            System.out.println(dateDeque.pollLast());
        }
    }


}
