package app;

import app.client.AwsClient;
import app.data.ProductData;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AwsTextractService {

    private AwsClient awsClient;

    public List<ProductData> findText(String fileName, List<String> dietBadProducts) {
        Optional<DetectDocumentTextResult> result = awsClient.findText(fileName);

        if (result.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProductData> badProducts = new ArrayList<>();

        for (String badProduct : dietBadProducts) {
            Optional<String> foundText = result.get().getBlocks().stream()
                    .map(Block::getText)
                    .filter(text -> text != null && text.toUpperCase().contains(badProduct.toUpperCase()))
                    .findFirst();

            if (foundText.isPresent()) {
                ProductData productData = ProductData.builder()
                        .productName(badProduct)
                        .build();

                badProducts.add(productData);
            }
        }

        return badProducts;
    }
}
