package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author LQZ
 * @date 2022-03-07 15:29
 */

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @param注解用于给参数取别名
    // 如果只有一个参数, 并且在 <if> 里使用, 则必须加别名
    // int selectDiscussPostRows(@Param("userId") int userId);
    int selectDiscussPostRows(int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
