package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author LQZ
 * @date 2022-03-12 22:23
 */

@Controller
@RequestMapping(path = "/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService disscussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        disscussPostService.addDiscussPost(post);

        // 报错的情况之后处理
        return CommunityUtil.getJSONString(0, "发送成功");
    }

    /**
     * 查询帖子详情
     * @param discussPostId
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = disscussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 查询帖子作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 查询评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        // 评论: 帖子的评论
        // 回复: 给评论的评论
        // 评论列表
        List<Comment> commentsList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 评论 VO 列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentsList != null) {
            for (Comment comment : commentsList) {
                // 一个评论的 Vo
                Map<String, Object> commentVo = new HashMap<>();
                // 添加一个评论
                commentVo.put("comment", comment);
                // 评论的作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 回复
                List<Comment> repliesList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                // 回复 VO 列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (repliesList != null) {
                    for (Comment reply : repliesList) {
                        // 一个回复的 Vo
                        Map<String, Object> replyVo = new HashMap<>();
                        // 添加一个回复
                        replyVo.put("reply", reply);
                        // 回复的作者
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复的对象
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replies", replyVoList);

                // 回复的数量
                int replyCount = commentService.findCommentsCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "/site/discuss-detail";
    }
}
