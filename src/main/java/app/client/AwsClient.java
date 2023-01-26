package app.client;

import app.config.AwsClientProperties;
import app.data.ProductData;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.AnalyzeDocumentRequest;
import com.amazonaws.services.textract.model.AnalyzeDocumentResult;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.services.textract.model.S3Object;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
@Slf4j
public class AwsClient {

    private final AwsClientProperties awsClientProperties;

    public Optional<DetectDocumentTextResult> findText(MultipartFile file) {
        try {
            EndpointConfiguration endpoint = new EndpointConfiguration(
                    awsClientProperties.getTextractUrl(), awsClientProperties.getRegion());

            AmazonTextract client = AmazonTextractClientBuilder.standard()
                    .withEndpointConfiguration(endpoint).build();

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(awsClientProperties.getRegion())
                    .build();

            if(!s3Client.doesBucketExistV2(awsClientProperties.getS3Bucket())) {
                s3Client.createBucket(awsClientProperties.getS3Bucket());
            }

            File tempFile = File.createTempFile("ingredients", null);
            tempFile.deleteOnExit();
            file.transferTo(tempFile);


            s3Client.putObject(
                    awsClientProperties.getS3Bucket(),
                    "ingredients",
                    tempFile
            );

            tempFile.delete();

            DetectDocumentTextRequest request = new DetectDocumentTextRequest()
                    .withDocument(new Document().withS3Object(
                            new S3Object()
                                    .withName("ingredients")
                                    .withBucket(awsClientProperties.getS3Bucket())));


            DetectDocumentTextResult result = client.detectDocumentText(request);
            log.info("Document was analyzed: {}", result);

            return Optional.of(result);
        } catch (Exception e) {
            log.error("Something went wrong during analyzing document={}: ", file.getName(), e);
        }

        return Optional.empty();
    }
}
