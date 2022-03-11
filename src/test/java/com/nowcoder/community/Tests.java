package com.nowcoder.community;

import org.junit.jupiter.api.Test;

/**
 * @author LQZ
 * @date 2022-03-10 17:14
 */

public class Tests {

    @Test
    public void test1() {
        String fileName = "D:/Project/JavaProject/Community-Development/upload/c423441f7ebe4c6aaf5d6915337bff91.jpg";
        int index = fileName.lastIndexOf(".") + 1;
        String suffix = fileName.substring(index);
        System.out.println(suffix);
    }
}
