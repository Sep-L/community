package com.nowcoder.community.service;

import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author LQZ
 * @date 2022-03-07 16:58
 */

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 处理注册信息
     * @param user 网页上用户填入的数据
     * @return 1. map形式的报错数据
     *         2. map为空代表注册成功并发送了激活邮件
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        // 控制处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 验证账号是否存在
        User existUser = userMapper.selectByName(user.getUsername());
        if (existUser != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        // 验证邮箱是否存在
        existUser = userMapper.selectByEmail(user.getEmail());
        if (existUser != null) {
            map.put("emailMsg", "该邮箱已存在");
            return map;
        }

        // 注册用户
        // 生成 5 位随机字符串
        user.setSalt(CommunityUtil.generateUID().substring(0, 5));
        // 初始密码和随机字符串拼接后使用 md5 加密, 并覆盖为新密码
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        // 设置用户类型, 默认普通
        user.setType(0);
        // 设置用户状态, 默认未激活
        user.setStatus(0);
        // 设置激活码, 生成随机字符串
        user.setActivationCode(CommunityUtil.generateUID());
        // 设置用户默认图片
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        // 设置用户账号创建时间
        user.setCreateTime(new Date());
        // 用户存入数据库
        userMapper.insertUser(user);

        // 激活邮件设置
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);

        // 处理并发送邮件
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

}
