package com.yupi.aiagentbackend.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Winter
 * @version 1.0
 * @className ai-agent-backend TerminalOperationToolTest
 * @Date 2026/6/24 16:34 周三
 * @since 1.0
 **/
@SpringBootTest
public class TerminalOperationToolTest {

    @Test
    public void testExecuteTerminalCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String command = "dir";
        String result = tool.executeTerminalCommand(command);
        assertNotNull(result);
    }
}
