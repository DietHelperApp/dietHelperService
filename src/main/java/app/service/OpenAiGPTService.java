package app.service;

import app.data.ProductData;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OpenAiGPTService {

    private final OpenAiService openAiService;

    public List<ProductData> findProducts(List<String> dietBadProducts, String text) {
        String prompt = "";
        for (String product : dietBadProducts) {
            prompt += product + ",";
        }
        String endPrompt = "Q:" + text + "Does this ingredients text contain allergens from the list: " + prompt + "?. Return like: milk +/-, ... A:";

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(endPrompt)
                .model("text-davinci-003")
                .temperature(0.0)
                .maxTokens(1000)
                .stop(List.of("Q:"))
                .build();

        return openAiService.createCompletion(completionRequest).getChoices().stream()
                .map(CompletionChoice::getText)
                .map(txt -> txt.replaceAll("\\.", "").replaceAll(" ", "").split(","))
                .flatMap(list -> Arrays.stream(list).filter(product -> product.contains("+")).map(product -> ProductData.builder().productName(product.replace("+", "")).build())
                ).toList();
    }
}
