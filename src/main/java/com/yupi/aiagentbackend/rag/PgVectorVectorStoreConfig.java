package com.yupi.aiagentbackend.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class PgVectorVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        // 手动建表，确保 pgvector 扩展和表都存在
        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector");
        jdbcTemplate.execute("DROP TABLE IF EXISTS public.vector_store");
        jdbcTemplate.execute("""
                CREATE TABLE public.vector_store (
                    id uuid PRIMARY KEY,
                    content text,
                    metadata jsonb,
                    embedding vector(1024)
                )
                """);
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1024)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(false)
                .schemaName("public")
                .vectorTableName("vector_store")
                .build();
        // 加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdownDocuments();
        // DashScope embedding 限制单次最多 10 条，手动分批
        int batchSize = 10;
        for (int i = 0; i < documents.size(); i += batchSize) {
            List<Document> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
            vectorStore.add(batch);
        }
        return vectorStore;
    }
}

