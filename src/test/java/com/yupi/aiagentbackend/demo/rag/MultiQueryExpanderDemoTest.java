package com.yupi.aiagentbackend.demo.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend MultiQueryExpanderDemoTest
 * @Date 2026/6/24 11:02 周三
 * @since 1.0
 **/
@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;
    @Test
    void expand() {
        List<Query> queryList = multiQueryExpanderDemo.expand("啥是程序员鱼皮啊,请回答我hhahhaha");
        Assertions.assertNotNull(queryList);
    }
}