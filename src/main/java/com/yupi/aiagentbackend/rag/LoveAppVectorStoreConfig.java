package com.yupi.aiagentbackend.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库Bean）
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend LoveAppVectorStoreConfig
 * @Date 2026/6/14 15:24 周日
 * @since 1.0
 **/
@Configuration
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documentsList = loveAppDocumentLoader.loadMarkdownDocuments();
        simpleVectorStore.add(documentsList);
        return simpleVectorStore;
    }

}
