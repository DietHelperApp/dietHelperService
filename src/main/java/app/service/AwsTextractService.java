package app.service;

import app.client.AwsClient;
import app.data.ProductData;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AwsTextractService {

    private final AwsClient awsClient;

    private final OpenAiGPTService openAiGPTService;

    public List<ProductData> findText(String fileName, List<String> dietBadProducts) {
        Optional<DetectDocumentTextResult> result = awsClient.findText(fileName);
        awsClient.deleteObject(fileName);

        if (result.isEmpty()) {
            return new ArrayList<>();
        }

        String foundText = result.get().getBlocks().stream()
                .map(block -> block.getText() + " ")
                .collect(Collectors.joining());

        return openAiGPTService.findProducts(dietBadProducts, foundText);
    }
}
