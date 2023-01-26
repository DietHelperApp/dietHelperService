package app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws-client")
public class AwsClientProperties {
    private String textractUrl;
    private String region;
    private String s3Bucket;
}
