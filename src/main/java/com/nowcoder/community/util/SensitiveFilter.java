package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LQZ
 * @date 2022-03-11 21:14
 */

@Component
public class SensitiveFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode;


    @PostConstruct
    /*
      初始化前缀树, 利用敏感词构建前缀树
     */
    public void init() {
        rootNode = new TrieNode();
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            LOGGER.error("加载敏感词文件失败: " + e.getMessage());
        }

    }

    /**
     * 将一个敏感词添加到前缀树
     *
     * @param keyword 敏感词
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);

            // 判断当前字符是否存在子节点
            TrieNode subNode = tempNode.getSubNode(c);
            // 没有就新建子节点
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // 指向子节点, 进入下一轮循环
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        // 空值处理
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针 1: 根节点
        TrieNode tempNode = rootNode;
        // 指针 2: 起始位置
        int begin = 0;
        // 指针 3: 当前位置
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (begin < text.length()) {
            // 指针 3 越界, 以 begin 开始的不可能再有敏感词
            if (position == text.length()) {
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 指针 1 重新指向根节点
                tempNode = rootNode;
                continue;
            }

            // 当前字符
            char c = text.charAt(position);

            // 跳过特殊符号
            if (isSymbol(c)) {
                // 将此符号计入结果, 让指针 2 向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头还是中间, 指针 3 都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以 begin 为开头的字符不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 指针 1 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd) {
                // 发现敏感词, 将 begin 开头 position 结尾的字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 指针 1 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }

        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否为符号
     *
     * @param c 当前字符
     * @return 是否是符号
     */
    private boolean isSymbol(Character c) {
        // 0x2E80 ~ 0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode {
        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 子节点（key是下级字符, value是下级节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
