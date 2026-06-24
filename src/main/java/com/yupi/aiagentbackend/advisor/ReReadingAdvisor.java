package com.yupi.aiagentbackend.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

/**
 * 自定义 Re2 Advisor
 * 可提高大型语言模型的推理能力
 * 原理：让用户的问题重复读一遍，提升 LLM 推理效果
 */
public class ReReadingAdvisor implements CallAdvisor, StreamAdvisor {

    private ChatClientRequest before(ChatClientRequest request) {
        // 参考 SelfRefineEvaluationAdvisor 的写法，通过 augmentUserMessage 修改用户消息
        Prompt augmentedPrompt = request.prompt()
                .augmentUserMessage(userMessage -> userMessage.mutate()
                        .text("""
                                %s
                                Read the question again: %s
                                """.formatted(userMessage.getText(), userMessage.getText()))
                        .build());

        return request.mutate().prompt(augmentedPrompt).build();
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        return chain.nextCall(this.before(request));
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        return chain.nextStream(this.before(request));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
