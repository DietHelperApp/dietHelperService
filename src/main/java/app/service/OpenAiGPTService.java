package app.service;

import app.data.ProductData;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OpenAiGPTService {

    private final OpenAiService openAiService;

    public String findProducts(List<String> dietBadProducts, String text) {
        String prompt = "";
        for (String product : dietBadProducts) {
            prompt += product + ",";
        }

        String endPrompt = "Текст состава:" + text + ". Список аллергенов:" + prompt + ". " +
                "Языки текста состава и списка аллергенов могут отличаться. Напиши только в ответе через запятую аллергены из списка, " +
                "которые содержатся в данном составе, на языке, на котором они написаны в списке.";

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), endPrompt);
        messages.add(userMessage);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo")
                .temperature(0.0)
                .maxTokens(1000)
                .build();

        String result = openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
        String[] splittedResult = result.split(":");

        if (splittedResult.length > 1) {
            return splittedResult[1];
        }

        return "";
    }
}
