package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author LQZ
 * @date 2022-03-08 19:12
 */

public class CommunityUtil {

    // 生成随机字符串
    public static String generateUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    // MD5 加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        // 加密成16进制的字符串
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
