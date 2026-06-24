package com.yupi.aiagentbackend.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend LoveAppDocumentLoader
 * @Date 2026/6/14 15:06 周日
 * @since 1.0
 **/

@Component
@Slf4j
public class LoveAppDocumentLoader {

    private final ResourcePatternResolver resolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resolver) {
        this.resolver = resolver;
    }

/**
     * 加载所有markdown资源
     * @return
     */

    /**
     * 加载多篇markdown文件
     * @return
     */
    public List<Document> loadMarkdownDocuments() {
        List<Document> allDocuments=new ArrayList<>();
        try {
            Resource[] resources = resolver.getResources("classpath:documents/*.md");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                String status = fileName.substring(fileName.length() - 6, fileName.length() - 4);
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
                        .withAdditionalMetadata("status",status)
                        .build();
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(markdownDocumentReader.get());
            }
        } catch (IOException e) {
            log.error("加载markdown文件失败",e);
//            throw new RuntimeException(e);
        }
    return allDocuments;
    }
}

