package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author LQZ
 * @date 2022-03-10 16:17
 */

/**
 * 持有用户信息, 用于代替 session 对象
 */
@Component
public class HostHolder {

    // 根据线程存取对象
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
