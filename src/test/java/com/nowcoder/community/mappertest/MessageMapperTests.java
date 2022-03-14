package com.nowcoder.community.mappertest;

import com.nowcoder.community.CommunityApplication;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @author LQZ
 * @date 2022-03-14 9:25
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageMapperTests {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectLetters() {
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        messages.forEach(System.out::println);

        int messageCount = messageMapper.selectConversationCount(111);
        System.out.println(messageCount);

        messages = messageMapper.selectLetters("111_112", 0, 10);
        messages.forEach(System.out::println);

        messageCount = messageMapper.selectLetterCount("111_112");
        System.out.println(messageCount);

        messageCount = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(messageCount);

        messageCount = messageMapper.selectLetterUnreadCount(131, null);
        System.out.println(messageCount);
    }
}
