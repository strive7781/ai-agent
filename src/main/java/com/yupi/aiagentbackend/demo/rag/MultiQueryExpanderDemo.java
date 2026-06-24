package com.yupi.aiagentbackend.demo.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend MultiQueryExpanderDemo
 * @Date 2026/6/24 10:58 周三
 * @since 1.0
 **/
@Component
public class MultiQueryExpanderDemo {
    @Resource
    private ChatClient.Builder chatClientBuilder;

    public List<Query> expand(String query){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query("谁是程序员鱼皮啊？"));
        return queries;
    }


}
