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

        String endPrompt = "Языки списка аллергенов (1) и текста состава (2) могут отличаться. " +
                "Напиши через запятую только те продукты из списка аллергенов (1), которые ты нашел в тексте состава (2), " +
                "исключая те, которых нет в списке аллергенов (1). Ответ напиши на языке, на котором написаны продукты из списка аллергенов (1). " +
                "В том случае, если ты ничего не нашел - напиши в ответе \"0\"." +
                "Список аллергенов (1):" + prompt +
                ". Текст состава (2):" + text;

        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), endPrompt);
        messages.add(userMessage);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model("gpt-3.5-turbo")
                .temperature(0.0)
                .maxTokens(1000)
                .build();

        return openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }
}
