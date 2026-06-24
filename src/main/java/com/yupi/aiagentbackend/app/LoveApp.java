package com.yupi.aiagentbackend.app;

import com.yupi.aiagentbackend.advisor.MyLoggerAdvisor;
import com.yupi.aiagentbackend.advisor.ReReadingAdvisor;
import com.yupi.aiagentbackend.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend LoveApp
 * @Date 2026/6/15 15:47 周一
 * @since 1.0
 **/
@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 初始化ChatClient
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        //初始化基于文件的会话记忆
        String fileDir=System.getProperty("user.dir")+"/tmp/chatMemory";
        FileBasedChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于内存的对话记忆（窗口大小10条）
//        ChatMemory chatMemory = MessageWindowChatMemory.builder(fileDir)
//                .chatMemoryRepository(new InMemoryChatMemoryRepository())
//                .maxMessages(10)
//                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                     MessageChatMemoryAdvisor.builder(chatMemory).build(),
                     //自定义拦截器Advisor
                     new MyLoggerAdvisor()
                        //new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI基础对话（支持多轮会话记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * AI恋爱报告功能（实战结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }
    //AI恋爱知识库问答功能
    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    @Resource
    private VectorStore pgVectorVectorStore;
    /**
     * 和RAG知识库进行对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                //开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
//                //应用RAG知识库问答
//                .advisors(RetrievalAugmentationAdvisor.builder()
//                        .documentRetriever(VectorStoreDocumentRetriever.builder()
//                                .vectorStore(loveAppVectorStore)
//                                .build())
//                        .build())
                /*应用RAG检索增强服务(基于云知识库服务)
                .advisors(loveAppRagCloudAdvisor)*/
                //应用RAG检索增强服务（基于PGVectorStore向量存储）
                .advisors(RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(VectorStoreDocumentRetriever.builder()
                                .vectorStore(pgVectorVectorStore)  // 用你注入的 PgVector 向量库
                                .build())
                        .build())
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

}