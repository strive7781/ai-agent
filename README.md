# Yu AI Agent

基于 Spring AI + Vue 3 的全栈 AI 智能体应用，包含恋爱 AI 大师和 Manus 超级智能体。

## 功能特性

### AI 恋爱大师（LoveApp）
- 多轮对话（支持同步 / SSE 流式输出）
- 基于文件的会话记忆持久化
- RAG 知识库问答（含查询重写、向量检索）
- 结构化输出（恋爱报告生成）
- 工具调用 & MCP 协议集成

### YuManus 超级智能体
- ReAct + ToolCall 多步推理架构
- 工具集：文件操作、资源下载、PDF 生成、Web 搜索、网页爬取、终端命令
- 支持最多 20 步自主规划与执行

### MCP 图片搜索服务（yu-image-search-mcp-server）
- 独立子项目，提供图片搜索能力

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 3.4.4、Spring AI 1.1.2、Java 21 |
| 前端 | Vue 3、Vite |
| 向量存储 | SimpleVectorStore、PgVector |
| API 文档 | Knife4j (OpenAPI 3) |
| 工具库 | Hutool、Lombok |

## 项目结构

```
ai-agent-backend/
├── src/main/java/com/yupi/aiagentbackend/
│   ├── agent/          # Agent 核心（BaseAgent、ReActAgent、ToolCallAgent、YuManus）
│   ├── app/            # LoveApp 应用层
│   ├── advisor/        # 自定义 Advisor（日志、ReReading）
│   ├── rag/            # RAG 检索增强（文档加载、向量存储、查询重写）
│   ├── tools/          # Agent 工具集（文件、网络、PDF、终端等）
│   ├── controller/     # REST 接口
│   ├── config/         # 配置类（CORS 等）
│   ├── chatmemory/     # 会话记忆
│   └── demo/invoke/    # 多种 AI 调用方式示例
├── src/main/resources/
│   └── documents/      # RAG 知识库文档
└── yu-image-search-mcp-server/   # MCP 图片搜索子服务

yu-ai-agent-frontend/   # Vue 3 前端
```

## 快速开始

### 环境要求
- JDK 21+
- Node.js 18+

### 后端启动

```bash
cd ai-agent-backend
# 配置 application.yml 中的 API Key
mvn spring-boot:run
```

### 前端启动

```bash
cd yu-ai-agent-frontend
npm install
npm run dev
```

### 接口说明

| 接口 | 说明 |
|------|------|
| `GET /ai/love_app/chat/sync` | 同步对话 |
| `GET /ai/love_app/chat/sse` | SSE 流式对话 |
| `GET /ai/love_app/chat/server_sent_event` | ServerSentEvent 对话 |
| `GET /ai/manus/chat` | YuManus 智能体 |

## Docker 部署

```bash
docker build -t yu-ai-agent .
docker run -p 8080:8080 yu-ai-agent
```
