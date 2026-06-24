package com.yupi.aiagentbackend.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend LoveAppDocumentLoaderTest
 * @Date 2026/6/14 15:20 周日
 * @since 1.0
 **/

@SpringBootTest
class LoveAppDocumentLoaderTest {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void loadMarkdownDocuments() {
        loveAppDocumentLoader.loadMarkdownDocuments();
    }
}
