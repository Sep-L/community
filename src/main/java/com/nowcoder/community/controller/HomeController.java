package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LQZ
 * @date 2022-03-07 17:03
 */

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService disscussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    // 方法调用前, SpringMVC 会自动实例化 Model 和 Page, 并将 Page 注入 Model.
    // 所以, 在 thymeleaf 中可以直接访问 Page 对象中的数据
    public String getIndexPage(Model model, Page page) {
        // 查询默认时所有数据总数, 并赋值给page
        page.setRows(disscussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = disscussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

}
