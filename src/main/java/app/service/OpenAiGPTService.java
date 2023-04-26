package app.service;

import app.data.ProductData;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
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

    public List<ProductData> findProducts(List<String> dietBadProducts, String text) {
        String prompt = "";
        prompt += "Q: " + text + "This is an ingredients text.";
        for (String product : dietBadProducts) {
            prompt += product + " ";
        }
        prompt+=" - return each from this list which this ingredients text contains.A:";

        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("text-davinci-002")
                .temperature(0.5)
                .maxTokens(1000)
                .stop(List.of("Q:"))
                .build();

        return openAiService.createCompletion(completionRequest).getChoices().stream()
                .map(CompletionChoice::getText)
                .map(txt -> txt.replaceAll("\\.", "").replaceAll(" ", "").split(","))
                .flatMap(list -> Arrays.stream(list).map(product -> ProductData.builder().productName(product).build())
                ).toList();
    }
}
