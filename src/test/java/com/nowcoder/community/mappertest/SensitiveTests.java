package com.nowcoder.community.mappertest;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author LQZ
 * @date 2022-03-11 21:58
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里可以赌博";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        text = "这里可以☆赌☆博";
        filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        text = "fabc";
        filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        text = "g☆";
        filter = sensitiveFilter.filter(text);
        System.out.println(filter);

        text = "k☆";
        filter = sensitiveFilter.filter(text);
        System.out.println(filter);

    }
}
