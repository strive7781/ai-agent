package com.yupi.yuimagesearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Winter
 * @version 1.0
 * @className yu-image-search-mcp-server ImageSearchToolTest
 * @Date 2026/6/25 15:31 周四
 * @since 1.0
 **/
@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;
    @Test
    void searchImage() {
        String image = imageSearchTool.searchImage("computer");
        Assertions.assertNotNull(image);
    }

    @Test
    void searchMediumImages() {
    }
}