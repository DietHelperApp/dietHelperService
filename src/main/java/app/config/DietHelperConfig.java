package app.config;

import com.theokanning.openai.service.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DietHelperConfig {

    private final OpenAiClientProperties openAiClientProperties;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(openAiClientProperties.getApiKey());
    }
}
