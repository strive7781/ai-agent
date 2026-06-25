package com.yupi.aiagentbackend.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend WebScrapingToolTest
 * @Date 2026/6/24 16:29 周三
 * @since 1.0
 **/
class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool webScrapingTool=new WebScrapingTool();
        String url="https://www.codefather.cn";
        String result = webScrapingTool.scrapeWebPage(url);
        Assertions.assertNotNull(result);
    }
}