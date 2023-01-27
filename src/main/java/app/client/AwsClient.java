package app.client;

import app.config.AwsClientProperties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.services.textract.model.S3Object;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class AwsClient {

    private final AwsClientProperties awsClientProperties;

    public Optional<DetectDocumentTextResult> findText(String fileName) {
        try {
            EndpointConfiguration endpoint = new EndpointConfiguration(
                    awsClientProperties.getTextractUrl(), awsClientProperties.getRegion());

            AmazonTextract client = AmazonTextractClientBuilder.standard()
                    .withEndpointConfiguration(endpoint).build();

            DetectDocumentTextRequest request = new DetectDocumentTextRequest()
                    .withDocument(new Document().withS3Object(
                            new S3Object()
                                    .withName(fileName)
                                    .withBucket(awsClientProperties.getS3Bucket())));


            DetectDocumentTextResult result = client.detectDocumentText(request);
            log.info("Document was analyzed: {}", result);

            return Optional.of(result);
        } catch (Exception e) {
            log.error("Something went wrong during analyzing document={}: ", fileName, e);
        }

        return Optional.empty();
    }
}
