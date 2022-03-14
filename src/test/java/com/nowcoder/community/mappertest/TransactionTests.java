package com.nowcoder.community.mappertest;

import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author LQZ
 * @date 2022-03-13 2:54
 */

public class TransactionTests {
    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1() {
        Object obj = alphaService.save1();
        System.out.println(obj);
    }
    @Test
    public void testSave2() {
        Object obj = alphaService.save2();
        System.out.println(obj);
    }
}
